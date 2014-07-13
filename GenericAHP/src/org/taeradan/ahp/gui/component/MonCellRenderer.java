/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.gui.component;

/**
 *
 * @author Yannick
 */

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public final class MonCellRenderer
	extends DefaultTableCellRenderer {

	private int row = 0;
	private int col = 0;

	public MonCellRenderer(int row, int col) {
		checkPositiveStrictly(row);
		checkPositiveStrictly(col);
		this.row = row;
		this.col = col;
	}

	private static void checkPositive(int value) {
		boolean isValid = value >= 0;
		if (!isValid) {
			throw new IllegalArgumentException("value must be Positive (>=0)");
		}
	}

	private static void checkPositiveStrictly(int value) {
		boolean isValid = value > 0;
		if (!isValid) {
			throw new IllegalArgumentException("value must be Positive (>0)");
		}
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		checkPositive(col);
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		checkPositive(row);
		this.row = row;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table,
						       Object value,
						       boolean isSelected,
						       boolean hasFocus,
						       int row,
						       int column) {
		checkPositive(row);
		checkPositive(column);
		if (table == null) {
			throw new NullPointerException("table not defined");
		}

		Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if ((this.row == row)
			&& (this.col == column)) {
			cell.setBackground(Color.YELLOW);
		}
		for (int r = 0; r < table.getRowCount(); r++) {
			for (int c = 0; c < table.getColumnCount(); c++) {
				if ((column < row || column == row) && (column != 0)) {
					cell.setBackground(Color.lightGray);
				} else if (row == 0) {
					cell.setBackground(Color.GRAY);
				} else if (column == 0) {
					cell.setBackground(Color.GRAY);
				} else {
					cell.setBackground(Color.WHITE);
				}
			}
		}

		return cell;
	}
}

