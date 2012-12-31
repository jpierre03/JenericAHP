package org.taeradan.ahp.prototype.ConsistencyMaker;

import org.nfunk.jep.JEP;
import org.taeradan.ahp.ConsistencyChecker;
import org.taeradan.ahp.PriorityVector;
import org.taeradan.ahp.matrix.MatrixValue;
import org.taeradan.ahp.matrix.MyMatrix;
import org.taeradan.ahp.prototype.ConsistencyMaker.csv_output_marianne.CharSequenceAppender;
import org.taeradan.ahp.prototype.SampleMatrixHeaders;
import org.taeradan.ahp.prototype.gui.matrix.MyMatrixTable;
import org.taeradan.ahp.prototype.gui.matrix.MyMatrixTableModel;

import javax.swing.JFrame;
import java.awt.HeadlessException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

/**
 * This class aims to execute the Saaty method, for a given matrix, that you can fill through the console
 *
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

	public static MatrixValue readSaatyRanking(Collection<MatrixValue> sortedMatrixValues,
											   MyMatrix myPreferenceMatrix,
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
			matrixValueToPrint.setValue(myPreferenceMatrix.get(matrixValueToPrint.getRow(),
															   matrixValueToPrint.getColumn()));

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

		while ((valueIterator.hasNext()) && (!isFound)) {

			final MatrixValue tempMatrixValue = valueIterator.next();
			csa.appendLineFeed();

			/*écriture du best fit associé à la valeur proposée*/
			//copie de la matrice initiale
			final MyMatrix tempMatrix = MyMatrix.copyMyMatrix(myPreferenceMatrix);

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
			csa.appendCommaSeparator();

			/*écriture des indices de la valeur proposée par Saaty dans le fichier*/
			tempString = "" + (tempMatrixValue.getRow() + 1);
			csa.append(tempString);
			csa.appendCommaSeparator();
			tempString = "" + (tempMatrixValue.getColumn() + 1);
			csa.append(tempString);
			csa.appendCommaSeparator();

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
			csa.appendCommaSeparator();

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

		final MyMatrix myMatrix = new MyMatrix(matrixSize, matrixSize);

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

		MyMatrix preferenceMatrix = createMatrix();
		//	preferenceMatrix.print(5, 5);

		System.out.println("Merci de patienter");

		/*Interface graphique*/
		//Attention true si c'est le 1er pb false si deuxième et false si langue francaise
		matrixTableModel.setMatrix(preferenceMatrix, SampleMatrixHeaders.getColumnHeader(true, false));
		maTable.setModel(matrixTableModel);

		showMatrixTable(maTable, preferenceMatrix);

		PriorityVector priorityVector = PriorityVector.build(preferenceMatrix);
		//	priorityVector.print(5, 5);


		/*Ecriture de la matrice et du vecteur de priorité dans le fichier*/
		csa.append(preferenceMatrix);
		csa.appendLineFeed();
		csa.append(priorityVector);
		csa.appendLineFeed();

		//Writing of the CR
//		tempBoolean = consistencyChecker.isConsistent(preferenceMatrix, priorityVector);
		tempString = "" + consistencyChecker.getConsistencyRatio();
		csa.append(tempString);
		csa.appendLineFeed();
		csa.appendLineFeed();

		//Writing of the headers of the table in which events are memorised
		csa.append(
				"BestFit;Saaty i;Saaty j; Saaty consistency;Expert Init Value;Expert Changed Value ; Expert Position in Saaty's ranking;CR\n");
		csa.appendLineFeed();
		csa.close();

		/*While they are inconsistencies*/
		while (!consistencyChecker.isConsistent(preferenceMatrix, priorityVector)) {

			//incrémentation du compteur du nombre d'itération
			iterationCounter++;

			System.out.println("\n**********          Matrice incohérente"
							   + "          **********\n CR = " + consistencyChecker.getConsistencyRatio()
							   + "\n");

			/*Calcul matrice epsilon*/
			MyMatrix epsilon = SaatyTools.calculateEpsilonMatrix(preferenceMatrix, priorityVector);

			/*Recherche de la valeur à modifier*/
			collectionOfSortedMatrixValues = SaatyTools.getRank(preferenceMatrix, priorityVector, epsilon);
			MatrixValue matrixValue = readSaatyRanking(collectionOfSortedMatrixValues, preferenceMatrix, file);

			System.out.println(
					"Vous avez choisi de remplacer la valeur "
					+ preferenceMatrix.get(matrixValue.getRow(), matrixValue.getColumn())
					+ " de coordonnées "
					+ " ( "
					+ (matrixValue.getRow() + 1)
					+ " , "
					+ (matrixValue.getColumn() + 1)
					+ " )"
					+ "\nSaisissez la valeur par laquelle vous souhaitez remplacer votre pondération");

			/*Ecrire la valeur que souhaite modifier l'expert*/
			csa = new CharSequenceAppender(file);
			tempString = "" + preferenceMatrix.get(matrixValue.getRow(), matrixValue.getColumn());
			csa.append(tempString);
			csa.appendCommaSeparator();

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
			csa.appendCommaSeparator();

			/*Calculer le placement dans le classement de Saaty*/
			int location = SaatyTools.getLocationInRank(collectionOfSortedMatrixValues,
														matrixValue.getRow(),
														matrixValue.getColumn());
			tempString = "" + location;
			csa.append(tempString);
			csa.appendCommaSeparator();

			/*Changement d'une valeur et de la valeur réciproque associée dans
			la matrice*/

			//Valeur directement modifiée
			matrixValue.setValue(newValue);
			preferenceMatrix.setMatrixValue(matrixValue);

			//Valeur réciproquement modifiée
			int tempI = matrixValue.getRow();
			int tempJ = matrixValue.getColumn();
			matrixValue.setValue(1 / newValue);
			matrixValue.setRow(tempJ);
			matrixValue.setColumn(tempI);
			preferenceMatrix.setMatrixValue(matrixValue);

			//Affichage nouvelle matrice
			//	preferenceMatrix.print(5, 5);

			//Affichage nouvelle matrice
			//Attention true si c'est le 1er problème false si deuxième et false si langue francaise
			matrixTableModel.setMatrix(preferenceMatrix, SampleMatrixHeaders.getColumnHeader(true, false));
			maTable.setModel(matrixTableModel);

			//Réactualisation du vecteur de priorité associé à la nouvelle matrice
			priorityVector = PriorityVector.build(preferenceMatrix);
			//	priorityVector.print(5, 5);

			//Ecriture du nouveau CR
//			tempBoolean = consistencyChecker.isConsistent(preferenceMatrix, priorityVector);
			tempString = "" + consistencyChecker.getConsistencyRatio();
			csa.append(tempString);

			csa.close();
		}

		System.out.println("CR = " + consistencyChecker.getConsistencyRatio());
		System.out.println("***********************************************"
						   + "\n**  Félicitation ! La matrice est cohérente  **\n"
						   + "***********************************************");

		csa = new CharSequenceAppender(file);
		//Ecriture de la matrice et du vecteur de priorité dans le fichier
		csa.appendLineFeed();
		csa.appendLineFeed();
		csa.append(preferenceMatrix);
		csa.appendLineFeed();
		csa.append(priorityVector);
		csa.appendLineFeed();

		//Ecriture du CR
//		tempBoolean = consistencyChecker.isConsistent(preferenceMatrix, priorityVector);
		tempString = "" + consistencyChecker.getConsistencyRatio();
		csa.append(tempString);
		csa.appendLineFeed();
		csa.appendLineFeed();

		//Ecriture du nombre d'iterations
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
