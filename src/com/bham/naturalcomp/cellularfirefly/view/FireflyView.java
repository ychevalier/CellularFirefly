package com.bham.naturalcomp.cellularfirefly.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.bham.naturalcomp.cellularfirefly.interfaces.FireflyDisplay;
import com.bham.naturalcomp.cellularfirefly.model.Firefly;

public class FireflyView extends JPanel implements FireflyDisplay {

	private static final long serialVersionUID = 2012110612;
	
	private static final int SIZE_OF_CELL = 10;
	
	private static final Color COLOR_DEFAULT = Color.LIGHT_GRAY;
	// Nice Blue
	private static final Color COLOR_FLASH = new Color(44, 182, 209);
	
	private Firefly mFirefly;
	
	private JLabel mLabel;

	public FireflyView() {
		
		// UI
		setBackground(COLOR_DEFAULT);
		setPreferredSize(new Dimension(SIZE_OF_CELL, SIZE_OF_CELL));
		setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		mLabel = new JLabel();
		add(mLabel);
		
		// Core
		mFirefly = new Firefly();
		mFirefly.setDisplay(this);
		mFirefly.launchFirefly();
	}
	
	public void addNeighbour(FireflyView f) {
		mFirefly.addNeighbour(f.mFirefly);
	}

	@Override
	public void displayFlash() {
		setBackground(COLOR_FLASH);
	}

	@Override
	public void stopFlash() {
		setBackground(COLOR_DEFAULT);
	}

	@Override
	public void updateState(int state) {
		mLabel.setText(String.valueOf(state));
	}

}
