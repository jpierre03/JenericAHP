/* Copyright 2009 Yves Dubromelle, Thamer Louati @ LSIS(www.lsis.org)
 * 
 * This file is part of GenericANP.
 * 
 * GenericANP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GenericANP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GenericANP.  If not, see <http://www.gnu.org/licenses/>.
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
	private boolean isUnderTreshold = true;

	public PriorityVector(PreferenceMatrix prefMatrix) {
		 constructVector(prefMatrix.getMatrix());
	}

        public PriorityVector(DependanceMatrix depMatrix) {
		 constructVector(depMatrix.getMatrix());
	}
	public PriorityVector(Matrix matrix) {
		constructVector(matrix);
	}

	PriorityVector() {
	}
	
	private void constructVector(Matrix matrix){
		Matrix multMatrix = (Matrix)matrix.clone();
                boolean nulle =true;
		Matrix oldVector;
		int dimension = matrix.getRowDimension();
		Matrix e = new Matrix(1, dimension, 1);
                
//		System.out.println("e=" + PreferenceMatrix.toString(e));
                //if( matrix.equals(new ))
		for (int i=0;i<matrix.getColumnDimension();i++)
                     {
                       for (int j=0;j<matrix.getColumnDimension();j++){
                           if (matrix.get(i, j)!=0){
                               nulle= false;
                           }
                       } 
                }
                if (nulle==false){
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
                                        //System.out.println(difference.get(i, 0));
					if(new BigDecimal(difference.get(i, 0)).abs().doubleValue()>1E-16){
						isUnderTreshold = false;
//						System.out.println("dirrefence en dessous du seuil");
					}
				}
			}
			else
				isUnderTreshold = false;
		} while (!isUnderTreshold);
                } else{
                       vector = new Matrix(dimension,1);
                       for (int j=0;j< dimension;j++){
                           vector.set(j, 0, 0);
                           
                       } 
                }
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
