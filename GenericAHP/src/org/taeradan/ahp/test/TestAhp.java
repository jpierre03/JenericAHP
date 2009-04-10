package org.taeradan.ahp.test;


import org.taeradan.ahp.Root;

/**
 *
 * @author Yves Dubromelle
 */
public class TestAhp {
	public static void main (String[] args){
		Root racine = new Root();
		System.out.println(racine.treeToString());
	}
}
