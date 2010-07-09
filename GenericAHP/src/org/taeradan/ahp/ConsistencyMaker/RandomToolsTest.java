/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.ConsistencyMaker;

import java.awt.HeadlessException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.nfunk.jep.JEP;
import org.taeradan.ahp.ConsistencyChecker;
import org.taeradan.ahp.PriorityVector;
import java.util.Collection;
import javax.swing.JFrame;
import org.taeradan.ahp.gui.MyMatrixTable;
import org.taeradan.ahp.gui.MyMatrixTableModel;

/**
 *
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
	 *
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

		/*Choix du nom du fichier*/
		System.out.println("Saisir le nom du fichier permettant de garder la trace des actions");
		file = userInput.next();
		file += ".csv";
		CharSequenceAppender csa = new CharSequenceAppender(file);

		/*Création de la matrice*/
		myPreferenceMatrix = SaatysToolsTest.createMatrix();
		myPreferenceMatrix.print(5, 2);
		priorityVector = PriorityVector.build(myPreferenceMatrix);
		priorityVector.print(5, 3);

		matrixTableModel.setMatrix(myPreferenceMatrix);
		maTable.setModel(matrixTableModel);
		/*Affichage de l'aperçu de la matrice*/
		showMatrixTable(maTable, myPreferenceMatrix);


		/*Ecriture de la matrice, du vecteur propre et du CR en tête du fichier*/

		//Ecriture de la matrice et du vecteur de priorité dans le fichier*/
		csa.insertMatrix(myPreferenceMatrix);
		csa.insertLineFeed();
		csa.insertMatrix(priorityVector);
		csa.insertLineFeed();

		//Ecriture du CR
		tempBoolean = consistencyChecker.isConsistent(myPreferenceMatrix, priorityVector);
		tempString = "" + consistencyChecker.getCrResult();
		csa.append(tempString);
		csa.insertLineFeed();
		csa.insertLineFeed();

		//en-tête du tableau
		csa.append(
				"BestFit;Saaty i;Saaty j;Saaty consistency;BestFit for random value;Random i;Random j;Position in Saaty's ranking;Random consistency;Expert Init Value;Expert Changed Value;CR\n");
		csa.insertLineFeed();

		csa.close();

		/*Tant que la matrice est incohérente*/
		while (!consistencyChecker.isConsistent(myPreferenceMatrix, priorityVector)) {
			System.out.println("\n**********          Matrice incohérente" +"          **********\n CR = "+ consistencyChecker.getCrResult()+"\n");
			
			collectionOfNonSortedMatrixValues = RandomTools.getRank(myPreferenceMatrix);
			matrixValue = RandomTools.getValueToModifiyByRanking(collectionOfNonSortedMatrixValues);

			/*Ecriture des propositions de Saaty et du classement aléatoire*/
			RandomTools.writeRandomAndSaatysProposition(myPreferenceMatrix,
														collectionOfNonSortedMatrixValues,
														matrixValue,
														priorityVector, file);

			/*Ecrire la valeur que souhaite modifier l'expert*/
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

			/*Ecriture de la nouvelle valeur*/
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
			matrixTableModel.setMatrix(myPreferenceMatrix);
			maTable.setModel(matrixTableModel);


			//Réactualisation du vecteur de priorité associé à la nouvelle matrice
			priorityVector = PriorityVector.build(myPreferenceMatrix);
		//	priorityVector.print(5, 5);

			//écriture du nouveau CR
			tempBoolean = consistencyChecker.isConsistent(myPreferenceMatrix, priorityVector);
			tempString = "" + consistencyChecker.getCrResult();
			csa.append(tempString);


			csa.close();
		}

		System.out.println("CR = " + consistencyChecker.getCrResult()+"\n");
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
		tempString = "" + consistencyChecker.getCrResult();
		csa.append(tempString);
		csa.insertLineFeed();
		csa.insertLineFeed();
		csa.close();

	}

	/**
	 * 
	 */
	private RandomToolsTest() {
	}
}
