package org.taeradan.ahp.ConsistencyMaker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * @author jpierre03
 * @author Marianne
 */
public class MyMatrixTest {
	public static int ROWS=5;
	public static int COLUMN=5;

	public static void main(String[] args) {
	
		MyMatrix myMatrix = new MyMatrix(ROWS, COLUMN);

		myMatrix.print(ROWS, COLUMN);

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMN; j++) {
				MatrixValue matrixValue = new MatrixValue();
//				matrixValue.setValue((i + 1) * (j + 1));
				matrixValue.setValue((i - j) * (j + i));
				matrixValue.setRow(i);
				matrixValue.setColumn(j);

				myMatrix.set(matrixValue);
			}
		}

		myMatrix.print(ROWS, COLUMN);

		Collection<MatrixValue> matrixValues = new ArrayList<MatrixValue>();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMN; j++) {
				if (j > i) {
					matrixValues.add(myMatrix.getMatrixValue(i, j));
				}
			}
		}

		System.out.print(matrixValues);

		TreeMap<Double, MatrixValue> myTreeMap = new TreeMap<Double, MatrixValue>();

		for (Iterator<MatrixValue> valueIterator = matrixValues.iterator(); valueIterator.hasNext();) {
			MatrixValue matrixValue = valueIterator.next();
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
