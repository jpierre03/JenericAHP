/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.ConsistencyMaker;

/**
 *
 * @author Yannick
 */

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class MonCellRenderer
	extends DefaultTableCellRenderer {

	int row;
	int col;

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
				if ((column < row || column == row) && (column != 0))
					cell.setBackground(Color.lightGray);
				else if (row == 0) cell.setBackground(Color.GRAY);
				else if (column == 0) cell.setBackground(Color.GRAY);
				//else cell.setBackground(Color.WHITE);
			}
		}
		return cell;
	}
}

class TestCellRenderer {
	public static void main(String args[]) {
		MyMatrix MyMatrix = new MyMatrix(5, 5);
		JTable matable = new JTable(5, 5);
		int j = 0;
		MonCellRenderer moncell = new MonCellRenderer(0, 0);
		matable.setDefaultRenderer(Object.class, moncell);
		JFrame jFrame = new JFrame();
		jFrame.add(matable);
		jFrame.pack();
		jFrame.setVisible(true);
		/*for(int i = 0;i<5;i++)
		{
			j = i;
			moncell.setRow(i);
			moncell.setCol(j);
		}
		*/

	}
}
