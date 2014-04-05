/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.taeradan.ahp.prototype.gui.yannick;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Beal Yannick
 */
class Meter implements ActionListener {
	private final Timer timer;
	private final JLabel label;
	private final InterfaceAHP frame;
	private int cpM = 0;
	private int cpS = 0;
	private boolean commencerAZero = false;

	public Meter(JLabel label, InterfaceAHP frame) {
		this.frame = frame;
		this.label = label;
		label.setText("00:00");

		timer = new Timer(1000, this);

		//timer.schedule(new MeterTask(label), 1);
	}

	public void start() {
		if (!commencerAZero) {
			cpM = 0;
			cpS = 0;
		}
		timer.start();
	}

	public void stop() {
		timer.stop();
	}

	public String getTime() {
		return label.getText();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		cpS += 1;
		if (cpS >= 60) {
			cpS = 0;
			cpM++;
		}
		if (cpM >= 60) {
			cpS = 0;
			cpM = 0;
		}

		String stS, stM;

		if (cpS < 10)
			stS = "0" + cpS;
		else
			stS = "" + cpS;
		if (cpM < 10)
			stM = "0" + cpM;
		else
			stM = "" + cpM;

		String time = stM + ":" + stS;
		label.setText(time);
	}

	public void setCommencerAZero(boolean commencerAZero) {
		this.commencerAZero = commencerAZero;
	}
}

