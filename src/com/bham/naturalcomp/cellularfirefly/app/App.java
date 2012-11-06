package com.bham.naturalcomp.cellularfirefly.app;

import java.util.Timer;
import java.util.TimerTask;

import com.bham.naturalcomp.cellularfirefly.listener.FlashCounter;
import com.bham.naturalcomp.cellularfirefly.model.Firefly;
import com.bham.naturalcomp.cellularfirefly.view.AppWindow;

public class App implements FlashCounter {
	
	public static final int NUMBER_OF_COLUMNS = 50;
	public static final int NUMBER_OF_ROWS = 50;
	
	private static final int CYCLE_PERIOD = 100;
	
	public static int nbFlashAtTick;
	public static int maxFlashAtTick;
	
	public static long nbTick;
	
	private Firefly[][] mSwarm;
	private Timer mTimer;
	
	private class SwarmUpdateTask extends TimerTask {
		@Override
		public void run() {
			System.out.println("At tick " + nbTick + " : " + (float)nbFlashAtTick/((float)NUMBER_OF_COLUMNS * (float)NUMBER_OF_ROWS) * 100 + "%");
			nbTick++;
			nbFlashAtTick = 0;
			// Display Things that are necessary
			for(int i = 0; i < NUMBER_OF_COLUMNS; i++) {
				for (int j = 0; j < NUMBER_OF_ROWS; j++) {
					mSwarm[i][j].act();
				}
			}
			
			// Update state
			for(int i = 0; i < NUMBER_OF_COLUMNS; i++) {
				for (int j = 0; j < NUMBER_OF_ROWS; j++) {
					mSwarm[i][j].nextStep();
				}
			}
		}
	}
	
	protected App() {
		nbFlashAtTick = 0;
		maxFlashAtTick = 0;
		nbTick = 0;
		mSwarm = new Firefly[NUMBER_OF_COLUMNS][NUMBER_OF_ROWS];
		for(int i = 0; i < NUMBER_OF_COLUMNS; i++) {
			for (int j = 0; j < NUMBER_OF_ROWS; j++) {
				mSwarm[i][j] = new Firefly();
				mSwarm[i][j].setCounter(this);
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
			}
		}
	}
	
	private void launchSimu() {
		mTimer = new Timer();
		SwarmUpdateTask mTask = new SwarmUpdateTask();
		mTimer.scheduleAtFixedRate(mTask, 0, CYCLE_PERIOD);
	}
	
	@Override
	public void reportFlash() {
		nbFlashAtTick++;
		maxFlashAtTick = nbFlashAtTick > maxFlashAtTick ? nbFlashAtTick : maxFlashAtTick;
	}
	
	public static void main(String args[]) {
		final App app = new App();
		/*final AppWindow window = */new AppWindow(app.mSwarm);
		app.launchSimu();
	}

	

}
