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
package org.taeradan.ahp.prototype.gui;

import org.nfunk.jep.JEP;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.logging.Logger;

/**
 * Class that listen for changes in the graphical JTable showing a preference matrix.
 * <p/>
 * It is used to verify that the value entered is correct and to automatically fill the second half of the matrix.
 *
 * @author Yves Dubromelle
 * @author Jean-Pierre PRUNARET
 */
public class PairWiseMatrixChangeListener
	implements TableModelListener {

	final JEP jep = new JEP();
	final static String WRONG_ZERO_VALUE = "The value \"0\" is not allowed here\nEnter a new value :";
	final static String WRONG_EMPTY_VALUE = "This can't be leaved blank\nPlease enter a value :";

	/**
	 * Handle of the event launched every time the JTable changes
	 */
	@Override
	public void tableChanged(TableModelEvent event) {
		assert event != null;
		assert event.getSource() instanceof PairWiseMatrixTableModel;

		info("row=" + event.getFirstRow() + ",column" + event.getColumn());

		final boolean isValidRow = event.getFirstRow() >= 0;
		final boolean isValidColumn = event.getColumn() >= 0;
		final boolean isUpperSide = event.getColumn() >= event.getFirstRow();

		final boolean invalidSituation = !isValidRow || !isValidColumn || !isUpperSide;

		if (invalidSituation) {
			// do nothing
		}

		if (!invalidSituation) {
			final TableModel preferenceMatrix = (PairWiseMatrixTableModel) event.getSource();
			final Object nonParsedValue = preferenceMatrix.getValueAt(event.getFirstRow(), event.getColumn());

			info("Non parsed value = " + nonParsedValue);

			Double value = parseValue(nonParsedValue);

			info("Parsed value = " + value);

			/** Case where the value is "0". Must be avoid because there will be a division later */
			if (value <= 1E-14) {
				String newValue = JOptionPane.showInputDialog(WRONG_ZERO_VALUE);
				if (newValue == null) {
					newValue = "1";
				}
				value = parseValue(newValue);
				preferenceMatrix.setValueAt(value, event.getFirstRow(), event.getColumn());
			}

			/** Case where the value is not entered */
			if (Double.isNaN(value)) {
				String newValue = JOptionPane.showInputDialog(WRONG_EMPTY_VALUE);
				if (newValue == null) {
					newValue = "1";
				}
				value = parseValue(newValue);
				preferenceMatrix.setValueAt(value, event.getFirstRow(), event.getColumn());
			}
			preferenceMatrix.setValueAt((1 / value), event.getColumn(), event.getFirstRow());
		}
	}

	private Double parseValue(Object nonParsedValue) {
		Double value = null;
		boolean conversionDone = false;

		/** If the changed value is a String, convert to Double */
		if (nonParsedValue instanceof String) {
			/** Use the parser to detect and evaluate basic operations (+ - * /) in the String */
			jep.parseExpression((String) nonParsedValue);
			value = jep.getValue();
			conversionDone = true;
		}
		/** If the changed value is a Double, just write it */
		if (nonParsedValue instanceof Double) {
			value = (Double) nonParsedValue;
			conversionDone = true;
		}

		assert conversionDone;
		assert value != null;
		return value;
	}

	private static void info(String string) {
		Logger.getAnonymousLogger().info(string);
	}
}
