/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.ConsistencyMaker;

import java.awt.Point;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Marianne
 */
public class IterationRandom {

	Collection<Point> pointCollection;

	/**
	 * Builder
	 */
	public IterationRandom() {
		pointCollection = new ArrayList<Point>();
	}

	/**
	 * Generates a point (x,y) randomly
	 * @param int
	 * @return Point
	 */
	public Collection<Point> getRandomRanking(int sizeOfMatrix) {

		List<Point> lp;



		for (int i = 0; i < sizeOfMatrix; i++) {
			for (int j = i+1; j < sizeOfMatrix; j++) {
				this.pointCollection.add(new Point(i, j));
			}
		}
		lp = new ArrayList<Point>(pointCollection);

		Collections.shuffle(lp);

		pointCollection.clear();

		/*Vider la collection triée pour la remplir d'éléments aléatoires.*/

		for (Point point : lp) {
			pointCollection.add(point);
		}


		return pointCollection;
	}

	public boolean isPresentInCollection(Point p, Collection<Point> pointCollection) {

		for (Point point : pointCollection) {
			if (point.getX() == p.getX() && point.getY() == p.getY()) {
				return true;
			}

		}

		return false;
	}

	/**
	 * This method gives the best location in preference matrix for which you
	 * have to change the preference weight.
	 * @param Collection<Point>,int
	 * @return Point
	 */
	public Point showBestInconsistency(Collection<Point> ranking, int i) {
		Point temp = new Point();
		Iterator iterator = ranking.iterator();

		for (int cptr = 0; cptr < i; cptr++) {
			temp = (Point) iterator.next();
		}

		return temp;

	}

	public static void main(String[] args) {

		IterationRandom r = new IterationRandom();
		Collection<Point> pC = new ArrayList<Point>();
		pC = r.getRandomRanking(3);
		for (Point point : pC) {
			System.out.println(point);
		}
	}
}
