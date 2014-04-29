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
	 * Write the rank which is printed on screen (Random rank), in a csv fileName Write also Saaty's method propositions
	 */
	public static void writeRandomAndSaatyPropositions(final MyMatrix preferenceMatrix,
							   final Collection<MatrixValue> nonSortedMatrixValues,
							   final MatrixValue chosenValueToBeModified,
							   final MyMatrix priorityVector,
							   String fileName)
		throws
		IOException {

		final CharSequenceAppender csa = new CharSequenceAppender(fileName);
		ConsistencyChecker consistencyChecker = new ConsistencyChecker();
		boolean isFound = false;

		/** Build Saaty's ranking */
		final MyMatrix epsilon = SaatyTools.calculateEpsilonMatrix(preferenceMatrix, priorityVector);
		final Collection<MatrixValue> sortedMatrixValues = SaatyTools.getRank(epsilon);

		/* Simultaneous reading of the 2 classifications as the value to edit is not found in the random ranking */

		/** iterator to read Saaty's ranking */
		final Iterator<MatrixValue> saatyIterator = sortedMatrixValues.iterator();
		/** iterator to read random ranking */
		final Iterator<MatrixValue> randomsIterator = nonSortedMatrixValues.iterator();

		/*reading of the list to write in the csv fileName*/
		while ((randomsIterator.hasNext()) && (!isFound)) {

			saatyPart(preferenceMatrix, csa, consistencyChecker, saatyIterator);

			isFound = randomPart(preferenceMatrix, chosenValueToBeModified, csa, consistencyChecker, isFound, sortedMatrixValues, randomsIterator);
		}
		csa.close();
	}

	/**
	 * SAATY'S PART
	 */
	private static void saatyPart(MyMatrix preferenceMatrix, CharSequenceAppender csa, ConsistencyChecker consistencyChecker, Iterator<MatrixValue> saatyIterator) {


		final MatrixValue saatyValue = saatyIterator.next();
		final int i_saaty = saatyValue.getRow();
		final int j_saaty = saatyValue.getColumn();

		csa.appendLineFeed();
		/*Writing of de the best fit related to the proposed value */
		//Copy of the original matrix
		final MyMatrix saatyMatrix = MyMatrix.copyMyMatrix(preferenceMatrix);
		//saatyMatrix's eigen vector calculation
		MyMatrix saatyVector = PriorityVector.build(saatyMatrix);
		//best fit calculation
		final double bestFit_saaty = SaatyTools.calculateBestFit(saatyMatrix, i_saaty, j_saaty);

		//best fit writing
		csa.append("" + bestFit_saaty);
		csa.appendCommaSeparator();

		/*écriture des indices de la valeur proposée par Saaty dans le fichier */
		csa.append("" + (i_saaty + 1));
		csa.appendCommaSeparator();
		csa.append("" + (j_saaty + 1));
		csa.appendCommaSeparator();

		/*écriture de la cohérence si l'expert suivait les conseils de Saaty */

		//remplacement de la valeur (i,j) par bestFit
		saatyMatrix.setMatrixValue(new MatrixValue(i_saaty, j_saaty, bestFit_saaty));

		//remplacement de la valeur (j,i) par 1/bestFit
		saatyMatrix.setMatrixValue(new MatrixValue(j_saaty, i_saaty, 1. / bestFit_saaty));

		//rafraîchissement du vecteur de priorité
		saatyVector = PriorityVector.build(saatyMatrix);
		//calcul et écriture de la cohérence
		final boolean isSaatyConsistent = consistencyChecker.isConsistent(saatyMatrix, saatyVector);
		csa.append("" + consistencyChecker.getConsistencyRatio());
		csa.appendCommaSeparator();

		assert isSaatyConsistent : "should be consistent";
	}

	/**
	 * RANDOM PART
	 */
	private static boolean randomPart(MyMatrix preferenceMatrix, MatrixValue chosenValueToBeModified, CharSequenceAppender csa, ConsistencyChecker consistencyChecker, boolean found, Collection<MatrixValue> sortedMatrixValues, Iterator<MatrixValue> randomsIterator) {
		final MatrixValue randomsValue = randomsIterator.next();

		//copie de la matrice initiale
		final MyMatrix randomsMatrix = MyMatrix.copyMyMatrix(preferenceMatrix);
		//calcul du vecteur propre associé à randomsMatrix
		MyMatrix randomsVector = PriorityVector.build(randomsMatrix);

		/*écriture best fit pour la méthode aléatoire*/
		final double bestFit_random = SaatyTools.calculateBestFit(
			randomsMatrix,
			randomsValue.getRow(),
			randomsValue.getColumn());
		csa.append("" + bestFit_random);
		csa.appendCommaSeparator();

		/*écriture des indices de la valeur aléatoire proposé*/
		csa.append("" + (randomsValue.getRow() + 1));
		csa.appendCommaSeparator();

		csa.append("" + (randomsValue.getColumn() + 1));
		csa.appendCommaSeparator();

		/*écriture du placement de la valeur aléatoire dans le classement de Saaty*/
		csa.append(""
			+ SaatyTools.getLocationInRank(
			sortedMatrixValues,
			randomsValue.getRow(),
			randomsValue.getColumn())
		);
		csa.appendCommaSeparator();

		/*écriture de la cohérence après modification de la valeur aléatoire par le bestfit*/

		final int i_rand = randomsValue.getRow();
		final int j_rand = randomsValue.getColumn();

		//remplacement de la valeur (i,j) par bestFit
		randomsMatrix.setMatrixValue(new MatrixValue(i_rand, j_rand, bestFit_random));

		//remplacement de la valeur (j,i) par 1/bestFit
		randomsMatrix.setMatrixValue(new MatrixValue(j_rand, i_rand, 1. / bestFit_random));

		//rafraîchissement du vecteur de priorité
		randomsVector = PriorityVector.build(randomsMatrix);
		//calcul et écriture de la cohérence
		final boolean isRandomConsistent = consistencyChecker.isConsistent(randomsMatrix, randomsVector);
		csa.append("" + consistencyChecker.getConsistencyRatio());
		csa.appendCommaSeparator();

		assert isRandomConsistent : "should be consistent";

		if (chosenValueToBeModified.equals(randomsValue)) {
			found = true;
		}

		return found;
	}
}
