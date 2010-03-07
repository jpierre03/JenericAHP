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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import org.jdom.Element;

/**
 * This class represents the criterias of the AHP tree, it contains Indicators and it executes its part of the AHP algorithm.
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
    transient private PreferenceMatrix matrixIndInd;
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
     * Creates a AHP Criteria from a JDOM Element
     * @param xmlCriteria
     */
    public Criteria(Element xmlCriteria) {
//		Initialisation of the id of the criteria
        identifier = xmlCriteria.getAttributeValue("id");
//		System.out.println("\tCriteria.id="+id);

//		Initialisation of the name
        name = xmlCriteria.getChildText("name");
//		System.out.println("\tCriteria.name="+name);

//		Initialisation of the preference matrix
        Element xmlPrefMatrix = xmlCriteria.getChild("prefmatrix");
        matrixIndInd = new PreferenceMatrix(xmlPrefMatrix);
//		System.out.println("\tCriteria.matrixIndInd="+matrixIndInd);
        vectorIndCr = new PriorityVector(matrixIndInd);

//		Consistency verification
        if (!ConsistencyChecker.isConsistent(matrixIndInd, vectorIndCr)) {
            Logger.getAnonymousLogger().severe("Is not consistent (criteria " + identifier + ")");
        }
//		Initialisation of the Indicators
        @SuppressWarnings("unchecked")
        List<Element> xmlIndicatorsList = (List<Element>) xmlCriteria.getChildren("indicator");
        @SuppressWarnings("unchecked")
        List<Element> xmlRowsList = (List<Element>) xmlPrefMatrix.getChildren("row");
        indicators = new ArrayList<Indicator>(xmlIndicatorsList.size());
//		Verification that the number of indicators matches the size of the matrix
        if (xmlIndicatorsList.size() != xmlRowsList.size()) {
            Logger.getAnonymousLogger().severe("Error : the number of Indicators and the size of the preference matrix does not match !");
        }
//		For each indicator declared in the configuration file
        for (int i = 0; i < xmlIndicatorsList.size(); i++) {
            Element xmlIndicator = xmlIndicatorsList.get(i);
//				System.out.println("\tCriteria.xmlIndicator="+xmlIndicator);
//				System.out.println("\tCriteria.xmlIndicator.attValue="+xmlIndicator.getAttributeValue("id"));
            String indName = Root.indicatorPath + Indicator.class.getSimpleName() + xmlIndicator.getAttributeValue("id");
            try {
//					Research of the class implementing the indicator , named "org.taeradan.ahp.ind.IndicatorCxIy"
                @SuppressWarnings("unchecked")
                Class<? extends Indicator> indClass = (Class<? extends Indicator>) Class.forName(indName);
//					System.out.println("\t\tCriteria.indClass="+indClass);
//					Extraction of its constructor
                Constructor<? extends Indicator> indConstruct = (Constructor<? extends Indicator>) indClass.getConstructor(Element.class);
//					System.out.println("\t\tCriteria.indConstruct="+indConstruct);
//					Instanciation of the indicator with its constructor
                indicators.add(indConstruct.newInstance(xmlIndicator));

//					System.out.println("\tCriteria.indicator="+indicators.get(i));
            } catch (NoSuchMethodException e) {
                Logger.getAnonymousLogger().severe("Error : no such constructor :" + e);
            } catch (SecurityException e) {
                Logger.getAnonymousLogger().severe("Error :" + e);
            } catch (ClassNotFoundException e) {
                Logger.getAnonymousLogger().severe("Error : class " + indName + " not found :" + e);
            } catch (InstantiationException e) {
                Logger.getAnonymousLogger().severe("Error :" + e);
            } catch (IllegalAccessException e) {
                Logger.getAnonymousLogger().severe("Error :" + e);
            } catch (IllegalArgumentException e) {
                Logger.getAnonymousLogger().severe("Error :" + e);
            } catch (InvocationTargetException e) {
                Logger.getAnonymousLogger().severe("Error :" + e);
            }
        }
    }

    /**
     *
     * @param alternatives
     * @return
     */
    public PriorityVector calculateAlternativesPriorityVector(Collection<? extends Alternative> alternatives) {
        Matrix matrixAltInd;
        matrixAltInd = new Matrix(alternatives.size(), indicators.size());
//		Concatenation of the indicators' alternatives vectors
        for (int i = 0; i < indicators.size(); i++) {
            matrixAltInd.setMatrix(0, alternatives.size() - 1, i, i, ((Indicator) indicators.toArray()[i]).calculateAlternativesPriorityVector(alternatives).getVector());
        }
//		Calculation of the criteria's alternatives vector
        vectorAltCr = new PriorityVector();
        vectorAltCr.setVector(matrixAltInd.times(vectorIndCr.getVector()));
        return vectorAltCr;
    }

    /**
     * Returns a string describing the criteria, but not its children
     * @return Criteria as a String
     */
    @Override
    public String toString() {
        return "Criteria " + identifier + " : " + name;
    }

    /**
     * Returns a string describing the criteria and all its children
     * @return Criteria and children as a String
     */
    public String toStringRecursive() {
        String string = this.toString();
        string = string.concat("\n" + matrixIndInd.toString("\t"));
        DecimalFormat printFormat = new DecimalFormat("0.000");
        for (int i = 0; i < indicators.size(); i++) {
            string = string.concat("\n\t\t(" + printFormat.format(vectorIndCr.getVector().get(i, 0)) + ") " + indicators.toArray()[i]);
        }
        return string;
    }

    /**
     * Returns a JDOM element that represents the criteria and all its children
     * @return JDOM Element representing the criteria and children
     */
    public Element toXml() {
        Element xmlCriteria = new Element("criteria");
        xmlCriteria.setAttribute("id", identifier);
        xmlCriteria.addContent(new Element("name").setText(name));
        xmlCriteria.addContent(matrixIndInd.toXml());
        for (int i = 0; i < indicators.size(); i++) {
            xmlCriteria.addContent(((Indicator) indicators.toArray()[i]).toXml());
        }
        return xmlCriteria;
    }

    /**
     *
     * @return
     */
    public String resultToString() {
        String string = this.toString();
        for (int i = 0; i < indicators.size(); i++) {
            string = string.concat("\n\t\t" + ((Indicator) indicators.toArray()[i]).resultToString());
        }
        string = string.concat("\n\tvectorAltCr=\n" + PreferenceMatrix.toString(vectorAltCr.getVector(), "\t"));
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
    public PreferenceMatrix getMatrixInd() {
        return matrixIndInd;
    }

    /**
     *
     * @param matrixInd
     */
    public void setMatrixInd(PreferenceMatrix matrixInd) {
        this.matrixIndInd = matrixInd;
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
    public Collection<Indicator> getIndicators() {
        return indicators;
    }
}
