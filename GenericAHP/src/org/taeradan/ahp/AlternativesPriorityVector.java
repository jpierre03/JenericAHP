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
class AlternativesPriorityVector {

    Matrix matrix = null;
    
    /**
     * Method that give the Matrix contained in this class.
     * @return matrix
     */
    public Matrix getMatrix() {
	return matrix;
    }

    public void setMatrix(Matrix matrix) {
	this.matrix=matrix;
    }

}
