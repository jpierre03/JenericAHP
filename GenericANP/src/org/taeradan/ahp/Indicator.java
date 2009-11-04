/* Copyright 2009 Yves Dubromelle, Thamer Louati @ LSIS(www.lsis.org)
 * 
 * This file is part of GenericANP.
 * 
 * GenericANP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GenericANP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GenericANP.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.taeradan.ahp;

import Jama.Matrix;
import java.util.ArrayList;
import org.jdom.Element;

/**
 * This class provides the basis of the indicators of AHP. It is abstract because we can't know what the calculateIndicator() method should do for each user.<br/>
 * The indicators implementing this class must be names with the syntax : "IndicatorCxIy", x being the criteria's number and y the indicator number within the criteria.
 * @author Yves Dubromelle
 */
public class Indicator {
//	AHP static attributes
	private String id;
	private String name;
	private DependanceMatrix matrixIndInd;
	private PriorityVector vectorIndInd;
	private boolean maximization = true;
//	AHP dynamic attributes
	private PriorityVector vectorAltInd;
	private Matrix matrixAltAlt;
	private ArrayList alternatives;

	/**
	 * Class default constructor
	 */
	public Indicator() {
		id = new String();
		name = new String();
		matrixIndInd = new DependanceMatrix();
//		We consider that a criteria must be maximized by default
		maximization = true;
	}

	/**
	 * Simple constructor to initialize an indicator by its ID and name
	 * @param id The indicator's ID
	 * @param name The indicator's name
	 */
	public Indicator(String id, String name, boolean maximization, DependanceMatrix matrixInd) {
		this.id = id;
		this.name = name;
		this.maximization = maximization;
		this.matrixIndInd = matrixInd;
	}

	/**
	 * Creates an Indicator from a JDOM Element
	 * @param xmlIndicator JDOM Element
	 */
	public Indicator(Element xmlIndicator) {
		fromXml(xmlIndicator);

	}

	/**
	 * Method called by the criterias for the execution of the AHP algorithm.
	 * @return MCr vector
	 */
	public PriorityVector calculateAlternativesPriorityVector(ArrayList alts) {
		alternatives = alts;
		double[] altValues = new double[alternatives.size()];
		int dimension = altValues.length;
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
	 * @param i Alternative to be evaluated from the list
	 * @return Indicator value
	 */
	public double calculateAlternativeValue(int i, ArrayList alternatives) {
		return 1;
	}

	/**
	 * Returns a string describing the indicator
	 * @return String describing the indicator
	 */
	@Override
	public String toString() {
		String string = "Indicator " + id + " : " + name;
		if(maximization) {
			string = string.concat(", maximize");
		}
		else {
			string = string.concat(", minimize");
		}
		return string;
	}

	public String toStringRecursive() {
		String string = "Indicator " + id + " : " + name;
		if(maximization) {
			string = string.concat(", maximize");
		}
		else {
			string = string.concat(", minimize");
		}
		//string = string.concat("\n\n"+matrixIndInd.toString("\t"));
		string = string.concat("\n\n" + PreferenceMatrix.toString(vectorIndInd.getVector(), "\t"));
		return string;
	}

	/**
	 * Returns a JDOM element that represents the indicator
	 * @return JDOM Element representing the indicator
	 */
	public Element toXml() {
		Element xmlIndicator = new Element("indicator");
		xmlIndicator.setAttribute("id", id);
		xmlIndicator.addContent(new Element("name").setText(name));
		if(maximization) {
			xmlIndicator.addContent(new Element("maximize"));
		}
		else {
			xmlIndicator.addContent(new Element("maximize"));
		}
		return xmlIndicator;
	}

	protected void fromXml(Element xmlIndicator) {

//		Initialisation of the id
		id = xmlIndicator.getAttributeValue("id");
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
		// Initialisation of the dependance matrix of indicator
		Element xmlDepMatrix = xmlIndicator.getChild("depmatrix");
		matrixIndInd = new DependanceMatrix(xmlDepMatrix);
		//System.out.println("\tIndicator.matrixIndInd="+matrixIndInd);
		vectorIndInd = new PriorityVector(matrixIndInd);

		if(!ConsistencyChecker.isConsistent(matrixIndInd, vectorIndInd)) {
			System.err.println("Is not consistent (Indicator " + id + ")");
		}
	}

	public String resultToString() {
		String string = this.toString();
		string = string.concat("\n\t\tmatrixAltAlt=\n" + PreferenceMatrix.toString(matrixAltAlt, "\t\t"));
		string = string.concat("\n\t\tvectorAltInd=\n" + PreferenceMatrix.toString(vectorAltInd.getVector(), "\t\t"));
		return string;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMaximization() {
		return maximization;
	}

	public void setMaximization(boolean maximization) {
		this.maximization = maximization;
	}

	public DependanceMatrix getMatrixInd() {
		return matrixIndInd;
	}

	public void setMatrixInd(DependanceMatrix matrixInd) {
		this.matrixIndInd = matrixInd;
	}

	public PriorityVector getVectorIndInd() {
		return vectorIndInd;
	}
}
