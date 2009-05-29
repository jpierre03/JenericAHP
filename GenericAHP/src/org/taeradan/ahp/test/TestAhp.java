/* Copyright 2009 Yves Dubromelle @ LSIS(www.lsis.org)
 * 
 * This file is part of GenericAHP.
 * 
 * GenericAHP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GenericAHP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GenericAHP.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.taeradan.ahp.test;


import java.util.ArrayList;
import java.util.Scanner;
import org.taeradan.ahp.Root;

/**
 * Test class for the AHP tree
 * @author Yves Dubromelle
 */
public class TestAhp {
	public static void main (String[] args){
		Scanner inScan = new Scanner(System.in);
//		System.out.println("Entrez le nom du fichier d'entrée :\n");
//		String file = inScan.nextLine();
		Root root = new Root("build/classes/org/taeradan/ahp/conf/ahp_conf.xml");
		System.out.println(root.toStringRecursive());
		ArrayList alts = new ArrayList(4);
		alts.add(new TestingAlternative("Alternative 1",3, 1 ,1, 1, 1, 1, 1, 1, 1));
		alts.add(new TestingAlternative("Alternative 2",1, 1 ,1, 1, 1, 1, 1, 1, 1));
		alts.add(new TestingAlternative("Alternative 3",4, 1 ,1, 1, 1, 1, 1, 1, 1));
		alts.add(new TestingAlternative("Alternative 4",2, 1 ,1, 1, 1, 1, 1, 1, 1));
		ArrayList resultat = root.calculateRanking(alts);
		System.out.println(root.resultToString());
		System.out.println("Resultat=");
		for(int i=0; i<resultat.size(); i++){
			TestingAlternative currentRes = (TestingAlternative)resultat.get(i);
			System.out.println(currentRes.name+" = "+currentRes.valueI11+", "+currentRes.valueI12);
		}
//		System.out.println("Entrez le nom du fichier de sortie :\n");
//		file = inScan.nextLine();
//		root.saveConfig(file);
	}
}
