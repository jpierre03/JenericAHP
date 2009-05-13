/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.taeradan.ahp.gui;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author taeradan
 */
public class PrefMatrixTableModel extends  DefaultTableModel{

	@Override
	public boolean isCellEditable(int row,int column){
		//Define wich cells are editable
		if(column >= row)
			return false;
		return true;
	}
}
