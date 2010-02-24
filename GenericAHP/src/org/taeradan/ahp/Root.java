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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

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
	private PriorityVector vectorAltOg;
	private ArrayList alternatives;
	private Matrix matrixAltCr;
//	XML configuration attributes
	private String inConfigurationFile = "/org/taeradan/ahp/conf/ahp_conf.xml";
	private String outConfigurationFile = "/org/taeradan/ahp/conf/ahp_conf_out.xml";
	private Document inXmlDocument;
	private Document outXmlDocument;
//	Control attributes
	private boolean calculationOccured = false;
	
	/**
	 * Class default constructor.
	 */
	public Root() {
		name = new String();
		matrixCrCr = new PreferenceMatrix();
		criterias = new ArrayList<Criteria>();
	}
	
	/**
	 * Class constructor that creates the AHP tree from a configuration file given in argument.
	 * @param inFile Path to the configuration file
	 */
	public Root(String inFile) {
//		XML parser creation
		SAXBuilder parser = new SAXBuilder();
		try {
//			JDOM document created from XML configuration file
			inXmlDocument = parser.build(new File(inFile));
//			Extraction of the root element from the JDOM document
			Element xmlRoot = inXmlDocument.getRootElement();

//			Initialisation of the AHP tree name
			name=xmlRoot.getChildText("name");
//			System.out.println("Root.name="+name);

//			Initialisation of the preference matrix
			Element xmlPrefMatrix = xmlRoot.getChild("prefmatrix");
			matrixCrCr = new PreferenceMatrix(xmlPrefMatrix);
//			System.out.println("Root.matrixCrCr="+matrixCrCr);
			vectorCrOg = new PriorityVector(matrixCrCr);
//			Consistency verification
			if(!ConsistencyChecker.isConsistent(matrixCrCr, vectorCrOg)){
				System.err.println("Is not consistent (root)");
			}

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
			System.err.println("File not found : " + inConfigurationFile);
			name = "unknow";
			matrixCrCr = new PreferenceMatrix();
			criterias = new ArrayList<Criteria>();
		}catch(JDOMException e){
			System.err.println(e);
		}catch(IOException e){
			System.err.println(e);
		}
	}

	public void delCriteria(Criteria crit) {
		if(criterias.contains(crit)){
			int i = criterias.lastIndexOf(crit);
			criterias.remove(i);
			matrixCrCr.remove(i);
		}
		else
			System.err.println("Criteria not found");
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
	public String toStringRecursive(){
		String string = this.toString();
		string = string.concat("\n"+matrixCrCr);
		DecimalFormat printFormat = new DecimalFormat("0.000");
		for(int i=0; i<criterias.size();i++){
			string = string.concat("\n\t("+printFormat.format(vectorCrOg.getVector().get(i, 0))+") "+criterias.get(i).toStringRecursive());
		}
		return string;
	}
	
	/**
	 * Returns a JDOM element that represents the AHP root and its children
	 * @return JDOM Element representing the whole AHP tree
	 */
	public Element toXml(){
		Element xmlRoot = new Element("root");
		xmlRoot.addContent(new Element("name").setText(name));
		xmlRoot.addContent(matrixCrCr.toXml());
		for(int i=0; i<criterias.size(); i++)
			xmlRoot.addContent(criterias.get(i).toXml());
		return xmlRoot;
	}
	
	public String resultToString(){
		String string;
		if(calculationOccured){
			string = this.toString();
			for(int i=0; i<criterias.size();i++){
				string = string.concat("\n\t"+criterias.get(i).resultToString());
			}
			string = string.concat("\nvectorAltOg="+PreferenceMatrix.toString(vectorAltOg.getVector(),null));
		}
		else
			string = "There is no result, please do a ranking first";
		return string;
	}
	
	/**
	 * Saves the whole AHP tree in a XML file
	 * @param outputFile Output XML file path
	 */
	public void saveConfig(String outputFile){
		try {
//			Save the AHP tree in a XML document matching the Doctype "ahp_conf.dtd"
			outXmlDocument = new Document(toXml(), new DocType("root", getClass().getResource("/org/taeradan/ahp/conf/ahp_conf.dtd").getFile()));
//			Use a write format easily readable by a human
			XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
//			Write the output into the specified file
			output.output(outXmlDocument, new FileOutputStream(outputFile));
		} catch (IOException ex) {
			Logger.getLogger(Root.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Root method of the AHP execution.
	 * Calculates the final alternatives ranking with the alternatives priority vectors from the criterias and the criterias priority vectors.
	 * @return ArrayList containing the ranked alternatives.
	 */
	public ArrayList calculateRanking(ArrayList alts) {
		alternatives = alts;
		matrixAltCr = new Matrix(alternatives.size(), criterias.size());
//		Concatenation in a matrix of the vectors calculated by the criterias
		for (int i = 0; i < criterias.size(); i++) {
			matrixAltCr.setMatrix(0, alternatives.size()-1, i, i, criterias.get(i).calculateAlternativesPriorityVector(alternatives).getVector());
		}
//		Calculation of the final alternatives priority vector
		vectorAltOg = new PriorityVector();
		vectorAltOg.setVector(matrixAltCr.times(vectorCrOg.getVector()));
//		Ranking of the alternatives with the initial alternatives array and  the MOg vector
		ArrayList sortedAlternatives = (ArrayList)alternatives.clone();
		Matrix sortedvectorAltOg = new Matrix(vectorAltOg.getVector().getArrayCopy());
		int minIndex;
		for(int i=0; i<alternatives.size(); i++){
			minIndex = i;
			for(int j=i+1; j<alternatives.size(); j++){
				if(sortedvectorAltOg.get(j, 0)>sortedvectorAltOg.get(minIndex, 0))
				    minIndex = j;
			}
			Object tempAlt = sortedAlternatives.get(i);
			sortedAlternatives.set(i, sortedAlternatives.get(minIndex));
			sortedAlternatives.set(minIndex, tempAlt);
			double tempValue =  sortedvectorAltOg.get(i, 0);
			sortedvectorAltOg.set(i, 0, sortedvectorAltOg.get(minIndex, 0));
			sortedvectorAltOg.set(minIndex, 0, tempValue);
		}
		calculationOccured = true;
		return sortedAlternatives;
	}

	public PreferenceMatrix getMatrixCrCr() {
		return matrixCrCr;
	}

	public void setMatrixCrCr(PreferenceMatrix matrixCrCr) {
		this.matrixCrCr = matrixCrCr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<Criteria> getCriterias() {
		return criterias;
	}
}
