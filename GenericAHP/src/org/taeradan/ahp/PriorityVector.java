/* Copyright 2009 Yves Dubromelle @ LSIS(www.lsis.org)
 * 
 * This file is part of GenericAHP.
 * 
 * GenericAHP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GenericAHP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GenericAHP.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.taeradan.ahp;

import Jama.Matrix;
import java.math.BigDecimal;

/**
 *
 * @author Yves Dubromelle
 */
public class PriorityVector {

	private Matrix vector = null;
	boolean isUnderTreshold = true;

	public PriorityVector(PreferenceMatrix prefMatrix) {
		 constructVector(prefMatrix.getMatrix());
	}

	public PriorityVector(Matrix matrix) {
		constructVector(matrix);
	}

	PriorityVector() {
	}
	
	private void constructVector(Matrix matrix){
		Matrix multMatrix = (Matrix)matrix.clone();
//		matrix.print(5, 4);
		Matrix oldVector;
		int dimension = matrix.getRowDimension();
		Matrix e = new Matrix(1, dimension, 1);
//		System.out.println("e=" + PreferenceMatrix.toString(e));
		do {
//			System.out.println("Séparateur d'itération ");
			oldVector = vector;
			multMatrix = multMatrix.times(matrix);
			Matrix numerator = matrix.times(e.transpose());
//			System.out.println("\tNumerator=" + PreferenceMatrix.toString(numerator));
			Matrix denominator = e.times(matrix).times(e.transpose());
//			System.out.println("\tDenominator=" + PreferenceMatrix.toString(denominator));
			vector = numerator.timesEquals(1/denominator.get(0, 0));
//			System.out.println("\tvector(times)=" + PreferenceMatrix.toString(vector));
			if(oldVector!=null){
				Matrix difference = vector.minus(oldVector);
//				System.out.println("\tdifference=" + PreferenceMatrix.toString(difference));
				isUnderTreshold = true;
				for(int i=0; i<dimension; i++){
					double diff = difference.get(i, 0);
//					System.out.println("diff ="+diff);
					if(new BigDecimal(diff).abs().doubleValue()>1E-16){
						isUnderTreshold = false;
//						System.out.println("dirrefence en dessous du seuil");
					}
				}
			}
			else
				isUnderTreshold = false;
		} while (!isUnderTreshold);
//		vector.print(5, 4);
	}

	/**
	 * Method that give the Matrix contained in this class.
	 * @return vector
	 */
	public Matrix getVector() {
		return vector;
	}

	public void setVector(Matrix vector) {
		this.vector = vector;
	}
}
