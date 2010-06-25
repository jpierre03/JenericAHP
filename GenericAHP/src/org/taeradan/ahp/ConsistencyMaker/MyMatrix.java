package org.taeradan.ahp.ConsistencyMaker;

import Jama.Matrix;

/**
 * @author jpierre03
 * @author Marianne
 */
public class MyMatrix
		extends Matrix {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * @param i
	 * @param i1
	 */
	public MyMatrix(int i, int i1) {
		super(i, i1);
	}

	/**
	 *
	 * @param matrixValue
	 */
	public void set(MatrixValue matrixValue) {
		super.set(matrixValue.getRow(),
				  matrixValue.getColumn(),
				  matrixValue.getValue());
	}

	/**
	 *
	 * @param i
	 * @param i1
	 * @return
	 */
	public MatrixValue getMatrixValue(int i, int i1) {
		MatrixValue matrixValue = new MatrixValue();
		matrixValue.setValue(super.get(i, i1));
		matrixValue.setRow(i);
		matrixValue.setColumn(i1);

		return matrixValue;
	}
}
