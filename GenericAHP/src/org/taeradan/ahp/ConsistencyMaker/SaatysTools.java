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
	 * @param MyMatrix
	 * @return MatrixValue
	 */
	public MatrixValue getValueToModifiyByRanking(MyMatrix myPreferencMatrix) {


		Scanner sc = new Scanner(System.in);
		int isValueChosen = 0;
		String expertsChoice;
		int cptr = 0;
		MatrixValue matrixValue = new MatrixValue();
		Collection<MatrixValue> collectionOfSortedMatrixValues = new ArrayList<MatrixValue>();
		Iterator<MatrixValue> valueIterator;

		TreeMap<Double, MatrixValue> myTreeMap = new TreeMap<Double, MatrixValue>();
		myTreeMap = createTreeMap(myPreferencMatrix);


		while ((!myTreeMap.isEmpty()) && (isValueChosen == 0)) {
			System.out.println("cptr = " + cptr);
			cptr++;
			matrixValue = myTreeMap.pollLastEntry().getValue();
			System.out.println("Souhaitez-vous modifier la valeur "
							   + matrixValue.getValue()
							   + " ( "
							   + matrixValue.getRow()
							   + " , "
							   + matrixValue.getColumn()
							   + " )"
							   + " ? O/N");
			collectionOfSortedMatrixValues.add(matrixValue);

			expertsChoice = sc.nextLine();
			if (expertsChoice.equalsIgnoreCase("O")) {
				isValueChosen = 1;
			}
		}



		/*Pour le cas où l'expert n'ai pas choisi de valeur à modifier*/
		valueIterator = collectionOfSortedMatrixValues.iterator();
		System.out.println("Retour en haut du classement");
		while (isValueChosen == 0) {
			matrixValue = valueIterator.next();
			System.out.println("Souhaitez-vous modifier la valeur "
							   + matrixValue.getValue()
							   + " ( "
							   + matrixValue.getRow()
							   + " , "
							   + matrixValue.getColumn()
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
	public MatrixValue getFirstValueOfSaatysRanking(MyMatrix myPreferenceMatrix) {

		TreeMap<Double, MatrixValue> myTreeMap = new TreeMap<Double, MatrixValue>();
		myTreeMap = createTreeMap(myPreferenceMatrix);

		return myTreeMap.pollLastEntry().getValue();

	}





}
