/* Copyright 2009 Yves Dubromelle, Thamer Louati @ LSIS(www.lsis.org)
 * 
 * This file is part of GenericANP.
 * 
 * GenericANP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GenericANP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GenericANP.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.taeradan.ahp.test;

import java.io.File;
import java.util.ArrayList;
import org.taeradan.ahp.Root;
import java.util.Scanner;

/**
 * Test class for the AHP tree
 * @author Yves Dubromelle
 */
public class TestAhp {
	public int nbralt = 4;

	public TestAhp() {
		// 4 alternatives pour tester (cette var est utilisée dans root pour construire la supermatrice)
		nbralt = 4;
	}

	public int nbralt() {
		return nbralt;
	}

	public static void main(String[] args) {
		Scanner inScan = new Scanner(System.in);
		String reponse;
		String file = "";
		do {
			System.out.println("\n*********Menu de test pour prise de décision**********");
			System.out.println("1- Décision sans dépendances");
			System.out.println("2- Décision avec dépendances");
			System.out.println("3- Sortir\n");
			reponse = inScan.nextLine();
			if(reponse.contains("1") == true) {
				file = "build/classes/org/taeradan/ahp/conf/ahp_conf.xml";
			}
			else if(reponse.contains("2") == true) {
				file = "build/classes/org/taeradan/ahp/conf/anp_conf.xml";
			}
			else if(reponse.contains("3") == true) {
				System.exit(0);
			}

			Root root = new Root(new File(file));
			ArrayList alts = new ArrayList(4);
			alts.add(new TestingAlternative("Produit  1", 3, 5, 3, 2 / 4., 3, 1200, 1, 100, 1));
			alts.add(new TestingAlternative("Produit  2", 3, 5, 3, 2 / 4., 1, 8638, 1, 250, 1));
			alts.add(new TestingAlternative("Produit  3", 5, 5, 5, 2 / 3., 3, 1800, 1, 300, 1));
			alts.add(new TestingAlternative("Produit  4", 3, 3, 3, 3 / 4., 3, 7136, 1, 300, 1));

			// Calcule de rang AHP
			ArrayList resultat = root.calculateRanking(alts);
			// Affichage des résultats intermediaire
			System.out.println(root.toStringRecursive());
			//Affichage du classement
			System.out.println("\nClassement AHP :\n");
			for(int i = 0; i < resultat.size(); i++) {
				TestingAlternative currentRes = (TestingAlternative) resultat.get(i);
				System.out.println(currentRes.name);
			}
			// Affichage du vecteur de classement ANP
			System.out.println("\nClassement ANP :\n");
			for(int i = 0; i < resultat.size(); i++) {
				System.out.println(root.vectorRank.get(i, 0));
			}

		}while(reponse.contains("3") == false);
	}
}
