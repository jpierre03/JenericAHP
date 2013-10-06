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
package org.taeradan.ahp.prototype.gui.matrix;

import org.taeradan.ahp.prototype.gui.PairWiseMatrixTableModel;

/**
 * Specialised implementation of tableModel for the Preference Matrix needs
 *
 * @author Jean-Pierre PRUNARET
 * @author Marianne
 * @author Yves Dubromelle
 */
public final class MyMatrixTableModel
	extends PairWiseMatrixTableModel {

	private static final long serialVersionUID = 1L;

	public MyMatrixTableModel() {
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		flushContent(row, col);

		return super.isCellEditable(row, col);
	}

	private void flushContent(int row, int col) {
		if (row > 0 && col > row) {
			setValueAt("", row, col);
		}
	}
}
