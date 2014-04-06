/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.prototype.ConsistencyMaker;

import org.taeradan.ahp.ConsistencyChecker;
import org.taeradan.ahp.PriorityVector;
import org.taeradan.ahp.matrix.MatrixValue;
import org.taeradan.ahp.matrix.MyMatrix;
import org.taeradan.ahp.prototype.ConsistencyMaker.csv_output_marianne.CharSequenceAppender;

import java.io.IOException;
import java.util.*;

/**
 * @author Marianne
 * @author Jean-Pierre PRUNARET
 */
public final class RandomTools {

	private RandomTools() {
	}

	/**
	 * This method returns the value which will be modified by the expert
	 */
	public static MatrixValue getValueToModifiyByRanking(Collection<MatrixValue> nonSortedMatrixValues) {
		if (nonSortedMatrixValues == null
			|| nonSortedMatrixValues.size() <= 0) {
			throw new IllegalArgumentException("Collection should contain element");
		}

		boolean isValueChosen = false;
		MatrixValue resultMatrixValue = new MatrixValue();
		final Scanner sc = new Scanner(System.in);
		Iterator<MatrixValue> valueIterator = nonSortedMatrixValues.iterator();

		/** Proposes a random ranking while the expert hasn't chosen the value he wants to modify */
		while (isValueChosen == false) {
			resultMatrixValue = valueIterator.next();

			final StringBuilder sb = new StringBuilder();
			sb.append("Souhaitez-vous modifier la valeur ");
			sb.append(resultMatrixValue.getValue());
			sb.append(" ( ");
			sb.append((resultMatrixValue.getRow() + 1));
			sb.append(" , ");
			sb.append((resultMatrixValue.getColumn() + 1));
			sb.append(" )");
			sb.append(" ? O/N");

			System.out.println(sb.toString());

			final String expertsChoice = sc.nextLine();

			if (expertsChoice.equalsIgnoreCase("O")) {
				isValueChosen = true;
			} else if (!valueIterator.hasNext()) {
				System.out.println("Retour en haut du classement");
				valueIterator = nonSortedMatrixValues.iterator();
			}
		}

		return resultMatrixValue;
	}

	/**
	 * Build randomly a rank of MatrixValue from a MyMatrix
	 */
	public static Collection<MatrixValue> getRank(MyMatrix myPreferenceMatrix) {

		final Collection<MatrixValue> collectionOfNonSortedMatrixValues = new ArrayList<>();
		final List<MatrixValue> listOfMatrixValue = new ArrayList<>();

		for (int i = 0; i < myPreferenceMatrix.getRowDimension(); i++) {
			for (int j = i + 1; j < myPreferenceMatrix.getColumnDimension(); j++) {
				MatrixValue matrixValue = new MatrixValue(i, j, myPreferenceMatrix.get(i, j));
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
	 * Write the rank which is printed on screen (Random rank), in a csv file Write also Saaty's method propositions
	 */
	public static void writeRandomAndSaatyPropositions(MyMatrix preferenceMatrix,
							   Collection<MatrixValue> nonSortedMatrixValues,
							   MatrixValue chosenValueToBeModified,
							   MyMatrix priorityVector,
							   String file)
		throws
		IOException {

		MyMatrix epsilon = new MyMatrix();
		Collection<MatrixValue> sortedMatrixValues = new ArrayList<>();
		Iterator<MatrixValue> saatyIterator;
		Iterator<MatrixValue> randomsIterator;
		CharSequenceAppender csa = new CharSequenceAppender(file);
		MyMatrix saatyMatrix = new MyMatrix();
		MyMatrix saatyVector = new MyMatrix();
		MyMatrix randomsMatrix = new MyMatrix();
		MyMatrix randomsVector = new MyMatrix();
		String tempString;
		ConsistencyChecker consistencyChecker = new ConsistencyChecker();
		boolean isFound = false;
		boolean tempBoolean;
		MatrixValue saatyMatrixValue = new MatrixValue();
		MatrixValue randomsMatrixValue = new MatrixValue();


		/*Build Saaty's ranking*/
		epsilon = SaatyTools.calculateEpsilonMatrix(preferenceMatrix, priorityVector);
		sortedMatrixValues = SaatyTools.getRank(preferenceMatrix, priorityVector, epsilon);

		/*Simultaneous reading of the 2 classifications as the value to edit is not found in the random ranking*/


		//iterator to read Saaty's ranking
		saatyIterator = sortedMatrixValues.iterator();
		//iterator to read random ranking
		randomsIterator = nonSortedMatrixValues.iterator();

		/*reading of the list to write in the csv file*/
		while ((randomsIterator.hasNext()) && (!isFound)) {

			// SAATY'S PART

			saatyMatrixValue = saatyIterator.next();
			csa.appendLineFeed();
			/*Writing of de the best fit related to the proposed value*/
			//Copy of the original matrix
			saatyMatrix = MyMatrix.copyMyMatrix(preferenceMatrix);
			//saatyMatrix's eigen vector calculation
			saatyVector = PriorityVector.build(saatyMatrix);
			//best fit calculation
			double bestFit = SaatyTools.calculateBestFit(
				saatyMatrix,
				saatyVector,
				saatyMatrixValue.getRow(),
				saatyMatrixValue.getColumn());

			//best fit writing
			tempString = "" + bestFit;
			csa.append(tempString);
			csa.appendCommaSeparator();

			/*écriture des indices de la valeur proposée par Saaty dans le fichier*/
			tempString = "" + (saatyMatrixValue.getRow() + 1);
			csa.append(tempString);
			csa.appendCommaSeparator();
			tempString = "" + (saatyMatrixValue.getColumn() + 1);
			csa.append(tempString);
			csa.appendCommaSeparator();

			/*écriture de la cohérence si l'expert suivait les conseils de Saaty*/

			//remplacement de la valeur (i,j) par bestFit
			MatrixValue newMatrixValue = new MatrixValue(
				saatyMatrixValue.getRow(),
				saatyMatrixValue.getColumn(),
				bestFit);
			saatyMatrix.setMatrixValue(newMatrixValue);

			//remplacement de la valeur (j,i) par 1/bestFit
			newMatrixValue.setRow(saatyMatrixValue.getColumn());
			newMatrixValue.setColumn(saatyMatrixValue.getRow());
			newMatrixValue.setValue(1. / bestFit);
			saatyMatrix.setMatrixValue(newMatrixValue);

			//rafraîchissement du vecteur de priorité
			saatyVector = PriorityVector.build(saatyMatrix);
			//calcul et écriture de la cohérence
			tempBoolean = consistencyChecker.isConsistent(saatyMatrix, saatyVector);
			tempString = "" + consistencyChecker.getConsistencyRatio();
			csa.append(tempString);
			csa.appendCommaSeparator();


			//PARTIE ALEATOIRE

			randomsMatrixValue = randomsIterator.next();

			//copie de la matrice initiale
			randomsMatrix = MyMatrix.copyMyMatrix(preferenceMatrix);
			//calcul du vecteur propre associé à randomsMatrix
			randomsVector = PriorityVector.build(randomsMatrix);

			/*écriture best fit pour la méthode aléatoire*/
			bestFit = SaatyTools.calculateBestFit(randomsMatrix,
				randomsVector,
				randomsMatrixValue.getRow(),
				randomsMatrixValue.getColumn());
			tempString = "" + bestFit;
			csa.append(tempString);
			csa.appendCommaSeparator();

			/*écriture des indices de la valeur aléatoire proposé*/
			tempString = "" + (randomsMatrixValue.getRow() + 1);
			csa.append(tempString);
			csa.appendCommaSeparator();

			tempString = "" + (randomsMatrixValue.getColumn() + 1);
			csa.append(tempString);
			csa.appendCommaSeparator();

			/*écriture du placement de la valeur aléatoire dans le classement de Saaty*/
			tempString = "" + SaatyTools.getLocationInRank(sortedMatrixValues,
				randomsMatrixValue.getRow(),
				randomsMatrixValue.getColumn());
			csa.append(tempString);
			csa.appendCommaSeparator();

			/*écriture de la cohérence après modification de la valeur aléatoire par le bestfit*/

			//remplacement de la valeur (i,j) par bestFit
			newMatrixValue = new MatrixValue(randomsMatrixValue.getRow(),
				randomsMatrixValue.getColumn(),
				bestFit);
			randomsMatrix.setMatrixValue(newMatrixValue);

			//remplacement de la valeur (j,i) par 1/bestFit
			newMatrixValue = new MatrixValue(randomsMatrixValue.getColumn(),
				randomsMatrixValue.getRow(),
				1. / bestFit);
			randomsMatrix.setMatrixValue(newMatrixValue);

			//rafraîchissement du vecteur de priorité
			randomsVector = PriorityVector.build(randomsMatrix);
			//calcul et écriture de la cohérence
			tempBoolean = consistencyChecker.isConsistent(randomsMatrix, randomsVector);
			tempString = "" + consistencyChecker.getConsistencyRatio();
			csa.append(tempString);
			csa.appendCommaSeparator();

			if (chosenValueToBeModified.equals(randomsMatrixValue)) {
				isFound = true;
			}
		}
		csa.close();
	}
}
