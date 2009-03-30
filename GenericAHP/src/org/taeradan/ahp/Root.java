/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp;

import Jama.Matrix;
import java.io.File;
import java.util.ArrayList;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 *  This is the root class of the AHP tree. It contains Criterias and execute its part of the AHP algorithm.
 * @author Yves Dubromelle
 */
public class Root {

//	AHP attributes
	private ArrayList<Criteria> criterias;
	private PreferenceMatrix matrixCrCr;
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

		Element xmlRoot = xmlDocument.getRootElement();

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
