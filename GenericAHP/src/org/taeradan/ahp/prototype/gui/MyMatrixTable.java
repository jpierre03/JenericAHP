/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.prototype.gui;

import org.taeradan.ahp.matrix.MatrixValue;
import org.taeradan.ahp.matrix.MyMatrix;

import javax.swing.*;

/**
 * @author Jean-Pierre PRUNARET
 * @author Marianne
 */
public class MyMatrixTable
	extends JTable {

	private static final int ROWS   = 5;
	private static final int COLUMN = 5;

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

		MyMatrixTable table = new MyMatrixTable();
		MyMatrixTableModel tableModel = new MyMatrixTableModel();

		//Attention true si c'est le 1er pb false si deuxième et false si la langue est francais
		tableModel.setMatrix(aMatrix, true, false);

		table.setModel(tableModel);


		// Show a frame with a table
		JFrame maFenetre = new JFrame("ma belle fenêtre");
		maFenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		maFenetre.setContentPane(table);
		maFenetre.setSize(1000, 27 * aMatrix.getRowDimension());

		maFenetre.setVisible(true);
	}
}
