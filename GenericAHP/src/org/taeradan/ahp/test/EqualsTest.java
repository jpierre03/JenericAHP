/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.test;

import org.taeradan.ahp.matrix.MatrixValue;

/** @author Marianne */
public final class EqualsTest {

	private EqualsTest() {
	}

	public static void main(String[] args) {
		MatrixValue m1 = new MatrixValue();
		MatrixValue m2 = new MatrixValue();
		MatrixValue m3 = new MatrixValue();

		System.out.println(m1.equals(m3));
		System.out.println(m1.equals(m2));
	}
}
