/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsistenceMakerTemp;

import org.taeradan.ahp.ConsistencyMaker.MyMatrix;

/**
 *
 * @author Marianne
 */
/*
 * This class aims to provide a Matrix created from an Array
 */
public class ArrayToMatrix {

	/**
	 * constructeur
	 */
	public ArrayToMatrix() {
	}

	/**
	 * 
	 * @param rowData
	 * @param rows
	 * @param columns
	 * @return
	 */
	public MyMatrix convertArrayToMatrix(double[][] rowData, int rows, int columns) {

		MyMatrix m = new MyMatrix(rows, columns);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				m.set(i, j, rowData[i][j]);
			}
		}
		return m;

	}
}
