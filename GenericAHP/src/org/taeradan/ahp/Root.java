/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package org.taeradan.ahp;

import Jama.Matrix;
import java.util.ArrayList;

/**
 *  This is the root class of the AHP tree. It contains Criterias and execute its part of the AHP algorithm.
 * @author Yves Dubromelle
 */
public class Root {
    
    private ArrayList<Criteria> criterias;
    private PreferenceMatrix matrixCrCr;
    private PriorityVector vectorCrOg;
    private AlternativesPriorityVector vectorMOg;
    private ArrayList alternatives;
    private Matrix matrixAltCr;
    
    /**
     * Class default constructor
     */
    public Root() {
    }
    
    /**
     * Root method of the AHP execution.
     * Calculates the final alternatives ranking with the alternatives priority vectors from the criterias and the criterias priority vectors.
     * @return ArrayList containing the ranked alternatives.
     */
    public ArrayList calculateRanking(){
	ArrayList rankedAlternatives = null;
//	Concatenation in a matrix of the arrays calculated by the criterias
	for(int k=0; k<criterias.size();k++){
		
	}
//	Calculation of the final alternatives priority vector
	vectorMOg.setMatrix(vectorCrOg.getMatrix().times(matrixAltCr));
//	Ranking of the alternatives with the initial alternatives array and  the MOg vector
	return rankedAlternatives;
    }
}
