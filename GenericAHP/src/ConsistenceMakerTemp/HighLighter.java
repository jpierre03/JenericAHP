package ConsistenceMakerTemp;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Jean-Pierre PRUNARET
 * @author Marianne
 */
public class HighLighter
	extends DefaultTableCellRenderer {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param table
	 * @param value
	 * @param isSelected
	 * @param hasFocus
	 * @param row
	 * @param column
	 * @return
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table,
						       Object value,
						       boolean isSelected,
						       boolean hasFocus,
						       int row,
						       int column) {

		Component cell = super.getTableCellRendererComponent(table,
			value,
			isSelected,
			hasFocus,
			row,
			column);

		if (row == 1 && column == 1) {
			cell.setBackground(Color.PINK);
		} else {
			cell.setBackground(Color.BLUE);
		}

		return cell;
	}
}
