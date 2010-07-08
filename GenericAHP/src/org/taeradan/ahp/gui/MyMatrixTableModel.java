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
package org.taeradan.ahp.gui;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.table.DefaultTableModel;
import org.taeradan.ahp.ConsistencyMaker.MyMatrix;

/**
 * Specialised implementation of tableModel for the Preference Matrix needs
 * @author jpierre03
 * @author Marianne
 * @author Yves Dubromelle
 */
public class MyMatrixTableModel
		extends DefaultTableModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private MyMatrix matrix;

	public MyMatrixTableModel() {
	}

	private Collection<String> getColumnHeader() {
		Collection<String> aLegend = new ArrayList<String>();

		aLegend.add("");
		for (int i = 0; i < matrix.getColumnDimension(); i++) {
			aLegend.add("Critere" + i);
		}
		return aLegend;
	}

	public void setMatrix(MyMatrix matrix) {
		this.matrix = matrix;

		Object[][] data = new Object[matrix.getRowDimension() + 1][matrix.getColumnDimension() + 1];

		for (int i = 0; i < matrix.getRowDimension(); i++) {

			String columnNames = "Critere" + i;
			data[0][i+1] = columnNames;
			data[i+1][0] = columnNames;

			for (int j = 0; j < matrix.getColumnDimension(); j++) {
				data[i + 1][j + 1] = matrix.getMatrixValue(i, j);
			}
		}
		setDataVector(data, new String [matrix.getColumnDimension() + 1]);
	}

//	We override this method to make editable only half of the matrix.
//	The other half will be filled automatically by an event listener on the table.
	@Override
	public boolean isCellEditable(final int row, final int column) {
		//Define wich cells are editable
//		boolean editable = true;
//		if (column >= row) {
//			editable = false;
//		}
//		return editable;
		return false;
	}

	/**
	 * @return the matrix
	 */
	public MyMatrix getMatrix() {
		return matrix;
	}
}
