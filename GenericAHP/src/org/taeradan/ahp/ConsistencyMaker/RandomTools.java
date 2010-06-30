/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.taeradan.ahp.ConsistencyMaker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author Marianne
 */
public class RandomTools {


	public MatrixValue getValueToModifiyByRanking(MyMatrix myPreferencMatrix) {


		Scanner sc = new Scanner(System.in);
		int isValueChosen = 0;
		String expertsChoice;
		MatrixValue matrixValue = new MatrixValue();
		Collection<MatrixValue> collectionOfNonSortedMatrixValues = new ArrayList<MatrixValue>();
		Iterator<MatrixValue> valueIterator;
		SaatysTools tool = new SaatysTools();
		List<MatrixValue> listOfMatrixValue;

		TreeMap<Double, MatrixValue> myTreeMap = new TreeMap<Double, MatrixValue>();
		myTreeMap = tool.createTreeMap(myPreferencMatrix);


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


		/*Boucle pour que l'utilisateur désigne la valeur à modifier*/
		valueIterator = collectionOfNonSortedMatrixValues.iterator();
		
		while (isValueChosen == 0) {
			matrixValue = valueIterator.next();
			System.out.println("Souhaitez-vous modifier la valeur "
							   + matrixValue.getValue()
							   + " ( "
							   + (matrixValue.getRow()+1)
							   + " , "
							   + (matrixValue.getColumn()+1)
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



}
