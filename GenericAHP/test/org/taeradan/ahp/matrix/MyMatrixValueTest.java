/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.matrix;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Jean-Pierre PRUNARET
 */
public final class MyMatrixValueTest {

    private static final int ROWS = 5;
    private static final int COLUMN = 5;
    private MyMatrix myMatrix;

    public MyMatrixValueTest() {
    }

    @Before
    public void setUp() {
        myMatrix = new MyMatrix(ROWS, COLUMN);

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMN; j++) {
                MatrixValue matrixValue = new MatrixValue(i, j, (i - j) * (j + i));

                myMatrix.setMatrixValue(matrixValue);
            }
        }
    }

    @Test
    public void testSetUp() {
        assertTrue(myMatrix != null);

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMN; j++) {

                final String location = String.format("(i,j)(%d, %d)", i, j);
                assertTrue(location, myMatrix.getMatrixValue(i, j) != null);
            }
        }
    }

    @Test
    public void testSetValue() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMN; j++) {

                final String location = String.format("(i,j)(%d, %d)", i, j);
                assertTrue(location, myMatrix.getMatrixValue(i, j).getValue() == (i - j) * (j + i));
            }
        }
    }


    @Test
    public void testSetValue2() {

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMN; j++) {
                MatrixValue matrixValue = new MatrixValue(i, j, (i + j) * (2 * j + i));

                myMatrix.setMatrixValue(matrixValue);
            }
        }

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMN; j++) {

                final String location = String.format("(i,j)(%d, %d)", i, j);
                assertTrue(location, myMatrix.getMatrixValue(i, j).getValue() == (i + j) * (2 * j + i));
            }
        }
    }

    @Test
    public void testCopy() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMN; j++) {
                MatrixValue matrixValue = new MatrixValue(i, j, (i + j + 9) * (j + i));

                myMatrix.setMatrixValue(matrixValue);
            }
        }

        final MyMatrix copy = MyMatrix.copyMyMatrix(myMatrix);


        assertTrue(copy.getRowDimension() == myMatrix.getRowDimension());
        assertTrue(copy.getColumnDimension() == myMatrix.getColumnDimension());

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMN; j++) {

                final String location = String.format("(i,j)(%d, %d)", i, j);
                assertTrue(location, myMatrix.getMatrixValue(i, j).getValue() == copy.getMatrixValue(i, j).getValue());
            }
        }
    }

}
