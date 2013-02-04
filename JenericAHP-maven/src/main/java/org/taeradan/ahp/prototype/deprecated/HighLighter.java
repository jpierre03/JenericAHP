package org.taeradan.ahp.prototype.deprecated;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;

/**
 * @author Jean-Pierre PRUNARET
 * @author Marianne
 */
@Deprecated
public final class HighLighter
		extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {

		Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (row == 1 && column == 1) {
			cell.setBackground(Color.PINK);
		} else {
			cell.setBackground(Color.BLUE);
		}

		return cell;
	}
}
