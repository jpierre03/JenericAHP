/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.prototype.gui.yannick;

/**
 *
 * @author Yannick
 */

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;

class MonCellRenderer
		extends DefaultTableCellRenderer {

	private int row;
	private int col;

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public MonCellRenderer(int row, int col) {
		this.row = row;
		this.col = col;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table,
												   Object value,
												   boolean isSelected,
												   boolean hasFocus,
												   int row,
												   int column) {

		Component cell = super.getTableCellRendererComponent(table, value,
															 isSelected, hasFocus, row, column);

		if (row == this.row && column == this.col) {
			cell.setBackground(Color.YELLOW);
		} else {
			cell.setBackground(Color.WHITE);
		}
		for (int r = 0; r < table.getRowCount(); r++) {
			for (int c = 0; c < table.getColumnCount(); c++) {
				if ((column < row || column == row) && (column != 0)) {
					cell.setBackground(Color.lightGray);
				} else if (row == 0) {
					cell.setBackground(Color.GRAY);
				} else if (column == 0) {
					cell.setBackground(Color.GRAY);
				}
				//else cell.setBackground(Color.WHITE);
			}
		}
		return cell;
	}
}

