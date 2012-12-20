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

import org.taeradan.ahp.Root;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * Test class for the AHP tree
 *
 * @author Jean-Pierre PRUNARET
 * @author Yves Dubromelle
 */
public class TestAhp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		URL resource = TestAhp.class.getResource("/org/taeradan/ahp/conf/ahp_conf.xml");
		File aFile = new File(resource.getFile());

		final Root root = new Root(aFile, Root.indicatorPath);
//		System.out.println(root.toStringRecursive());
		final ArrayList<TestingAlternative> alts = new ArrayList<TestingAlternative>(4);
		for (int index = 0; index < 1; index++) {
			alts.add(new TestingAlternative("Alternative 1-" + index, 3, 3, 3, 3, 3, 3, 3, 3, 3));
			alts.add(new TestingAlternative("Alternative 2-" + index, 1, 1, 1, 1, 1, 5, 5, 1, 5));
			alts.add(new TestingAlternative("Alternative 3-" + index, 4, 4, 4, 4, 4, 2, 2, 4, 2));
			alts.add(new TestingAlternative("Alternative 4-" + index, 2, 2, 2, 2, 2, 4, 4, 2, 4));
			alts.add(new TestingAlternative("Alternative 5-" + index, 5, 5, 5, 5, 5, 1, 1, 5, 1));
		}
//		alts.add(new TestingAlternative("Alternative 1", 3, 5, 3, 1 / 2., 3, 1200, 1, 100, 1));
//		alts.add(new TestingAlternative("Alternative 2", 3, 5, 3, 1 / 2., 1, 8638, 1, 250, 1));
//		alts.add(new TestingAlternative("Alternative 3", 5, 5, 5, 2 / 3., 3, 1800, 1, 30, 1));
//		alts.add(new TestingAlternative("Alternative 4", 3, 3, 3, 3 / 4., 3, 7136, 1, 300, 1));
		System.out.println("Calculate Ranking =============");
		root.calculateRanking(alts);

//		alts.add(new TestingAlternative("Alternative "+Math.random(), Math.random(),Math.random(),Math.random(),Math.random(),Math.random(), Math.random(), Math.random(), Math.random(), Math.random()));
//		alts.add(new TestingAlternative("Alternative "+Math.random(), Math.random(),Math.random(),Math.random(),Math.random(),Math.random(), Math.random(), Math.random(), Math.random(), Math.random()));
//		root.calculateRanking(alts);
		System.out.println(
			"======================================================");
		System.out.println(root.resultToString());
		System.out.println("Valeurs de \"rank\" pour chaque alternative:");
		for (TestingAlternative currentAlt : alts) {
			System.out.println(currentAlt.name + " = " + currentAlt.getRank());
		}
	}

	private TestAhp() {
	}
}
