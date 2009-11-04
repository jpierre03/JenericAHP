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
import org.taeradan.ahp.test.TestAhp;
import java.math.BigDecimal;
/**
 *  This is the root class of the AHP tree. It contains Criterias and execute its part of the AHP algorithm.
 * @author Yves Dubromelle
 */
public class Root {
//	AHP static attributes
	private String algorithm;
	private String name;
	private PreferenceMatrix matrixCrCr;
	private PriorityVector vectorCrOg;
	private ArrayList<Criteria> criterias;
//	AHP dynamic attributes
	private PriorityVector vectorAltOg;
	private ArrayList alternatives;
	private Matrix matrixAltCr;
	private Matrix supermatrix, supermatrixStock, supermatrixLim, supermatrixinter, vectorRank;
	private TestAhp testahp;
	private int sizesupermatrix, nbralt;
	private int nbrind = 0;
//	XML configuration attributes
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
	public Root(File inFile) {
//		XML parser creation
		SAXBuilder parser = new SAXBuilder();
		try {
//			JDOM document created from XML configuration file
			//inXmlDocument = parser.build(new File(inFile));
			inXmlDocument = parser.build(inFile);
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
			System.err.println("File not found : " + inFile);
			name = "unknow";
			matrixCrCr = new PreferenceMatrix();
			criterias = new ArrayList<Criteria>();
		}catch(JDOMException e){
			System.err.println(e);
		}catch(IOException e){
			System.err.println(e);
		}
               // Pour obtenir le nombre des alternatives
                testahp = new TestAhp();
                nbralt= testahp.nbralt();
                
                //Taille de la supermatrice
                sizesupermatrix= 1+ nbralt + criterias.size();
                for (int i=0; i< criterias.size(); i++) {
                  sizesupermatrix= sizesupermatrix + criterias.get(i).getIndicators().size(); 
                  // nbrind= Le  nombre des indicateurs
                  nbrind = nbrind+ criterias.get(i).getIndicators().size();
                } 
                
                //*********** Creation of the supermatrix************//
                
                supermatrix = new Matrix(sizesupermatrix,sizesupermatrix); 
                supermatrix.set(0, 0, 1);
                supermatrix.setMatrix(1, criterias.size(), 0, 0, vectorCrOg.getVector());
                int rang=criterias.size();
                int rang1=criterias.size()+1;
                for (int i=0; i< criterias.size(); i++) {
                    supermatrix.setMatrix(1, criterias.size(), i+1,i+1, criterias.get(i).getVectorCr().getVector());
                    supermatrix.setMatrix(rang +1, rang +criterias.get(i).getIndicators().size(), i+1,i+1, criterias.get(i).getVectorIndCr().getVector());
                    for (int j=0; j<criterias.get(i).getIndicators().size(); j++){
                     
                    supermatrix.setMatrix(criterias.size()+1, criterias.size()+ nbrind, rang1,rang1, criterias.get(i).getIndicators().get(j).getVectorIndInd().getVector());   
                    rang1=rang1+1;
                    }    
                    rang = rang + criterias.get(i).getIndicators().size();
                    
                }
                for (int i=0; i< nbralt;i++){
                  supermatrix.set(i+rang1, i+rang1, 1);  
                }
                //**************** Création de la matrice des données reelles *************//
                double[][] vals = {{3,5,3,2/4.,3,1/1200.,100,1},{3,5,3,2/4.,1,1/8638.,250,1},{5,5,5,2/3.,3,1/1800.,300,1},{3,3,3,3/4.,3,1/7136.,300,1}};
                //matrix of Alternatives-Indicators
                Matrix AltInd = new Matrix(vals);
                Matrix AltIndStock = new Matrix(vals);
                //Normalization of the simulation matrix
                double[] somme1 = new double[nbrind];
                
                for (int i=0; i<nbrind;i++){
                    for(int j=0; j<nbralt;j++){
                        somme1[i]=somme1[i]+AltInd.get(j, i);
                    }
                }
                for (int i=0; i<nbralt;i++){
                    for(int j=0; j<nbrind;j++){
                        AltIndStock.set(i,j , AltInd.get(i, j)/somme1[j]);
                    }
                }
                supermatrix.setMatrix(criterias.size()+nbrind+1, sizesupermatrix-1, criterias.size()+1,criterias.size()+nbrind , AltIndStock);
                
                //*******The stockastic supermatrix*********//
                supermatrixStock = new Matrix(sizesupermatrix,sizesupermatrix);
                 
                double[] somme = new double[sizesupermatrix];
                for (int i=0; i<sizesupermatrix;i++){
                    for(int j=0; j<sizesupermatrix;j++){
                        somme[i]=somme[i]+supermatrix.get(j, i);
                    }
                }
                for (int i=0; i<sizesupermatrix;i++){
                    for(int j=0; j<sizesupermatrix;j++){
                        supermatrixStock.set(i,j , supermatrix.get(i, j)/somme[j]);
                    }
                }
                //********* Construction of the Limit supermatrix**********//
	         supermatrixLim = new Matrix(sizesupermatrix,sizesupermatrix);
                 supermatrixLim = (Matrix)supermatrixStock.clone();
                 supermatrixinter = new Matrix(sizesupermatrix,sizesupermatrix);
                 boolean isUnderTreshold = true;
                 do {
			 supermatrixinter = (Matrix)supermatrixLim.clone();
			 supermatrixLim= supermatrixLim.times(supermatrixLim);
			if(supermatrixinter!=null){
			Matrix difference = supermatrixLim.minus(supermatrixinter);	
				isUnderTreshold = true;
				for(int i=0; i<sizesupermatrix; i++){
                                    for(int j=0;j<sizesupermatrix;j++){
					if(new BigDecimal(difference.get(i, j)).abs().doubleValue()>1E-16){
						isUnderTreshold = false;						
					}
				     }
			         }
			}
			else
				isUnderTreshold = false;
		} while (!isUnderTreshold);
                //****** Rank of alternatives*******//
                vectorRank = new Matrix(nbralt,1);
                vectorRank = (Matrix) supermatrixLim.getMatrix(sizesupermatrix-nbralt, sizesupermatrix-1,0,0).clone();
    
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
                string = string.concat("\n\n"+PreferenceMatrix.toString(vectorCrOg.getVector(), "\t"));
                
	        // affichage de la taille de supermatrix
                string = string.concat("\n Nbre alternatives  "+  nbralt);
                string = string.concat("\n Taille de la supermatrice  "+  sizesupermatrix);
                //affichage de la supermatrix
                string = string.concat("\n La supermatrice est   \n"+ PreferenceMatrix.toString(supermatrix,"\t")+"\n\n");
                string = string.concat("\n La supermatrice Stock est   \n"+ PreferenceMatrix.toString(supermatrixStock,"\t"));
                string = string.concat("\n La supermatrice Limite est   \n"+ PreferenceMatrix.toString(supermatrixLim,"\t"));
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
        public Matrix getSuperMatrix() {
		return supermatrix;
	}
        public Matrix getSuperMatrixLim() {
		return supermatrixLim;
	}
	public String getName() {
		return name;
	}
        public int getsizesupermatrix() {
		return sizesupermatrix;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<Criteria> getCriterias() {
		return criterias;
	}
}
