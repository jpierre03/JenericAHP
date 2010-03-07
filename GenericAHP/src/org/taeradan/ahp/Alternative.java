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

/**
 * Alternative to be processed by AHP
 * @author Yves Dubromelle
 */
public interface Alternative {

	/**
	 * Méthod that returns the current rank of the alternative.
	 * @return Rank value.
	 */
	public abstract int getRank();

	/**
	 * Méthod that writes the rank of the alternative.
	 * @param rank Rank value.
	 */
	public abstract void setRank(int rank);
}
