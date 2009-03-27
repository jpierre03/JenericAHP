/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.taeradan.ahp;

import Jama.Matrix;

/**
 * The PreferenceMatrix class is a container for a Matrix adapted to the needs of AHP in terms of configuration.
 * @author Yves Dubromelle
 */
class PreferenceMatrix{
    
    Matrix matrix = null;

    /**
     * Class default Constructor.
     */
    public PreferenceMatrix(){
    }
    
    /**
     * Method that give the Matrix contained in this class.
     * @return matrix
     */
    public Matrix getMatrix() {
	return matrix;
    }
    
    /**
     * Method that overwrite the matrix contained in this class.
     * @param matrix
     */
    public void setMatrix(Matrix matrix) {
	this.matrix = matrix;
    }
    
}
