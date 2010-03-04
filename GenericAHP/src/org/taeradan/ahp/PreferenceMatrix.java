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
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Logger;
import org.jdom.Attribute;
import org.jdom.Element;
import org.nfunk.jep.JEP;

/**
 * The PreferenceMatrix class is a container for a Matrix adapted to the needs of AHP in terms of configuration.
 * @author Yves Dubromelle
 */
public class PreferenceMatrix {

	/**
	 *
	 */
	private Matrix matrix = null;

	/**
	 * Class default Constructor.
	 */
	public PreferenceMatrix() {
	}

	/**
	 * Creates a PreferenceMatrix from a JDOM Element
	 * @param xmlPrefMatrix JDOM Element
	 */
	public PreferenceMatrix(Element xmlPrefMatrix) {
		@SuppressWarnings("unchecked")
		List<Element> xmlRowsList = (List<Element>) xmlPrefMatrix.getChildren("row");
		int size = xmlRowsList.size();
		matrix = new Matrix(size, size);
		for(int i = 0; i < xmlRowsList.size(); i++) {
//			Extraction of a row from the matrix
			Element xmlRow = xmlRowsList.get(i);
			@SuppressWarnings("unchecked")
			List<Element> xmlEltsList = (List<Element>) xmlRow.getChildren("elt");
			for(int j = 0; j < xmlEltsList.size(); j++) {
//				Extraction of an element from a row
				Element xmlElt = xmlEltsList.get(j);
//				Setting of an element of the temporary matrix
				final Attribute att = xmlElt.getAttribute("value");
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

	/**
	 *
	 * @param prefix
	 * @return
	 */
	public String toString(String prefix) {
		return makeString(this.matrix, prefix);
	}

	/**
	 *
	 * @param matrix
	 * @param prefix
	 * @return
	 */
	public static String toString(Matrix matrix, String prefix) {
		return makeString(matrix, prefix);
	}

	/**
	 * 
	 * @param matrix
	 * @param prefix
	 * @return
	 */
	private static String makeString(Matrix matrix, String prefix) {
		StringBuilder string = new StringBuilder();
		int nRows = matrix.getRowDimension();
		int nCols = matrix.getColumnDimension();
		DecimalFormat printFormat = new DecimalFormat("0.000");
//		For each row in the matrix
		for(int i = 0; i < nRows; i++) {
			if(prefix != null) {
				string.append(prefix);
			}
//			For each element of the row
			for(int j = 0; j < nCols; j++) {
				string.append(" ");
				string.append(printFormat.format(matrix.get(i, j)));
			}
//			Last row line return
			if(i < nRows - 1) {
				string.append("\n");
			}
		}
		return string.toString();
	}

	/**
	 * Returns a JDOM element that represents the preference matrix
	 * @return JDOM element representing the preference matrix
	 */
	public Element toXml() {
		Element xmlPrefMatrix = new Element("prefmatrix");
//		For each row in the matrix
		for(int i = 0; i < matrix.getRowDimension(); i++) {
			Element xmlRow = new Element("row");
//			For each element in the row
			for(int j = 0; j < matrix.getColumnDimension(); j++) {
				Element xmlElt = new Element("elt");
				xmlElt.setAttribute("value", Double.toString(matrix.get(i, j)));
				xmlRow.addContent(xmlElt);
			}
			xmlPrefMatrix.addContent(xmlRow);
		}
		return xmlPrefMatrix;
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

	/**
	 * 
	 * @param index
	 */
	public void remove(int index) {
		int newDimension = matrix.getRowDimension() - 1;
		Matrix newMatrix = new Matrix(newDimension, newDimension);
		Logger.getAnonymousLogger().info("Ancienne dimension =" + matrix.getRowDimension() + ", nouvelle=" + newDimension + "\n");
		int newI = 0;
		int newJ = 0;
		for(int i = 0; i < matrix.getRowDimension(); i++) {
			if(i != index) {
				for(int j = 0; j < matrix.getColumnDimension(); j++) {
					if(j != index) {
						double newValue = matrix.get(i, j);
						Logger.getAnonymousLogger().info("i=" + i + "j=" + j + "value=" + newValue + "newI=" + newI + "newJ=" + newJ + "\n");
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
