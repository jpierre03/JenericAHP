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

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

/**
 * This class aims to execute the random method, for a given matrix, that you can fill through the console
 *
 * @author Jean-Pierre PRUNARET
 * @author Marianne
 */
public final class RandomToolsTest {

	private RandomToolsTest() {
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

	public static void main(String[] args)
		throws
		IOException {

		final Scanner userInput = new Scanner(System.in);
		final ConsistencyChecker consistencyChecker = new ConsistencyChecker();
		final MyMatrixTable maTable = new MyMatrixTable();
		final MyMatrixTableModel matrixTableModel = new MyMatrixTableModel();
		int iterationCounter = 0;

		MyMatrix preferenceMatrix;
		MyMatrix priorityVector;
		MatrixValue matrixValue;
		String expertsChoice;
		Collection<MatrixValue> nonSortedMatrixValues;
		String tempString;

		/** Select the file, in which simulation results  will be saved */
		System.out.println("Saisir le nom du fichier permettant de garder la trace des actions");
		String file = userInput.next();
		file += ".csv";
		CharSequenceAppender csa = new CharSequenceAppender(file);

		/*Matrix creation*/
		preferenceMatrix = SaatyToolsTest.createMatrix();
		priorityVector = PriorityVector.build(preferenceMatrix);

		/*WIP*/
		System.out.println("Merci de patienter.");

		boolean isFirstProblem = true;
		boolean isEnglish = false;
		matrixTableModel.setMatrix(preferenceMatrix, SampleMatrixHeaders.getColumnHeader(isFirstProblem, isEnglish));
		maTable.setModel(matrixTableModel);

		/*Print a matrix view*/
		showMatrixTable(maTable, preferenceMatrix);

		/*Writing of :
		 * the matrix
		 * the eigenvector
		 * the Consistency Ratio
		 * in the header lines of the file
		 */

		//Writing of the matrix
		csa.append(preferenceMatrix);
		csa.appendLineFeed();
		csa.append(priorityVector);
		csa.appendLineFeed();

		//Writing of the CR
		consistencyChecker.computeConsistency(preferenceMatrix, priorityVector);
		tempString = "" + consistencyChecker.getConsistencyRatio();
		csa.append(tempString);
		csa.appendLineFeed();
		csa.appendLineFeed();

		//Writing of the headers of the table in which events are memorised
		csa.append(
			"BestFit;Saaty i;Saaty j;Saaty consistency;BestFit for random value;Random i;Random j;Position in Saaty's ranking;Random consistency;Expert Init Value;Expert Changed Value;CR\n");
		csa.appendLineFeed();
		csa.close();

		/*While they are inconsistencies*/
		while (!consistencyChecker.isConsistent(preferenceMatrix, priorityVector)) {

			//incrémentation du compteur du nombre d'itération
			iterationCounter++;

			System.out.println("\n**********          Matrice incohérente"
				+ "          **********\n CR = " + consistencyChecker.getConsistencyRatio()
				+ "\n");

			nonSortedMatrixValues = RandomTools.getRank(preferenceMatrix);
			matrixValue = RandomTools.getValueToModifiyByRanking(nonSortedMatrixValues);

			/*Writing of Saaty's propositions and of random ranking*/
			RandomTools.writeRandomAndSaatyPropositions(
				preferenceMatrix,
				nonSortedMatrixValues,
				matrixValue,
				priorityVector,
				file);

			/*Writing of the value, which will be changed by the expert*/
			csa = new CharSequenceAppender(file);
			tempString = "" + matrixValue.getValue();
			csa.append(tempString);
			csa.appendCommaSeparator();

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


			/*Lecture de la valeur saisie au clavier*/
			expertsChoice = userInput.next();
			final JEP myParser = new JEP();
			myParser.parseExpression(expertsChoice);
			double newValue = myParser.getValue();

			while (!SaatyToolsTest.isInSaatyScale(newValue)) {
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
}
