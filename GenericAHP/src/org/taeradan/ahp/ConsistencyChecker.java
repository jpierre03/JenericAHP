/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.taeradan.ahp;

/**
 *
 * @author Yves Dubromelle
 */
public class ConsistencyChecker {
	
	private final double[] randomIndex = {0.00, 0.00, 0.58, 0.90, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49, 1.51, 1.48, 1.56, 1.57, 1.59};

	public static boolean isConsistent(PreferenceMatrix prefMatrix, PriorityVector prioVector){
		boolean consistent = false;
		double[] maxLambda ;
		int dimension = 0;
		if(prefMatrix.getMatrix().getColumnDimension()==prioVector.getVector().getColumnDimension()){
			dimension = prefMatrix.getMatrix().getColumnDimension();
			maxLambda = new double[dimension];
			for(int i=0; i<dimension; i++){
				double sum = 0;
				for(int j=0; j<dimension; j++){
					
				}
			}
		}
		else
			System.err.println("The matrix and vector dimension does not match !!");
		return consistent;
	}
	
}
