/* Copyright 2009 Yves Dubromelle @ LSIS(www.lsis.org)
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

package org.taeradan.ahp;

import java.util.ArrayList;
import org.jdom.Element;

/**
 * This class provides the basis of the indicators of AHP. It is abstract because we can't know what the calculateIndicator() method should do for each user.<br/>
 * The indicators implementing this class must be names with the syntax : "IndicatorCxIy", x being the criteria's number and y the indicator number within the criteria.
 * @author Yves Dubromelle
 */
public class Indicator {
//	AHP static attributes
	private String id;
	private String name;
//	AHP dynamic attributes
	private AlternativesPriorityVector vectorAltIndk;
	private ArrayList alternatives;


	/**
	* Class default constructor
	*/
	public Indicator() {
	}
	
	/**
	 * Creates an Indicator from a JDOM Element
	 * @param xmlIndicator JDOM Element
	 */
	public Indicator(Element xmlIndicator){
//		Initialisation of the id
		id = xmlIndicator.getAttributeValue("id");
//		System.out.println("\t\tIndicator.id="+id);
		
//		Initialisation of the name
		name = xmlIndicator.getChildText("name");
//		System.out.println("\t\tIndicator.name="+name);
	}
	
	/**
	 * Method called by the criterias for the execution of the AHP algorithm.
	 * @return MCr vector
	 */
	public AlternativesPriorityVector calculatePriorityVector(){
//		For each alternative, evaluation of its value for the indicator
		double[] altValues = new double[alternatives.size()];
		for(int i=0;i<alternatives.size();i++){
			altValues[i]=calculateAlternativeIndicator(i);
		}
		return vectorAltIndk;
	}
	
	/**
	 * Method that calculates the value (floating point) of the indicator for an alternative i.
	 * @param i Alternative to be evaluated from the list
	 * @return Indicator value
	 */
	public double calculateAlternativeIndicator(int i){
		return 1;
	}

	@Override
	public String toString() {
		String string = "Indicator "+id+" : "+name;
		return string;
	}
	
}
