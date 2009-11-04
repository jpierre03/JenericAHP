/* Copyright 2009 Thamer Louati @ LSIS(www.lsis.org)
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
import java.text.DecimalFormat;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;
import org.nfunk.jep.JEP;

/**
 *
 * @author Louati
 */
public class DependanceMatrix {
	private Matrix matrix = null;

	//Default Constructor/
	public DependanceMatrix() {
	}

	/**
	 * Creates a PreferenceMatrix from a JDOM Element
	 * @param xmlPrefMatrix JDOM Element
	 */
	public DependanceMatrix(Element xmlDepMatrix) {
		List<Element> xmlRowsList1 = xmlDepMatrix.getChildren("row");
		int size = xmlRowsList1.size();
		matrix = new Matrix(size, size);
		for(int i = 0; i < xmlRowsList1.size(); i++) {
//			Extraction of a row from the matrix
			Element xmlRow = xmlRowsList1.get(i);
			List<Element> xmlEltsList = xmlRow.getChildren("elt");
			for(int j = 0; j < xmlEltsList.size(); j++) {
//				Extraction of an element from a row
				Element xmlElt = xmlEltsList.get(j);
//				Setting of an element of the temporary matrix
				Attribute att = xmlElt.getAttribute("value");
//				Creation of a math expression parser to handle fractions in the XML file
				JEP myParser = new JEP();
//				The expression contained in the String is passed to the parser and is evaluated
				myParser.parseExpression(att.getValue());
				double value = myParser.getValue();
				matrix.set(i, j, value);
			}
		}
	}

	/**
	 * Describes a preference matrix in a String
	 * @return String description
	 */
	@Override
	public String toString() {
		return makeString(this.matrix, null);
	}

	public String toString(String prefix) {
		return makeString(this.matrix, prefix);
	}

	public static String toString(Matrix matrix, String prefix) {
		return makeString(matrix, prefix);
	}

	private static String makeString(Matrix matrix, String prefix) {
		String string = "";
		int nRows = matrix.getRowDimension();
		int nCols = matrix.getColumnDimension();
		DecimalFormat printFormat = new DecimalFormat("0.000");
//		For each row in the matrix
		for(int i = 0; i < nRows; i++) {
			if(prefix != null) {
				string = string.concat(prefix);
			}
//			For each element of the row
			for(int j = 0; j < nCols; j++) {
				string = string.concat(" " + printFormat.format(matrix.get(i, j)));
			}
//			Last row line return
			if(i < nRows - 1) {
				string = string.concat("\n");
			}
		}
		return string;
	}

	/**
	 * Returns a JDOM element that represents the preference matrix
	 * @return JDOM element representing the preference matrix
	 */
	public Element toXml() {
		Element xmlDepMatrix = new Element("depmatrix");
//		For each row in the matrix
		for(int i = 0; i < matrix.getRowDimension(); i++) {
			Element xmlRow = new Element("row");
//			For each element in the row
			for(int j = 0; j < matrix.getColumnDimension(); j++) {
				Element xmlElt = new Element("elt");
				xmlElt.setAttribute("value", Double.toString(matrix.get(i, j)));
				xmlRow.addContent(xmlElt);
			}
			xmlDepMatrix.addContent(xmlRow);
		}
		return xmlDepMatrix;
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

	public void remove(int index) {
		int newDimension = matrix.getRowDimension() - 1;
		Matrix newMatrix = new Matrix(newDimension, newDimension);
		System.out.println("Ancienne dimension =" + matrix.getRowDimension() + ", nouvelle=" + newDimension + "\n");
		int newI = 0;
		int newJ = 0;
		for(int i = 0; i < matrix.getRowDimension(); i++) {
			if(i != index) {
				for(int j = 0; j < matrix.getColumnDimension(); j++) {
					if(j != index) {
						double newValue = matrix.get(i, j);
						System.out.print("i=" + i + "j=" + j + "value=" + newValue + "newI=" + newI + "newJ=" + newJ + "\n");
						newMatrix.set(newI, newJ, newValue);
						newJ++;
					}
				}
				newJ = 0;
				newI++;
			}
		}
	}
}
