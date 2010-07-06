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

	public static TreeMap<Double, MatrixValue> createTreeMap(MyMatrix myPreferencMatrix) {
		int rows = myPreferencMatrix.getRowDimension();
		int columns = myPreferencMatrix.getColumnDimension();
		TreeMap<Double, MatrixValue> myTreeMap = new TreeMap<Double, MatrixValue>();


		/*Création d'une collection de MatrixValue*/
		Collection<MatrixValue> matrixValues = new ArrayList<MatrixValue>();
		for (int i = 0; i < rows; i++) {
			for (int j = i + 1; j < columns; j++) {
				matrixValues.add(myPreferencMatrix.getMatrixValue(i, j));
			}
		}
		/*Remplit myTreeMap de MatrixValue stockées dans la collection*/
		for (Iterator<MatrixValue> valueIterator = matrixValues.iterator(); valueIterator.hasNext();) {
			MatrixValue matrixValue = valueIterator.next();
			myTreeMap.put(matrixValue.getValue(), matrixValue);
		}
		return myTreeMap;
	}

	public static MatrixValue getValueToModifiyByRanking(
			Collection<MatrixValue> collectionOfNonSortedMatrixValues) {


		Scanner sc = new Scanner(System.in);
		int isValueChosen = 0;
		String expertsChoice;
		MatrixValue matrixValue = new MatrixValue();
		Iterator<MatrixValue> valueIterator;



		/*Boucle pour que l'utilisateur désigne la valeur à modifier*/
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

	public static Collection<MatrixValue> getRank(MyMatrix myPreferenceMatrix) {

		MatrixValue matrixValue = new MatrixValue();
		Collection<MatrixValue> collectionOfNonSortedMatrixValues = new ArrayList<MatrixValue>();
		List<MatrixValue> listOfMatrixValue;
		TreeMap<Double, MatrixValue> myTreeMap = new TreeMap<Double, MatrixValue>();
		myTreeMap = createTreeMap(myPreferenceMatrix);


		while (!myTreeMap.isEmpty()) {
			matrixValue = myTreeMap.pollLastEntry().getValue();
			collectionOfNonSortedMatrixValues.add(matrixValue);
		}

		listOfMatrixValue = new ArrayList<MatrixValue>(collectionOfNonSortedMatrixValues);

		Collections.shuffle(listOfMatrixValue);

		collectionOfNonSortedMatrixValues.clear();

		/*Vider la collection triée pour la remplir d'éléments aléatoires.*/

		for (MatrixValue matrixValue1 : listOfMatrixValue) {
			collectionOfNonSortedMatrixValues.add(matrixValue1);
		}

		return collectionOfNonSortedMatrixValues;

	}

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


		/*Dresser classement de Saaty*/
		epsilon = SaatysTools.calculateEpsilonMatrix(myPreferenceMatrix,
													 priorityVector);
		collectionOfSortedMatrixValues = SaatysTools.getRank(myPreferenceMatrix,
															 priorityVector,
															 epsilon);

		/*Parcours simultané des 2 classement tant que la valeur à modifier n'est
		pas trouvée dans le classement aléatoire*/


		//iterateur pour parcourir le classement de Saaty
		saatysIterator = collectionOfSortedMatrixValues.iterator();
		//iterateur pour parcourir le classement aléatoire
		randomsIterator = collectionOfNonSortedMatrixValues.iterator();

		/*parcours de la liste pour l'écriture dans le fichier*/
		while ((randomsIterator.hasNext()) && (!isFound)) {

			// PARTIE SAATY

			saatysMatrixValue = saatysIterator.next();
			csa.insertLineFeed();
			/*écriture du best fit associé à la valeur proposée*/
			//copie de la matrice initiale
			saatysMatrix = saatysMatrix.copyMyMatrix(myPreferenceMatrix);
			//calcul du vecteur propre associé à saatyMatrix
			saatysVector = PriorityVector.build(saatysMatrix);
			//calcul du best fit
			double BestFit = SaatysTools.calculateBestFit(saatysMatrix, saatysVector, saatysMatrixValue.
					getRow(), saatysMatrixValue.getColumn());
			//écriture du best fit
			tempString = "" + BestFit;
			csa.append(tempString);
			csa.insertSeparator();

			/*écriture des indices de la valeur proposée par Saaty dans le fichier*/
			tempString = "" + saatysMatrixValue.getRow();
			csa.append(tempString);
			csa.insertSeparator();
			tempString = "" + saatysMatrixValue.getColumn();
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
			tempString = "" + randomsMatrixValue.getRow();
			csa.append(tempString);
			csa.insertSeparator();

			tempString = "" + randomsMatrixValue.getColumn();
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
