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

import org.taeradan.ahp.AHPRoot;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Jean-Pierre PRUNARET
 * @author Yves Dubromelle
 */
public final class ControversialTest {

	private static final double TEMOIN_MAX   = 1000;
	private static final double VARIABLE_MAX = 1000000000;
	private static final double DECALAGE     = 0;

	private ControversialTest() {
	}

	public static void main(String[] args) {
		final URL resource = TestAhp.class.getResource("/org/taeradan/ahp/conf/controversial_test.xml");
		final File aFile = new File(resource.getFile());

		final AHPRoot ahpRoot = new AHPRoot(aFile, AHPRoot.indicatorPath);
		System.out.println(ahpRoot.toStringRecursive());

		final ArrayList<ControversialAlternative> alternatives = makeAlternatives();
		ahpRoot.calculateRanking(alternatives);


		System.out.println("======================================================");
		System.out.println(ahpRoot.resultToString());
		System.out.println("Valeurs de \"rank\" pour chaque alternative:");

		printRanking(alternatives);
	}

	private static ArrayList<ControversialAlternative> makeAlternatives() {
		final ArrayList<ControversialAlternative> alts = new ArrayList<>(4);
		alts.add(new ControversialAlternative("Alternative 1",
											  (1 / 5.) * TEMOIN_MAX,
											  ((1 / 5.) * VARIABLE_MAX) + DECALAGE));
		System.out.println("Alternative 1, valueIT=" + (1 / 5.) * TEMOIN_MAX + ", valueIV=" + (((1 / 5.) * VARIABLE_MAX) + DECALAGE));

		alts.add(new ControversialAlternative("Alternative 2",
											  (2 / 5.) * TEMOIN_MAX,
											  ((2 / 5.) * VARIABLE_MAX) + DECALAGE));
		System.out.println("Alternative 2, valueIT=" + (2 / 5.) * TEMOIN_MAX + ", valueIV=" + (((2 / 5.) * VARIABLE_MAX) + DECALAGE));

		alts.add(new ControversialAlternative("Alternative 3",
											  (3 / 5.) * TEMOIN_MAX,
											  ((3 / 5.) * VARIABLE_MAX) + DECALAGE));
		System.out.println("Alternative 3, valueIT=" + (3 / 5.) * TEMOIN_MAX + ", valueIV=" + (((3 / 5.) * VARIABLE_MAX) + DECALAGE));

		alts.add(new ControversialAlternative("Alternative 4",
											  (4 / 5.) * TEMOIN_MAX,
											  ((4 / 5.) * VARIABLE_MAX) + DECALAGE));
		System.out.println("Alternative 4, valueIT=" + (4 / 5.) * TEMOIN_MAX + ", valueIV=" + (((4 / 5.) * VARIABLE_MAX) + DECALAGE));

		alts.add(new ControversialAlternative("Alternative 5",
											  (5 / 5.) * TEMOIN_MAX,
											  ((5 / 5.) * VARIABLE_MAX) + DECALAGE));
		System.out.println("Alternative 5, valueIT=" + (5 / 5.) * TEMOIN_MAX + ", valueIV=" + (((5 / 5.) * VARIABLE_MAX) + DECALAGE));
		return alts;
	}

	private static void printRanking(ArrayList<ControversialAlternative> alternatives) {
		for (ControversialAlternative currentAlt : alternatives) {
			System.out.println(currentAlt.name + " = " + currentAlt.getRank());
		}
	}
}
