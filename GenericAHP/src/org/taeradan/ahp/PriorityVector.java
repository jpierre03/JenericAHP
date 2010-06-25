/* Copyright 2009-2010 Yves Dubromelle @ LSIS(www.lsis.org)
 * 
 * This file is part of JenericAHP.
 * 
 * JenericAHP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JenericAHP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JenericAHP.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.taeradan.ahp;

import java.math.BigDecimal;
import org.taeradan.ahp.ConsistencyMaker.MatrixValue;
import org.taeradan.ahp.ConsistencyMaker.MyMatrix;

/**
 *
 * @author Yves Dubromelle
 */
public class PriorityVector {

	/**
	 *
	 */
	private MyMatrix vector = null;
	/**
	 *
	 */
	transient private boolean isUnderTreshold = true;

	/**
	 *
	 * @param prefMatrix
	 */
	public PriorityVector(final PreferenceMatrix prefMatrix) {
		constructVector(prefMatrix.getMatrix());
	}

	/**
	 *
	 * @param matrix
	 */
	public PriorityVector(final MyMatrix matrix) {
		constructVector(matrix);
	}

	/**
	 *
	 */
	PriorityVector() {
	}

	/**
	 *
	 * @param matrix
	 */
	private void constructVector(final MyMatrix matrix) {
		if (matrix.getRowDimension() < 2) {
			this.vector = new MyMatrix(matrix.getRowDimension(), 1);
		}
		MyMatrix multMatrix = (MyMatrix) matrix.clone();
//		matrix.print(5, 4);
		MyMatrix oldVector;
		final int dimension = matrix.getRowDimension();
		MyMatrix e = new MyMatrix(1,dimension); /*check*/
		MatrixValue tripletMatrixValue= new MatrixValue();
		for(int cptr=0;cptr<dimension;cptr++){
		//MyMatrix e = new MyMatrix(1, dimension, 1.0);
		tripletMatrixValue.setColumn(cptr);
		tripletMatrixValue.setRow(0);
		tripletMatrixValue.setValue(1);
		}
		do {
			oldVector = vector;
			final MyMatrix numerator = (MyMatrix) multMatrix.times(e.transpose());
			final MyMatrix denominator = (MyMatrix) e.times(multMatrix).times(e.transpose());
			vector = (MyMatrix) numerator.timesEquals(1 / denominator.get(0, 0));
			if (oldVector == null) {
				isUnderTreshold = false;
			} else {
				MyMatrix difference = (MyMatrix) vector.minus(oldVector);
				isUnderTreshold = true;
				for (int i = 0; i < dimension; i++) {
					if (new BigDecimal(difference.get(i, 0)).abs().doubleValue() > 1E-16) {
						isUnderTreshold = false;
//						dirrefence en dessous du seuil
					}
				}
			}
			multMatrix = (MyMatrix) multMatrix.times(matrix);
		} while (!isUnderTreshold);
//		vector.print(5, 4);
	}

	/**
	 * Method that give the Matrix contained in this class.
	 * @return vector
	 */
	public MyMatrix getVector() {
		return vector;
	}

	/**
	 * 
	 * @param vector
	 */
	public void setVector(final MyMatrix vector) {
		this.vector = vector;
	}
}
