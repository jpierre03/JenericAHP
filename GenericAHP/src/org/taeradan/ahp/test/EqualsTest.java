/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.test;

import org.taeradan.ahp.ConsistencyMaker.MatrixValue;

/**
 * @author Marianne
 */
public class EqualsTest {

	public static void main(String[] args) {
		MatrixValue m1 = new MatrixValue();
		MatrixValue m2 = new MatrixValue();
		MatrixValue m3 = new MatrixValue();

		System.out.println(m1.equals(m3));
		System.out.println(m1.equals(m2));
	}
}
