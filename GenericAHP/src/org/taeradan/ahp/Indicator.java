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

import java.util.Collection;

/**
 * This class provides the basis of the indicators of AHP. It is abstract because we can't know what the
 * calculateIndicator() method should do for each user.<br/> The indicators implementing this class must be names with
 * the syntax : "IndicatorCxIy", x being the criteria's number and y the indicator number within the criteria.
 *
 * @author Jean-Pierre PRUNARET
 * @author Yves Dubromelle
 */
public abstract class Indicator
		implements XmlOutputable {

    private static final String MAXIMIZE = "maximize";
    private static final String MINIMIZE = "minimize";

    //	AHP configuration attributes
	private String identifier;
	private String name;
	private boolean maximization = true;

	//	AHP execution attributes
	private PriorityVector                    alternativeIndicatorVector;
	private PairWiseMatrix                    alternativeAlternativeMatrix;
	private Collection<? extends Alternative> lastAlternatives;

	/** Creates an Indicator from a JDOM Element */
	protected Indicator(final Element xmlIndicator) {
		this.fromXml(xmlIndicator);
	}

	/** Method called by the criteria for the execution of the AHP algorithm. */
	public PriorityVector calculateAlternativesPriorityVector(
			final Collection<? extends Alternative> alternatives) {

		this.lastAlternatives = alternatives;
		final int dimension = this.lastAlternatives.size();
		double[] altValues = new double[dimension];

		alternativeAlternativeMatrix = new PairWiseMatrix(dimension, dimension);

//		For each alternative, evaluation of its value for the indicator
		for (int i = 0; i < this.lastAlternatives.size(); i++) {
			altValues[i] = calculateAlternativeValue(i, this.lastAlternatives);
		}

//		Construction of the alternative/alternative matrix
		buildAlternativeAlternativeMatrix(dimension, altValues);

//		Conversion from pairwise matrix to priority vector
		alternativeIndicatorVector = PriorityVector.build(alternativeAlternativeMatrix);

		return alternativeIndicatorVector;
	}

	private void buildAlternativeAlternativeMatrix(int dimension, double[] altValues) {
		for (int i = 0; i < dimension; i++) {
			alternativeAlternativeMatrix.set(i, i, 1);
			for (int j = 0; j < i; j++) {
				if (maximization) {
					alternativeAlternativeMatrix.set(i, j, altValues[i] / altValues[j]);
					alternativeAlternativeMatrix.set(j, i, altValues[j] / altValues[i]);
				} else {
					alternativeAlternativeMatrix.set(j, i, altValues[i] / altValues[j]);
					alternativeAlternativeMatrix.set(i, j, altValues[j] / altValues[i]);
				}
			}
		}
	}

	/**
	 * Method that calculates the value (floating point) of the indicator for an alternative i.
	 *
	 * @param alternativeIndex Alternative to be evaluated from the list
	 * @param alternatives
	 * @return Indicator value
	 */
	public abstract double calculateAlternativeValue(int alternativeIndex,
													 Collection<? extends Alternative> alternatives);

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Indicator " + identifier + " : " + name);

        sb.append(", ");
		if (isMaximized()) {
            sb.append(MAXIMIZE);
		} else {
			sb.append(MINIMIZE);
		}
		return sb.toString();
	}

	@Override
	public Element toXml() {
		final Element xmlIndicator = new Element("indicator");
		xmlIndicator.setAttribute("id", identifier);
		xmlIndicator.addContent(new Element("name").setText(name));
		if (isMaximized()) {
			xmlIndicator.addContent(new Element(MAXIMIZE));
		} else {
			xmlIndicator.addContent(new Element(MINIMIZE));
		}
		return xmlIndicator;
	}

	protected final void fromXml(final Element xmlIndicator) {

//		Initialisation of the id
		identifier = xmlIndicator.getAttributeValue("id");
//		System.out.println("\t\tIndicator.id="+id);

//		Initialisation of the name
		name = xmlIndicator.getChildText("name");
//		System.out.println("\t\tIndicator.name="+name);

//		Initialisation of the maximization/minimization parameter
//		System.out.println("Maximise="+xmlIndicator.getChild("maximize")+", minimize="+xmlIndicator.getChild("minimize"));
		final Element maximize = xmlIndicator.getChild(MAXIMIZE);
		final Element minimize = xmlIndicator.getChild(MINIMIZE);
		if (maximize != null) {
			maximization = true;
		}
		if (minimize != null) {
			maximization = false;
		}
	}

	/**
	 * @return
	 */
	public String resultToString() {
		final int LIMIT_ALTERNATIVES = 30;

		final StringBuilder sb = new StringBuilder(this.toString());
		if (lastAlternatives.size() < LIMIT_ALTERNATIVES) {
			sb.append("\n\t\talternativeAlternativeMatrix=\n");
			sb.append(PairWiseMatrix.toString(alternativeAlternativeMatrix, "\t\t"));
		} else {
			sb.append("\n\t\t many alternatives (>" + LIMIT_ALTERNATIVES + ")\n");
		}
		sb.append("\n\t\talternativeIndicatorVector=\n");
		sb.append(PairWiseMatrix.toString(alternativeIndicatorVector, "\t\t"));

		return sb.toString();
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean isMaximized() {
		return maximization;
	}

	public void setMaximization(final boolean maximization) {
		this.maximization = maximization;
	}
}
