/* Copyright 2009 Yves Dubromelle @ LSIS(www.lsis.org)
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *  This is the root class of the AHP tree. It contains Criterias and execute its part of the AHP algorithm.
 * @author Yves Dubromelle
 */
public class Root {
//	AHP static attributes
	private String name;
	private PreferenceMatrix matrixCrCr;
	private PriorityVector vectorCrOg;
	private ArrayList<Criteria> criterias;
//	AHP dynamic attributes
	private AlternativesPriorityVector vectorMOg;
	private ArrayList alternatives;
	private Matrix matrixAltCr;
//	XML configuration attributes
	private String configurationFile = "/org/taeradan/ahp/conf/ahp_conf.xml";
	private Document xmlDocument;
	
	/**
	 * Class default constructor, creates the AHP tree from a configuration file.
	 */
	public Root() {
//		XML parser creation
		SAXBuilder parser = new SAXBuilder();
		try {
//			JDOM document created from XML configuration file
			
			xmlDocument = parser.build(getClass().getResource(configurationFile));
//			Extraction of the root element from the JDOM document
			Element xmlRoot = xmlDocument.getRootElement();

//			Initialisation of the AHP tree name
			name=xmlRoot.getChildText("name");
//			System.out.println("Root.name="+name);

//			Initialisation of the preference matrix
			Element xmlPrefMatrix = xmlRoot.getChild("prefmatrix");
			matrixCrCr = new PreferenceMatrix(xmlPrefMatrix);
//			System.out.println("Root.matrixCrCr="+matrixCrCr);

//			Initialisation of the criterias
			List<Element> xmlCriteriasList = xmlRoot.getChildren("criteria");
			List<Element> xmlRowsList = xmlPrefMatrix.getChildren("row");
			criterias = new ArrayList<Criteria>(xmlCriteriasList.size());
//			Verification that the number of criterias matches the size of the preference matrix
			if(xmlCriteriasList.size()!=xmlRowsList.size()){
				System.err.println("Error : the number of criterias and the size of the preference matrix does not match !");
			}
			for(int i=0; i<xmlCriteriasList.size(); i++){
				Element xmlCriteria = xmlCriteriasList.get(i);
				criterias.add(new Criteria(xmlCriteria));
//				System.out.println("Root.criteria="+criterias.get(i));
			}
		}catch (FileNotFoundException e) {
			System.err.println("File not found : " + configurationFile);
			name = "unknow";
			matrixCrCr = new PreferenceMatrix();
			criterias = new ArrayList<Criteria>();
		}catch(JDOMException e){
			System.err.println(e);
		}catch(IOException e){
			System.err.println(e);
		}
	}

	/**
	 * Returns a string describing the AHP root, and NOT its children.
	 * @return Describing string
	 */
	@Override
	public String toString() {
		String string = " AHP Root : "+name+", "+criterias.size()+" criterias";
		return string;
	}
	
	/**
	 * Returns a big string (multiple lines) containing recursively the description of the whole AHP tree. Best suited for testing purposes.
	 * @return Multiline string describing the AHP tree
	 */
	public String treeToString(){
		String string = this.toString();
		string = string.concat("\n"+matrixCrCr);
		for(int i=0; i<criterias.size();i++){
			string = string.concat("\n\t"+criterias.get(i).treeToString());
		}
		return string;
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
	
	public ArrayList<Criteria> getCriterias() {
		return criterias;
	}
}
