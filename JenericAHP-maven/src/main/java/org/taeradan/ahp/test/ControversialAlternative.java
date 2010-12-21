/* Copyright 2009-2010 Yves Dubromelle
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
package org.taeradan.ahp.test;

import org.taeradan.ahp.Alternative;

/**
 *
 * @author Yves Dubromelle
 * @author jpierre03
 */
public class ControversialAlternative
		implements Alternative {

	/**
	 *
	 */
	transient public final String name;
	/**
	 *
	 */
	private int rank;
	/**
	 *
	 */
	private double valueIT;
	/**
	 *
	 */
	private double valueIV;

	/**
	 *
	 * @param name
	 * @param valueIT
	 * @param valueIV
	 */
	public ControversialAlternative(String name, double valueIT, double valueIV) {
		this.name = name;
		this.valueIT = valueIT;
		this.valueIV = valueIV;
	}

	@Override
	public int getRank() {
		return rank;
	}

	@Override
	public void setRank(final int rank) {
		this.rank = rank;
	}

	/**
	 * @return the valueIT
	 */
	public double getValueIT() {
		return valueIT;
	}

	/**
	 * @param valueIT the valueIT to set
	 */
	public void setValueIT(double valueIT) {
		this.valueIT = valueIT;
	}

	/**
	 * @return the valueIV
	 */
	public double getValueIV() {
		return valueIV;
	}

	/**
	 * @param valueIV the valueIV to set
	 */
	public void setValueIV(double valueIV) {
		this.valueIV = valueIV;
	}
}
