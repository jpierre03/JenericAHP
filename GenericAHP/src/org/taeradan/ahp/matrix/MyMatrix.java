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
		this(0, 0);
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
		return new MatrixValue(i, j, super.get(i, j));
	}

	public static MyMatrix copyMyMatrix(final MyMatrix originalMatrix) {
		final MyMatrix duplicateMatrix = new MyMatrix(originalMatrix.getRowDimension(),
													  originalMatrix.getColumnDimension());
		for (int i = 0; i < originalMatrix.getRowDimension(); i++) {
			for (int j = 0; j < originalMatrix.getColumnDimension(); j++) {
				duplicateMatrix.setMatrixValue(new MatrixValue(originalMatrix.getMatrixValue(i, j)));
			}
		}

		return duplicateMatrix;
	}
}
