/* Copyright 2009 Yves Dubromelle
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

package org.taeradan.ahp.ind;

import java.util.ArrayList;
import org.jdom.Element;
import org.taeradan.ahp.Indicator;
import org.taeradan.ahp.test.TestingAlternative;

/**
 * Dummy implementation of an AHP indicator
 * @author Yves Dubromelle
 */
public class IndicatorI22 extends Indicator{
	
	public IndicatorI22(Element xmlIndicator){
		super.fromXml(xmlIndicator);
	}
	
	@Override
	public double calculateAlternativeValue(int i,ArrayList alternatives) {
		TestingAlternative alt = (TestingAlternative)alternatives.get(i);
		return alt.valueI22;
	}
}
