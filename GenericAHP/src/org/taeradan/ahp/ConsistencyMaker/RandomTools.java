/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.ConsistencyMaker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import org.taeradan.ahp.ConsistencyChecker;
import org.taeradan.ahp.PriorityVector;

/**
 *
 * @author Marianne
 */
public class RandomTools {


	/**
	 * This method returns the value which will be modified by the expert
	 * @param collectionOfNonSortedMatrixValues
	 * @return
	 */
	public static MatrixValue getValueToModifiyByRanking(
			Collection<MatrixValue> collectionOfNonSortedMatrixValues) {


		Scanner sc = new Scanner(System.in);
		// boolean
		int isValueChosen = 0;
		String expertsChoice;
		MatrixValue matrixValue = new MatrixValue();
		Iterator<MatrixValue> valueIterator;



		/* While loop which proposes a random ranking of MatrixValue
		 * while the expert hasn't chosen the value he wants to modify
		 */
		valueIterator = collectionOfNonSortedMatrixValues.iterator();

		while (isValueChosen == 0) {
			matrixValue = valueIterator.next();
			System.out.println("Souhaitez-vous modifier la valeur "
							   + matrixValue.getValue()
							   + " ( "
							   + (matrixValue.getRow() + 1)
							   + " , "
							   + (matrixValue.getColumn() + 1)
							   + " )"
							   + " ? O/N");
			expertsChoice = sc.nextLine();

			if (expertsChoice.equalsIgnoreCase("O")) {
				isValueChosen = 1;
			} else if (!valueIterator.hasNext()) {
				System.out.println("Retour en haut du classement");
				valueIterator = collectionOfNonSortedMatrixValues.iterator();
			}

		}


		return matrixValue;
	}
/**
 * Build randomly a rank of MatrixValue from a MyMatrix
 * @param myPreferenceMatrix
 * @return
 */
	public static Collection<MatrixValue> getRank(MyMatrix myPreferenceMatrix) {

		MatrixValue matrixValue = new MatrixValue();
		Collection<MatrixValue> collectionOfNonSortedMatrixValues = new ArrayList<MatrixValue>();
		List<MatrixValue> listOfMatrixValue = new ArrayList<MatrixValue>();

		for (int i = 0; i < myPreferenceMatrix.getRowDimension(); i++) {
			for (int j = i + 1; j < myPreferenceMatrix.getColumnDimension(); j++) {
				matrixValue = new MatrixValue();
				matrixValue.setRow(i);
				matrixValue.setColumn(j);
				matrixValue.setValue(myPreferenceMatrix.get(i, j));
				listOfMatrixValue.add(matrixValue);
			}
		}

		Collections.shuffle(listOfMatrixValue);

		/*Mettre éléments aléatoire dans collection*/
		for (MatrixValue matrixValue1 : listOfMatrixValue) {
			collectionOfNonSortedMatrixValues.add(matrixValue1);
		}

		return collectionOfNonSortedMatrixValues;
	}

	/**
	 * Write the rank which is printed on screen (Random rank), in a csv file
	 * Write also Saaty's method propositions
	 * @param myPreferenceMatrix
	 * @param collectionOfNonSortedMatrixValues
	 * @param chosenValueToBeModified
	 * @param priorityVector
	 * @param file
	 * @throws IOException
	 */
	public static void writeRandomAndSaatysProposition(MyMatrix myPreferenceMatrix,
													   Collection<MatrixValue> collectionOfNonSortedMatrixValues,
													   MatrixValue chosenValueToBeModified,
													   MyMatrix priorityVector, String file) throws IOException {
		MyMatrix epsilon = new MyMatrix();
		Collection<MatrixValue> collectionOfSortedMatrixValues = new ArrayList<MatrixValue>();
		Iterator<MatrixValue> saatysIterator;
		Iterator<MatrixValue> randomsIterator;
		CharSequenceAppender csa = new CharSequenceAppender(file);
		MyMatrix saatysMatrix = new MyMatrix();
		MyMatrix saatysVector = new MyMatrix();
		MyMatrix randomsMatrix = new MyMatrix();
		MyMatrix randomsVector = new MyMatrix();
		String tempString;
		ConsistencyChecker consistencyChecker = new ConsistencyChecker();
		boolean isFound = false;
		boolean tempBoolean;
		MatrixValue saatysMatrixValue = new MatrixValue();
		MatrixValue randomsMatrixValue = new MatrixValue();


		/*Build Saaty's ranking*/
		epsilon = SaatysTools.calculateEpsilonMatrix(myPreferenceMatrix,
													 priorityVector);
		collectionOfSortedMatrixValues = SaatysTools.getRank(myPreferenceMatrix,
															 priorityVector,
															 epsilon);

		/*Simultaneous reading of the 2 classifications as the value to edit is
not found in the random ranking*/


		//iterator to read Saaty's ranking
		saatysIterator = collectionOfSortedMatrixValues.iterator();
		//iterator to read random ranking
		randomsIterator = collectionOfNonSortedMatrixValues.iterator();

		/*reading of the list to write in the csv file*/
		while ((randomsIterator.hasNext()) && (!isFound)) {

			// SAATY'S PART

			saatysMatrixValue = saatysIterator.next();
			csa.insertLineFeed();
			/*Writing of de the best fit related to the proposed value*/
			//Copy of the original matrix
			saatysMatrix = saatysMatrix.copyMyMatrix(myPreferenceMatrix);
			//saatysMatrix's eigenvector calculation
			saatysVector = PriorityVector.build(saatysMatrix);
			//best fit calculation
			double BestFit = SaatysTools.calculateBestFit(saatysMatrix, saatysVector, saatysMatrixValue.
					getRow(), saatysMatrixValue.getColumn());
			//best fit writing
			tempString = "" + BestFit;
			csa.append(tempString);
			csa.insertSeparator();

			/*écriture des indices de la valeur proposée par Saaty dans le fichier*/
			tempString = "" + (saatysMatrixValue.getRow()+1);
			csa.append(tempString);
			csa.insertSeparator();
			tempString = "" + (saatysMatrixValue.getColumn()+1);
			csa.append(tempString);
			csa.insertSeparator();

			/*écriture de la cohérence si l'expert suivait les conseils de Saaty*/

			//remplacement de la valeur (i,j) par BestFit
			MatrixValue newMatrixValue = new MatrixValue();
			newMatrixValue.setRow(saatysMatrixValue.getRow());
			newMatrixValue.setColumn(saatysMatrixValue.getColumn());
			newMatrixValue.setValue(BestFit);
			saatysMatrix.setMatrixValue(newMatrixValue);

			//remplacement de la valeur (j,i) par 1/BestFit
			newMatrixValue.setRow(saatysMatrixValue.getColumn());
			newMatrixValue.setColumn(saatysMatrixValue.getRow());
			newMatrixValue.setValue(1. / BestFit);
			saatysMatrix.setMatrixValue(newMatrixValue);

			//rafraîchissement du vecteur de priorité
			saatysVector = PriorityVector.build(saatysMatrix);
			//calcul et écriture de la cohérence
			tempBoolean = consistencyChecker.isConsistent(saatysMatrix, saatysVector);
			tempString = "" + consistencyChecker.getCrResult();
			csa.append(tempString);
			csa.insertSeparator();


			//PARTIE ALEATOIRE

			randomsMatrixValue = randomsIterator.next();

			//copie de la matrice initiale
			randomsMatrix = randomsMatrix.copyMyMatrix(myPreferenceMatrix);
			//calcul du vecteur propre associé à randomsMatrix
			randomsVector = PriorityVector.build(randomsMatrix);

			/*écriture best fit pour la méthode aléatoire*/
			BestFit = SaatysTools.calculateBestFit(randomsMatrix, randomsVector, randomsMatrixValue.
					getRow(), randomsMatrixValue.getColumn());
			tempString = "" + BestFit;
			csa.append(tempString);
			csa.insertSeparator();

			/*écriture des indices de la valeur aléatoire proposé*/
			tempString = "" + (randomsMatrixValue.getRow()+1);
			csa.append(tempString);
			csa.insertSeparator();

			tempString = "" + (randomsMatrixValue.getColumn()+1);
			csa.append(tempString);
			csa.insertSeparator();

			/*écriture du placement de la valeur aléatoire dans le classement de Saaty*/
			tempString = "" + SaatysTools.getLocationInRank(collectionOfSortedMatrixValues, randomsMatrixValue.
					getRow(), randomsMatrixValue.getColumn());
			csa.append(tempString);
			csa.insertSeparator();

			/*écriture de la cohérence après modification de la valeur aléatoire par le bestfit*/

			//remplacement de la valeur (i,j) par BestFit
			newMatrixValue = new MatrixValue();
			newMatrixValue.setRow(randomsMatrixValue.getRow());
			newMatrixValue.setColumn(randomsMatrixValue.getColumn());
			newMatrixValue.setValue(BestFit);
			randomsMatrix.setMatrixValue(newMatrixValue);

			//remplacement de la valeur (j,i) par 1/BestFit
			newMatrixValue.setRow(randomsMatrixValue.getColumn());
			newMatrixValue.setColumn(randomsMatrixValue.getRow());
			newMatrixValue.setValue(1. / BestFit);
			randomsMatrix.setMatrixValue(newMatrixValue);

			//rafraîchissement du vecteur de priorité
			randomsVector = PriorityVector.build(randomsMatrix);
			//calcul et écriture de la cohérence
			tempBoolean = consistencyChecker.isConsistent(randomsMatrix, randomsVector);
			tempString = "" + consistencyChecker.getCrResult();
			csa.append(tempString);
			csa.insertSeparator();


			if (chosenValueToBeModified.equals(randomsMatrixValue)) {
				isFound = true;
			}



		}

		csa.close();

	}
}
