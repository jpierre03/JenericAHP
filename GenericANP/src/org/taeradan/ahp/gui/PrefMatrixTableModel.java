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

package org.taeradan.ahp.gui;

import javax.swing.table.DefaultTableModel;

/**
 * Specialised implementation of tableModel for the Preference Matrix needs
 * @author Yves Dubromelle
 */
public class PrefMatrixTableModel extends  DefaultTableModel{

//	We override this method to make editable only half of the matrix.
//	The other half will be filled automatically by an event listener on the table.
	@Override
	public boolean isCellEditable(int row,int column){
		//Define wich cells are editable
		if(column >= row)
			return false;
		return true;
	}
}
