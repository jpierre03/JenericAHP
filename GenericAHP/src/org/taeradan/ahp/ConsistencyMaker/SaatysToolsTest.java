/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.taeradan.ahp.ConsistencyMaker;

import org.taeradan.ahp.ConsistencyMaker.MatrixValue;
import org.taeradan.ahp.ConsistencyMaker.MyMatrix;

/**
 *
 * @author Marianne
 */
public class SaatysToolsTest {

	public static void main(String[] args) {

		MyMatrix myMatrix = new MyMatrix(5, 5);

		myMatrix.print(5, 5);

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				MatrixValue matrixValue = new MatrixValue();
//				matrixValue.setValue((i + 1) * (j + 1));
				matrixValue.setValue((i - j) * (j + i));
				matrixValue.setRow(i);
				matrixValue.setColumn(j);

				myMatrix.setMatrixValue(matrixValue);
			}
		}

		myMatrix.print(5, 0);
		

		SaatysTools saatysTools  = new SaatysTools();
		MatrixValue matrixValue = saatysTools.getValueToModifiyByRanking(myMatrix);
		System.out.println("En quoi souhaitez vous changer la valeur "
							   + matrixValue.getValue()
							   + " ( "
							   + matrixValue.getRow()
							   + " , "
							   + matrixValue.getColumn()
							   + " )"
							   );

	}


}
