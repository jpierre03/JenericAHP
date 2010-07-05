/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.ConsistencyMaker;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;
import java.io.IOException;
import java.util.Scanner;
import org.nfunk.jep.JEP;
import org.taeradan.ahp.ConsistencyChecker;
import org.taeradan.ahp.PriorityVector;

/**
 *
 * @author jpierre03
 * @author Yves Dubromelle
 * @author Marianne
 */
public class SaatysToolsTest {

	/**
	 * 
	 */
	private final static double[] SAATY_VALUES = {1. / 9, 1. / 8, 1. / 7, 1. / 6, 1. / 5, 1. / 4,
												  1. / 3, 1. / 2, 1, 2, 3, 4, 5, 6, 7, 8, 9};

	/**
	 *
	 * @param value
	 * @return
	 */
	public static boolean isInSaatysSacale(double value) {
		boolean result = false;

		for (int i = 0; i < SAATY_VALUES.length; i++) {
			double d = SAATY_VALUES[i];
			if(value==d){
				result=true;
			}
		}
		return result;
	}

	public static void main(String[] args) throws IOException {


		MyMatrix priorityVector = new MyMatrix();
		MatrixValue matrixValue = new MatrixValue();
		SaatysTools saatysTools = new SaatysTools();
		MyMatrix epsilon = new MyMatrix();
		ConsistencyChecker consistencyChecker = new ConsistencyChecker();
		Scanner userInput = new Scanner(System.in);
		String expertsChoice;
	
		System.out.println("De quelle dimension est votre matrice?");
		expertsChoice = userInput.next();
		int matrixSize = Integer.parseInt(expertsChoice);

		MyMatrix myMatrix = new MyMatrix(matrixSize, matrixSize);

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

				expertsChoice = userInput.next();
				final JEP myParser = new JEP();
				myParser.parseExpression(expertsChoice);
				double newValue = myParser.getValue();

				/*Test la validité de la valeur (doit appartenir à l'échelle de Saaty)*/
				while (!isInSaatysSacale(newValue)) {
					System.out.println(
							"Erreur : cette valeur n'appartient à l'échelle de Saaty. Retapez votre valeur.");
					expertsChoice = userInput.next();
					myParser.parseExpression(expertsChoice);
					newValue = myParser.getValue();
				}


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

		/*Diagonale*/
		for (int i = 0; i < myMatrix.getRowDimension(); i++) {

			matrixValue.setValue(1);
			matrixValue.setRow(i);
			matrixValue.setColumn(i);
			myMatrix.setMatrixValue(matrixValue);

		}



		myMatrix.print(5, 5);

		priorityVector = PriorityVector.build(myMatrix);

		priorityVector.print(5, 5);

		CharSequenceAppender csa = new CharSequenceAppender("Sequence.csv");

		/*Ecriture de la matrice et du vecteur de priorité dans le fichier*/
		csa.insertMatrix(myMatrix);
		csa.insertLineFeed();
		csa.insertMatrix(priorityVector);
		csa.insertLineFeed();

		/*Ecriture du CR*/
		//csa.append(temp);
		csa.insertLineFeed();

		while (!consistencyChecker.isConsistent(myMatrix, priorityVector)) {
			System.out.println("Matrice incohérente\n CR = " + consistencyChecker.getCrResult());
			/*Calcul matrice epsilon*/
			epsilon = saatysTools.calculateEpsilonMatrix(myMatrix, priorityVector);

			/*Recherche de la valeur à modifier*/
			matrixValue = saatysTools.getValueToModifiyByRanking(myMatrix, priorityVector,
																 epsilon);
			System.out.println(
					"Vous avez choisi de remplacer la valeur "
					+ myMatrix.get(matrixValue.getRow(), matrixValue.getColumn())
					+ " de coordonnées "
					+ " ( "
					+ (matrixValue.getRow() + 1)
					+ " , "
					+ (matrixValue.getColumn() + 1)
					+ " )"
					+ "\nSaisissez la valeur par laquelle vous souhaitez remplacer votre pondération");

			/*Lecture de lavaleur saisie au clavier*/
			expertsChoice = userInput.next();
			final JEP myParser = new JEP();
			myParser.parseExpression(expertsChoice);
			double newValue = myParser.getValue();

			while (!isInSaatysSacale(newValue)) {
				System.out.println(
						"Erreur : cette valeur n'appartient à l'échelle de Saaty. Retapez votre valeur.");
				expertsChoice = userInput.next();
				myParser.parseExpression(expertsChoice);
				newValue = myParser.getValue();
			}

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
			System.out.println("---");
			myMatrix.print(5, 5);

			System.out.println("---");

			//Réactualisation du vecteur de priorité associé à la nouvelle matrice
			priorityVector = PriorityVector.build(myMatrix);
			priorityVector.print(5, 5);

		}

		System.out.println("CR = " + consistencyChecker.getCrResult());
		System.out.println("***********************************************"
						   + "\n**  Félicitation ! La matrice est cohérente  **\n"
						   + "***********************************************");


		/*Ecriture de la matrice et du vecteur de priorité dans le fichier*/
		csa.insertLineFeed();
		csa.insertMatrix(myMatrix);
		csa.insertLineFeed();
		csa.insertMatrix(priorityVector);
		csa.insertLineFeed();


		csa.close();


	}

	/**
	 *
	 */
	private SaatysToolsTest() {
	}
}
