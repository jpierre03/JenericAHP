package org.taeradan.ahp.matrix;

import Jama.Matrix;

/**
 * @author Jean-Pierre PRUNARET
 * @author Marianne
 */
public class MyMatrix
		extends Matrix {

	private static final long serialVersionUID = 1L;

	public MyMatrix() {
		super(0, 0);
	}

	public MyMatrix(int i, int j) {
		super(i, j);
	}

	public void setMatrixValue(MatrixValue matrixValue) {
		super.set(
				matrixValue.getRow(),
				matrixValue.getColumn(),
				matrixValue.getValue()
				 );
	}

	public MatrixValue getMatrixValue(int i, int j) {
		MatrixValue matrixValue = new MatrixValue();
		matrixValue.setValue(super.get(i, j));
		matrixValue.setRow(i);
		matrixValue.setColumn(j);

		return matrixValue;
	}

	public static MyMatrix copyMyMatrix(final MyMatrix initMatrix) {
		final MyMatrix finalMatrix = new MyMatrix(initMatrix.getRowDimension(), initMatrix.getColumnDimension());
		MatrixValue temp = new MatrixValue();

		for (int i = 0; i < initMatrix.getRowDimension(); i++) {
			for (int j = 0; j < initMatrix.getColumnDimension(); j++) {
				temp.setRow(i);
				temp.setColumn(j);
				temp.setValue(initMatrix.getMatrixValue(i, j).getValue());
				finalMatrix.setMatrixValue(temp);
			}
		}
		return finalMatrix;
	}
}
