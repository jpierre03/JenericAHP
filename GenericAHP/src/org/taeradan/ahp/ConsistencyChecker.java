/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.taeradan.ahp;

import Jama.Matrix;

/**
 *
 * @author Yves Dubromelle
 */
public class ConsistencyChecker {
	
	private static final double[] randomIndex = {0.00, 0.00, 0.58, 0.90, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49, 1.51, 1.48, 1.56, 1.57, 1.59};

	public static boolean isConsistent(PreferenceMatrix prefMatrix, PriorityVector prioVector){
		boolean consistent = false;
		Matrix matrix = prefMatrix.getMatrix();
		Matrix vector = prioVector.getVector();
		double[] lambdas ;
		int dimension = 0;
		if(prefMatrix.getMatrix().getRowDimension()==prioVector.getVector().getRowDimension()){
			dimension = prefMatrix.getMatrix().getRowDimension();
			if(dimension==1)
				consistent = true;
			if(dimension<15 && dimension>0){
				lambdas = new double[dimension];
				for(int i=0; i<dimension; i++){
					double sum = 0;
					for(int j=0; j<dimension; j++){
						sum = sum + matrix.get(i, j)*vector.get(j, 0);
					}
					lambdas[i]=sum/vector.get(i, 0);
				}
				double lambdaMax = Double.MIN_VALUE;
				for(int index=0; index<dimension; index++)
					if(lambdas[index]>lambdaMax)
						lambdaMax = lambdas[index];
				double CI = (lambdaMax - dimension)/(dimension -1);
				double CR = CI/randomIndex[dimension];
				if(CR<0.1)
					consistent = true;
			}
			else
				System.err.println("Preference matrix and priority vector are too wide (15 max) or empty !!"+dimension);
		}
		else
			System.err.println("The matrix and vector dimension does not match !!"+prefMatrix.getMatrix().getRowDimension()+","+prioVector.getVector().getRowDimension());
		return consistent;
	}
	
}
