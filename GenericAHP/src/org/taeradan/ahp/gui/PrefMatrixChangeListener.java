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
package org.taeradan.ahp.gui;

import java.util.logging.Logger;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.JOptionPane;
import org.nfunk.jep.JEP;

/**
 * Class that listen for changes in the graphical JTable showing a preference matrix.
 * It is used to verify that the value entered is correct and to automatically fill the second half of the matrix.
 * @author Yves Dubromelle
 */
public class PrefMatrixChangeListener implements TableModelListener {

	/**
	 * Handle of the event launched every time the JTable changes
	 * @param evt
	 */
	@Override
	public void tableChanged(TableModelEvent evt) {
		Logger.getAnonymousLogger().info("row=" + evt.getFirstRow() + ",column" + evt.getColumn());
		if(evt.getFirstRow() >= evt.getColumn()) {
			PrefMatrixTableModel prefMatrix = (PrefMatrixTableModel) evt.getSource();
			Double value = null;
			Object nonParsedValue = prefMatrix.getValueAt(evt.getFirstRow(), evt.getColumn());
			Logger.getAnonymousLogger().info("Non parsed value = " + nonParsedValue);
			JEP myParser = new JEP();
//			If the changed value is a String, convert to Double
			if(nonParsedValue instanceof String) {
//				Use the parser to detect and evaluate basic operations (+ - * /) in the String
				myParser.parseExpression((String) nonParsedValue);
				value = myParser.getValue();
			}
//			If the changed value is a Double, just write it
			if(nonParsedValue instanceof Double) {
				value = (Double) nonParsedValue;
			}
			Logger.getAnonymousLogger().info("Parsed value = " + value.doubleValue());

//			Case where the value is "0". Must be avoid because there will be a division later
			if(value == 0) {
				String newValue = JOptionPane.showInputDialog("The value \"0\" is not allowed here\nEnter a new value :");
				if(newValue == null) {
					newValue = "1";
				}
				myParser.parseExpression(newValue);
				value=myParser.getValue();
				prefMatrix.setValueAt(value, evt.getFirstRow(), evt.getColumn());
			}
//			Case where the value is not entered : DON'T WORK FOR NOW
			if(Double.isNaN(value)) {
				String newValue = JOptionPane.showInputDialog("This can't be leaved blank\nPlease enter a value :");
				if(newValue == null) {
					newValue = "1";
				}
				myParser.parseExpression(newValue);
				value=myParser.getValue();
				prefMatrix.setValueAt(value, evt.getFirstRow(), evt.getColumn());
			}
			prefMatrix.setValueAt((1 / value), evt.getColumn(), evt.getFirstRow());
		}
	}
}
