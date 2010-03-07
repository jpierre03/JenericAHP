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
import java.util.Collection;
import org.jdom.Element;

/**
 * This class provides the basis of the indicators of AHP. It is abstract because we can't know what the calculateIndicator() method should do for each user.<br/>
 * The indicators implementing this class must be names with the syntax : "IndicatorCxIy", x being the criteria's number and y the indicator number within the criteria.
 * @author jpierre03
 * @author Yves Dubromelle
 */
public abstract class Indicator {

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
	transient private boolean maximization = true;
//	AHP execution attributes
	/**
	 *
	 */
	transient private PriorityVector vectorAltInd;
	/**
	 *
	 */
	transient private Matrix matrixAltAlt;
	/**
	 *
	 */
	transient private Collection<? extends Alternative> alternatives;

	/**
	 * Creates an Indicator from a JDOM Element
	 * @param xmlIndicator JDOM Element
	 */
	public Indicator(Element xmlIndicator) {
		this.fromXml(xmlIndicator);
	}

	/**
	 * Method called by the criterias for the execution of the AHP algorithm.
	 * @param alts
	 * @return MCr vector
	 */
	public PriorityVector calculateAlternativesPriorityVector(final Collection<? extends Alternative> alts) {
		alternatives = alts;
//		double[] altValues = new double[alternatives.size()];
		double[] altValues = new double[alternatives.size()];
		final int dimension = altValues.length;
		matrixAltAlt = new Matrix(dimension, dimension);
//		For each alternative, evaluation of its value for the indicator
		for(int i = 0; i < alternatives.size(); i++) {
			altValues[i] = calculateAlternativeValue(i, alternatives);
		}
//		Construction of the alternative/altervative matrix
		for(int i = 0; i < dimension; i++) {
			matrixAltAlt.set(i, i, 1);
			for(int j = 0; j < i; j++) {
				if(maximization) {
					matrixAltAlt.set(i, j, altValues[i] / altValues[j]);
					matrixAltAlt.set(j, i, altValues[j] / altValues[i]);
				}
				else {
					matrixAltAlt.set(j, i, altValues[i] / altValues[j]);
					matrixAltAlt.set(i, j, altValues[j] / altValues[i]);
				}
			}
		}
//		Conversion from pairwise matrix to priority vector
		vectorAltInd = new PriorityVector(matrixAltAlt);
		return vectorAltInd;
	}

	/**
	 * Method that calculates the value (floating point) of the indicator for an alternative i.
	 * @param altIndex Alternative to be evaluated from the list
	 * @param alternatives
	 * @return Indicator value
	 */
	public abstract double calculateAlternativeValue(int altIndex, Collection<? extends Alternative> alternatives);

	/**
	 * Returns a string describing the indicator
	 * @return String describing the indicator
	 */
	@Override
	public String toString() {
		String string = "Indicator " + identifier + " : " + name;
		if(maximization) {
			string = string.concat(", maximize");
		}
		else {
			string = string.concat(", minimize");
		}
		return string;
	}

	/**
	 * Returns a JDOM element that represents the indicator
	 * @return JDOM Element representing the indicator
	 */
	public Element toXml() {
		Element xmlIndicator = new Element("indicator");
		xmlIndicator.setAttribute("id", identifier);
		xmlIndicator.addContent(new Element("name").setText(name));
		if(maximization) {
			xmlIndicator.addContent(new Element("maximize"));
		}
		else {
			xmlIndicator.addContent(new Element("maximize"));
		}
		return xmlIndicator;
	}

	/**
	 *
	 * @param xmlIndicator
	 */
	protected final void fromXml(Element xmlIndicator) {

//		Initialisation of the id
		identifier = xmlIndicator.getAttributeValue("id");
//		System.out.println("\t\tIndicator.id="+id);

//		Initialisation of the name
		name = xmlIndicator.getChildText("name");
//		System.out.println("\t\tIndicator.name="+name);

//		Initialisation of the maximization/minimization parameter
//		System.out.println("Maximise="+xmlIndicator.getChild("maximize")+", minimize="+xmlIndicator.getChild("minimize"));
		Element maximize = xmlIndicator.getChild("maximize");
		Element minimize = xmlIndicator.getChild("minimize");
		if(maximize != null) {
			maximization = true;
		}
		if(minimize != null) {
			maximization = false;
		}
	}

	/**
	 *
	 * @return
	 */
	public String resultToString() {
		String string = this.toString();
		if(alternatives.size() < 30) {
			string = string.concat("\n\t\tmatrixAltAlt=\n" + PreferenceMatrix.toString(matrixAltAlt, "\t\t"));
		}
		string = string.concat("\n\t\tvectorAltInd=\n" + PreferenceMatrix.toString(vectorAltInd.getVector(), "\t\t"));
		return string;
	}

	/**
	 *
	 * @return
	 */
	public String getId() {
		return identifier;
	}

	/**
	 *
	 * @param identifier
	 */
	public void setId(String identifier) {
		this.identifier = identifier;
	}

	/**
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *
	 * @return
	 */
	public boolean isMaximized() {
		return maximization;
	}

	/**
	 *
	 * @param maximization
	 */
	public void setMaximization(boolean maximization) {
		this.maximization = maximization;
	}
}
