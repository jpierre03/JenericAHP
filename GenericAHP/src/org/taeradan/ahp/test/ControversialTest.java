/* Copyright 2009-2010 Yves Dubromelle
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
import java.net.URL;
import java.util.ArrayList;
import org.taeradan.ahp.Root;

/**
 * @author jpierre03
 * @author Yves Dubromelle
 */
public class ControversialTest {

	/**
	 *
	 */
	final static private double TEMOIN_MAX = 1000;
	/**
	 *
	 */
	final static private double VARIABLE_MAX = 1000000000;
	/**
	 *
	 */
	final static private double DECALAGE = 0;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		URL resource = TestAhp.class.getResource(
				"/org/taeradan/ahp/conf/controversial_test.xml");
		File aFile = new File(resource.getFile());

		final Root root = new Root(aFile, Root.indicatorPath);
		System.out.println(root.toStringRecursive());
		final ArrayList<ControversialAlternative> alts = new ArrayList<ControversialAlternative>(
				4);
		alts.add(new ControversialAlternative("Alternative 1",
											  (1 / 5.) * TEMOIN_MAX,
											  ((1 / 5.) * VARIABLE_MAX) + DECALAGE));
		System.out.println("Alternative 1, valueIT=" + (1 / 5.) * TEMOIN_MAX
						   + ", valueIV=" + (((1 / 5.) * VARIABLE_MAX) + DECALAGE));
		alts.add(new ControversialAlternative("Alternative 2",
											  (2 / 5.) * TEMOIN_MAX,
											  ((2 / 5.) * VARIABLE_MAX) + DECALAGE));
		System.out.println("Alternative 2, valueIT=" + (2 / 5.) * TEMOIN_MAX
						   + ", valueIV=" + (((2 / 5.) * VARIABLE_MAX) + DECALAGE));
		alts.add(new ControversialAlternative("Alternative 3",
											  (3 / 5.) * TEMOIN_MAX,
											  ((3 / 5.) * VARIABLE_MAX) + DECALAGE));
		System.out.println("Alternative 3, valueIT=" + (3 / 5.) * TEMOIN_MAX
						   + ", valueIV=" + (((3 / 5.) * VARIABLE_MAX) + DECALAGE));
		alts.add(new ControversialAlternative("Alternative 4",
											  (4 / 5.) * TEMOIN_MAX,
											  ((4 / 5.) * VARIABLE_MAX) + DECALAGE));
		System.out.println("Alternative 4, valueIT=" + (4 / 5.) * TEMOIN_MAX
						   + ", valueIV=" + (((4 / 5.) * VARIABLE_MAX) + DECALAGE));
		alts.add(new ControversialAlternative("Alternative 5",
											  (5 / 5.) * TEMOIN_MAX,
											  ((5 / 5.) * VARIABLE_MAX) + DECALAGE));
		System.out.println("Alternative 5, valueIT=" + (5 / 5.) * TEMOIN_MAX
						   + ", valueIV=" + (((5 / 5.) * VARIABLE_MAX) + DECALAGE));
		root.calculateRanking(alts);
		System.out.println("======================================================");
		System.out.println(root.resultToString());
		System.out.println("Valeurs de \"rank\" pour chaque alternative:");
		for (ControversialAlternative currentAlt : alts) {
			System.out.println(currentAlt.name + " = " + currentAlt.getRank());
		}
	}
}
