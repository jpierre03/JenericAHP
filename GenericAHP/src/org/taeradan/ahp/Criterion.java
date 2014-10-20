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
import org.jdom.Element;

import java.lang.reflect.Constructor;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents the criteria of the AHP tree, it contains Indicators and it executes its part of the AHP
 * algorithm.
 *
 * @author Yves Dubromelle
 * @author Jean-Pierre PRUNARET
 */
public class Criterion
	implements XmlOutputable {

	private static final Logger logger = Logger.getAnonymousLogger();
	//	AHP configuration attributes
	private final PriorityVector vectorIndicatorCriteria;
	private final Collection<Indicator> indicators;
	//	AHP execution attributes
	private final ConsistencyChecker consistencyChecker = new ConsistencyChecker();
	private String identifier;
	private String name;
	private PairWiseMatrix matrixIndicatorIndicator;
	private PriorityVector vectorAlternativesCriteria;

	public Criterion(String identifier, Collection<Indicator> indicators, PairWiseMatrix matrixIndicatorIndicator, String name, PriorityVector vectorAlternativesCriteria, PriorityVector vectorIndicatorCriteria) {
		this.identifier = identifier;
		this.indicators = indicators;
		this.matrixIndicatorIndicator = matrixIndicatorIndicator;
		this.name = name;
		this.vectorAlternativesCriteria = vectorAlternativesCriteria;
		this.vectorIndicatorCriteria = vectorIndicatorCriteria;
	}

	@Deprecated
	public Criterion(final Element xmlCriteria) {
		throw new IllegalStateException("This constusctor is no more supported. Instead use builder.");
	}

	/**
	 * Creates a AHP Criterion from a JDOM Element
	 *
	 * @param xml
	 */
	public static Criterion fromXml(final Element xml) throws Exception {

		final PriorityVector vectorIndicatorCriteria;
		final Collection<Indicator> indicators;
		String identifier;
		String name;
		PairWiseMatrix matrixIndicatorIndicator;
		final ConsistencyChecker consistencyChecker = new ConsistencyChecker();

		/** Initialisation of the id of the criteria */
		identifier = xml.getAttributeValue(XmlKey.id.name());

		/** Initialisation of the name */
		name = xml.getChildText(XmlKey.name.name());

		/** Initialisation of the preference matrix */
		final Element xmlPrefMatrix = xml.getChild(XmlKey.prefmatrix.name());
		matrixIndicatorIndicator = PairWiseMatrix.builder(xmlPrefMatrix);
		vectorIndicatorCriteria = PriorityVector.build(matrixIndicatorIndicator);

		info("\tCriterion.identifier=" + identifier);
		info("\tCriterion.name=" + name);
		info("\tCriterion.matrixIndicatorIndicator=" + matrixIndicatorIndicator);


		/** Consistency verification */
		if (!consistencyChecker.isConsistent(matrixIndicatorIndicator, vectorIndicatorCriteria)) {
			logger.log(Level.SEVERE, "Is not consistent (criteria {0})", identifier);
			throw new IllegalStateException("criterion not consistent");
		}

		/** Initialisation of the Indicators */
		@SuppressWarnings("unchecked")
		final List<Element> xmlIndicatorsList = (List<Element>) xml.getChildren(XmlKey.indicator.name());
		@SuppressWarnings("unchecked")
		final List<Element> xmlRowsList = (List<Element>) xmlPrefMatrix.getChildren(XmlKey.row.name());

		/** Verification that the number of indicators matches the size of the matrix */
		if (xmlIndicatorsList.size() != xmlRowsList.size()) {
			final String msg = "Error : the number of Indicators and the size of the preference matrix does not match !";
			logger.severe(msg);
			throw new IllegalStateException(msg);
		}

		indicators = new ArrayList<>(xmlIndicatorsList.size());
		/** For each indicator declared in the configuration file */
		for (Element xmlIndicator : xmlIndicatorsList) {
			info("\tCriterion.xmlIndicator=" + xmlIndicator);
			info("\tCriterion.xmlIndicator.attValue=" + xmlIndicator.getAttributeValue(XmlKey.id.name()));

			final String indicatorFullClassName = AHPRoot.indicatorPath
				+ Indicator.class.getSimpleName()
				+ xmlIndicator.getAttributeValue(XmlKey.id.name());

			try {
				/** Research of the class implementing the indicator , named "org.taeradan.ahp.ind.IndicatorCxIy" */
				@SuppressWarnings("unchecked")
				final Class<? extends Indicator> indClass = (Class<? extends Indicator>) Class.forName(indicatorFullClassName);
				info("\t\tCriterion.indClass=" + indClass);
				/** Extraction of its constructor */
				final Constructor<? extends Indicator> indConstruct = indClass.getConstructor(Element.class);
				info("\t\tCriterion.indConstruct=" + indConstruct);
				/** Instanciation of the indicator with its constructor */
				Indicator indicator = indConstruct.newInstance(xmlIndicator);
				indicators.add(indicator);

				info("\tCriterion.indicator=" + indicator.toString());
			} catch (NoSuchMethodException e) {
				logger.log(Level.SEVERE, "Error : no such constructor :{0}", e);
			} catch (ClassNotFoundException e) {
				logger.log(Level.SEVERE, "Error : class {0} not found :{1}", new Object[]{indicatorFullClassName, e});
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Error :{0}", e);
			}
		}
		return new Criterion(identifier, indicators, matrixIndicatorIndicator, name, vectorIndicatorCriteria, vectorIndicatorCriteria);
	}

	private static void info(String msg) {
		logger.info(msg);
	}

	public PriorityVector calculateAlternativesPriorityVector(final Collection<? extends Alternative> alternatives) {

		final PairWiseMatrix alternativesIndicator = new PairWiseMatrix(alternatives.size(), indicators.size());

		/** Concatenation of the indicators' alternatives vectors **/
		int index = 0;
		for (Indicator indicator : indicators) {
			final PriorityVector alternativesPriorityVector = indicator.calculateAlternativesPriorityVector(alternatives);

			alternativesIndicator.setMatrix(
				0,
				alternatives.size() - 1,
				index,
				index,
				alternativesPriorityVector);

			index++;
		}

		/** Calculation of the criteria's alternatives vector */
		final Matrix value = alternativesIndicator.times(vectorIndicatorCriteria);

		vectorAlternativesCriteria = new PriorityVector(alternativesIndicator.getRowDimension());
		vectorAlternativesCriteria.setMatrix(alternativesIndicator.getRowDimension() - 1, value);

		return vectorAlternativesCriteria;
	}

	/**
	 * @return Criterion description alone (no child)
	 */
	@Override
	public String toString() {
		return "Criterion " + identifier + " : " + name;
	}

	/**
	 * @return Criterion description with all its children
	 */
	public String toStringRecursive() {
		final StringBuilder sb = new StringBuilder(this.toString());
		sb.append("\n").append(matrixIndicatorIndicator.toString("\t"));
		DecimalFormat printFormat = new DecimalFormat("0.000");
		final Iterator<Indicator> itIndicators = indicators.iterator();
		int index = 0;
		while (itIndicators.hasNext()) {
			sb.append("\n\t\t(");
			sb.append(printFormat.format(vectorIndicatorCriteria.get(index, 0)));
			sb.append(") ");
			sb.append(itIndicators.next().toString());
			index++;
		}
		return sb.toString();
	}

	/**
	 * @return a JDOM element that represents the criteria and all its children
	 */
	@Override
	public Element toXml() {
		final Element xmlCriteria = new Element(XmlKey.criteria.name());
		xmlCriteria.setAttribute(XmlKey.id.name(), identifier);
		xmlCriteria.addContent(new Element(XmlKey.name.name()).setText(name));
		xmlCriteria.addContent(matrixIndicatorIndicator.toXml());
		for (Indicator indicator : indicators) {
			xmlCriteria.addContent(indicator.toXml());
		}
		return xmlCriteria;
	}

	public StringBuilder resultToString() {
		final StringBuilder sb = new StringBuilder(this.toString());
		for (Indicator next : indicators) {
			sb.append("\n\t\t");
			sb.append(next.resultToString());
		}
		sb.append("\n\tvectorAlternativesCriteria=\n");
		sb.append(PairWiseMatrix.toString(vectorAlternativesCriteria, "\t"));

		return sb;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	public PairWiseMatrix getMatrixIndicatorIndicator() {
		return matrixIndicatorIndicator;
	}

	public void setMatrixIndicatorIndicator(final PairWiseMatrix matrixIndicatorIndicator) {
		this.matrixIndicatorIndicator = matrixIndicatorIndicator;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Collection<Indicator> getIndicators() {
		return indicators;
	}

	enum XmlKey {
		id, name, criteria, indicator, row, prefmatrix
	}
}
