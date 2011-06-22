/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.taeradan.ahp.ConsistencyMaker;

/**
 *
 * @author Yannick
 */
import java.awt.Color;
import java.awt.Component;
import javax.swing.JFrame;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

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

	public Component getTableCellRendererComponent(JTable table, Object value,
												   boolean isSelected, boolean hasFocus, int row,
												   int column) {
		Component cell = super.getTableCellRendererComponent(table, value,
															 isSelected, hasFocus, row, column);

		if (row == this.row && column == this.col) {
			cell.setBackground(Color.GRAY);
		} else {
			cell.setBackground(Color.WHITE);
		}
		return cell;
	}
}
class testCellRenderer{
	public static void main(String args[])
	{
		MyMatrix MyMatrix = new MyMatrix(25, 25);
		JTable matable = new JTable(25, 25);
		int j =0;
		MonCellRenderer moncell = new MonCellRenderer(0, 0);
		matable.setDefaultRenderer(Object.class, moncell);
		JFrame jFrame = new JFrame();
		jFrame.add(matable);
		jFrame.pack();
		jFrame.setVisible(true);
		for(int i = 0;i<25;i++)
		{
			j = i;
			moncell.setRow(i);
			moncell.setCol(j);

		}
		
	}

}
