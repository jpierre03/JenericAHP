/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Element;

/**
 * This class represents the criterias of the AHP tree, it contains Indicators and it executes its part of the AHP algorithm.
 * @author Yves Dubromelle
 */
public class Criteria {
//	AHP static attributes
	private String id;
	private String name;
	private PreferenceMatrix matrixInd;
	private ArrayList<Indicator> indicators;
//	AHP dynamic attributes
	private PriorityVector vectorICrk;
	private AlternativesPriorityVector vectorAltCrk;
	private ArrayList alternatives;

	/**
	 * Class default constructor
	 */
	public Criteria() {
	}

	/**
	 * Creates a AHP Criteria from a JDOM Element
	 * @param xmlCriteria
	 */
	public Criteria(Element xmlCriteria) {
//		Initialisation of the id of the criteria
		id = xmlCriteria.getAttributeValue("id");
//		Initialisation of the name
		name = xmlCriteria.getChildText("name");
//		Initialisation of the preference matrix
		Element xmlPrefMatrix = xmlCriteria.getChild("prefmatrix");
		matrixInd = new PreferenceMatrix(xmlPrefMatrix);
//		Initialisation of the Indicators
		List<Element> xmlIndicatorsList = xmlCriteria.getChildren("indicator");
		List<Element> xmlRowsList = xmlPrefMatrix.getChildren("row");
		if(xmlIndicatorsList.size()!=xmlRowsList.size()){
			System.out.println("Error : the number of Indicators and the size of the preference matrix does not match !");
		}
		for(int i=0; i<xmlIndicatorsList.size(); i++){
				Element xmlIndicator = xmlIndicatorsList.get(i);
				String indName = "org.taeradan.ahp.ind.Indicator" + xmlIndicator.getAttributeValue(id);
				try {
					Class indClass = Class.forName(indName);
					Constructor<Indicator> indConstruct = indClass.getConstructor(Element.class);
					indicators.add(indConstruct.newInstance(xmlIndicator));
				} catch (NoSuchMethodException e) {
					System.out.println("Error : no such constructor in " + indName + " :" + e.getMessage());
				} catch (SecurityException e) {
					System.out.println("Error :" + e.getMessage());
				} catch (ClassNotFoundException e) {
					System.out.println("Error : class " + indName + " not found :" + e.getMessage());
				} catch (InstantiationException e) {
					System.out.println("Error :" + e.getMessage());
				} catch (IllegalAccessException e) {
					System.out.println("Error :" + e.getMessage());
				} catch (IllegalArgumentException e) {
					System.out.println("Error :" + e.getMessage());
				} catch (InvocationTargetException e) {
					System.out.println("Error :" + e.getMessage());
				}
		}
	}
}
