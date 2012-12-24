/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.prototype.ConsistencyMaker;

import org.nfunk.jep.JEP;
import org.taeradan.ahp.ConsistencyChecker;
import org.taeradan.ahp.PriorityVector;
import org.taeradan.ahp.matrix.MatrixValue;
import org.taeradan.ahp.matrix.MyMatrix;
import org.taeradan.ahp.prototype.ConsistencyMaker.csv_output_marianne.CharSequenceAppender;
import org.taeradan.ahp.prototype.gui.matrix.MyMatrixTable;
import org.taeradan.ahp.prototype.gui.matrix.MyMatrixTableModel;

import javax.swing.JFrame;
import java.awt.HeadlessException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author Jean-Pierre PRUNARET
 * @author Yves Dubromelle
 * @author Marianne
 */
public final class SaatyToolsTest {

	private static final double[] SAATY_VALUES = {1. / 9, 1. / 8, 1. / 7, 1. / 6, 1. / 5, 1. / 4,
												  1. / 3, 1. / 2, 1, 2, 3, 4, 5, 6, 7, 8, 9};

	private SaatyToolsTest() {
	}

	public static boolean isInSaatyScale(double value) {
		boolean result = false;

		for (int i = 0; i < SAATY_VALUES.length; i++) {
			double d = SAATY_VALUES[i];
			if (value == d) {
				result = true;
			}
		}
		return result;
	}

	public static MatrixValue readSaatyRanking(
			Collection<MatrixValue> sortedMatrixValues, MyMatrix myPreferenceMatrix,
			String file)
			throws
			IOException {

		Scanner userInput = new Scanner(System.in);
		CharSequenceAppender csa = new CharSequenceAppender(file);
		MatrixValue matrixValue = new MatrixValue();

		Iterator<MatrixValue> valueIterator = sortedMatrixValues.iterator();
		MatrixValue matrixValueToPrint = new MatrixValue();
		int isValueChosen = 0;
		boolean isFound = false;

		while (isValueChosen == 0) {
			matrixValue = valueIterator.next();
			matrixValueToPrint.setRow(matrixValue.getRow());
			matrixValueToPrint.setColumn(matrixValue.getColumn());
			matrixValueToPrint.setValue(
					myPreferenceMatrix.get(matrixValueToPrint.getRow(), matrixValueToPrint.getColumn()));

			System.out.println("Souhaitez-vous modifier la valeur "
							   + matrixValueToPrint.getValue()
							   + " ( "
							   + (matrixValueToPrint.getRow() + 1)
							   + " , "
							   + (matrixValueToPrint.getColumn() + 1)
							   + " )"
							   + " ? O/N");

			String expertsChoice = userInput.nextLine();

			if (expertsChoice.equalsIgnoreCase("O")) {
				isValueChosen = 1;
			} else if (!valueIterator.hasNext()) {
				System.out.println("Retour en haut du classement");
				valueIterator = sortedMatrixValues.iterator();
			}
		}

		/*parcours de la liste pour l'écriture dans le fichier*/
		valueIterator = sortedMatrixValues.iterator();

		MyMatrix tempMatrix = new MyMatrix();

		while ((valueIterator.hasNext()) && (!isFound)) {

			MatrixValue tempMatrixValue = valueIterator.next();
			csa.insertLineFeed();

			/*écriture du best fit associé à la valeur proposée*/
			//copie de la matrice initiale
			tempMatrix = tempMatrix.copyMyMatrix(myPreferenceMatrix);

			//calcul du vecteur propre associé à tempMatrix
			PriorityVector tempVector = PriorityVector.build(tempMatrix);
			//calcul du best fit
			double BestFit = SaatyTools.calculateBestFit(tempMatrix,
														 tempVector,
														 tempMatrixValue.getRow(),
														 tempMatrixValue.getColumn());
			//écriture du best fit
			String tempString = "" + BestFit;
			csa.append(tempString);
			csa.insertSeparator();

			/*écriture des indices de la valeur proposée par Saaty dans le fichier*/
			tempString = "" + (tempMatrixValue.getRow() + 1);
			csa.append(tempString);
			csa.insertSeparator();
			tempString = "" + (tempMatrixValue.getColumn() + 1);
			csa.append(tempString);
			csa.insertSeparator();

			/*écriture de la cohérence si l'expert suivait les conseils de Saaty*/

			//remplacement de la valeur (i,j) par BestFit
			MatrixValue newMatrixValue = new MatrixValue();
			newMatrixValue.setRow(tempMatrixValue.getRow());
			newMatrixValue.setColumn(tempMatrixValue.getColumn());
			newMatrixValue.setValue(BestFit);
			tempMatrix.setMatrixValue(newMatrixValue);

			//remplacement de la valeur (j,i) par 1/BestFit
			newMatrixValue.setRow(tempMatrixValue.getColumn());
			newMatrixValue.setColumn(tempMatrixValue.getRow());
			newMatrixValue.setValue(1. / BestFit);
			tempMatrix.setMatrixValue(newMatrixValue);

			//rafraîchissement du vecteur de priorité
			tempVector = PriorityVector.build(tempMatrix);
			//calcul et écriture de la cohérence
//			consistencyChecker.isConsistent(tempMatrix, tempVector);
			tempString = "" + new ConsistencyChecker().getConsistencyRatio();
			csa.append(tempString);
			csa.insertSeparator();

			if (matrixValue.equals(tempMatrixValue)) {
				isFound = true;
			}
		}

		csa.close();

		return matrixValue;
	}

	public static MyMatrix createMatrix() {
		MatrixValue matrixValue = new MatrixValue();
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
						"Pondération entre Critère "
						+ (i + 1)
						+ " et Critère  "
						+ (j + 1));

				expertsChoice = userInput.next();
				final JEP myParser = new JEP();
				myParser.parseExpression(expertsChoice);
				double newValue = myParser.getValue();

				/*Test la validité de la valeur (doit appartenir à l'échelle de Saaty)*/
				while (!isInSaatyScale(newValue)) {
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
		return myMatrix;
	}

	public static void main(String[] args)
			throws
			IOException {

		ConsistencyChecker consistencyChecker = new ConsistencyChecker();
		Scanner userInput = new Scanner(System.in);
		MyMatrixTable maTable = new MyMatrixTable();
		MyMatrixTableModel matrixTableModel = new MyMatrixTableModel();

		Collection<MatrixValue> collectionOfSortedMatrixValues;
		String tempString;
		CharSequenceAppender csa;
		int iterationCounter = 0;
		String file;

		System.out.println("Saisir le nom du fichier permettant de garder la trace des actions");
		file = userInput.next();
		file += ".csv";
		csa = new CharSequenceAppender(file);

		MyMatrix myMatrix = createMatrix();
		//	myMatrix.print(5, 5);

		System.out.println("Merci de patienter");

		/*Interface graphique*/
		//Attention true si c'est le 1er pb false si deuxième et false si langue francaise
		matrixTableModel.setMatrix(myMatrix, true, false);
		maTable.setModel(matrixTableModel);

		showMatrixTable(maTable, myMatrix);

		PriorityVector priorityVector = PriorityVector.build(myMatrix);
		//	priorityVector.print(5, 5);


		/*Ecriture de la matrice et du vecteur de priorité dans le fichier*/
		csa.insertMatrix(myMatrix);
		csa.insertLineFeed();
		csa.insertMatrix(priorityVector);
		csa.insertLineFeed();

		/*Ecriture du CR*/
		consistencyChecker.isConsistent(myMatrix, priorityVector);
		tempString = "" + consistencyChecker.getConsistencyRatio();
		csa.append(tempString);
		csa.insertLineFeed();
		csa.insertLineFeed();

		//en-tête du tableau
		csa.append(
				"BestFit;Saaty i;Saaty j; Saaty consistency;Expert Init Value;Expert Changed Value ; Expert Position in Saaty's ranking;CR\n");
		csa.insertLineFeed();
		csa.close();

		while (!consistencyChecker.isConsistent(myMatrix, priorityVector)) {

			//incrémentation du compteur du nombre d'itération
			iterationCounter++;

			System.out.println("\n**********          Matrice incohérente"
							   + "          **********\n CR = " + consistencyChecker.getConsistencyRatio()
							   + "\n");
			/*Calcul matrice epsilon*/
			MyMatrix epsilon = SaatyTools.calculateEpsilonMatrix(myMatrix, priorityVector);

			/*Recherche de la valeur à modifier*/
			collectionOfSortedMatrixValues = SaatyTools.getRank(myMatrix, priorityVector, epsilon);
			MatrixValue matrixValue = readSaatyRanking(collectionOfSortedMatrixValues, myMatrix, file);

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

			/*Ecrire la valeur que souhaite modifier l'expert*/
			csa = new CharSequenceAppender(file);
			tempString = "" + myMatrix.get(matrixValue.getRow(), matrixValue.getColumn());
			csa.append(tempString);
			csa.insertSeparator();

			/*Lecture de la valeur saisie au clavier*/
			String expertsChoice = userInput.next();
			final JEP myParser = new JEP();
			myParser.parseExpression(expertsChoice);
			double newValue = myParser.getValue();

			while (!isInSaatyScale(newValue)) {
				System.out.println(
						"Erreur : cette valeur n'appartient à l'échelle de Saaty. Retapez votre valeur.");
				expertsChoice = userInput.next();
				myParser.parseExpression(expertsChoice);
				newValue = myParser.getValue();
			}


			/*Ecrire la valeur modifiée par l'utilisateur*/
			tempString = "" + newValue;
			csa.append(tempString);
			csa.insertSeparator();

			/*Calculer le placement dans le classement de Saaty*/
			int location = SaatyTools.getLocationInRank(collectionOfSortedMatrixValues,
														matrixValue.getRow(),
														matrixValue.getColumn());
			tempString = "" + location;
			csa.append(tempString);
			csa.insertSeparator();

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
			//	myMatrix.print(5, 5);

			//Affichage nouvelle matrice
			//Attention true si c'est le 1er pb false si deuxième et false si langue francaise
			matrixTableModel.setMatrix(myMatrix, true, false);
			maTable.setModel(matrixTableModel);

			//Réactualisation du vecteur de priorité associé à la nouvelle matrice
			priorityVector = PriorityVector.build(myMatrix);
			//		priorityVector.print(5, 5);

			//Ecriture du nouveau CR
			consistencyChecker.isConsistent(myMatrix, priorityVector);
			tempString = "" + consistencyChecker.getConsistencyRatio();
			csa.append(tempString);

			csa.close();
		}

		System.out.println("CR = " + consistencyChecker.getConsistencyRatio());
		System.out.println("\n***********************************************"
						   + "\n**  Félicitation ! La matrice est cohérente  **\n"
						   + "***********************************************");

		csa = new CharSequenceAppender(file);
		/*Ecriture de la matrice et du vecteur de priorité dans le fichier*/
		csa.insertLineFeed();
		csa.insertLineFeed();
		csa.insertMatrix(myMatrix);
		csa.insertLineFeed();
		csa.insertMatrix(priorityVector);
		csa.insertLineFeed();

		//Ecriture du CR
		consistencyChecker.isConsistent(myMatrix, priorityVector);
		tempString = "" + consistencyChecker.getConsistencyRatio();
		csa.append(tempString);
		csa.insertLineFeed();
		csa.insertLineFeed();

		tempString = "Number of Iterations;" + iterationCounter;
		csa.append(tempString);

		csa.close();
	}

	private static void showMatrixTable(MyMatrixTable maTable, MyMatrix myMatrix)
			throws
			HeadlessException {

		// Show a frame with a table
		JFrame maFenetre = new JFrame("Aperçu de la Matrice de Préférences");
		maFenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		maFenetre.setContentPane(maTable);
		maFenetre.setSize(1000, 27 * myMatrix.getRowDimension());
		maFenetre.setVisible(true);
	}
}
