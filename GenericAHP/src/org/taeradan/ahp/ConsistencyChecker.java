/* Copyright 2009-2010 Yves Dubromelle
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

import static org.taeradan.ahp.UsualCheck.notNullOrFail;

/**
 * This class is used to comptute AHP conststency according to Saaty's Definition.
 *
 * @author Yves Dubromelle
 * @author Jean-Pierre PRUNARET
 */
public final class ConsistencyChecker {

	private static final double[] randomIndex = {
		0.00, 0.00, 0.58, 0.90, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49, 1.51, 1.48, 1.56, 1.57, 1.59
	};
	private static final int MAX_NORMAL_MATRIX_SIZE = 15;
	private static final double CONSISTENCY_THRESHOLD = 1.0 / 100.0;
	private final ConsistencyData consistencyData = new ConsistencyData();

	public void computeConsistency(final Matrix preferenceMatrix, final Matrix priorityVector) {

		notNullOrFail(preferenceMatrix, "preferenceMatrix should be not null");
		notNullOrFail(priorityVector, "priorityVector should be not null");

		final int preferenceMatrixDimension = preferenceMatrix.getRowDimension();


		checkMatricesDimensions(preferenceMatrix, priorityVector);

		consistencyData.setConsistent(false);

		final boolean isCaseOne = preferenceMatrixDimension == 1;
		final boolean isCaseTwo = preferenceMatrixDimension == 2;
		final boolean isGenericCase = preferenceMatrixDimension > 2 && preferenceMatrixDimension <= MAX_NORMAL_MATRIX_SIZE;

		if (isCaseOne) {
			caseDimension_One();
		} else if (isCaseTwo) {
			caseDimension_Two(preferenceMatrix);
		} else if (isGenericCase) {
			caseDimension_NormalRange(preferenceMatrix, priorityVector);
		} else {
			throw new IllegalStateException("This should not happend. Error in logic");
		}
	}

	public boolean isConsistent(final Matrix preferenceMatrix, final Matrix priorityVector) {
		computeConsistency(preferenceMatrix, priorityVector);

		return consistencyData.isConsistent();
	}

	public double getConsistencyRatio() {
		consistencyData.checkConsistencyInRange();

		final Double consistencyRatio = consistencyData.getConsistencyRatio();

		return consistencyRatio;
	}

	private void caseDimension_One() {
		consistencyData.setConsistent(true);
	}

	private void caseDimension_Two(final Matrix preferenceMatrix) {
		final boolean isOpposite = preferenceMatrix.get(0, 1) == (1 / preferenceMatrix.get(1, 0));

		if (isOpposite) {
			consistencyData.setConsistent(true);
		} else {
			consistencyData.setConsistent(false);
		}
	}

	private void caseDimension_NormalRange(final Matrix preferenceMatrix, final Matrix priorityVector) {

		final int preferenceMatrixDimension = preferenceMatrix.getRowDimension();
		final double[] lambdas = new double[preferenceMatrixDimension];

		for (int row = 0; row < preferenceMatrixDimension; row++) {
			double sum = 0;
			for (int column = 0; column < preferenceMatrixDimension; column++) {
				sum = sum + preferenceMatrix.get(row, column) * priorityVector.get(column, 0);
			}
			lambdas[row] = sum / priorityVector.get(row, 0);
		}
		double lambdaMax = Double.MIN_VALUE;
		for (int index = 0; index < preferenceMatrixDimension; index++) {
			if (lambdas[index] > lambdaMax) {
				lambdaMax = lambdas[index];
			}
		}

		if (lambdaMax <= Double.MIN_VALUE) {
			throw new IllegalStateException(
				String.format("LambdaMax should be defined (nor MIN_VALUE, nor MAX_Value). Value: %f", lambdaMax));
		}
		if (lambdaMax >= Double.MAX_VALUE) {
			throw new IllegalStateException(
				String.format("LambdaMax should be defined (nor MIN_VALUE, nor MAX_Value). Value: %f", lambdaMax));
		}
		if (preferenceMatrixDimension - 1 == 0.0) {
			throw new IllegalStateException("Preference matrix size must be greater than 1. Otherwise, divide by 0");
		}

		final double consistencyIndex = (lambdaMax - preferenceMatrixDimension) / (preferenceMatrixDimension - 1);
		final double consistencyRatio = (consistencyIndex / randomIndex[preferenceMatrixDimension]);
		consistencyData.setConsistencyRatio(consistencyRatio);
	}

	private static void checkMatricesDimensions(Matrix preferenceMatrix, Matrix priorityVector) {
		final int preferenceMatrixDimension = preferenceMatrix.getRowDimension();

		/** matrices properties */
		{
			if (preferenceMatrix.getRowDimension() <= 0) {
				throw new IllegalArgumentException();
			}
			if (preferenceMatrix.getColumnDimension() <= 0) {
				throw new IllegalArgumentException();
			}
			if (preferenceMatrix.getColumnDimension() != preferenceMatrix.getRowDimension()) {
				throw new IllegalArgumentException();
			}

			if (priorityVector.getRowDimension() <= 0) {
				throw new IllegalArgumentException();
			}
			if (priorityVector.getColumnDimension() <= 0) {
				throw new IllegalArgumentException();
			}
			if (priorityVector.getColumnDimension() != 1) {
				throw new IllegalArgumentException();
			}
		}

		if (preferenceMatrixDimension < 1) {
			throw new IllegalArgumentException(
				String.format("Preference matrix is empty !!  %d", preferenceMatrixDimension));
		}

		if (preferenceMatrixDimension > MAX_NORMAL_MATRIX_SIZE) {
			throw new IllegalArgumentException(
				String.format("Preference matrix is too wide (%d max)!!%d",
					MAX_NORMAL_MATRIX_SIZE,
					preferenceMatrixDimension));
		}

		if (preferenceMatrix.getRowDimension() != priorityVector.getRowDimension()) {
			throw new IllegalArgumentException(
				String.format("The preference matrix and vector dimensions does not match (Row) !!%d,%d",
					preferenceMatrix.getRowDimension(),
					priorityVector.getRowDimension()));
		}

		final boolean preferenceSizeGood = preferenceMatrixDimension <= randomIndex.length;
		if (!preferenceSizeGood) {
			throw new IllegalArgumentException(String.format(
				"Saaty random index not defined for this size (known size: %d) current size: %d)",
				randomIndex.length,
				preferenceMatrixDimension)
			);
		}
	}

	private class ConsistencyData {

		private Double consistencyRatio = null;
		private boolean isConsistent;

		Double getConsistencyRatio() {
			notNullOrFail(consistencyRatio, "consistency ratio have to be computed first");

			return consistencyRatio;
		}

		void setConsistencyRatio(Double consistencyRatio) {
			if (consistencyRatio < 0) {
				throw new IllegalArgumentException("Ratio must be positive. value: " + consistencyRatio);
			}

			assert !consistencyRatio.isInfinite();
			assert !consistencyRatio.isNaN();

			assert consistencyRatio <= 100.0 / 100.0;
			assert consistencyRatio >= 0.0 / 100.0;

			this.consistencyRatio = consistencyRatio;

			if (this.consistencyRatio < CONSISTENCY_THRESHOLD) {
				setConsistent(true);
			}
		}

		boolean isConsistent() {
			return isConsistent;
		}

		void setConsistent(boolean consistent) {
			isConsistent = consistent;
		}

		void checkConsistencyInRange() {
			notNullOrFail(consistencyRatio, "consistency ratio have to be computed first");

			assert !consistencyRatio.isInfinite();
			assert !consistencyRatio.isNaN();

			assert consistencyRatio <= 100.0 / 100.0;
			assert consistencyRatio >= 0.0 / 100.0;
		}
	}
}
