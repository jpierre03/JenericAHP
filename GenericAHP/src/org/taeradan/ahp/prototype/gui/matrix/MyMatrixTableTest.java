/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.prototype.gui.matrix;

import org.taeradan.ahp.matrix.MatrixValue;
import org.taeradan.ahp.matrix.MyMatrix;
import org.taeradan.ahp.prototype.SampleMatrixHeaders;

import javax.swing.JFrame;

/**
 * @author Jean-Pierre PRUNARET
 * @author Marianne
 */
public final class MyMatrixTableTest {

	private MyMatrixTableTest() {
	}

	public static void main(String[] args) {

		final MyMatrixTable table = new MyMatrixTable();
		final MyMatrix matrix = new MyMatrix(MyMatrixTable.ROWS, MyMatrixTable.COLUMN);

		setTableFromMatrix(table, matrix);

		showTableInFrame(27 * matrix.getRowDimension(), table);
	}

	private static void setTableFromMatrix(MyMatrixTable table, MyMatrix matrix) {
		for (int i = 0; i < MyMatrixTable.ROWS; i++) {
			for (int j = 0; j < MyMatrixTable.COLUMN; j++) {
				MatrixValue matrixValue = new MatrixValue();
				matrixValue.setValue((i - j) * (j + i));
				matrixValue.setRow(i);
				matrixValue.setColumn(j);

				matrix.setMatrixValue(matrixValue);
			}
		}


		MyMatrixTableModel tableModel = new MyMatrixTableModel();

		//Attention true si c'est le 1er pb false si deuxième et false si la langue est francais
		tableModel.setMatrix(matrix, SampleMatrixHeaders.getColumnHeader(true, false));

		table.setModel(tableModel);
	}

	private static void showTableInFrame(int height, MyMatrixTable table) {
		// Show a frame with a table
		JFrame frame = new JFrame("A beautiful frame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(table);
		frame.setSize(1000, height);

		frame.setVisible(true);
	}
}
