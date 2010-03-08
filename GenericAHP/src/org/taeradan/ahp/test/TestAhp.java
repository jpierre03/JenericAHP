/* Copyright 2009-2010 Yves Dubromelle @ LSIS(www.lsis.org)
 * 
 * This file is part of JenericAHP.
 * 
 * JenericAHP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JenericAHP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JenericAHP.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.taeradan.ahp.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.taeradan.ahp.Root;

/**
 * Test class for the AHP tree
 * @author Yves Dubromelle
 */
public class TestAhp {

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Root root = new Root(new File(
				"build/classes/org/taeradan/ahp/conf/ahp_conf.xml"),
							 Root.indicatorPath);
		System.out.println(root.toStringRecursive());
		final ArrayList<TestingAlternative> alts = new ArrayList<TestingAlternative>(
				4);
		alts.add(new TestingAlternative("Alternative 1", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 2", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 3", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 4", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 5", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 6", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 7", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 8", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 9", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 10", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 1", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 2", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 3", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 4", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 5", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 6", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 7", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 8", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 9", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 10", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 1", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 2", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 3", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 4", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 5", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 6", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 7", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 8", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 9", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 10", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 1", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 2", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 3", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 4", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 5", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 6", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 7", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 8", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 9", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 10", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 1", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 2", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 3", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 4", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 5", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 6", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 7", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 8", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 9", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 10", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 1", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 2", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 3", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 4", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 5", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 6", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 7", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 8", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 9", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 10", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 1", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 2", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 3", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 4", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 5", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 6", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 7", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 8", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 9", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 10", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 1", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 2", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 3", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 4", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 5", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 6", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 7", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 8", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 9", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 10", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 1", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 2", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 3", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 4", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 5", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 6", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 7", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 8", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 9", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 10", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 1", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 2", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 3", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 4", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 5", 3, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 6", 1, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 7", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 8", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 9", 4, 1, 1, 1, 1, 1, 1, 1,
										1));
		alts.add(new TestingAlternative("Alternative 10", 2, 1, 1, 1, 1, 1, 1, 1,
										1));
		root.calculateRanking(alts);

//		alts.add(new TestingAlternative("Alternative "+Math.random(), Math.random(),Math.random(),Math.random(),Math.random(),Math.random(), Math.random(), Math.random(), Math.random(), Math.random()));
//		alts.add(new TestingAlternative("Alternative "+Math.random(), Math.random(),Math.random(),Math.random(),Math.random(),Math.random(), Math.random(), Math.random(), Math.random(), Math.random()));
//		root.calculateRanking(alts);
		System.out.println(
				"======================================================");
		System.out.println(root.resultToString());
		System.out.println("Valeurs de \"rank\" pour chaque alternative:");
		Iterator<TestingAlternative> itAlts = alts.iterator();
		while (itAlts.hasNext()) {
			TestingAlternative currentRes = itAlts.next();
			System.out.println(currentRes.name + " = " + currentRes.getRank());
		}
	}
}
