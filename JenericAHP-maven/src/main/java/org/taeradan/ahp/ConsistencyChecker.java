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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yves Dubromelle
 * @author jpierre03
 */
public class ConsistencyChecker {

	/**
	 *
	 */
	private static final double[] randomIndex = {0.00, 0.00, 0.58, 0.90, 1.12,
												 1.24, 1.32, 1.41,
												 1.45, 1.49, 1.51, 1.48, 1.56,
												 1.57, 1.59};
	/**
	 * 
	 */
	private double consistenceCrit = 0;

//	/**
//	 *
//	 * @param prefMatrix
//	 * @param prioVector
//	 * @return
//	 */
//	public static boolean isConsistent(final PairWiseMatrix prefMatrix,
//									   final PriorityVector prioVector) {
//		return isConsistent(prefMatrix, prioVector);
//	}
	/**
	 *
	 * @param prefMatrix
	 * @param prioVector
	 * @return
	 */
	public boolean isConsistent(final Matrix prefMatrix,
								final Matrix prioVector) {
		boolean consistent = false;

		double[] lambdas;
		int dimension = 0;
		if (prefMatrix.getRowDimension() == prioVector.getRowDimension()) {
			dimension = prefMatrix.getRowDimension();
			if (dimension == 1) {
				consistent = true;
			} else if (dimension == 2) {
				if (prefMatrix.get(0, 1) == ( 1 / prefMatrix.get(1, 0) )) {
					consistent = true;
				} else {
					consistent = false;
				}
			} else if (dimension < 15 && dimension > 0) {
				lambdas = new double[dimension];
				for (int i = 0; i < dimension; i++) {
					double sum = 0;
					for (int j = 0; j < dimension; j++) {
						sum += prefMatrix.get(i, j) * prioVector.get(j, 0);
					}
					lambdas[i] = sum / prioVector.get(i, 0);
				}
				double lambdaMax = Double.MIN_VALUE;
				for (int index = 0; index < dimension; index++) {
					if (lambdas[index] > lambdaMax) {
						lambdaMax = lambdas[index];
					}
				}
				final double CI = ( lambdaMax - dimension ) / ( dimension - 1 );
				consistenceCrit = CI / randomIndex[dimension - 1];
				if (consistenceCrit < 0.1) {
					consistent = true;
				}
			} else {
				Logger.getAnonymousLogger().log(Level.SEVERE,
												"Preference matrix and priority vector are too wide (15 max) or empty !!{0}",
												dimension);
			}
		} else {
			Logger.getAnonymousLogger().severe("The matrix and vector dimension does not match !!" + prefMatrix.
					getRowDimension() + "," + prioVector.getRowDimension());
		}
		return consistent;
	}

	/**
	 *
	 * @return
	 */
	public double getCrResult() {
		return consistenceCrit;
	}
}
