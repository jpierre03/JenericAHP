/* Copyright 2009 Yves Dubromelle @ LSIS(www.lsis.org)
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

/**
 *
 * @author Yves Dubromelle
 */
public class AlternativesPriorityVector {

    private Matrix matrix = null;
    
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
