package ConsistenceMakerTemp;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author jpierre03
 * @author Marianne
 */
public class HighLighter
		extends DefaultTableCellRenderer {

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
