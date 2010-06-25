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

//	Point[] pointsToModify;
	/**
	 *
	 */
	private Collection<Point> ranking = new ArrayList<Point>();

	/**
	 * Builder
	 */
	public IterationSaaty() {
	}

	/**
	 * This method uses Saaty's algorithm to show where the most important 
	 * inconsistency is.
	 * @param preferencesMatrix
	 * @param priorityVector
	 * @return Collection<Point>
	 */
	public Collection<Point> rankingOfInconsistencies(Matrix preferencesMatrix,
													  Matrix priorityVector) {
		int x = 0;
		int y = 0;
//		int maxValueOfEpsilon = 0;
		Matrix epsilon = new Matrix(preferencesMatrix.getColumnDimension(),
									preferencesMatrix.getColumnDimension());
		Collection<Point> ranking = new ArrayList<Point>();


		/*Création de la matrice Epsilon*/
		for (int i = 0; i < preferencesMatrix.getColumnDimension(); i++) {
			for (int j = 0; j < preferencesMatrix.getColumnDimension(); j++) {
				epsilon.set(i, j, preferencesMatrix.get(i, j) * priorityVector.get(j, 0) / priorityVector.
						get(i, 0));

				/*epsilon.set(i, j, preferencesMatrix.get(i, j)- priorityVector.get(i, 0) / priorityVector.
				get(j, 0));*/
			}
		}

		/*Recherche de la valeur maximale */
		for (int i = 0; i < preferencesMatrix.getColumnDimension(); i++) {
			for (int j = 0; j < preferencesMatrix.getColumnDimension(); j++) {

				ranking.add(this.getMaxOfMatrix(epsilon, ranking));
			}
		}
//		for (Point point : ranking) {
//			System.out.println(point);
//		}

		Collection<Point> aboutToBeremoved = new ArrayList<Point>();
		/*Elimination des coordonnées de la partie inférieure gauche de la matrice (réciprocité)*/
		for (Point point : ranking) {
			if (point.getX() < point.getY()) {
				aboutToBeremoved.add(point);
			}
		}

		ranking.removeAll(aboutToBeremoved);

//		System.out.println("Après suppression");
//		for (Point point : ranking) {
//			System.out.println(point);
//		}

		return ranking;
	}

	/**
	 *
	 * @param priorityVector
	 * @param ranking
	 * @return
	 */
	public double[] calculateBestFits(Matrix priorityVector, Collection<Point> ranking) {
		int cptr = 0;
		double[] bestFits = new double[(priorityVector.getRowDimension()
										* priorityVector.getRowDimension() - priorityVector.
				getRowDimension()) / 2];

		//Pour chaque point de la collection on calcul le best fit associé
		for (Point point : ranking) {
			bestFits[cptr] = ((int) priorityVector.get((int) point.getX(), 0) / priorityVector.get((int) point.
					getY(), 0));
			System.out.println(bestFits[cptr]);
			cptr++;
		}

		return bestFits;
	}

	/**
	 * This method gives the best location in preference matrix for which you
	 * have to change the preference weight.
	 * @param ranking 
	 * @param i
	 * @return Point
	 */
	public Point showBestInconsistency(Collection<Point> ranking, int i) {
		Point temp = (Point) ranking.toArray()[i];
		return temp;
	}

	/**
	 * Returns the location in a matrix of the greatest value
	 * @param m
	 * @param ranking
	 * @return Point
	 */
	public Point getMaxOfMatrix(Matrix m, Collection<Point> ranking) {

		Point maxPoint = new Point();
		Point tempPoint = new Point();
		double maxValue = -1.0;

		for (int i = 0; i < m.getColumnDimension(); i++) {
			for (int j = 0; j < m.getColumnDimension(); j++) {

				tempPoint.setLocation(i, j);
				if (!this.isPresentInCollection(tempPoint, ranking)) {
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
	 * @param p
	 * @param pointCollection
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

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		Matrix m = new Matrix(3, 3);
		Matrix w = new Matrix(3, 1);

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


		IterationSaaty s = new IterationSaaty();
		System.out.println("Réviser jugement" + s.showBestInconsistency(
				s.rankingOfInconsistencies(m, w), 5));
	}

	/**
	 * @return the ranking
	 */
	public Collection<Point> getRanking() {
		return ranking;
	}

	/**
	 * @param ranking the ranking to set
	 */
	public void setRanking(Collection<Point> ranking) {
		this.ranking = ranking;
	}
}
