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
package org.taeradan.ahp.prototype.gui;

import org.taeradan.ahp.matrix.MyMatrix;

import javax.swing.table.DefaultTableModel;

/**
 * Specialised implementation of tableModel for the Preference Matrix needs
 *
 * @author Yves Dubromelle
 * @author Jean-Pierre PRUNARET
 */
public class PairWiseMatrixTableModel
	extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	/**
	 * We override this method to make editable only half of the matrix. The other half will be filled automatically by an
	 * event listener on the table.
	 */
	@Override
	public boolean isCellEditable(final int row, final int column) {
		boolean editable = false;

		if (column > row) {
			editable = true;
		}

		return editable;
	}

	public final void setMatrix(final MyMatrix matrix, String... columnNames) {
		assert matrix != null;
		assert columnNames != null;
		if (columnNames.length == 0 || matrix.getColumnDimension() == 0) {
			throw new IllegalArgumentException("column names and matrix column count should be !=0");
		}
		if (matrix.getRowDimension() != matrix.getColumnDimension()) {
			throw new IllegalArgumentException("matrix should be square");
		}
		if (columnNames.length < matrix.getColumnDimension()) {
			throw new IllegalArgumentException("column names should be (at least) as long as matrix column count !");
		}

		Object[][] data = new Object[matrix.getRowDimension() + 1][matrix.getColumnDimension() + 1];

		for (int i = 0; i < matrix.getRowDimension(); i++) {
			data[0][i + 1] = columnNames[i];
			data[i + 1][0] = columnNames[i];

			for (int j = 0; j < matrix.getColumnDimension(); j++) {
				data[i + 1][j + 1] = matrix.getMatrixValue(i, j);
			}
		}
		setDataVector(data, new String[matrix.getColumnDimension() + 1]);
	}
}
