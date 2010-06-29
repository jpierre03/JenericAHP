/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.ConsistencyMaker;

import java.util.Scanner;
import org.nfunk.jep.JEP;
import org.taeradan.ahp.ConsistencyChecker;

/**
 *
 * @author Marianne
 */
public class SaatysToolsTest {

	public static void main(String[] args) {

		MyMatrix myMatrix = new MyMatrix(3, 3);
		MyMatrix priorityVector = new MyMatrix();
		MatrixValue matrixValue = new MatrixValue();
		SaatysTools saatysTools = new SaatysTools();
		MyMatrix epsilon = new MyMatrix();
		PriorityVectorNewVersion pvnv = new PriorityVectorNewVersion();
		ConsistencyChecker consistencyChecker = new ConsistencyChecker();
		Scanner scan = new Scanner(System.in);
		String expertsChoice;


		/*Déclaration matrice*/
		matrixValue.setValue(1);
		matrixValue.setRow(0);
		matrixValue.setColumn(0);
		myMatrix.setMatrixValue(matrixValue);
		matrixValue.setValue(2);
		matrixValue.setRow(0);
		matrixValue.setColumn(1);
		myMatrix.setMatrixValue(matrixValue);
		matrixValue.setValue(1);
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

		matrixValue.setValue(0.11);
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


		priorityVector = pvnv.build(myMatrix);

		priorityVector.print(5, 5);


		while (!consistencyChecker.isConsistent(myMatrix, priorityVector)) {
			epsilon = saatysTools.calculateEpsilonMatrix(myMatrix, priorityVector);
			matrixValue = saatysTools.getValueToModifiyByRanking(myMatrix, priorityVector, epsilon);
			System.out.println(
					"Vous avez choisi de remplacer la valeur "
					+ myMatrix.get(matrixValue.getRow(), matrixValue.getColumn())
					+ " de coordonnées "
					+ " ( "
					+ matrixValue.getRow()
					+ " , "
					+ matrixValue.getColumn()
					+ " )"
					+ "Saisissez la valeur par laquelle vous souhaitez remplacer votre pondération");

		/*	System.out.println("BestFit = "+saatysTools.calculateBestFit(priorityVector, matrixValue.getRow(), matrixValue.
					getColumn()));*/

			expertsChoice = scan.next();
			final JEP myParser = new JEP();
			myParser.parseExpression(expertsChoice);
			double newValue = myParser.getValue();

			/*Changement d'une valeur et de la valeur réciproque associée dans
			la matrice*/

			//Valeur directement modifiée
			matrixValue.setValue(newValue);
			myMatrix.setMatrixValue(matrixValue);

			//Valeur réciproquement modifiée
			int tempI = matrixValue.getRow();
			int tempJ = matrixValue.getColumn();
			matrixValue.setValue(1 / newValue);
			matrixValue.setRow(tempJ);
			matrixValue.setColumn(tempI);
			myMatrix.setMatrixValue(matrixValue);

			//Affichage nouvelle matrice
			myMatrix.print(5, 5);

			//Réactualisation du vecteur de priorité associé à la nouvelle matrice
			priorityVector = pvnv.build(myMatrix);



		}


		System.out.println("***********************************************"
						   + "\n**  Félicitation ! La matrice est cohérente  **\n"
						   + "***********************************************");


	}
}
