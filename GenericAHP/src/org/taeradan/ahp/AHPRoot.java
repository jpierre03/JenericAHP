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
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
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
	public static String indicatorPath = "org.taeradan.ahp.test.ind.";
	//	AHP configuration attributes
	private           String               name;
	private transient PairWiseMatrix       matrixCriteriaCriteria;
	private transient PriorityVector       vectorCriteriaGoal;
	private transient Collection<Criteria> criterias;
	//	AHP execution attributes
	private transient PriorityVector       vectorAlternativesGoal;
	private transient Matrix               matrixAlternativesCriteria;
	//	Execution control attributes
	private transient    boolean            isCalculationDone  = false;
	private final        ConsistencyChecker consistencyChecker = new ConsistencyChecker();
	private static final boolean            DEBUG              = false;

	/**
	 * Class constructor that creates the AHP tree from a configuration file given in argument.
	 *
	 * @param inFile        Path to the configuration file
	 * @param indicatorPath
	 */
	public AHPRoot(final File inFile, final String indicatorPath) {
		if (inFile == null) {
			name = "";
			matrixCriteriaCriteria = new PairWiseMatrix();
			criterias = new ArrayList<Criteria>();
		} else {
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
				final Element xmlPrefMatrix = xmlRoot.getChild("prefmatrix");
				matrixCriteriaCriteria = PairWiseMatrix.builder(xmlPrefMatrix);
				vectorCriteriaGoal = PriorityVector.build(matrixCriteriaCriteria);
//				Consistency verification
				if (!consistencyChecker.isConsistent(matrixCriteriaCriteria, vectorCriteriaGoal)) {
					Logger.getAnonymousLogger().severe(
							"Is not consistent (root)");
				}
//				Initialisation of the criterias
				@SuppressWarnings("unchecked")
				final List<Element> xmlCriteriasList = (List<Element>) xmlRoot.getChildren(
						"criteria");
				@SuppressWarnings("unchecked")
				final List<Element> xmlRowsList = (List<Element>) xmlPrefMatrix.getChildren("row");
				criterias = new ArrayList<Criteria>(xmlCriteriasList.size());
//				Verification that the number of criterias matches the size of the preference matrix
				if (xmlCriteriasList.size() != xmlRowsList.size()) {
					Logger.getAnonymousLogger().severe(
							"Error : the number of criterias and the size of the preference matrix does not match !");
				}
				final Iterator<Element> itXmlCritList = xmlCriteriasList.iterator();
				while (itXmlCritList.hasNext()) {
					final Element xmlCriteria = itXmlCritList.next();
					criterias.add(new Criteria(xmlCriteria));
				}
			} catch (FileNotFoundException e) {
				Logger.getAnonymousLogger().log(Level.SEVERE, "File not found : {0}", inFile.
																									getAbsolutePath());
				name = "unknow";
				matrixCriteriaCriteria = new PairWiseMatrix();
				criterias = new ArrayList<Criteria>();
			} catch (JDOMException e) {
				Logger.getAnonymousLogger().severe(e.getLocalizedMessage());
			} catch (IOException e) {
				Logger.getAnonymousLogger().severe(e.getLocalizedMessage());
			}
		}
	}

	/** @param crit  */
	public void delCriteria(final Criteria crit) {
		if (criterias.contains(crit)) {
			final int critIndex = new ArrayList<Criteria>(criterias).lastIndexOf(crit);
			criterias.remove(crit);
			matrixCriteriaCriteria.remove(critIndex);
		} else {
			Logger.getAnonymousLogger().severe("Criteria not found");
		}
	}

	/**
	 * Returns a string describing the AHP root, and NOT its children.
	 *
	 * @return Describing string
	 */
	@Override
	public String toString() {
		return " AHP Root : " + name + ", " + criterias.size() + " criterias";
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
		final Iterator<Criteria> criteriaIterator = criterias.iterator();
		int index = 0;

		while (criteriaIterator.hasNext()) {
			final Criteria criteria = criteriaIterator.next();

			sb.append("\n\t(");
			sb.append(printFormat.format(vectorCriteriaGoal.get(index, 0)));
			sb.append(") ");
			sb.append(criteria.toStringRecursive());
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
		final Iterator<Criteria> itCriterias = criterias.iterator();
		while (itCriterias.hasNext()) {
			xmlRoot.addContent(itCriterias.next().toXml());
		}
		return xmlRoot;
	}

	/** @return  */
	public String resultToString() {
		final StringBuilder string = new StringBuilder();
		if (isCalculationDone) {
			string.append(this.toString());
			final Iterator<Criteria> itCriterias = criterias.iterator();
			while (itCriterias.hasNext()) {
				string.append("\n\t").append(itCriterias.next().resultToString());
			}
			string.append("\nvectorAlternativesGoal=\n").append(PairWiseMatrix.toString(vectorAlternativesGoal, null));
		} else {
			string.append("There is no result, please do a ranking first");
		}
		return string.toString();
	}

	/**
	 * Saves the whole AHP tree in a XML file
	 *
	 * @param outputFile Output XML file path
	 */
	public void saveConfig(final String outputFile) {
		try {
//			Save the AHP tree in a XML document matching the Doctype "ahp_conf.dtd"
			final Document outXmlDocument =
					new Document(toXml(),
								 new DocType("root", getClass().getResource(
										 "/org/taeradan/ahp/conf/ahp_conf.dtd").getFile()));
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
	 * from the criterias and the criterias priority vectors.
	 *
	 * @param alternatives
	 */
	public void calculateRanking(final Collection<? extends Alternative> alternatives) {
		matrixAlternativesCriteria = new Matrix(alternatives.size(), criterias.size());
//		Concatenation in a matrix of the vectors calculated by the criterias
		int index = 0;
		if (DEBUG) {
			System.out.println("alternatives = " + alternatives.size());
			System.out.println("criterias = " + criterias.size());
		}
		for (Criteria criteria : criterias) {
			PriorityVector temp = criteria.calculateAlternativesPriorityVector(alternatives);
			if (DEBUG) {
				System.out.println("criteria n= " + index);
				System.out.println("alt vector n= " + temp.getRowDimension());
				temp.print(5, 4);
				matrixAlternativesCriteria.print(5, 4);
			}
			matrixAlternativesCriteria.setMatrix(0, alternatives.size() - 1, index, index, temp);
			index++;
		}
//		Calculation of the final alternatives priority vector
		vectorAlternativesGoal = new PriorityVector(matrixAlternativesCriteria.getRowDimension());
		vectorAlternativesGoal.setMatrix(alternatives.size() - 1, matrixAlternativesCriteria.times(vectorCriteriaGoal));
//		Ranking of the alternatives with the MOg vector
		double[][] sortedVectorAltOg = vectorAlternativesGoal.getArrayCopy();
//		vectorAlternativesGoal.getVector().print(6, 4);
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
				if (sortedVectorAltOg[j][0] < sortedVectorAltOg[minIndex][0]) {
					minIndex = j;
				}
			}
//			indexes[i] <-> indexes[minIndex]
			tmpIndex = originIndexes[i];
			originIndexes[i] = originIndexes[minIndex];
			originIndexes[minIndex] = tmpIndex;
			tmpValue = sortedVectorAltOg[i][0];
//			vector[i] <-> vector[minIndex]
			sortedVectorAltOg[i][0] = sortedVectorAltOg[minIndex][0];
			sortedVectorAltOg[minIndex][0] = tmpValue;
			ranks[alternatives.size() - i - 1] = originIndexes[i];
//			System.out.println("ranks[" + i + "]=" + originIndexes[i] + " : "
//							   + sortedVectorAltOg[i][0]);
		}
		index = 0;
		for (int i : ranks) {
			((Alternative) alternatives.toArray()[i]).setRank(index + 1);
			index++;
		}
		isCalculationDone = true;
	}

	/** @return  */
	public PairWiseMatrix getMatrixCriteriaCriteria() {
		return matrixCriteriaCriteria;
	}

	/** @return  */
	public String getName() {
		return name;
	}

	/** @param name  */
	public void setName(final String name) {
		this.name = name;
	}

	/** @return  */
	public Collection<Criteria> getCriterias() {
		return criterias;
	}

	/** @param matrix  */
	public void setMatrixCriteriaCriteria(PairWiseMatrix matrix) {
		matrixCriteriaCriteria = matrix;
	}
}
