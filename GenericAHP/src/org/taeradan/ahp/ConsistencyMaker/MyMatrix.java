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
	 */
	public MyMatrix(){
		super(0, 0);
	}

	/**
	 *
	 * @param i
	 * @param j
	 */
	public MyMatrix(int i, int j) {
		super(i, j);
	}

	/**
	 *
	 * @param matrixValue
	 */
	public void setMatrixValue(MatrixValue matrixValue) {
		super.set(matrixValue.getRow(),
				  matrixValue.getColumn(),
				  matrixValue.getValue());
	}

	/**
	 *
	 * @param i
	 * @param j
	 * @return
	 */
	public MatrixValue getMatrixValue(int i, int j) {
		MatrixValue matrixValue = new MatrixValue();
		matrixValue.setValue(super.get(i, j));
		matrixValue.setRow(i);
		matrixValue.setColumn(j);

		return matrixValue;
	}
}
