/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.prototype.ConsistencyMaker;

import org.taeradan.ahp.PriorityVector;
import org.taeradan.ahp.matrix.MatrixValue;
import org.taeradan.ahp.matrix.MyMatrix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;


/**
 * This class provides 2 tools of the Saaty's method : the ranking of values in the matrix which generate
 * inconsistencies the best fit, which propose a new value in the comparison pairwise matrix
 *
 * @author Marianne
 * @author Jean-Pierre PRUNARET
 */
public final class SaatyTools {

	private SaatyTools() {
	}

	private static TreeMap<Double, MatrixValue> createTreeMap(MyMatrix epsilon) {

		int rows = epsilon.getRowDimension();
		int columns = epsilon.getColumnDimension();
		TreeMap<Double, MatrixValue> myTreeMap = new TreeMap<>();

		/*Création d'une collection de MatrixValue*/
		Collection<MatrixValue> matrixValues = new ArrayList<>();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (i != j) {
					matrixValues.add(epsilon.getMatrixValue(i, j));
				}
			}
		}

		/*Remplit myTreeMap de MatrixValue stockées dans la collection*/
		for (MatrixValue matrixValue : matrixValues) {
			myTreeMap.put(matrixValue.getValue(), matrixValue);
		}
		return myTreeMap;
	}

	public static void printTreeMap(TreeMap<Double, MatrixValue> map) {
		while (!map.isEmpty()) {
			MatrixValue matrixValue = map.pollLastEntry().getValue();
			System.out.println(
				+matrixValue.getValue()
					+ " ( "
					+ matrixValue.getRow()
					+ " , "
					+ matrixValue.getColumn()
					+ " )");
		}
	}

	public static MatrixValue getFirstValueOfSaatysRanking(MyMatrix epsilon) {

		final TreeMap<Double, MatrixValue> myTreeMap = createTreeMap(epsilon);
		final MatrixValue tempMatrixValue = myTreeMap.pollLastEntry().getValue();

		int i = tempMatrixValue.getRow();
		int j = tempMatrixValue.getColumn();

		/*Si on est dans la partie inférieure de la matrice*/
		/*Proposer modification de la valeur réciproque*/
		if (i > j) {
			tempMatrixValue.setRow(j);
			tempMatrixValue.setColumn(i);
			tempMatrixValue.setValue(epsilon.get(j, i));
		}

		return tempMatrixValue;
	}

	/*
	 * Calculates the espilon matrix of Saaty ; Epsilon[i][j]=A[i][j]*w[j]/w[j]
	 */
	public static MyMatrix calculateEpsilonMatrix(MyMatrix preferenceMatrix,
						      MyMatrix priorityVector) {

		MyMatrix epsilon = new MyMatrix(preferenceMatrix.getRowDimension(),
			preferenceMatrix.getColumnDimension());

		for (int i = 0; i < preferenceMatrix.getRowDimension(); i++) {
			for (int j = 0; j < preferenceMatrix.getColumnDimension(); j++) {

				double aij;
				double wj;
				double wi;
				double eij;

				aij = preferenceMatrix.get(i, j);
				wj = priorityVector.get(j, 0);
				wi = priorityVector.get(i, 0);


				eij = aij * wj / wi;

				final MatrixValue epsilonValue = new MatrixValue(i, j, eij);
				epsilon.setMatrixValue(epsilonValue);
			}
		}
		return epsilon;
	}

	public static double calculateBestFit(MyMatrix preferenceMatrix,
					      MyMatrix priorityVector,
					      int i,
					      int j) {

		MatrixValue matrixValue;
		MyMatrix tempMatrix = new MyMatrix();

		tempMatrix = MyMatrix.copyMyMatrix(preferenceMatrix);

		/*Remplacer aii et ajj par 2*/
		matrixValue = preferenceMatrix.getMatrixValue(i, i);
		matrixValue.setValue(2);
		tempMatrix.setMatrixValue(matrixValue);

		matrixValue = preferenceMatrix.getMatrixValue(j, j);
		matrixValue.setValue(2);
		tempMatrix.setMatrixValue(matrixValue);

		/*Remplacer aij et aji par 0*/
		matrixValue = preferenceMatrix.getMatrixValue(i, j);
		matrixValue.setValue(0);
		tempMatrix.setMatrixValue(matrixValue);

		matrixValue = preferenceMatrix.getMatrixValue(j, i);
		matrixValue.setValue(0);
		tempMatrix.setMatrixValue(matrixValue);

		/*Recalculer vecteur priorité*/
		priorityVector = PriorityVector.build(tempMatrix);

		return priorityVector.get(i, 0) / priorityVector.get(j, 0);
	}

	public static Collection<MatrixValue> getRank(MyMatrix preferenceMatrix,
						      MyMatrix priorityVector,
						      MyMatrix epsilon) {

		MatrixValue sortedMatrixValue;
		Collection<MatrixValue> matrixValues = new ArrayList<>();
		boolean isPresent = false;

		/*Creation du TreeMap à partir de la matrice epsilon*/
		final TreeMap<Double, MatrixValue> myTreeMap = createTreeMap(epsilon);

		/*Recopie dans une collection, du TreeMap dans l'ordre décroissantTant*/
		while (!myTreeMap.isEmpty()) {
			sortedMatrixValue = myTreeMap.pollLastEntry().getValue();

			int row = sortedMatrixValue.getRow();
			int column = sortedMatrixValue.getColumn();
			double value = sortedMatrixValue.getValue();

			/*Si la valeur à modifier est dans la partie inférieure de la matrice*/
			if (row > column) {
				/*On retient la valeur réciproque*/

				sortedMatrixValue.setRow(column);
				sortedMatrixValue.setColumn(row);
				sortedMatrixValue.setValue(1 / value);
			}

			/*Avant d'ajouter, on teste si l'élément n'est pas déjà présent*/
			for (MatrixValue matrixValue1 : matrixValues) {
				if (Math.abs(sortedMatrixValue.getValue() - matrixValue1.getValue()) < 0.000000001) {
					isPresent = true;
				}
			}

			if (!isPresent) {
				/*Ajout dans la collection des éléments triés.*/
				matrixValues.add(sortedMatrixValue);
			}

			isPresent = false;
		}
		return matrixValues;
	}

	public static int getLocationInRank(Collection<MatrixValue> matrixValues, int row, int column) {
		assert matrixValues != null;
		assert row >= 0;
		assert column >= 0;

		final Iterator<MatrixValue> iterator = matrixValues.iterator();
		int counter = 0;
		boolean isFound = false;

		while ((iterator.hasNext()) && (!isFound)) {
			final MatrixValue matrixValue = iterator.next();

			if ((row == matrixValue.getRow()) && (column == matrixValue.getColumn())) {
				isFound = true;
			}
			counter++;
		}
		return counter;
	}
}
