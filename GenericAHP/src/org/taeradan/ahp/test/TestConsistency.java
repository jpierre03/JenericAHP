/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.test;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.jdom.Document;
import org.jdom.Element;
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
	 * @param args the command line arguments
	 */
	public static void main(final String[] args) {
		final JFileChooser matrixChooser = new JFileChooser(System.getProperty("user.dir"));
		matrixChooser.showOpenDialog(null);
		final SAXBuilder parser = new SAXBuilder();
		try {
			final Document inDocument = parser.build(matrixChooser.getSelectedFile());
			final PreferenceMatrix matrix = new PreferenceMatrix(inDocument.getRootElement());
			final PriorityVector vector = new PriorityVector(matrix);
			final boolean result = ConsistencyChecker.isConsistent(matrix, vector);
			System.out.println("CR = "+ConsistencyChecker.getCrResult());
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
