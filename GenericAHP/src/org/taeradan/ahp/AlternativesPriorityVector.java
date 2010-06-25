/* Copyright 2009-2010 Yves Dubromelle @ LSIS(www.lsis.org)
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

import org.taeradan.ahp.ConsistencyMaker.MyMatrix;

/**
 * @author Marianne
 * @author jpierre03
 * @author Yves Dubromelle
 */
public class AlternativesPriorityVector {

	/**
	 */
	private MyMatrix matrix = null;

	/**
	 * Method that give the Matrix contained in this class.
	 * @return matrix
	 */
	public MyMatrix getMatrix() {
		return matrix;
	}

	/**
	 * @param matrix
	 */
	public void setMatrix(final MyMatrix matrix) {
		this.matrix = matrix;
	}
}
