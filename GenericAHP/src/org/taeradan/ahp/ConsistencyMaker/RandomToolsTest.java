/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/* This class aims to execute the random method, for a given matrix,
 that you can fill through the console
 */
package org.taeradan.ahp.ConsistencyMaker;

import org.nfunk.jep.JEP;
import org.taeradan.ahp.ConsistencyChecker;
import org.taeradan.ahp.PriorityVector;
import org.taeradan.ahp.gui.MyMatrixTable;
import org.taeradan.ahp.gui.MyMatrixTableModel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/**
 * @author Marianne
 */
public class RandomToolsTest {

	private static void showMatrixTable(MyMatrixTable maTable, MyMatrix myMatrix) throws HeadlessException {
		// Show a frame with a table
		JFrame maFenetre = new JFrame("Aperçu de la Matrice de Préférences");
		maFenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		maFenetre.setContentPane(maTable);
		maFenetre.setSize(1000, 27 * myMatrix.getRowDimension());
		maFenetre.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {

		MyMatrix myPreferenceMatrix = new MyMatrix();
		MyMatrix priorityVector = new MyMatrix();
		MatrixValue matrixValue = new MatrixValue();
		String expertsChoice;
		Scanner userInput = new Scanner(System.in);
		ConsistencyChecker consistencyChecker = new ConsistencyChecker();
		Collection<MatrixValue> collectionOfNonSortedMatrixValues = new ArrayList<MatrixValue>();
		String file;
		Boolean tempBoolean;
		String tempString;
		MyMatrixTable maTable = new MyMatrixTable();
		MyMatrixTableModel matrixTableModel = new MyMatrixTableModel();
		int iterationsCounter = 0;

		/*Select the file, in which simulation will be saved*/
		System.out.println("Saisir le nom du fichier permettant de garder la trace des actions");
		file = userInput.next();
		file += ".csv";
		CharSequenceAppender csa = new CharSequenceAppender(file);

		/*Matrix creation*/
		myPreferenceMatrix = SaatysToolsTest.createMatrix();
		//	myPreferenceMatrix.print(5, 2);
		priorityVector = PriorityVector.build(myPreferenceMatrix);
		//	priorityVector.print(5, 3);

		/*WIP*/
		System.out.println("Merci de patienter.");

		//Attention true si c'est le 1er pb false si deuxième et false si langue francaise
		matrixTableModel.setMatrix(myPreferenceMatrix, true, false);
		maTable.setModel(matrixTableModel);

		/*Print a matrix view*/
		showMatrixTable(maTable, myPreferenceMatrix);


		/*Writing of :
		 * the matrix
		 * the eigenvector
		 * the Consistency Ratio
		 * in the header lines of the file
		 */

		//Writing of the matrix
		csa.insertMatrix(myPreferenceMatrix);
		csa.insertLineFeed();

		//Writing of the eigenvector
		csa.insertMatrix(priorityVector);
		csa.insertLineFeed();

		//Writing of the CR
		tempBoolean = consistencyChecker.isConsistent(myPreferenceMatrix, priorityVector);
		tempString = "" + consistencyChecker.getConsistencyRatio();
		csa.append(tempString);
		csa.insertLineFeed();
		csa.insertLineFeed();

		//Writing of the headers of the table in which events are memorised
		csa.append(
			"BestFit;Saaty i;Saaty j;Saaty consistency;BestFit for random value;Random i;Random j;Position in Saaty's ranking;Random consistency;Expert Init Value;Expert Changed Value;CR\n");
		csa.insertLineFeed();

		csa.close();

		/*While they are inconsistencies*/
		while (!consistencyChecker.isConsistent(myPreferenceMatrix, priorityVector)) {

			iterationsCounter++;

			System.out.println("\n**********          Matrice incohérente" + "          **********\n CR = " + consistencyChecker.getConsistencyRatio() + "\n");

			collectionOfNonSortedMatrixValues = RandomTools.getRank(myPreferenceMatrix);
			matrixValue = RandomTools.getValueToModifiyByRanking(collectionOfNonSortedMatrixValues);

			/*Writing of Saaty's propositions and of random ranking*/
			RandomTools.writeRandomAndSaatysProposition(myPreferenceMatrix,
				collectionOfNonSortedMatrixValues,
				matrixValue,
				priorityVector, file);

			/*Writing of the value, which will be changed by the expert*/
			csa = new CharSequenceAppender(file);
			tempString = "" + matrixValue.getValue();
			csa.append(tempString);
			csa.insertSeparator();

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


			expertsChoice = userInput.next();
			final JEP myParser = new JEP();
			myParser.parseExpression(expertsChoice);
			double newValue = myParser.getValue();

			while (!SaatysToolsTest.isInSaatysSacale(newValue)) {
				System.out.println(
					"Erreur : cette valeur n'appartient à l'échelle de Saaty. Retapez votre valeur.");
				expertsChoice = userInput.next();
				myParser.parseExpression(expertsChoice);
				newValue = myParser.getValue();
			}

			/*Wrriting of the new value*/
			tempString = "" + newValue;
			csa.append(tempString);
			csa.insertSeparator();

			/*Changement d'une valeur et de la valeur réciproque associée dans
			la matrice*/

			//Valeur directement modifiée
			matrixValue.setValue(newValue);
			myPreferenceMatrix.setMatrixValue(matrixValue);

			//Valeur réciproquement modifiée
			int tempI = matrixValue.getRow();
			int tempJ = matrixValue.getColumn();
			matrixValue.setValue(1 / newValue);
			matrixValue.setRow(tempJ);
			matrixValue.setColumn(tempI);
			myPreferenceMatrix.setMatrixValue(matrixValue);

			//Affichage nouvelle matrice
			//	myPreferenceMatrix.print(5, 5);

			//Affichage nouvelle matrice
			//Attention true si c'est le 1er problème false si deuxième et false si langue francaise
			matrixTableModel.setMatrix(myPreferenceMatrix, true, false);
			maTable.setModel(matrixTableModel);


			//Réactualisation du vecteur de priorité associé à la nouvelle matrice
			priorityVector = PriorityVector.build(myPreferenceMatrix);
			//	priorityVector.print(5, 5);

			//écriture du nouveau CR
			tempBoolean = consistencyChecker.isConsistent(myPreferenceMatrix, priorityVector);
			tempString = "" + consistencyChecker.getConsistencyRatio();
			csa.append(tempString);


			csa.close();
		}

		System.out.println("CR = " + consistencyChecker.getConsistencyRatio() + "\n");
		System.out.println("***********************************************"
			+ "\n**  Félicitation ! La matrice est cohérente  **\n"
			+ "***********************************************");

		csa = new CharSequenceAppender(file);
		//Ecriture de la matrice et du vecteur de priorité dans le fichier*/
		csa.insertLineFeed();
		csa.insertLineFeed();
		csa.insertMatrix(myPreferenceMatrix);
		csa.insertLineFeed();
		csa.insertMatrix(priorityVector);
		csa.insertLineFeed();

		//Ecriture du CR
		tempBoolean = consistencyChecker.isConsistent(myPreferenceMatrix, priorityVector);
		tempString = "" + consistencyChecker.getConsistencyRatio();
		csa.append(tempString);
		csa.insertLineFeed();
		csa.insertLineFeed();

		//Ecriture du nombre d'iterations
		tempString = "Number of Iterations;" + iterationsCounter;
		csa.append(tempString);

		csa.close();

	}

	/**
	 *
	 */
	private RandomToolsTest() {
	}
}
