/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.ConsistencyMaker;

import java.awt.Point;
import java.util.Random;

/**
 *
 * @author Marianne
 */
public class IterationRandom {

	/**
	 * Builder
	 */
	public IterationRandom() {
	}

	/**
	 * Generates a point (x,y) randomly
	 * @param int
	 * @return Point
	 */
	public Point getRandomLocation(int sizeOfMatrix) {
		Point location;
		double posX = 0;
		double posY = -1;

		while (posX > posY || posX < 1 || posX > sizeOfMatrix || posY < 1 || posY > sizeOfMatrix) {
			posX = 10*Math.random();
			posY = 10*Math.random();
			System.out.println("X = "+posX);
		}
		return location = new Point((int)posX,(int)posY);
	}



	public static void main(String[] args) {

		IterationRandom r = new IterationRandom();
		Point p = r.getRandomLocation(3);
		System.out.println("X = "+p.getX()+"Y = "+p.getY());
	}
}
