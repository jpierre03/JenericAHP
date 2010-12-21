package org.taeradan.ahp.ConsistencyMaker;

/**
 * @author jpierre03
 * @author Marianne
 */
public class MyMatrixTest {

	/**
	 *
	 */
	private final static int ROWS = 5;
	/**
	 *
	 */
	private final static int COLUMN = 5;

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		MyMatrix myMatrix = new MyMatrix(ROWS, COLUMN);

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMN; j++) {
				MatrixValue matrixValue = new MatrixValue();
//				matrixValue.setValue((i + 1) * (j + 1));
				matrixValue.setValue(( i - j ) * ( j + i ));
				matrixValue.setRow(i);
				matrixValue.setColumn(j);

				myMatrix.setMatrixValue(matrixValue);
			}
		}
	}
}
