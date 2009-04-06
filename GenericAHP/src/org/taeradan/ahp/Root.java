/* Copyright 2009 Yves Dubromelle
 * 
 * This file is part of GenericAHP.
 * 
 * GenericAHP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GenericAHP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GenericAHP.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.taeradan.ahp;

import Jama.Matrix;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 *  This is the root class of the AHP tree. It contains Criterias and execute its part of the AHP algorithm.
 * @author Yves Dubromelle
 */
public class Root {
//	AHP static attributes
	private String name;
	private PreferenceMatrix matrixCrCr;
	private ArrayList<Criteria> criterias;
//	AHP dynamic attributes
	private PriorityVector vectorCrOg;
	private AlternativesPriorityVector vectorMOg;
	private ArrayList alternatives;
	private Matrix matrixAltCr;
//	XML configuration attributes
	private String configurationFile = "conf/ahp_conf.xml";
	private Document xmlDocument;

	/**
	 * Class default constructor, creates the AHP tree from a configuration file.
	 */
	public Root() {
//		XML parser creation
		SAXBuilder parser = new SAXBuilder();
		try {
//			JDOM document created from XML configuration file
			File file = new File(configurationFile);
			xmlDocument = parser.build(file);
		} catch (Exception e) {
			System.out.println("File not found :" + configurationFile);
		}
//		Extraction of the root element from the JDOM document
		Element xmlRoot = xmlDocument.getRootElement();
		
//		Initialisation of the AHP tree name
		this.name=xmlRoot.getChildText("name");
		
//		Initialisation of the preference matrix
		Element xmlPrefMatrix = xmlRoot.getChild("prefmatrix");
		matrixCrCr = new PreferenceMatrix(xmlPrefMatrix);
		
//		Initialisation of the criterias
		List<Element> xmlCriteriasList = xmlRoot.getChildren("criteria");
		List<Element> xmlRowsList = xmlPrefMatrix.getChildren("row");
		if(xmlCriteriasList.size()!=xmlRowsList.size()){
			System.out.println("Error : the number of criterias and the size of the preference matrix does not match !");
		}
		for(int i=0; i<xmlCriteriasList.size(); i++){
			Element xmlCriteria = xmlCriteriasList.get(i);
			criterias.add(new Criteria(xmlCriteria));
		}
	}

	/**
	 * Root method of the AHP execution.
	 * Calculates the final alternatives ranking with the alternatives priority vectors from the criterias and the criterias priority vectors.
	 * @return ArrayList containing the ranked alternatives.
	 */
	public ArrayList calculateRanking() {
		ArrayList rankedAlternatives = null;
//		Concatenation in a matrix of the arrays calculated by the criterias
		for (int k = 0; k < criterias.size(); k++) {

		}
//		Calculation of the final alternatives priority vector
		vectorMOg.setMatrix(vectorCrOg.getMatrix().times(matrixAltCr));
//		Ranking of the alternatives with the initial alternatives array and  the MOg vector
		return rankedAlternatives;
	}
}
