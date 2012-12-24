/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.prototype.gui.matrix;

import org.taeradan.ahp.matrix.MatrixValue;
import org.taeradan.ahp.matrix.MyMatrix;

import javax.swing.JFrame;

/**
 * @author Jean-Pierre PRUNARET
 * @author Marianne
 */
public final class MyMatrixTableTest {

	private MyMatrixTableTest() {
	}

	public static void main(String[] args) {
		MyMatrix aMatrix = new MyMatrix(MyMatrixTable.ROWS, MyMatrixTable.COLUMN);


		for (int i = 0; i < MyMatrixTable.ROWS; i++) {
			for (int j = 0; j < MyMatrixTable.COLUMN; j++) {
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
