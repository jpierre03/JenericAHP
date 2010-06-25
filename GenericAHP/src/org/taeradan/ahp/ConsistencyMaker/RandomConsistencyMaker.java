/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.ConsistencyMaker;

import Jama.Matrix;
import java.awt.Point;
import java.util.Scanner;

/**
 *
 * @author Marianne
 */
public class RandomConsistencyMaker {


	/*
	 * Builder
	 */
	public RandomConsistencyMaker() {
	}

	public Matrix modificationOfInconsistentMatrix(Matrix m, Matrix w) {

		IterationRandom itRandom = new IterationRandom();
		Scanner sc = new Scanner(System.in);
		String str = "N";
		int cptr = 1;
		double posX;
		double posY;
		Point testPoint = new Point();

		testPoint = (Point) itRandom.getRandomRanking(m.getColumnDimension());
		System.out.println("Vous pouvez réviser le jugement" + testPoint
						   + "\nSouhaitez vous le modifier? O/N");
		str = sc.nextLine();


		/*Parcours du classement jusqu'à obtenir une valeur que l'utilisateur veuille modifier*/
		while (!str.equalsIgnoreCase("O")) {
			if (str.equals("N")) {

				testPoint = (Point) itRandom.getRandomRanking(m.getColumnDimension());
				System.out.println("Vous pouvez réviser le jugement" + testPoint
								   + "\nSouhaitez vous le modifier? O/N");
				str = sc.nextLine();
				cptr++;

			} else {

				System.out.println("Erreur de frappe, veuillez resaisir");
				str = sc.nextLine();
			}
		}



		/*Saisie de la nouvelle valeur dans la matrice de préférences*/
		System.out.println("Taper votre nouvelle valeur");
		str = sc.nextLine();
		int newValue = Integer.parseInt(str);


		/*Récupération de la valeur saisie u clavier et remplacement dans la matrice*/
		m.set((int) testPoint.getX(), (int) testPoint.getY(), (double) newValue);/*Aij = x*/
		m.set((int) testPoint.getY(), (int) testPoint.getX(), (double) 1 / newValue);/*Aji = 1/x*/

		return m;

	}

	public static void main(String[] args) {
		Matrix m = new Matrix(3, 3);
		Matrix w = new Matrix(3, 1);
		ConsistencyCheckerMatrix Cst = new ConsistencyCheckerMatrix();
		RandomConsistencyMaker rcm = new RandomConsistencyMaker();




		System.out.println("WRITE EXPLANATIONS");
		/*
		EXEMPLE DE MATRICE COHERENTE*/
		m.set(0, 0, 1);
		m.set(0, 1, 16);
		m.set(0, 2, 4);

		m.set(1, 0, 0.5);
		m.set(1, 1, 1);
		m.set(1, 2, 2);

		m.set(2, 0, 0.25);
		m.set(2, 1, 0.5);
		m.set(2, 2, 1);

		m.print(5, 5);

		w.set(0, 0, (double) 28 / 49);
		w.set(1, 0, (double) 28 / 98);
		w.set(2, 0, (double) 28 / 196);

		w.print(5, 5);



		/*EXEMPLE DE MATRICE INCOHERENTE
		m.set(0, 0, 1);
		m.set(0, 1, 4);
		m.set(0, 2, 9);

		m.set(1, 0, 0.25);
		m.set(1, 1, 1);
		m.set(1, 2, 8);

		m.set(2, 0, (double) 1 / 8);
		m.set(2, 1, (double) 1 / 9);
		m.set(2, 2, 1);

		//	m.print(5, 5);

		w.set(0, 0, 0.8428094);
		w.set(1, 0, 0.0827759);
		w.set(2, 0, 0.0744147);*/

		//	w.print(5, 5);


		/*Calls a Saaty's Iteration while the pairwise comparison matrix is inconsistent*/
		while (!Cst.isConsistent(m, w)) {
			m.print(5, 5);
			System.out.println("Votre matrice est incohérente.");
			m = rcm.modificationOfInconsistentMatrix(m, w);
		}

		System.out.println("Votre matrice est cohérente ! Merci de votre participation.");
		System.out.println(Cst.isConsistent(m, w));



	}
}
