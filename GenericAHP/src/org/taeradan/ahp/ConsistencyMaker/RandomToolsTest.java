/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.ConsistencyMaker;

import java.util.Scanner;
import org.nfunk.jep.JEP;
import org.taeradan.ahp.ConsistencyChecker;
import org.taeradan.ahp.PriorityVector;

/**
 *
 * @author Marianne
 */
public class RandomToolsTest {

	public static void main(String[] args) {

		MyMatrix myMatrix = new MyMatrix(3, 3);
		MyMatrix priorityVector = new MyMatrix(3, 1);
		MatrixValue matrixValue = new MatrixValue();
		String expertsChoice;
		Scanner scan = new Scanner(System.in);
		ConsistencyChecker consistencyChecker = new ConsistencyChecker();
		myMatrix.print(5, 5);

		/*Déclaration matrice
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
		myMatrix.setMatrixValue(matrixValue);*/


		/*Saisie matrice*/
		for (int i = 0; i < myMatrix.getRowDimension(); i++) {
			for (int j = i + 1; j < myMatrix.getColumnDimension(); j++) {
				System.out.println(
						"Saisir la valeur pour les coordonnées "
						+ " ( "
						+ (i + 1)
						+ " , "
						+ (j + 1)
						+ " )");

				expertsChoice = scan.next();
				final JEP myParser = new JEP();
				myParser.parseExpression(expertsChoice);
				double newValue = myParser.getValue();


				/*Partie supérieure*/
				matrixValue.setValue(newValue);
				matrixValue.setRow(i);
				matrixValue.setColumn(j);
				myMatrix.setMatrixValue(matrixValue);

				/*Réciprocité*/
				matrixValue.setValue(1 / newValue);
				matrixValue.setRow(j);
				matrixValue.setColumn(i);
				myMatrix.setMatrixValue(matrixValue);


			}
		}

		for (int i = 0; i < myMatrix.getRowDimension(); i++) {

			matrixValue.setValue(1);
			matrixValue.setRow(i);
			matrixValue.setColumn(i);
			myMatrix.setMatrixValue(matrixValue);

		}

		myMatrix.print(5, 2);


		priorityVector = PriorityVector.build(myMatrix);

		priorityVector.print(5, 5);

		/*Tant que la matrice est incohérente*/
		while (!consistencyChecker.isConsistent(myMatrix, priorityVector)) {
			System.out.println("Matrice incohérente\n CR = " + ConsistencyChecker.getCrResult());

			RandomTools randomTools = new RandomTools();
			matrixValue = randomTools.getValueToModifiyByRanking(myMatrix);
			System.out.println(
					"Vous avez choisi de remplacer la valeur "
					+ matrixValue.getValue()
					+ " de coordonnées "
					+ " ( "
					+ (matrixValue.getRow() + 1)
					+ " , "
					+ (matrixValue.getColumn() + 1)
					+ " )"
					+ "\nSaisissez la valeur par laquelle vous souhaitez remplacer votre pondération");
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
			priorityVector = PriorityVector.build(myMatrix);


		}


		System.out.println("CR = " + ConsistencyChecker.getCrResult());

		System.out.println("***********************************************"
						   + "\n**  Félicitation ! La matrice est cohérente  **\n"
						   + "***********************************************");

	}
}
