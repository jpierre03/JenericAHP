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
	private int minuteCounter = 0;
	private int secondCounter = 0;
	private boolean commencerAZero = false;

	public Meter(JLabel label) {
		this.label = label;
		label.setText("00:00");

		timer = new Timer(1000, this);
	}

	public void start() {
		if (!commencerAZero) {
			minuteCounter = 0;
			secondCounter = 0;
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

		secondCounter += 1;
		if (secondCounter >= 60) {
			secondCounter = 0;
			minuteCounter++;
		}
		if (minuteCounter >= 60) {
			secondCounter = 0;
			minuteCounter = 0;
		}

		final String secondString, minuteString;

		if (secondCounter < 10) {
			secondString = "0" + secondCounter;
		} else {
			secondString = "" + secondCounter;
		}

		if (minuteCounter < 10) {
			minuteString = "0" + minuteCounter;
		} else {
			minuteString = "" + minuteCounter;
		}

		final String formattedDuration = minuteString + ":" + secondString;
		label.setText(formattedDuration);
	}

	public void setCommencerAZero(boolean commencerAZero) {
		this.commencerAZero = commencerAZero;
	}
}

