package org.taeradan.ahp.ConsistencyMaker;

import Jama.Matrix;

/**
 * @author jpierre03
 * @author Marianne
 */
public class MyMatrix
		extends Matrix {

	public MyMatrix(int i, int i1) {
		super(i, i1);
	}

	public void set(MatrixValue matrixValue) {
		super.set(matrixValue.getRow(),
				  matrixValue.getColumn(),
				  matrixValue.getValue());
	}

	public MatrixValue getMatrixValue(int i, int i1) {
		MatrixValue matrixValue = new MatrixValue();
		matrixValue.setValue(super.get(i, i1));
		matrixValue.setRow(i);
		matrixValue.setColumn(i1);
		
		return matrixValue;
	}
}
