/* Copyright 2009 Yves Dubromelle
 * 
 * This file is part of GenericAHP.
 * 
 * GenericAHP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GenericAHP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GenericAHP.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.taeradan.ahp.test.ind;

import java.util.Collection;
import org.jdom.Element;
import org.taeradan.ahp.Alternative;
import org.taeradan.ahp.Indicator;
import org.taeradan.ahp.test.TestingAlternative;

/**
 * Dummy implementation of an AHP indicator
 * @author jpierre03
 * @author Yves Dubromelle
 */
public class IndicatorI41 extends Indicator {

	/**
	 * 
	 * @param xmlIndicator
	 */
	public IndicatorI41(Element xmlIndicator) {
		super(xmlIndicator);
	}

	@Override
	public double calculateAlternativeValue(int altIndex, Collection<? extends Alternative> alternatives) {
		return ((TestingAlternative) alternatives.toArray()[altIndex]).valueI41;
	}
}
