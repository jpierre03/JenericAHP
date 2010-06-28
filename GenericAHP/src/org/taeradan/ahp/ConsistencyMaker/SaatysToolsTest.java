/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.taeradan.ahp.ConsistencyMaker;

/**
 *
 * @author Marianne
 */
public class SaatysToolsTest {

	public static void main(String[] args) {

		MyMatrix myMatrix = new MyMatrix(3,3);
		MyMatrix priorityVector = new MyMatrix();
		MatrixValue matrixValue = new MatrixValue();

		myMatrix.print(5, 5);

		/*Déclaration matrice*/
		matrixValue.setValue(1);
		matrixValue.setRow(0);
		matrixValue.setColumn(0);
		myMatrix.setMatrixValue(matrixValue);
		matrixValue.setValue(21);
		matrixValue.setRow(0);
		matrixValue.setColumn(1);
		myMatrix.setMatrixValue(matrixValue);
		matrixValue.setValue(4);
		matrixValue.setRow(0);
		matrixValue.setColumn(2);
		myMatrix.setMatrixValue(matrixValue);

		matrixValue.setValue(0.5);
		matrixValue.setRow(1);
		matrixValue.setColumn(0);
		myMatrix.setMatrixValue(matrixValue);
		matrixValue.setValue(1);
		matrixValue.setRow(1);
		matrixValue.setColumn(1);
		myMatrix.setMatrixValue(matrixValue);
		matrixValue.setValue(2);
		matrixValue.setRow(1);
		matrixValue.setColumn(2);
		myMatrix.setMatrixValue(matrixValue);

		matrixValue.setValue(0.25);
		matrixValue.setRow(2);
		matrixValue.setColumn(0);
		myMatrix.setMatrixValue(matrixValue);
		matrixValue.setValue(0.5);
		matrixValue.setRow(2);
		matrixValue.setColumn(1);
		myMatrix.setMatrixValue(matrixValue);
		matrixValue.setValue(1);
		matrixValue.setRow(2);
		matrixValue.setColumn(2);
		myMatrix.setMatrixValue(matrixValue);



		myMatrix.print(5, 5);
		

		SaatysTools saatysTools  = new SaatysTools();
	/*	MatrixValue matrixValue = saatysTools.getFirstValueOfSaatysRanking(myMatrix);
		System.out.println("En quoi souhaitez vous changer la valeur "
							   + matrixValue.getValue()
							   + " ( "
							   + matrixValue.getRow()
							   + " , "
							   + matrixValue.getColumn()
							   + " )"
							   );
*/

		MyMatrix epsilon = new MyMatrix();
		PriorityVectorNewVersion pvnv = new PriorityVectorNewVersion();
		priorityVector = pvnv.build(myMatrix);
		priorityVector.print(5,1);
		saatysTools.calculateEpsilonMatrix(myMatrix, priorityVector);
	}


}
