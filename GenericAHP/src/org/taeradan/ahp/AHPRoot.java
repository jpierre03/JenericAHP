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
import java.util.Collections;
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

	/**
	 * Contains the default path to access indicators
	 */
	public static final String DEFAULT_INDICATOR_PATH = "org.taeradan.ahp.test.ind.";

	public static String indicatorPath = DEFAULT_INDICATOR_PATH;
	private final AHP_Structure structure = new AHP_Structure();
	private final AHP_Execution execution = new AHP_Execution();
	public final AHP_GUI_methods guiMethods = new AHP_GUI_methods();
	private static final boolean DEBUG = false;

	private class AHP_Structure implements XmlOutputable {

		private String name = "";
		private Matrix alternativesCriteriaMatrix;
		private PairWiseMatrix criteriaCriteriaMatrix = new PairWiseMatrix();
		private PriorityVector alternativesGoalVector;
		private PriorityVector criteriaGoalVector;
		private Collection<Criterion> criteria = new ArrayList<>();

		public void build(File inFile) throws JDOMException, IOException {

			/** XML parser creation */
			final SAXBuilder parser = new SAXBuilder();
			/** JDOM document created from XML configuration file */
			final Document inXmlDocument = parser.build(inFile);
			/** Extraction of the root element from the JDOM document */
			final Element xmlRoot = inXmlDocument.getRootElement();
			/** Initialisation of the AHP tree name */
			structure.name = xmlRoot.getChildText("name");
			/** Initialisation of the preference matrix */
			final Element xmlCriteriaCriteriaPreferenceMatrix = xmlRoot.getChild("prefmatrix");
			structure.criteriaCriteriaMatrix = PairWiseMatrix.builder(xmlCriteriaCriteriaPreferenceMatrix);
			structure.criteriaGoalVector = PriorityVector.build(structure.criteriaCriteriaMatrix);

			/** Initialisation of the criteria */
			@SuppressWarnings("unchecked")
			final List<Element> xmlCriteriaList = (List<Element>) xmlRoot.getChildren("criteria");
			@SuppressWarnings("unchecked")
			final List<Element> xmlRowsList = (List<Element>) xmlCriteriaCriteriaPreferenceMatrix.getChildren("row");
			structure.criteria = new ArrayList<>(xmlCriteriaList.size());
			/** Verification that the number of criteria matches the size of the preference matrix */
			if (xmlCriteriaList.size() != xmlRowsList.size()) {
				throw new IllegalArgumentException(
					"Error : the number of criteria and the size of the preference matrix does not match !");
			}

			for (Element xmlCriteria : xmlCriteriaList) {
				structure.criteria.add(new Criterion(xmlCriteria));
			}
		}

		@Override
		public Element toXml() {
			final Element xmlRoot = new Element("root");
			xmlRoot.addContent(new Element("name").setText(name));
			xmlRoot.addContent(criteriaCriteriaMatrix.toXml());
			for (Criterion aCriteria : criteria) {
				xmlRoot.addContent(aCriteria.toXml());
			}
			return xmlRoot;
		}
	}

	private class AHP_Execution {
		private boolean isCalculationDone = false;
		private final ConsistencyChecker consistencyChecker = new ConsistencyChecker();

		private AHP_Execution() {
		}

		private boolean isCalculationDone() {
			return isCalculationDone;
		}

		private void setCalculationDone(boolean calculationDone) {
			isCalculationDone = calculationDone;
		}

		public boolean isConsistent(PairWiseMatrix matrixCriteriaCriteria, PriorityVector vectorCriteriaGoal) {
			return consistencyChecker.isConsistent(matrixCriteriaCriteria, vectorCriteriaGoal);
		}

		public void assertConsistency() {
			if (!isConsistent(structure.criteriaCriteriaMatrix, structure.criteriaGoalVector)) {
				throw new IllegalArgumentException("AHP preference matrix not consistent (root)");
			}
		}
	}

	@Deprecated
	public class AHP_GUI_methods {
		@Deprecated
		public void removeCriterion(final Criterion criterion) {
			assert criterion != null;

			if (structure.criteria.contains(criterion)) {

				final int criterionIndex = new ArrayList<>(structure.criteria).lastIndexOf(criterion);
				structure.criteria.remove(criterion);
				structure.criteriaCriteriaMatrix.remove(criterionIndex);
			} else {
				Logger.getAnonymousLogger().severe("Criterion not found");
			}
		}

		@Deprecated
		public StringBuilder resultToString() {
			final StringBuilder sb = new StringBuilder();
			if (execution.isCalculationDone()) {
				sb.append(this.toString());
				for (Criterion aCriteria : structure.criteria) {
					sb.append("\n\t");
					sb.append(aCriteria.resultToString());
				}
				sb.append("\nalternativesGoalVector=\n");
				sb.append(PairWiseMatrix.toString(structure.alternativesGoalVector, null));
			} else {
				sb.append("There is no result, please do a ranking first");
			}
			return sb;
		}

		@Deprecated
		public void setName(final String name) {
			assert name != null;
			assert !name.isEmpty();

			structure.name = name;
		}

		@Deprecated
		public void setMatrixCriteriaCriteria(PairWiseMatrix matrix) {
			assert matrix != null;
			assert matrix.getColumnDimension() > 0;
			assert matrix.getRowDimension() > 0;

			structure.criteriaCriteriaMatrix = matrix;
		}

		@Deprecated
		public PairWiseMatrix getMatrixCriteriaCriteria() {
			return structure.criteriaCriteriaMatrix;
		}

		@Deprecated
		public Collection<Criterion> getCriteria() {
			return Collections.unmodifiableCollection(structure.criteria);
		}
	}

	/**
	 * Class constructor that creates the AHP tree from a configuration file given in argument.
	 *
	 * @param inFile        Path to the configuration file
	 * @param indicatorPath
	 */
	public AHPRoot(final File inFile, final String indicatorPath) {
		if (inFile == null
			|| !inFile.exists()
			|| !inFile.canRead()) {
			throw new IllegalArgumentException("File should exist and be read");
		}
		if (indicatorPath == null
			|| indicatorPath.isEmpty()) {
			throw new IllegalArgumentException("indicatorPath should be defined");
		}

		AHPRoot.indicatorPath = indicatorPath;

		try {
			structure.build(inFile);

			execution.assertConsistency();
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("File not found : " + inFile.getAbsolutePath(), e);
		} catch (JDOMException | IOException e) {
			throw new IllegalArgumentException("AHPRoot instantiation error", e);
		}
	}

	/**
	 * @return String describing the AHP root, and NOT its children.
	 */
	@Override
	public String toString() {
		return " AHP Root: " + structure.name + ", " + structure.criteria.size() + " criteria";
	}

	/**
	 * Returns a big string (multiple lines) containing recursively the description of the whole AHP tree. Best suited for
	 * testing purposes.
	 *
	 * @return Multiline string describing the AHP tree
	 */
	public String toStringRecursive() {
		final StringBuilder sb = new StringBuilder();

		sb.append(this.toString());
		sb.append("\n").append(structure.criteriaCriteriaMatrix);
		final DecimalFormat printFormat = new DecimalFormat("0.000");
		int index = 0;

		for (Criterion criterion : structure.criteria) {
			sb.append("\n\t(");
			sb.append(printFormat.format(structure.criteriaGoalVector.get(index, 0)));
			sb.append(") ");
			sb.append(criterion.toStringRecursive());
			index++;
		}

		return sb.toString();
	}

	/**
	 * Saves the whole AHP tree in a XML file
	 *
	 * @param outputFile Output XML file path
	 */
	public void saveConfiguration(final String outputFile) {
		try {
			/** Save the AHP tree in a XML document matching the document definition "ahp_conf.dtd" */
			final Document outXmlDocument =
				new Document(structure.toXml(),
					new DocType(
						"root",
						getClass().getResource("/org/taeradan/ahp/conf/ahp_conf.dtd").getFile()));

			/** Use a write format easily readable by a human */
			final XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
			/** Write the output into the specified file */
			output.output(outXmlDocument, new FileOutputStream(outputFile));
		} catch (IOException ex) {
			Logger.getLogger(AHPRoot.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Root method of the AHP execution.
	 * <p/>
	 * Calculates the final alternatives ranking with the alternatives priority vectors
	 * from the criteria and the criteria priority vectors.
	 * <p/>
	 * This is the main method of this project.
	 */
	public void calculateRanking(final Collection<? extends Alternative> alternatives) {
		structure.alternativesCriteriaMatrix = new Matrix(alternatives.size(), structure.criteria.size());

		if (DEBUG) {
			info("alternatives = " + alternatives.size());
			info("criteria = " + structure.criteria.size());
		}

		/** Concatenation in a matrix of the vectors calculated by the criteria */
		int index = 0;
		for (Criterion criterion : structure.criteria) {
			final PriorityVector priorityVector = criterion.calculateAlternativesPriorityVector(alternatives);

			if (DEBUG) {
				info("criterion n= " + index);
				info("alternatives vector n= " + priorityVector.getRowDimension());
				priorityVector.print(5, 4);
				structure.alternativesCriteriaMatrix.print(5, 4);
			}

			structure.alternativesCriteriaMatrix.setMatrix(0, alternatives.size() - 1, index, index, priorityVector);
			index++;
		}

		/** Calculation of the final alternatives priority vector */
		structure.alternativesGoalVector = new PriorityVector(structure.alternativesCriteriaMatrix.getRowDimension());
		structure.alternativesGoalVector.setMatrix(
			alternatives.size() - 1,
			structure.alternativesCriteriaMatrix.times(structure.criteriaGoalVector));

		/** Ranking of the alternatives with the MOg vector */
		final double[][] sortedVectorAlternativesGoal = structure.alternativesGoalVector.getArrayCopy();

		// alternativesGoalVector.getVector().print(6, 4);
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
			/** swap indexes[i] <-> indexes[minIndex] */
			tmpIndex = originIndexes[i];
			originIndexes[i] = originIndexes[minIndex];
			originIndexes[minIndex] = tmpIndex;
			tmpValue = sortedVectorAlternativesGoal[i][0];
			/** swap vector[i] <-> vector[minIndex] */
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

		execution.setCalculationDone(true);
	}

	public String getName() {
		return structure.name;
	}

	private void info(String string) {
		Logger.getAnonymousLogger().info(string);
	}
}
