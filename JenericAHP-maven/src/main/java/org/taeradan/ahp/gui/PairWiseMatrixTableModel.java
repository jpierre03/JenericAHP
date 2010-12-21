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

import javax.swing.table.DefaultTableModel;

/**
 * Specialised implementation of tableModel for the Preference Matrix needs
 * @author Yves Dubromelle
 * @author jpierre03
 */
public class PairWiseMatrixTableModel
		extends DefaultTableModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

//	We override this method to make editable only half of the matrix.
//	The other half will be filled automatically by an event listener on the table.
	@Override
	public boolean isCellEditable(final int row, final int column) {
		//Define wich cells are editable
		return false;
	}
}