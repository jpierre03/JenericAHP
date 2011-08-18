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
	/*
	 * redéfinition de la classe getColumnHeader(), elle permet de retourner le tableau de string des column
	 * 
	 */
	public String[] getColumnHeader(boolean testB, boolean langueB)
	{
		if( testB == true){
			if(langueB==false)
			{
				String columnNames[] = {"Temps de transport", "Coût", "Confort" , "Pollution",
				"Qualité de service","Sécurité"};
				return columnNames;
			}
			else
			{
				String columnNames[] = {"Travel time", "Cost", "Comfort", "Pollution",
				"Quality of Service", "Security"};
				return columnNames;
			}			
		}
		else{
			if(langueB==false)
			{
				String columnNames[] = {"Prix", "Sécurité", "Pollution" , "Design",
				"Durée de vie", "Taille"};
				return columnNames;
			}
			else
			{
				String columnNames[] = {"Price", "Security", "Pollution","Design",
				"Life","Size"};
				return columnNames;
			}			
		}
	}
/*
	private Collection<String> getColumnHeader(){
		Collection<String> aLegend = new ArrayList<String>();

		aLegend.add("");
		for (int i = 0; i < matrix.getColumnDimension(); i++) {
			aLegend.add("Critere" + (i+1));			
		}
		return aLegend;
	}*/
	/**
	* Retourne vrai si la cellule est éditable : celle-ci sera donc éditable
	* @return boolean
	*/
	@Override
	public boolean isCellEditable(int row, int col){
		if(row>0&&col>row){
			setValueAt("", row, col);
			return true;
		}
		else return false;		
	}

	public void setMatrix(MyMatrix matrix,boolean testPb, boolean langueB) {
		this.matrix = matrix;
		Object[][] data = new Object[matrix.getRowDimension() + 1][matrix.getColumnDimension() + 1];

		for (int i = 0; i < matrix.getRowDimension(); i++) {
			//on cree les critère en fonction du pb choisi
			if( testPb == true){
				String columnNames[] = getColumnHeader(testPb, langueB);
				data[0][i+1] = columnNames[i];
				data[i+1][0] = columnNames[i];
			}
			else{
				String columnNames[] = getColumnHeader(testPb, langueB);
				data[0][i+1] = columnNames[i];
				data[i+1][0] = columnNames[i];
			}
			//String columnNames = "Critere" + (i+1);
			//data[0][i+1] = columnNames[i];
			//data[i+1][0] = columnNames;

			for (int j = 0; j < matrix.getColumnDimension(); j++) {
				data[i + 1][j + 1] = matrix.getMatrixValue(i, j);
			}
		}
		setDataVector(data, new String [matrix.getColumnDimension() + 1]);
	}

	/**
	 * @return the matrix
	 */
	public MyMatrix getMatrix() {
		return matrix;
	}
}
