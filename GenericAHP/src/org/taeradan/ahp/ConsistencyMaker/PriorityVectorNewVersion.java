/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.ConsistencyMaker;

import Jama.Matrix;

/**
 *
 * @author Marianne
 */
public class PriorityVectorNewVersion
		extends MyMatrix {

	public PriorityVectorNewVersion() {
	}

	public static MyMatrix build(MyMatrix CC) {
		MyMatrix priorityVector = new MyMatrix(CC.getColumnDimension(), 1);
		Matrix tempPriorityVector = new Matrix(CC.getColumnDimension(), 1);
		MyMatrix eTranspose = new MyMatrix(CC.getColumnDimension(), 1);
		MyMatrix e = new MyMatrix(1, CC.getColumnDimension());
		MatrixValue matrixValue = new MatrixValue();
		MyMatrix CrOg = new MyMatrix(CC.getColumnDimension(), 1);

		for (int i = 0; i < CC.getColumnDimension(); i++) {
			matrixValue.setValue(1);
			matrixValue.setRow(i);
			matrixValue.setColumn(0);
			eTranspose.setMatrixValue(matrixValue);
			matrixValue.setRow(0);
			matrixValue.setColumn(i);
			e.setMatrixValue(matrixValue);
		}

		Matrix numerator = new Matrix(CC.getColumnDimension(), 1);
		numerator = CC.times(eTranspose);

		Matrix denominator = new Matrix(1, 1);
		denominator = e.times(CC.times(eTranspose));
		double scal = denominator.get(0, 0);
		tempPriorityVector = numerator.times(1 / scal);

		for (int i = 0; i < tempPriorityVector.getRowDimension(); i++) {
			matrixValue.setValue(tempPriorityVector.get(i, 0));
			matrixValue.setRow(i);
			matrixValue.setColumn(0);
			priorityVector.setMatrixValue(matrixValue);
		}


		return priorityVector;
	}
}
