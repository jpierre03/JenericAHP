/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.matrix;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Marianne
 * @author Jean-Pierre PRUNARET
 */
public final class MatrixValueTest {

	public MatrixValueTest() {
	}

	@Test
	public void test100_idempotent() {
		MatrixValue m1 = new MatrixValue();
		MatrixValue m2 = new MatrixValue();
		MatrixValue m3 = new MatrixValue();

		assertTrue(m1.equals(m3));
		assertTrue(m1.equals(m2));

		assertTrue(m2.equals(m3));

		assertEquals(m1.getValue() == m2.getValue(), m2.getValue() == m3.getValue());
		assertEquals(m1.getColumn() == m2.getColumn(), m2.getColumn() == m3.getColumn());
		assertEquals(m1.getRow() == m2.getRow(), m2.getRow() == m3.getRow());
	}

	@Test
	public void test200_position() {
		MatrixValue m1 = new MatrixValue();
		MatrixValue m2 = new MatrixValue();
		MatrixValue m3 = new MatrixValue();

		m2.setRow(1);
		m3.setColumn(1);

		assertFalse(m1.equals(m3));
		assertFalse(m1.equals(m2));
		assertFalse(m2.equals(m3));
	}

	@Test
	public void test300_value(){
		MatrixValue m1 = new MatrixValue();
		MatrixValue m2 = new MatrixValue();
		MatrixValue m3 = new MatrixValue();

		m1.setValue(3);
		m2.setValue(3);
		m3.setValue(3);

		assertTrue(m1.equals(m3));
		assertTrue(m1.equals(m2));
		assertTrue(m2.equals(m3));

		assertEquals(m1.getValue() == m2.getValue(), m2.getValue() == m3.getValue());
		assertEquals(m1.getColumn() == m2.getColumn(), m2.getColumn() == m3.getColumn());
		assertEquals(m1.getRow() == m2.getRow(), m2.getRow() == m3.getRow());
	}

}
