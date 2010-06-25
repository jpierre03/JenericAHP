/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.ConsistencyMaker;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
		Point location;
		Point temp = new Point(0, -1);
		Point pointToAdd = new Point();
		double posX = 0;
		double posY = -1;
		int flag = 0;
		List<Point> lp;



		for (int i = 0; i < sizeOfMatrix; i++) {
			for (int j = i; j < sizeOfMatrix; j++) {
				this.pointCollection.add(new Point(i, j));
			}
		}
		lp=new ArrayList<Point>(pointCollection);
		for (Point point : lp) {
			System.out.println(point);
		}
		Collections.shuffle(lp);



		System.out.println("***********");
		for (Point point : lp) {
			System.out.println(point);
		}

		//faire 2 boucles pour mettre ds collections (liste)
		// shuffle




//		for (Point point : pointCollection) {
//			System.out.println(point);
//		}
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

	public static void main(String[] args) {

		IterationRandom r = new IterationRandom();
		r.getRandomRanking(3);

	}
}
