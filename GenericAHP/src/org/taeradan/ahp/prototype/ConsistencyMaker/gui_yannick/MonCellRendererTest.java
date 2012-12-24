package org.taeradan.ahp.prototype.ConsistencyMaker.gui_yannick;

import org.taeradan.ahp.matrix.MyMatrix;

import javax.swing.JFrame;
import javax.swing.JTable;

public final class MonCellRendererTest {
	private MonCellRendererTest() {
	}

	public static void main(String args[]) {
		MyMatrix MyMatrix = new MyMatrix(5, 5);
		JTable table = new JTable(5, 5);
		int j = 0;
		MonCellRenderer cellRenderer = new MonCellRenderer(0, 0);
		for (int i = 0; i < 5; i++) {
			int value = i;
			cellRenderer.setRow(value);
			cellRenderer.setCol(value);
		}

		table.setDefaultRenderer(Object.class, cellRenderer);
		JFrame jFrame = new JFrame();
		jFrame.add(table);
		jFrame.pack();
		jFrame.setVisible(true);
	}
}
