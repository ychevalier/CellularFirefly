package com.bham.naturalcomp.cellularfirefly.view;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AppWindow extends JFrame {

	private static final long serialVersionUID = 2012110611;
	
	private static final int NUMBER_OF_COLUMNS = 3;
	private static final int NUMBER_OF_ROWS = 3;
	
	private FireflyView[][] mSwarm;

	public AppWindow() {
		
		setTitle("Firefly Coordination " + NUMBER_OF_COLUMNS + " x " + NUMBER_OF_ROWS);
		addWindowListener(new WindowAdapter()
	      {
			public void windowClosing(WindowEvent e)
	         {
	           dispose();
	           System.exit(0); //calling the method is a must
	         }
	      });
		
		JPanel pan = new JPanel();
		add(pan);
		GridLayout layout = new GridLayout(NUMBER_OF_ROWS, NUMBER_OF_COLUMNS);
		pan.setLayout(layout);

		
		mSwarm = new FireflyView[NUMBER_OF_COLUMNS][NUMBER_OF_ROWS];
		for(int i = 0; i < NUMBER_OF_COLUMNS; i++) {
			for (int j = 0; j < NUMBER_OF_ROWS; j++) {
				mSwarm[i][j] = new FireflyView();
				
			}
		}
		
		// Checking for neighbours...
		for(int i = 0; i < NUMBER_OF_COLUMNS; i++) {
			for (int j = 0; j < NUMBER_OF_ROWS; j++) {
				
				//////////////////////////
				// VonNeuman neighbourhood.
				/////////////////////////
				// i-1, j
				if(i-1 >= 0) {
					mSwarm[i][j].addNeighbour(mSwarm[i-1][j]);	
				}
				// i+1
				if(i+1 < NUMBER_OF_COLUMNS) {
					mSwarm[i][j].addNeighbour(mSwarm[i+1][j]);
				}
				// i, j-1
				if(j-1 >= 0) {
					mSwarm[i][j].addNeighbour(mSwarm[i][j-1]);
				}
				// i, j+1
				if(j+1 < NUMBER_OF_ROWS) {
					mSwarm[i][j].addNeighbour(mSwarm[i][j+1]);
				}
				
				//////////////////////////
				// Moore neighbourhood.
				/////////////////////////
				/*
				// i-1, j-1
				if(i-1 >= 0 && j-1 >= 0) {
					mSwarm[i][j].addNeighbour(mSwarm[i-1][j-1]);
				}
				// i-1, j+1
				if(i-1 >= 0 && j+1 < NUMBER_OF_ROWS) {
					mSwarm[i][j].addNeighbour(mSwarm[i-1][j+1]);
				}
				// i-1, j-1
				if(i+1 < NUMBER_OF_COLUMNS && j-1 >= 0) {
					mSwarm[i][j].addNeighbour(mSwarm[i+1][j-1]);
				}
				// i-1, j+1
				if(i+1 < NUMBER_OF_COLUMNS && j+1 < NUMBER_OF_ROWS) {
					mSwarm[i][j].addNeighbour(mSwarm[i+1][j+1]);
				}
				*/
				pan.add(mSwarm[i][j]);
			}
		}
		
		setSize(800, 600);
		//pack();
		//setResizable(false);
		setVisible(true);
	}
	
	public static void main(String args[]) {
		new AppWindow();
	}
}
