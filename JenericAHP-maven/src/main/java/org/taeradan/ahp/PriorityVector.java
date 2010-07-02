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
import java.math.BigDecimal;
import org.taeradan.ahp.ConsistencyMaker.MyMatrix;

/**
 *
 * @author Yves Dubromelle
 */
public class PriorityVector
		extends MyMatrix {

	/**
	 *
	 * @param i
	 */
	public PriorityVector(int i) {
		super(i, 1);
	}

	/**
	 *
	 * @param matrix
	 * @return
	 */
	public static PriorityVector build(final Matrix matrix) {
		final int dimension = matrix.getRowDimension();
		PriorityVector resultVector;
//		if (matrix.getRowDimension() < 2) {
		resultVector = new PriorityVector(dimension);
//		}
		Matrix workVector = new PriorityVector(dimension);
		Matrix multMatrix = (Matrix) matrix.clone();
//		matrix.print(5, 4);
		Matrix e = new Matrix(1, dimension, 1);
		Matrix lastVector;
		boolean isUnderTreshold = true;
		do {
			lastVector = workVector;
			final Matrix numerator = multMatrix.times(e.transpose());
			final Matrix denominator = e.times(multMatrix).times(e.transpose());
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
			multMatrix = multMatrix.times(matrix);
		} while (!isUnderTreshold);
		resultVector.setMatrix(dimension - 1, workVector);
		return resultVector;
	}

	/**
	 *
	 * @param i
	 * @param matrix
	 */
	void setMatrix(int i, Matrix matrix) {
		setMatrix(0, i, 0, 0, matrix);
	}
}
