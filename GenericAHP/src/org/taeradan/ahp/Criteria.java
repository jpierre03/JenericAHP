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

import org.jdom.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents the criterias of the AHP tree, it contains Indicators and it executes its part of the AHP algorithm.
 *
 * @author Yves Dubromelle
 */
public class Criteria {

//	AHP configuration attributes
	/**
	 *
	 */
	private String identifier;
	/**
	 *
	 */
	private String name;
	/**
	 *
	 */
	transient private PairWiseMatrix matrixIndInd;
	/**
	 *
	 */
	transient private final PriorityVector vectorIndCr;
	/**
	 *
	 */
	transient private final Collection<Indicator> indicators;
//	AHP execution attributes
	/**
	 *
	 */
	transient private PriorityVector vectorAltCr;
	/**
	 *
	 */
	private final ConsistencyChecker consistencyChecker = new ConsistencyChecker();

	/**
	 * Creates a AHP Criteria from a JDOM Element
	 *
	 * @param xmlCriteria
	 */
	public Criteria(final Element xmlCriteria) {
//		Initialisation of the id of the criteria
		identifier = xmlCriteria.getAttributeValue("id");
//		System.out.println("\tCriteria.id="+id);

//		Initialisation of the name
		name = xmlCriteria.getChildText("name");
//		System.out.println("\tCriteria.name="+name);

//		Initialisation of the preference matrix
		final Element xmlPrefMatrix = xmlCriteria.getChild("prefmatrix");
		matrixIndInd = PairWiseMatrix.builder(xmlPrefMatrix);
//		System.out.println("\tCriteria.matrixIndInd="+matrixIndInd);
		vectorIndCr = PriorityVector.build(matrixIndInd);

//		Consistency verification
		if (!consistencyChecker.isConsistent(matrixIndInd, vectorIndCr)) {
			Logger.getAnonymousLogger().log(Level.SEVERE,
				"Is not consistent (criteria {0})",
				identifier);
		}
//		Initialisation of the Indicators
		@SuppressWarnings("unchecked")
		final List<Element> xmlIndicatorsList = (List<Element>) xmlCriteria.getChildren("indicator");
		@SuppressWarnings("unchecked")
		final List<Element> xmlRowsList = (List<Element>) xmlPrefMatrix.getChildren(
			"row");
		indicators = new ArrayList<Indicator>(xmlIndicatorsList.size());
//		Verification that the number of indicators matches the size of the matrix
		if (xmlIndicatorsList.size() != xmlRowsList.size()) {
			Logger.getAnonymousLogger().severe(
				"Error : the number of Indicators and the size of the preference matrix does not match !");
		}
//		For each indicator declared in the configuration file
		final Iterator<Element> itxmlIndList = xmlIndicatorsList.iterator();
		while (itxmlIndList.hasNext()) {
			final Element xmlIndicator = itxmlIndList.next();
//				System.out.println("\tCriteria.xmlIndicator="+xmlIndicator);
//				System.out.println("\tCriteria.xmlIndicator.attValue="+xmlIndicator.getAttributeValue("id"));
			final String indName = Root.indicatorPath
				+ Indicator.class.getSimpleName()
				+ xmlIndicator.getAttributeValue("id");
			try {
//					Research of the class implementing the indicator , named "org.taeradan.ahp.ind.IndicatorCxIy"
				@SuppressWarnings("unchecked")
				final Class<? extends Indicator> indClass = (Class<? extends Indicator>) Class.
					forName(
						indName);
//					System.out.println("\t\tCriteria.indClass="+indClass);
//					Extraction of its constructor
				final Constructor<? extends Indicator> indConstruct = (Constructor<? extends Indicator>) indClass.
					getConstructor(Element.class);
//					System.out.println("\t\tCriteria.indConstruct="+indConstruct);
//					Instanciation of the indicator with its constructor
				indicators.add(indConstruct.newInstance(xmlIndicator));

//					System.out.println("\tCriteria.indicator="+indicators.get(i));
			} catch (NoSuchMethodException e) {
				Logger.getAnonymousLogger().log(Level.SEVERE, "Error : no such constructor :{0}", e);
			} catch (SecurityException e) {
				Logger.getAnonymousLogger().log(Level.SEVERE, "Error :{0}", e);
			} catch (ClassNotFoundException e) {
				Logger.getAnonymousLogger().log(Level.SEVERE, "Error : class {0} not found :{1}",
					new Object[]{indName, e});
			} catch (InstantiationException e) {
				Logger.getAnonymousLogger().log(Level.SEVERE, "Error :{0}", e);
			} catch (IllegalAccessException e) {
				Logger.getAnonymousLogger().log(Level.SEVERE, "Error :{0}", e);
			} catch (IllegalArgumentException e) {
				Logger.getAnonymousLogger().log(Level.SEVERE, "Error :{0}", e);
			} catch (InvocationTargetException e) {
				Logger.getAnonymousLogger().log(Level.SEVERE, "Error :{0}", e);
			}
		}
	}

	/**
	 * @param alternatives
	 * @return
	 */
	public PriorityVector calculateAlternativesPriorityVector(
		final Collection<? extends Alternative> alternatives) {
		PairWiseMatrix matrixAltInd;
		matrixAltInd = new PairWiseMatrix(alternatives.size(), indicators.size());
//		Concatenation of the indicators' alternatives vectors
		final Iterator<Indicator> itIndicators = indicators.iterator();
		int index = 0;
		while (itIndicators.hasNext()) {
			matrixAltInd.setMatrix(0, alternatives.size() - 1, index, index, itIndicators.next().
				calculateAlternativesPriorityVector(alternatives));
			index++;
		}
//		Calculation of the criteria's alternatives vector
		vectorAltCr = new PriorityVector(matrixAltInd.getRowDimension());
		vectorAltCr.setMatrix(matrixAltInd.getRowDimension() - 1, matrixAltInd.times(vectorIndCr));
		return vectorAltCr;
	}

	/**
	 * Returns a string describing the criteria, but not its children
	 *
	 * @return Criteria as a String
	 */
	@Override
	public String toString() {
		return "Criteria " + identifier + " : " + name;
	}

	/**
	 * Returns a string describing the criteria and all its children
	 *
	 * @return Criteria and children as a String
	 */
	public String toStringRecursive() {
		final StringBuilder string = new StringBuilder(this.toString());
		string.append("\n").append(matrixIndInd.toString("\t"));
		DecimalFormat printFormat = new DecimalFormat("0.000");
		final Iterator<Indicator> itIndicators = indicators.iterator();
		int index = 0;
		while (itIndicators.hasNext()) {
			string.append("\n\t\t(").
				append(printFormat.format(vectorIndCr.get(index, 0))).
				append(") ").
				append(itIndicators.next());
			index++;
		}
		return string.toString();
	}

	/**
	 * Returns a JDOM element that represents the criteria and all its children
	 *
	 * @return JDOM Element representing the criteria and children
	 */
	public Element toXml() {
		final Element xmlCriteria = new Element("criteria");
		xmlCriteria.setAttribute("id", identifier);
		xmlCriteria.addContent(new Element("name").setText(name));
		xmlCriteria.addContent(matrixIndInd.toXml());
		final Iterator<Indicator> itIndicators = indicators.iterator();
		while (itIndicators.hasNext()) {
			xmlCriteria.addContent(itIndicators.next().toXml());
		}
		return xmlCriteria;
	}

	/**
	 * @return
	 */
	public String resultToString() {
		final StringBuilder string = new StringBuilder(this.toString());
		final Iterator<Indicator> itIndicators = indicators.iterator();
		while (itIndicators.hasNext()) {
			string.append("\n\t\t").append(itIndicators.next().resultToString());
		}
		string.append("\n\tvectorAltCr=\n").append(PairWiseMatrix.toString(vectorAltCr, "\t"));
		return string.toString();
	}

	/**
	 * @return
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier
	 */
	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return
	 */
	public PairWiseMatrix getMatrixInd() {
		return matrixIndInd;
	}

	/**
	 * @param matrixInd
	 */
	public void setMatrixInd(final PairWiseMatrix matrixInd) {
		this.matrixIndInd = matrixInd;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public Collection<Indicator> getIndicators() {
		return indicators;
	}
}
