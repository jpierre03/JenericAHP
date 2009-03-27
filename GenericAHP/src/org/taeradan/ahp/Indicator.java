/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.taeradan.ahp;

import java.util.ArrayList;

/**
 * This class provides the basis of the indicators of AHP. It is abstract because we can't know what the calculateIndicator() method should do for each user.
 * @author Yves Dubromelle
 */
public abstract class Indicator {
    
    private AlternativesPriorityVector vectorAltIndk;
    private ArrayList alternatives;
    
    
    /**
     * Class default constructor
     */
    public Indicator() {
    }
	
	/**
	 * Method called by the criterias for the execution of the AHP algorithm.
	 * @return MCr vector
	 */
	public AlternativesPriorityVector calculatePriorityVector(){
//		For each alternative, evaluation of its value for the indicator
		double[] altValues = new double[alternatives.size()];
		for(int i=0;i<alternatives.size();i++){
			altValues[i]=calculateAlternativeIndicator(i);
		}
		return vectorAltIndk;
	}
	
	/**
	 * Method that calculates the value (floating point) of the indicator for an alternative i.
	 * @param i Alternative to be evaluated from the list
	 * @return Indicator value
	 */
	abstract public double calculateAlternativeIndicator(int i);
}
