package org.taeradan.ahp.matrix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

/**
 * @author Jean-Pierre PRUNARET
 * @author Marianne
 */
public final class MyMatrixTest {

	private static final int ROWS   = 5;
	private static final int COLUMN = 5;

	private MyMatrixTest() {
	}

	public static void main(String... args) {

		MyMatrix myMatrix = new MyMatrix(ROWS, COLUMN);

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMN; j++) {
				MatrixValue matrixValue = new MatrixValue(i, j, (i - j) * (j + i));

				myMatrix.setMatrixValue(matrixValue);
			}
		}

		Collection<MatrixValue> matrixValues = new ArrayList<>();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMN; j++) {
				if (j > i) {
					matrixValues.add(myMatrix.getMatrixValue(i, j));
				}
			}
		}

		System.out.print(matrixValues);

		TreeMap<Double, MatrixValue> myTreeMap = new TreeMap<>();

		for (MatrixValue matrixValue : matrixValues) {
			myTreeMap.put(matrixValue.getValue(), matrixValue);
		}

		System.out.println("-------------");

		while (!myTreeMap.isEmpty()) {
			MatrixValue matrixValue = myTreeMap.pollLastEntry().getValue();
			System.out.println(matrixValue.getValue()
							   + "( "
							   + matrixValue.getRow()
							   + " , "
							   + matrixValue.getColumn()
							   + " )");
		}
	}
}
