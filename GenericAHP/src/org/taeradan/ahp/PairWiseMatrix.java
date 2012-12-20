/* Copyright 2009-2010 Yves Dubromelle @ LSIS(www.lsis.org)
 *
 * This file is part of JenericAHP.
 *
 * JenericAHP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JenericAHP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JenericAHP.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.taeradan.ahp;

import org.jdom.Attribute;
import org.jdom.Element;
import org.nfunk.jep.JEP;
import org.taeradan.ahp.ConsistencyMaker.MyMatrix;

import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Logger;

/**
 * The PairWiseMatrix class is a container for a Matrix adapted to the needs of AHP in terms of configuration.
 *
 * @author Jean-Pierre PRUNARET
 * @author Yves Dubromelle
 */
public class PairWiseMatrix
	extends MyMatrix {

	public PairWiseMatrix() {
	}

	public PairWiseMatrix(int i, int j) {
		super(i, j);
	}

	/**
	 * Creates a PairWiseMatrix from a JDOM Element
	 *
	 * @param xmlPrefMatrix JDOM Element
	 * @return
	 */
	public static PairWiseMatrix builder(final Element xmlPrefMatrix) {
		@SuppressWarnings("unchecked")
		final List<Element> xmlRowsList = (List<Element>) xmlPrefMatrix.getChildren("row");
		final int size = xmlRowsList.size();
		PairWiseMatrix matrix = new PairWiseMatrix(size, size);
		final JEP myParser = new JEP();
		for (int i = 0; i < size; i++) {
//			Extraction of a row from the matrix
			final Element xmlRow = xmlRowsList.get(i);
			@SuppressWarnings("unchecked")
			final List<Element> xmlEltsList = (List<Element>) xmlRow.getChildren("elt");
			for (int j = 0; j < xmlEltsList.size(); j++) {
//				Extraction of an element from a row
				final Element xmlElt = xmlEltsList.get(j);
//				Setting of an element of the temporary matrix
				final Attribute att = xmlElt.getAttribute("value");
//				Creation of a math expression parser to handle fractions in the XML file
//				The expression contained in the String is passed to the parser and is evaluated
				myParser.parseExpression(att.getValue());
				final double value = myParser.getValue();
				matrix.set(i, j, value);
			}
		}
		return matrix;
	}

	/**
	 * Describes a preference matrix in a String
	 *
	 * @return String description
	 */
	@Override
	public String toString() {
		return makeString(this, null);
	}

	public String toString(final String prefix) {
		return makeString(this, prefix);
	}

	public static String toString(final MyMatrix matrix, final String prefix) {
		return makeString(matrix, prefix);
	}

	private static String makeString(final MyMatrix matrix, final String prefix) {
		final StringBuilder string = new StringBuilder();
		final int nRows = matrix.getRowDimension();
		final int nCols = matrix.getColumnDimension();
		DecimalFormat printFormat = new DecimalFormat("0.00000000");
//		For each row in the matrix
		for (int i = 0; i < nRows; i++) {
			if (prefix != null) {
				string.append(prefix);
			}
//			For each element of the row
			for (int j = 0; j < nCols; j++) {
				string.append(" ");
				string.append(printFormat.format(matrix.get(i, j)));
			}
//			Last row line return
			if (i < nRows - 1) {
				string.append("\n");
			}
		}
		return string.toString();
	}

	/**
	 * Returns a JDOM element that represents the preference matrix
	 *
	 * @return JDOM element representing the preference matrix
	 */
	public Element toXml() {
		final Element xmlPrefMatrix = new Element("prefmatrix");
//		For each row in the matrix
		for (int i = 0; i < getRowDimension(); i++) {
			final Element xmlRow = new Element("row");
//			For each element in the row
			for (int j = 0; j < getColumnDimension(); j++) {
				final Element xmlElt = new Element("elt");
				xmlElt.setAttribute("value", Double.toString(getMatrixValue(i, j).getValue()));
				xmlRow.addContent(xmlElt);
			}
			xmlPrefMatrix.addContent(xmlRow);
		}
		return xmlPrefMatrix;
	}

	public void remove(final int index) {
		final int newDimension = getRowDimension() - 1;
		MyMatrix newMatrix = new MyMatrix(newDimension, newDimension);
		Logger.getAnonymousLogger().info("Ancienne dimension =" + getRowDimension()
			+ ", nouvelle=" + newDimension + "\n");
		int newI = 0;
		int newJ = 0;
		for (int i = 0; i < getRowDimension(); i++) {
			if (i != index) {
				for (int j = 0; j < getColumnDimension(); j++) {
					if (j != index) {
						final double newValue = getMatrixValue(i, j).getValue();
						Logger.getAnonymousLogger().info("i=" + i + "j=" + j + "value=" + newValue
							+ "newI=" + newI + "newJ=" + newJ + "\n");
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
