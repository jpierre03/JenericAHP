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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.taeradan.ahp.ConsistencyChecker;
import org.taeradan.ahp.PreferenceMatrix;
import org.taeradan.ahp.PriorityVector;

/**
 *
 * @author Yves Dubromelle
 */
public class TestConsistency {

	/**
	 * 
	 */
	final private static JFileChooser matrixChooser = new JFileChooser(
			System.getProperty("user.dir"));

	/**
	 * @param args the command line arguments
	 */
	public static void main(final String[] args) {
		matrixChooser.showOpenDialog(null);
		final SAXBuilder parser = new SAXBuilder();
		try {
			final Document inDocument = parser.build(matrixChooser.getSelectedFile());
			final PreferenceMatrix matrix = new PreferenceMatrix(inDocument.getRootElement());
			final PriorityVector vector = new PriorityVector(matrix);
			System.out.println(PreferenceMatrix.toString(vector.getVector(), null));
			final boolean result = ConsistencyChecker.isConsistent(matrix, vector);
			System.out.println("CR = " + ConsistencyChecker.getCrResult());
			if (result) {
				System.out.println("Matrice consistante");
			} else {
				System.out.println("Matrice non consistante");
			}
		} catch (JDOMException ex) {
			Logger.getLogger(TestConsistency.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(TestConsistency.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
