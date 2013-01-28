/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.prototype.ConsistenceMakerTemp;

import org.taeradan.ahp.matrix.MyMatrix;

/**
 * This class aims to provide a Matrix created from an Array
 *
 * @author Marianne
 * @author Jean-Pierre PRUNARET
 */
@Deprecated
public final class ArrayToMatrix {

	private ArrayToMatrix() {
	}

	@Deprecated
	public static MyMatrix convertArrayToMatrix(double[][] rowData, int rows, int columns) {

		final MyMatrix matrix = new MyMatrix(rows, columns);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				matrix.set(i, j, rowData[i][j]);
			}
		}

		return matrix;
	}
}
