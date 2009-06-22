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

package org.taeradan.ahp.test;

/**
 *
 * @author Yves Dubromelle
 */
public class TestingAlternative {
	public final String name;
	public final double valueI11;
	public final double valueI12;
	public final double valueI13;
	public final double valueI21;
	public final double valueI22;
	public final double valueI31;
	public final double valueI32;
	public final double valueI41;
	public final double valueI51;

	public TestingAlternative(String name,double valueI11, double valueI12, double valueI13, double valueI21, double valueI22, double valueI31, double valueI32, double valueI41, double valueI51) {
		this.name = name;
		this.valueI11 = valueI11;
		this.valueI12 = valueI12;
		this.valueI13 = valueI13;
		this.valueI21 = valueI21;
		this.valueI22 = valueI22;
		this.valueI31 = valueI31;
		this.valueI32 = valueI32;
		this.valueI41 = valueI41;
		this.valueI51 = valueI51;
	}
	
}
