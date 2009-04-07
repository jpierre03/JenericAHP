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
import java.util.List;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;

/**
 * The PreferenceMatrix class is a container for a Matrix adapted to the needs of AHP in terms of configuration.
 * @author Yves Dubromelle
 */
public class PreferenceMatrix{
    
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
		List<Element> xmlRowsList = xmlPrefMatrix.getChildren("row");
		int size = xmlRowsList.size();
		matrix = new Matrix(size, size);
		for(int i=0; i<xmlRowsList.size(); i++){
//			Extraction of a row from the matrix
			Element xmlRow = xmlRowsList.get(i);
			List<Element> xmlEltsList = xmlRow.getChildren("elt");
			for(int j=0; j<xmlEltsList.size();j++){
//				Extraction of an element from a row
				Element xmlElt = xmlEltsList.get(j);
//				Setting of an element of the temporary matrix
				try{
					Attribute att = xmlElt.getAttribute("value");
					matrix.set(i, j, att.getDoubleValue());
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
