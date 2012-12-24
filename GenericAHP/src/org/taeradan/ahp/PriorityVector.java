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

import Jama.Matrix;
import org.taeradan.ahp.matrix.MyMatrix;

import java.math.BigDecimal;

/** @author Yves Dubromelle */
public class PriorityVector
		extends MyMatrix {

	/** @param i  */
	public PriorityVector(int i) {
		super(i, 1);
	}

	/**
	 * @param matrix
	 * @return
	 */
	public static PriorityVector build(final Matrix matrix) {
		final int dimension = matrix.getRowDimension();
		assert dimension > 0;
		assert dimension < 15 : "So huge preference matrix, you should double check";

		final PriorityVector resultVector = new PriorityVector(dimension);
		final Matrix e = new Matrix(1, dimension, 1);
		Matrix workVector = new PriorityVector(dimension);
		Matrix multiplyMatrix = (Matrix) matrix.clone();

		final int MAX_ITERATION = 50;
		Matrix lastVector;
		boolean isUnderTreshold = true;
		int iteration = 0;
		do {
			lastVector = workVector;
			final Matrix numerator = multiplyMatrix.times(e.transpose());
			final Matrix denominator = e.times(multiplyMatrix).times(e.transpose());
			workVector = numerator.timesEquals(1 / denominator.get(0, 0));
			if (lastVector == null) {
				isUnderTreshold = false;
			} else {
				Matrix difference = workVector.minus(lastVector);
				isUnderTreshold = true;
				for (int i = 0; i < dimension; i++) {
					double valeur0 = difference.get(i, 0);
					if (new BigDecimal(valeur0).abs().doubleValue() > 1E-16) {
//						difference en dessous du seuil
						isUnderTreshold = false;
					}
				}
			}
			multiplyMatrix = multiplyMatrix.times(matrix);

			iteration++;
			assert iteration < MAX_ITERATION : "This should already be done. Something is wrong";
		} while (!isUnderTreshold);

		resultVector.setMatrix(dimension - 1, workVector);
		return resultVector;
	}

	/**
	 * @param i
	 * @param matrix
	 */
	void setMatrix(int i, Matrix matrix) {
		setMatrix(0, i, 0, 0, matrix);
	}
}
