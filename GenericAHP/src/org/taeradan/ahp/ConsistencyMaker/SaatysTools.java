/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.ConsistencyMaker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;
import org.taeradan.ahp.ConsistencyMaker.MatrixValue;
import org.taeradan.ahp.ConsistencyMaker.MyMatrix;
import org.taeradan.ahp.PriorityVector;

/**
 *
 * @author Marianne
 */
/*
 * This class provides 2 tools of the Saaty's method :
 * the ranking of values in the matrix which generate inconsistencies
 * the best fit, which propose a new value in the comparison pairwise matrix
 */
public class SaatysTools {

	/*Builder*/
	public SaatysTools() {
	}

	public TreeMap<Double, MatrixValue> createTreeMap(MyMatrix myPreferencMatrix) {

		int rows = myPreferencMatrix.getRowDimension();
		int columns = myPreferencMatrix.getColumnDimension();
		TreeMap<Double, MatrixValue> myTreeMap = new TreeMap<Double, MatrixValue>();


		/*Création d'une collection de MatrixValue*/
		Collection<MatrixValue> matrixValues = new ArrayList<MatrixValue>();
		for (int i = 0; i < rows; i++) {
			for (int j = i - 1; j < columns; j++) {
				if (j > i) {
					matrixValues.add(myPreferencMatrix.getMatrixValue(i, j));
				}
			}
		}

		/*Remplit myTreeMap de MatrixValue stockées dans la collection*/
		for (Iterator<MatrixValue> valueIterator = matrixValues.iterator(); valueIterator.hasNext();) {
			MatrixValue matrixValue = valueIterator.next();
			myTreeMap.put(matrixValue.getValue(), matrixValue);
		}

		return myTreeMap;

	}

	/*Print a TreeMap
	 *@param TreeMap<Double, MatrixValue>
	 * @return void
	 */
	public void printTreeMap(TreeMap<Double, MatrixValue> myTreeMap) {
		while (!myTreeMap.isEmpty()) {
			MatrixValue matrixValue = myTreeMap.pollLastEntry().getValue();
			System.out.println(
					+matrixValue.getValue()
					+ " ( "
					+ matrixValue.getRow()
					+ " , "
					+ matrixValue.getColumn()
					+ " )");
		}

	}


	/*
	 * Calculates the rankingof value which should be changed while
	 * user does not choose a value to review
	 * @param MyMatrix myPreferenceMatrix, MyMatrix priorityVector, MyMatrix epsilon
	 * @return MatrixValue
	 */
	public MatrixValue getValueToModifiyByRanking(MyMatrix myPreferenceMatrix,
												  MyMatrix priorityVector, MyMatrix epsilon) {


		Scanner sc = new Scanner(System.in);
		int isValueChosen = 0;
		String expertsChoice;
		MatrixValue matrixValue = new MatrixValue();
		Collection<MatrixValue> collectionOfSortedMatrixValues = new ArrayList<MatrixValue>();
		Iterator<MatrixValue> valueIterator;
		MatrixValue matrixValueToPrint = new MatrixValue();

		TreeMap<Double, MatrixValue> myTreeMap = new TreeMap<Double, MatrixValue>();
		myTreeMap = createTreeMap(epsilon);


		while ((!myTreeMap.isEmpty()) && (isValueChosen == 0)) {

			matrixValue = myTreeMap.pollLastEntry().getValue();
			matrixValueToPrint.setRow(matrixValue.getRow());
			matrixValueToPrint.setColumn(matrixValue.getColumn());
			matrixValueToPrint.setValue(myPreferenceMatrix.get(matrixValueToPrint.getRow(), matrixValueToPrint.
					getColumn()));
			/*System.out.println("Souhaitez-vous modifier la valeur "
			+ matrixValueToPrint.getValue()
			+ " ( "
			+ matrixValueToPrint.getRow()
			+ " , "
			+ matrixValueToPrint.getColumn()
			+ " )"
			+ " ? O/N");*/
			collectionOfSortedMatrixValues.add(matrixValue);
	
			/*	expertsChoice = sc.nextLine();
			if (expertsChoice.equalsIgnoreCase("O")) {
			isValueChosen = 1;
			}*/
		}



		/*Pour le cas où l'expert n'ai pas choisi de valeur à modifier*/
		valueIterator = collectionOfSortedMatrixValues.iterator();

		while (isValueChosen == 0) {
			matrixValue = valueIterator.next();
			matrixValueToPrint.setRow(matrixValue.getRow());
			matrixValueToPrint.setColumn(matrixValue.getColumn());
			matrixValueToPrint.setValue(myPreferenceMatrix.get(matrixValueToPrint.getRow(), matrixValueToPrint.
					getColumn()));

			System.out.println("Souhaitez-vous modifier la valeur "
							   + matrixValueToPrint.getValue()
							   + " ( "
							   + (matrixValueToPrint.getRow()+1)
							   + " , "
							   + (matrixValueToPrint.getColumn()+1)
							   + " )"
							   + " ? O/N");
			expertsChoice = sc.nextLine();

			if (expertsChoice.equalsIgnoreCase("O")) {
				isValueChosen = 1;
			} else if (!valueIterator.hasNext()) {
				System.out.println("Retour en haut du classement");
				valueIterator = collectionOfSortedMatrixValues.iterator();
			}

		}



		return matrixValue;
	}

	/*
	 * Returns the first element of SaatysRanking
	 * @param MyMatrix
	 * @return MatrixValue
	 */
	public MatrixValue getFirstValueOfSaatysRanking(MyMatrix epsilon) {

		TreeMap<Double, MatrixValue> myTreeMap = new TreeMap<Double, MatrixValue>();
		myTreeMap = createTreeMap(epsilon);

		return myTreeMap.pollLastEntry().getValue();

	}

	/*
	 * Calculates the espilon matrix of Saaty ; Epsilon[i][j]=A[i][j]*w[j]/w[j]
	 * @param MyMatrix myPreferenceMatrix, MyMatrix priorityVector
	 * @return MyMatrix Epsilon
	 */
	public MyMatrix calculateEpsilonMatrix(MyMatrix myPreferenceMatrix, MyMatrix priorityVector) {
		MyMatrix epsilon = new MyMatrix(myPreferenceMatrix.getRowDimension(), myPreferenceMatrix.
				getColumnDimension());
		MatrixValue epsilonValue = new MatrixValue();

		for (int i = 0; i < myPreferenceMatrix.getRowDimension(); i++) {
			for (int j = 0; j < myPreferenceMatrix.getColumnDimension(); j++) {

				double aij;
				double wj;
				double wi;
				double eij;

				aij = myPreferenceMatrix.get(i, j);
				wj = priorityVector.get(j, 0);
				wi = priorityVector.get(i, 0);


				eij = aij * wj / wi;
				epsilonValue.setValue(eij);
				epsilonValue.setRow(i);
				epsilonValue.setColumn(j);

				epsilon.setMatrixValue(epsilonValue);
			}
		}

		return epsilon;

	}

	public double calculateBestFit(MyMatrix preferenceMatrix, MyMatrix priorityVector, int i, int j) {
		MatrixValue matrixValue = new MatrixValue();
		MyMatrix tempMatrix = new MyMatrix();

		tempMatrix = tempMatrix.copyMyMatrix(preferenceMatrix);

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
		preferenceMatrix.setMatrixValue(matrixValue);

		
		/*Recalculer vecteur priorité*/
		priorityVector = PriorityVector.build(tempMatrix);


		return priorityVector.get(i, 0) / priorityVector.get(j, 0);

	}

}
