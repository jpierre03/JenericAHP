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
package org.taeradan.ahp.prototype.gui.netbeans_gui_modeler;

import java.awt.*;

/**
 * Graphical user interface to configure the AHP tree and the preference matrix
 *
 * @author Yves Dubromelle
 * @author Jean-Pierre PRUNARET
 */
public final class ConfigurationFrameTest {

	private ConfigurationFrameTest() {
	}

	public static void main(final String args[]) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new ConfigurationFrame().setVisible(true);
			}
		});
	}
}
