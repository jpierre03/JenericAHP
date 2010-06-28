/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.ConsistencyMaker;

import org.taeradan.ahp.ConsistencyChecker;
import org.taeradan.ahp.PriorityVector;

/**
 *
 * @author Marianne
 */
public class RandomToolsTest {

	public static void main(String[] args) {

		MyMatrix myMatrix = new MyMatrix(3, 3);
		MatrixValue matrixValue = new MatrixValue();

		myMatrix.print(5, 5);

		/*	for (int i = 0; i < 5; i++) {
		for (int j = 0; j < 5; j++) {
		MatrixValue matrixValue = new MatrixValue();
		//				matrixValue.setValue((i + 1) * (j + 1));
		matrixValue.setValue((i - j) * (j + i));
		matrixValue.setRow(i);
		matrixValue.setColumn(j);

		myMatrix.setMatrixValue(matrixValue);
		}
		}*/


		matrixValue.setValue(1);
		matrixValue.setRow(0);
		matrixValue.setColumn(0);
		myMatrix.setMatrixValue(matrixValue);
		matrixValue.setValue(2);
		matrixValue.setRow(0);
		matrixValue.setColumn(1);
		myMatrix.setMatrixValue(matrixValue);
		matrixValue.setValue(4);
		matrixValue.setRow(0);
		matrixValue.setColumn(2);
		myMatrix.setMatrixValue(matrixValue);

		matrixValue.setValue(0.5);
		matrixValue.setRow(1);
		matrixValue.setColumn(0);
		myMatrix.setMatrixValue(matrixValue);
		matrixValue.setValue(1);
		matrixValue.setRow(1);
		matrixValue.setColumn(1);
		myMatrix.setMatrixValue(matrixValue);
		matrixValue.setValue(2);
		matrixValue.setRow(1);
		matrixValue.setColumn(2);
		myMatrix.setMatrixValue(matrixValue);
		matrixValue.setValue(0.25);
		matrixValue.setRow(2);
		matrixValue.setColumn(0);
		myMatrix.setMatrixValue(matrixValue);
		matrixValue.setValue(0.5);
		matrixValue.setRow(2);
		matrixValue.setColumn(1);
		myMatrix.setMatrixValue(matrixValue);
		matrixValue.setValue(1);
		matrixValue.setRow(2);
		matrixValue.setColumn(2);
		myMatrix.setMatrixValue(matrixValue);

		myMatrix.print(5, 2);

		PriorityVectorNewVersion priorityVector = new PriorityVectorNewVersion();
		priorityVector.build(myMatrix).print(5,5);


		/*	RandomTools randomTools = new RandomTools();
		MatrixValue matrixValue = randomTools.getValueToModifiyByRanking(myMatrix);
		System.out.println("En quoi souhaitez vous changer la valeur "
		+ matrixValue.getValue()
		+ " ( "
		+ matrixValue.getRow()
		+ " , "
		+ matrixValue.getColumn()
		+ " )");
		 */
	}
}
