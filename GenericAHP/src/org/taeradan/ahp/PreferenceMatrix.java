/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.taeradan.ahp;

import Jama.Matrix;
import java.util.List;
import org.jdom.DataConversionException;
import org.jdom.Element;

/**
 * The PreferenceMatrix class is a container for a Matrix adapted to the needs of AHP in terms of configuration.
 * @author Yves Dubromelle
 */
class PreferenceMatrix{
    
	Matrix matrix = null;

	/**
	* Class default Constructor.
	*/
	public PreferenceMatrix(){
	}

	/**
	 * Creates a PreferenceMatrix from a JDOM Element
	 * @param xmlPrefMatrix JDOM Element
	 */
	public PreferenceMatrix(Element xmlPrefMatrix) {
//		Temporary matrix
		double[][] tempMatrix = null;
		List<Element> xmlRowsList = xmlPrefMatrix.getChildren("row");
		for(int i=0; i<xmlRowsList.size(); i++){
//			Extraction of a row from the matrix
			Element xmlRow = xmlRowsList.get(i);
			List<Element> xmlEltsList = xmlRow.getChildren("elt");
			for(int j=0; j<xmlEltsList.size();j++){
//				Extraction of an element from a row
				Element xmlElt = xmlEltsList.get(j);
//				Setting of an element of the temporary matrix
				try{
					tempMatrix[i][j] = xmlElt.getAttribute("value").getDoubleValue();
				}catch(DataConversionException e){
					System.out.println("Error with the value ("+i+","+j+") of prefmatrix :"+e.getMessage());
				}
			}
		}
	}
    
	/**
	* Method that give the Matrix contained in this class.
	* @return matrix
	*/
	public Matrix getMatrix() {
	return matrix;
	}

	/**
	* Method that overwrite the matrix contained in this class.
	* @param matrix
	*/
	public void setMatrix(Matrix matrix) {
	this.matrix = matrix;
	}
    
}
