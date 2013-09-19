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
    public void test300_value() {
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

    @Test
    public void test_CloneConstructor() {
        MatrixValue m1 = new MatrixValue();
        m1.setValue(4);

        MatrixValue clone = new MatrixValue(m1);
        assertEquals(m1.getColumn(), clone.getColumn());
        assertEquals(m1.getRow(), clone.getRow());

        assertEquals(m1.getValue(), clone.getValue(), 1E-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_tooBigValue() {
        MatrixValue value = new MatrixValue();
        value.setValue(10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_tooSmallValue() {
        MatrixValue value = new MatrixValue();
        value.setValue(-1);
    }

    @Test
    public void test_equalIfValuesDifferents() {
        MatrixValue m1 = new MatrixValue();
        m1.setValue(5.2);

        MatrixValue m2 = new MatrixValue();
        m2.setValue(5.2);

        assertEquals(m1.getColumn(), m2.getColumn());
        assertEquals(m1.getRow(), m2.getRow());
        assertEquals(m1.getValue(), m2.getValue(), 1E-6);
        assertTrue(m1.equals(m2));

        m2.setValue(4.1);
        assertFalse(m1.equals(m2));
    }

    @Test
    public void test_compareWithNull() {
        MatrixValue value = new MatrixValue();
        value.setValue(2);

        assertFalse(value.equals(null));
    }

    @Test
    public void test_roundValue() {
        MatrixValue value = new MatrixValue();

        value.setValue(1);
        assertEquals(1, value.getValue(), 1E-6);

        value.setValue(0.123456789);
        assertEquals(0.1235, value.getValue(), 1E-4);
    }

    @Test
    public void test_string() {
        MatrixValue value = new MatrixValue();
        value.setValue(5.1111);

        assertTrue("5.0".equals(value.toString()));
    }
}
