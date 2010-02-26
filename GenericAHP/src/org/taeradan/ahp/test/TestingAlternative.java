/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.test;

import org.taeradan.ahp.Alternative;

/**
 *
 * @author Yves Dubromelle
 */
public class TestingAlternative implements Alternative {

	/**
	 *
	 */
	public final String name;
	/**
	 *
	 */
	private int rank;
	/**
	 *
	 */
	public final double valueI11;
	/**
	 *
	 */
	public final double valueI12;
	/**
	 *
	 */
	public final double valueI13;
	/**
	 *
	 */
	public final double valueI21;
	/**
	 *
	 */
	public final double valueI22;
	/**
	 *
	 */
	public final double valueI31;
	/**
	 *
	 */
	public final double valueI32;
	/**
	 * 
	 */
	public final double valueI41;
	/**
	 *
	 */
	public final double valueI51;

	/**
	 * 
	 * @param name
	 * @param valueI11
	 * @param valueI12
	 * @param valueI13
	 * @param valueI21
	 * @param valueI22
	 * @param valueI31
	 * @param valueI32
	 * @param valueI41
	 * @param valueI51
	 */
	public TestingAlternative(String name, double valueI11, double valueI12, double valueI13, double valueI21, double valueI22, double valueI31, double valueI32, double valueI41, double valueI51) {
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

	@Override
	public int getRank() {
		return rank;
	}

	@Override
	public void setRank(int rank) {
		this.rank = rank;
	}
}
