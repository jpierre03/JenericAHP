/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.gui;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import org.taeradan.ahp.ConsistencyMaker.MatrixValue;
import org.taeradan.ahp.ConsistencyMaker.MyMatrix;

/**
 *
 * @author jpierre03
 * @author Marianne
 */
public class MyMatrixTable
		extends JTable {

	/**
	 *
	 */
	private final static int ROWS = 5;
	/**
	 *
	 */
	private final static int COLUMN = 5;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		MyMatrix aMatrix = new MyMatrix(ROWS, COLUMN);


		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMN; j++) {
				MatrixValue matrixValue = new MatrixValue();
				matrixValue.setValue((i - j) * (j + i));
				matrixValue.setRow(i);
				matrixValue.setColumn(j);

				aMatrix.setMatrixValue(matrixValue);
			}
		}

		MyMatrixTable maTable = new MyMatrixTable();
		MyMatrixTableModel matrixTableModel = new MyMatrixTableModel();

		matrixTableModel.setMatrix(aMatrix);

		maTable.setModel(matrixTableModel);


		// Show a frame with a table
		JFrame maFenetre = new JFrame("ma belle fenêtre");
		maFenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		maFenetre.setContentPane(maTable);
		maFenetre.setSize(1000,27*aMatrix.getRowDimension());
		
		maFenetre.setVisible(true);
	}
}
