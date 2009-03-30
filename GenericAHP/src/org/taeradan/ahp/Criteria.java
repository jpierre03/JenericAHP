/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp;

import java.util.ArrayList;

/**
 * This class represents the criterias of the AHP tree, it contains Indicators and it executes its part of the AHP algorithm.
 * @author Yves Dubromelle
 */
public class Criteria {

	private ArrayList<Indicator> indicators;
	private PreferenceMatrix matrixInd;
	private PriorityVector vectorICrk;
	private AlternativesPriorityVector vectorAltCrk;
	private ArrayList alternatives;

	/**
	 * Class default constructor
	 */
	public Criteria() {
	}
}
