package org.taeradan.ahp.gui.component;

import javax.swing.*;

public final class MonCellRendererTest {
	private MonCellRendererTest() {
	}

	public static void main(String... args) {
		JTable table = new JTable(5, 5);
		MonCellRenderer cellRenderer = new MonCellRenderer(0, 0);
		for (int i = 0; i < 5; i++) {
			cellRenderer.setRow(i);
			cellRenderer.setCol(i);
		}

		table.setDefaultRenderer(Object.class, cellRenderer);
		JFrame jFrame = new JFrame();
		jFrame.add(table);
		jFrame.pack();
		jFrame.setVisible(true);
	}
}
