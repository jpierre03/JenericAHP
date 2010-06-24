/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.ConsistencyMaker;

import Jama.Matrix;
import java.awt.Point;
import org.taeradan.ahp.ConsistencyChecker;

/**
 *
 * @author Marianne
 */
public class SaatyConsistencyMaker {

	/**
	 * Builder
	 */
	public SaatyConsistencyMaker() {

		ConsistencyCheckerMatrix Cst = new ConsistencyCheckerMatrix();
		Matrix m = new Matrix(3, 3);
		Matrix w = new Matrix(3, 1);

		/*
		EXEMPLE DE MATRICE COHERENTE 
		m.set(0, 0, 1);
		m.set(0, 1, 2);
		m.set(0, 2, 4);

		m.set(1, 0, 0.5);
		m.set(1, 1, 1);
		m.set(1, 2, 2);

		m.set(2, 0, 0.25);
		m.set(2, 1, 0.5);
		m.set(2, 2, 1);

		m.print(5, 5);

		w.set(0, 0,(double)28/49);
		w.set(1, 0,(double) 28/98);
		w.set(2, 0, (double)28/196);

		w.print(5, 5);
		 */


		/*EXEMPLE DE MATRICE INCOHERENTE*/
		m.set(0, 0, 1);
		m.set(0, 1, 4);
		m.set(0, 2, 9);

		m.set(1, 0, 0.25);
		m.set(1, 1, 1);
		m.set(1, 2, 8);

		m.set(2, 0, (double) 1/8);
		m.set(2, 1, (double) 1/9);
		m.set(2, 2, 1);

		m.print(5, 5);

		w.set(0, 0, 0.8428094);
		w.set(1, 0, 0.0827759);
		w.set(2, 0, 0.0744147);

		w.print(5, 5);



		System.out.println("***" + Cst.isConsistent(m, w));
		IterationSaaty itSaaty = new IterationSaaty();

		System.out.println("Vous pouvez réviser le jugement" + itSaaty.showBestInconsistency(itSaaty.rankingOfInconsistencies(m, w)));

	}

	public static void main(String[] args) {
		SaatyConsistencyMaker scm = new SaatyConsistencyMaker();


	}
}
