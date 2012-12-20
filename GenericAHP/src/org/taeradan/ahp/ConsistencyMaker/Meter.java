/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.taeradan.ahp.ConsistencyMaker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Beal Yannick
 */
public class Meter implements ActionListener {
	private Timer timer;
	private JLabel label;
	private InterfaceAHP frame;
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
	/*
	String time = "" + (new Hour(new Date(System.currentTimeMillis()))).getHour();
        time += ":" + (new Minute(new Date(System.currentTimeMillis()))).getMinute();
        time += ":" + (new Second(new Date(System.currentTimeMillis()))).getSecond();*/
		String stS, stM, stH;
		stS = stM = stH = "";
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

