/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.ConsistencyMaker;

import Jama.Matrix;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author Marianne
 */
public class IterationSaaty {

	Point[] pointsToModify;

	/**
	 * Builder
	 */
	public IterationSaaty() {
	}


	/**
	 * This method uses Saaty's algorithm to show where the most important 
	 * inconsistency is.
	 * @param Matrix,Matrix
	 * @return Collection<Point>
	 */
	public Collection<Point> rankingOfInconsistencies(Matrix preferencesMatrix,
													  Matrix priorityVector) {
		int x = 0;
		int y = 0;
		int maxValueOfEpsilon = 0;
		Matrix epsilon = new Matrix(preferencesMatrix.getColumnDimension(), preferencesMatrix.
				getColumnDimension());
		Collection<Point> cp = new ArrayList<Point>();


		/*Création de la matrice Epsilon*/
		for (int i = 0; i < preferencesMatrix.getColumnDimension(); i++) {
			for (int j = 0; j < preferencesMatrix.getColumnDimension(); j++) {
				epsilon.set(i, j, preferencesMatrix.get(i, j) * priorityVector.get(j, 0) / priorityVector.
						get(i, 0));

			/*	epsilon.set(i, j, preferencesMatrix.get(i, j)- priorityVector.get(i, 0) / priorityVector.
						get(j, 0));*/

			/*	epsilon.identity(preferencesMatrix.getColumnDimension(), preferencesMatrix.getColumnDimension());*/


			}
		}

		epsilon.print(5, 5);


		/*Recherche de la valeur maximale */
		for (int i = 0; i < preferencesMatrix.getColumnDimension(); i++) {
			for (int j = 0; j < preferencesMatrix.getColumnDimension(); j++) {

				cp.add(this.getMaxOfMatrix(epsilon, cp));


			}
		}

		for (Point point : cp) {
			System.out.println(preferencesMatrix.get((int)point.getX(), (int)point.getY()));
		}
		return cp;
	}

	/**
	 * This method gives the best location in preference matrix for which you
	 * have to change the preference weight.
	 * @param Collection<Point>
	 * @return Point
	 */
	public Point showBestInconsistency(Collection<Point> ranking) {
		Iterator iterator = ranking.iterator();
		return (Point) iterator.next();
		
	}


	/**
	 * Returns the location in a matrix of the greatest value
	 * @param Matrix, Collection<Point>
	 * @return Point
	 */
	public Point getMaxOfMatrix(Matrix m, Collection<Point> c) {

		Point maxPoint = new Point();
		Point tempPoint = new Point();
		double maxValue = -1.0;

		for (int i = 0; i < m.getColumnDimension(); i++) {
			for (int j = 0; j < m.getColumnDimension(); j++) {

				tempPoint.setLocation(i, j);
				if (!this.isPresentInCollection(tempPoint, c)) {
					if (maxValue < m.get(i, j)) {
						maxValue = m.get(i, j);
						maxPoint.setLocation(tempPoint.getX(), tempPoint.getY());
					}
				}
			}
		}

		return maxPoint;

	}

	/**
	 * Returns true if Point p is present in the point collection pointCollection
	 * @param Point, Collection<Point>
	 * @return boolean
	 */
	public boolean isPresentInCollection(Point p, Collection<Point> pointCollection) {

		for (Point point : pointCollection) {
			if (point.getX() == p.getX() && point.getY() == p.getY()) {
				return true;
			}

		}

		return false;
	}

	public static void main(String[] args) {

		Matrix m = new Matrix(3, 3);
		Matrix w = new Matrix(3, 1);

		m.set(0, 0, 1);
		m.set(0, 1, 2);
		m.set(0, 2, 3);

		m.set(1, 0, 4);
		m.set(1, 1, 5);
		m.set(1, 2, 6);

		m.set(2, 0, 7);
		m.set(2, 1, 8);
		m.set(2, 2, 9);

		//m.print(5, 5);

		w.set(0, 0, 1);
		w.set(1, 0, 2);
		w.set(2, 0, 3);

		//w.print(5, 5);


		IterationSaaty s = new IterationSaaty();
		System.out.println(s.showBestInconsistency(s.rankingOfInconsistencies(m, w)));
		
	}
}
