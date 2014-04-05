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

/**
 * @author Yves Dubromelle
 */
public class PriorityVector
	extends MyMatrix {

	/**
	 * A standard matrix should be far smaller than that.
	 */
	private static final int MAX_DIMENSION = 1000;
	/**
	 * high value (normal genetic operations require high value)
	 */
	private static final int MAX_ITERATION = 150;
	/**
	 * This treshold is used to stop iteration
	 */
	private static final double EQUALITY_TRESHOLD = 1E-16;

	public PriorityVector(int i) {
		super(i, 1);
	}

	public static PriorityVector build(final Matrix matrix) {
		final int dimension = matrix.getRowDimension();
		assert matrix.getColumnDimension() == matrix.getRowDimension() : "the matrix should be square";
		assert dimension > 0 : "Matrix dimension, can't be negative";
		assert dimension <= MAX_DIMENSION : "So huge matrix, you should double check (size=" + dimension + ")";

		final PriorityVector resultVector = new PriorityVector(dimension);
		final Matrix e = new Matrix(1, dimension, 1);
		Matrix workVector = new PriorityVector(dimension);
		Matrix multiplyMatrix = (Matrix) matrix.clone();


		Matrix lastVector;
		boolean isUnderTreshold;
		int iteration = 0;
		do {
			lastVector = workVector;
			final Matrix numerator = multiplyMatrix.times(e.transpose());
			final Matrix denominator = e.times(multiplyMatrix).times(e.transpose());
			workVector = numerator.timesEquals(1 / denominator.get(0, 0));
			Matrix difference = workVector.minus(lastVector);
			isUnderTreshold = true;
			for (int i = 0; i < dimension; i++) {
				double valeur0 = difference.get(i, 0);

				if (new BigDecimal(valeur0).abs().doubleValue() > EQUALITY_TRESHOLD) {
					/** difference en dessous du seuil */
					isUnderTreshold = false;
				}
			}
			multiplyMatrix = multiplyMatrix.times(matrix);

			iteration++;
			assert iteration < MAX_ITERATION : "This should already be done. Something is wrong";
		} while (!isUnderTreshold);

		resultVector.setMatrix(dimension - 1, workVector);
		return resultVector;
	}

	protected void setMatrix(int i, final Matrix matrix) {
		setMatrix(0, i, 0, 0, matrix);
	}
}
