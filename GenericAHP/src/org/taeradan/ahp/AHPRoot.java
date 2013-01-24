/* Copyright 2009-2010 Yves Dubromelle @ LSIS(www.lsis.org)
 *
 * This file is part of JenericAHP.
 *
 * JenericAHP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JenericAHP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JenericAHP.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.taeradan.ahp;

import Jama.Matrix;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the root class of the AHP tree. It contains Criteria and execute its part of the AHP algorithm.
 *
 * @author Jean-Pierre PRUNARET
 * @author Yves Dubromelle
 */
public class AHPRoot {


	/** Contains the path to access indicators */
	public static final String         DEFAULT_INDICATOR_PATH = "org.taeradan.ahp.test.ind.";
	public static       String         indicatorPath          = DEFAULT_INDICATOR_PATH;
	//	AHP configuration attributes
	private             String         name                   = "";
	private             PairWiseMatrix matrixCriteriaCriteria = new PairWiseMatrix();
	private PriorityVector vectorCriteriaGoal;
	private Collection<Criterion> criteria = new ArrayList<>();
	//	AHP execution attributes
	private PriorityVector vectorAlternativesGoal;
	private Matrix         matrixAlternativesCriteria;
	//	Execution control attributes
	private              boolean            isCalculationDone  = false;
	private final        ConsistencyChecker consistencyChecker = new ConsistencyChecker();
	private static final boolean            DEBUG              = false;

	/**
	 * Class constructor that creates the AHP tree from a configuration file given in argument.
	 *
	 * @param inFile        Path to the configuration file
	 * @param indicatorPath
	 */
	public AHPRoot(final File inFile, final String indicatorPath) {
		if (inFile == null
			|| inFile.exists() == false
			|| inFile.canRead() == false) {
			throw new IllegalArgumentException("File should exist and be read");
		}
		if (indicatorPath == null
			|| indicatorPath.isEmpty()) {
			throw new IllegalArgumentException("indicatorPath should be defined");
		}

		AHPRoot.indicatorPath = indicatorPath;

//			XML parser creation
		final SAXBuilder parser = new SAXBuilder();
		try {
//				JDOM document created from XML configuration file
			final Document inXmlDocument = parser.build(inFile);
//				Extraction of the root element from the JDOM document
			final Element xmlRoot = inXmlDocument.getRootElement();
//				Initialisation of the AHP tree name
			name = xmlRoot.getChildText("name");
//				Initialisation of the preference matrix
			final Element xmlCriteriaCriteriaPreferenceMatrix = xmlRoot.getChild("prefmatrix");
			matrixCriteriaCriteria = PairWiseMatrix.builder(xmlCriteriaCriteriaPreferenceMatrix);
			vectorCriteriaGoal = PriorityVector.build(matrixCriteriaCriteria);
//				Consistency verification
			if (!consistencyChecker.isConsistent(matrixCriteriaCriteria, vectorCriteriaGoal)) {
				throw new IllegalArgumentException("AHP preference matrix not consistent (root)");
			}
//				Initialisation of the criteria
			@SuppressWarnings("unchecked")
			final List<Element> xmlCriteriaList = (List<Element>) xmlRoot.getChildren("criteria");
			@SuppressWarnings("unchecked")
			final List<Element> xmlRowsList = (List<Element>) xmlCriteriaCriteriaPreferenceMatrix.getChildren("row");
			criteria = new ArrayList<Criterion>(xmlCriteriaList.size());
//				Verification that the number of criteria matches the size of the preference matrix
			if (xmlCriteriaList.size() != xmlRowsList.size()) {
				throw new IllegalArgumentException(
						"Error : the number of criteria and the size of the preference matrix does not match !");
			}

			final Iterator<Element> xmlCriteriaListIterator = xmlCriteriaList.iterator();
			while (xmlCriteriaListIterator.hasNext()) {
				final Element xmlCriteria = xmlCriteriaListIterator.next();

				criteria.add(new Criterion(xmlCriteria));
			}
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("File not found : " + inFile.getAbsolutePath(), e);
		} catch (Exception e) {
			throw new IllegalArgumentException("AHPRoot instantiation error", e);
		}
	}

	public void removeCriterion(final Criterion criterion) {
		if (criteria.contains(criterion)) {

			final int criterionIndex = new ArrayList<>(criteria).lastIndexOf(criterion);
			criteria.remove(criterion);
			matrixCriteriaCriteria.remove(criterionIndex);
		} else {
			Logger.getAnonymousLogger().severe("Criterion not found");
		}
	}

	/**
	 * Returns a string describing the AHP root, and NOT its children.
	 *
	 * @return Describing string
	 */
	@Override
	public String toString() {
		return " AHP Root : " + name + ", " + criteria.size() + " criteria";
	}

	/**
	 * Returns a big string (multiple lines) containing recursively the description of the whole AHP tree. Best suited for
	 * testing purposes.
	 *
	 * @return Multiline string describing the AHP tree
	 */
	public String toStringRecursive() {
		final StringBuilder sb = new StringBuilder(this.toString());

		sb.append("\n").append(matrixCriteriaCriteria);
		final DecimalFormat printFormat = new DecimalFormat("0.000");
		final Iterator<Criterion> criteriaIterator = criteria.iterator();
		int index = 0;

		while (criteriaIterator.hasNext()) {
			final Criterion criterion = criteriaIterator.next();

			sb.append("\n\t(");
			sb.append(printFormat.format(vectorCriteriaGoal.get(index, 0)));
			sb.append(") ");
			sb.append(criterion.toStringRecursive());
			index++;
		}

		return sb.toString();
	}

	/**
	 * Returns a JDOM element that represents the AHP root and its children
	 *
	 * @return JDOM Element representing the whole AHP tree
	 */
	public Element toXml() {
		final Element xmlRoot = new Element("root");
		xmlRoot.addContent(new Element("name").setText(name));
		xmlRoot.addContent(matrixCriteriaCriteria.toXml());
		final Iterator<Criterion> iterator = criteria.iterator();
		while (iterator.hasNext()) {
			xmlRoot.addContent(iterator.next().toXml());
		}
		return xmlRoot;
	}

	public StringBuilder resultToString() {
		final StringBuilder sb = new StringBuilder();
		if (isCalculationDone) {
			sb.append(this.toString());
			final Iterator<Criterion> itCriterias = criteria.iterator();
			while (itCriterias.hasNext()) {
				sb.append("\n\t");
				sb.append(itCriterias.next().resultToString());
			}
			sb.append("\nvectorAlternativesGoal=\n").append(PairWiseMatrix.toString(vectorAlternativesGoal, null));
		} else {
			sb.append("There is no result, please do a ranking first");
		}
		return sb;
	}

	/**
	 * Saves the whole AHP tree in a XML file
	 *
	 * @param outputFile Output XML file path
	 */
	public void saveConfiguration(final String outputFile) {
		try {
//			Save the AHP tree in a XML document matching the Doctype "ahp_conf.dtd"
			final Document outXmlDocument =
					new Document(toXml(),
								 new DocType("root",
											 getClass().getResource("/org/taeradan/ahp/conf/ahp_conf.dtd").getFile()));

//			Use a write format easily readable by a human
			final XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
//			Write the output into the specified file
			output.output(outXmlDocument, new FileOutputStream(outputFile));
		} catch (IOException ex) {
			Logger.getLogger(AHPRoot.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Root method of the AHP execution. Calculates the final alternatives ranking with the alternatives priority vectors
	 * from the criteria and the criteria priority vectors.
	 *
	 * @param alternatives
	 */
	public void calculateRanking(final Collection<? extends Alternative> alternatives) {
		matrixAlternativesCriteria = new Matrix(alternatives.size(), criteria.size());
//		Concatenation in a matrix of the vectors calculated by the criteria
		int index = 0;
		if (DEBUG) {
			Logger.getAnonymousLogger().info("alternatives = " + alternatives.size());
			Logger.getAnonymousLogger().info("criteria = " + criteria.size());
		}

		for (Criterion criterion : this.criteria) {
			final PriorityVector priorityVector = criterion.calculateAlternativesPriorityVector(alternatives);
			if (DEBUG) {
				Logger.getAnonymousLogger().info("criterion n= " + index);
				Logger.getAnonymousLogger().info("alternatives vector n= " + priorityVector.getRowDimension());
				priorityVector.print(5, 4);
				matrixAlternativesCriteria.print(5, 4);
			}
			matrixAlternativesCriteria.setMatrix(0, alternatives.size() - 1, index, index, priorityVector);
			index++;
		}

//		Calculation of the final alternatives priority vector
		vectorAlternativesGoal = new PriorityVector(matrixAlternativesCriteria.getRowDimension());
		vectorAlternativesGoal.setMatrix(alternatives.size() - 1, matrixAlternativesCriteria.times(vectorCriteriaGoal));

//			Ranking of the alternatives with the MOg vector
		final double[][] sortedVectorAlternativesGoal = vectorAlternativesGoal.getArrayCopy();

//			vectorAlternativesGoal.getVector().print(6, 4);
		int[] originIndexes = new int[alternatives.size()];
		for (int i = 0; i < originIndexes.length; i++) {
			originIndexes[i] = i;
		}

		int minIndex;
		double tmpValue;
		int tmpIndex;
		int[] ranks = new int[alternatives.size()];
		for (int i = 0; i < alternatives.size(); i++) {
			minIndex = i;
			for (int j = i + 1; j < alternatives.size(); j++) {
				if (sortedVectorAlternativesGoal[j][0] < sortedVectorAlternativesGoal[minIndex][0]) {
					minIndex = j;
				}
			}
//				indexes[i] <-> indexes[minIndex]
			tmpIndex = originIndexes[i];
			originIndexes[i] = originIndexes[minIndex];
			originIndexes[minIndex] = tmpIndex;
			tmpValue = sortedVectorAlternativesGoal[i][0];
//				vector[i] <-> vector[minIndex]
			sortedVectorAlternativesGoal[i][0] = sortedVectorAlternativesGoal[minIndex][0];
			sortedVectorAlternativesGoal[minIndex][0] = tmpValue;
			ranks[alternatives.size() - i - 1] = originIndexes[i];
//				System.out.println("ranks[" + i + "]=" + originIndexes[i] + " : "
//							   + sortedVectorAlternativesGoal[i][0]);
		}

		index = 0;
		for (int i : ranks) {
			((Alternative) alternatives.toArray()[i]).setRank(index + 1);
			index++;
		}

		isCalculationDone = true;
	}

	public PairWiseMatrix getMatrixCriteriaCriteria() {
		return matrixCriteriaCriteria;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		assert name != null;
		assert name.isEmpty() == false;

		this.name = name;
	}

	public Collection<Criterion> getCriteria() {
		return Collections.unmodifiableCollection(criteria);
	}

	public void setMatrixCriteriaCriteria(PairWiseMatrix matrix) {
		assert matrix != null;
		assert matrix.getColumnDimension() > 0;
		assert matrix.getRowDimension() > 0;

		matrixCriteriaCriteria = matrix;
	}
}
