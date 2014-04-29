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

/**
 * @author Yves Dubromelle
 * @author Jean-Pierre PRUNARET
 */
public final class ConsistencyChecker {

	private static final double[] randomIndex = {0.00,
		0.00,
		0.58,
		0.90,
		1.12,
		1.24,
		1.32,
		1.41,
		1.45,
		1.49,
		1.51,
		1.48,
		1.56,
		1.57,
		1.59};
	private static final int MAX_NORMAL_MATRIX_SIZE = 15;
	private static final double CONSISTENCY_THRESHOLD = 10.0 / 100.0;
	private final ConsistencyData consistencyData = new ConsistencyData();

	public void computeConsistency(final Matrix preferenceMatrix,
				       final Matrix priorityVector) {
		assert preferenceMatrix != null : "preferenceMatrix should be not null";
		assert preferenceMatrix.getRowDimension() > 0;
		assert preferenceMatrix.getColumnDimension() > 0;
		assert priorityVector != null : "priorityVector should be not null";
		assert priorityVector.getRowDimension() > 0;
		assert priorityVector.getColumnDimension() > 0;

		final int preferenceMatrixDimension = preferenceMatrix.getRowDimension();
		if (preferenceMatrixDimension < 1) {
			throw new IllegalArgumentException(
				"Preference matrix is empty !!"
					+ preferenceMatrixDimension);
		}

		if (preferenceMatrixDimension > MAX_NORMAL_MATRIX_SIZE) {
			throw new IllegalArgumentException(
				"Preference matrix is too wide (" + MAX_NORMAL_MATRIX_SIZE + " max)!!"
					+ preferenceMatrixDimension);
		}

		if (preferenceMatrix.getRowDimension() != priorityVector.getRowDimension()) {
			throw new IllegalArgumentException(
				"The preference matrix and vector dimensions does not match (Row) !!"
					+ preferenceMatrix.getRowDimension() + "," + priorityVector.getRowDimension());
		}

		consistencyData.setConsistent(false);

		if (preferenceMatrixDimension == 1) {
			caseDimension_One();
		}

		if (preferenceMatrixDimension == 2) {
			caseDimension_Two(preferenceMatrix);
		}

		if (preferenceMatrixDimension > 2 && preferenceMatrixDimension <= MAX_NORMAL_MATRIX_SIZE) {
			caseDimension_NormalRange(preferenceMatrix, priorityVector, preferenceMatrixDimension);
		}
	}

	public boolean isConsistent(final Matrix preferenceMatrix,
				    final Matrix priorityVector) {
		computeConsistency(preferenceMatrix, priorityVector);

		return consistencyData.isConsistent();
	}

	private void caseDimension_One() {
		consistencyData.setConsistent(true);
	}

	private void caseDimension_Two(final Matrix preferenceMatrix) {
		if (preferenceMatrix.get(0, 1) == (1 / preferenceMatrix.get(1, 0))) {
			consistencyData.setConsistent(true);
		} else {
			consistencyData.setConsistent(false);
		}
	}

	private void caseDimension_NormalRange(final Matrix preferenceMatrix,
					       final Matrix priorityVector,
					       int preferenceMatrixDimension) {
		final double[] lambdas = new double[preferenceMatrixDimension];

		for (int i = 0; i < preferenceMatrixDimension; i++) {
			double sum = 0;
			for (int j = 0; j < preferenceMatrixDimension; j++) {
				sum = sum + preferenceMatrix.get(i, j) * priorityVector.get(j, 0);
			}
			lambdas[i] = sum / priorityVector.get(i, 0);
		}
		double lambdaMax = Double.MIN_VALUE;
		for (int index = 0; index < preferenceMatrixDimension; index++) {
			if (lambdas[index] > lambdaMax) {
				lambdaMax = lambdas[index];
			}
		}

		assert preferenceMatrixDimension - 1 != 0.0;

		final double consistencyIndex = (lambdaMax - preferenceMatrixDimension) / (preferenceMatrixDimension - 1);
		consistencyData.setConsistencyRatio(consistencyIndex / randomIndex[preferenceMatrixDimension - 1]);
	}

	public double getConsistencyRatio() {
		final Double consistencyRatio = consistencyData.getConsistencyRatio();

		if (consistencyRatio == null) {
			throw new RuntimeException("computing method have to be invoked first");
		}
		assert consistencyRatio != null;
		assert consistencyRatio.isInfinite() == false;
		assert consistencyRatio.isNaN() == false;

		assert consistencyRatio <= 100.0 / 100.0;
		assert consistencyRatio >= 0.0 / 100.0;

		return consistencyRatio;
	}

	private class ConsistencyData {

		private Double consistencyRatio = null;
		private boolean isConsistent;

		public Double getConsistencyRatio() {
			return consistencyRatio;
		}

		public void setConsistencyRatio(Double consistencyRatio) {
			this.consistencyRatio = consistencyRatio;

			if (this.consistencyRatio < CONSISTENCY_THRESHOLD) {
				setConsistent(true);
			}
		}

		public boolean isConsistent() {
			return isConsistent;
		}

		public void setConsistent(boolean consistent) {
			isConsistent = consistent;
		}
	}
}
