package com.bham.naturalcomp.cellularfirefly.app;

import java.util.Timer;
import java.util.TimerTask;

import com.bham.naturalcomp.cellularfirefly.listener.FlashCounter;
import com.bham.naturalcomp.cellularfirefly.model.Firefly;
import com.bham.naturalcomp.cellularfirefly.view.AppWindow;

public class App implements FlashCounter {
	
	private static final boolean SIMU_IS_OVER = true;
	private static final boolean SIMU_IS_RUNNING = false;
	
	// The number of times we actually get the criterion.
	public static int mCurrentTimesCriterion;
	
	public static int mNbFlashAtTick;
	public static int mMaxFlashAtTick;
	
	public static long mNbTick;
	
	public static SwarmUpdateTask mTask;
	
	private Firefly[][] mSwarm;
	private Timer mTimer;
	
	private long[] mSimuEnd;
	
	public static int CURRENT_SENSIBILITY;
	
	private class SwarmUpdateTask extends TimerTask {
		@Override
		public void run() {
			// Run the simulation until the boolean says its over.
			// Then cancel it...
			if(simulation()) {
				cancel();
			}
		}
	}
	
	private boolean simulation() {
		//float percentageFlash = (float)mNbFlashAtTick/((float)Config.NUMBER_OF_COLUMNS * (float)Config.NUMBER_OF_ROWS) * 100;
		
		if(mNbFlashAtTick == Config.NUMBER_OF_ROWS*Config.NUMBER_OF_COLUMNS) {
			return SIMU_IS_OVER;
		} else if(mNbTick >= Config.UNFINISHED && Config.UNFINISHED != 0) {
			return SIMU_IS_OVER;
		}
		
		mNbTick++;
		mNbFlashAtTick = 0;
		// Display Things that are necessary
		for(int i = 0; i < Config.NUMBER_OF_COLUMNS; i++) {
			for (int j = 0; j < Config.NUMBER_OF_ROWS; j++) {
				mSwarm[i][j].act();
			}
		}
		
		// Update state
		for(int i = 0; i < Config.NUMBER_OF_COLUMNS; i++) {
			for (int j = 0; j < Config.NUMBER_OF_ROWS; j++) {
				mSwarm[i][j].nextStep();
			}
		}
		
		return SIMU_IS_RUNNING;
	}
	
	private void resetSimu() {
		mNbFlashAtTick = 0;
		mMaxFlashAtTick = 0;
		mNbTick = 0;
		mCurrentTimesCriterion = 0;
	}
	
	private void resetSwarm() {
		for(int i = 0; i < Config.NUMBER_OF_COLUMNS; i++) {
			for (int j = 0; j < Config.NUMBER_OF_ROWS; j++) {
				mSwarm[i][j].reset();
			}
		}
	}
	
	protected App() {
		resetSimu();
		mSwarm = new Firefly[Config.NUMBER_OF_COLUMNS][Config.NUMBER_OF_ROWS];
		mSimuEnd = new long[Config.NUM_SIMULATIONS];
		for(int i = 0; i < Config.NUMBER_OF_COLUMNS; i++) {
			for (int j = 0; j < Config.NUMBER_OF_ROWS; j++) {
				mSwarm[i][j] = new Firefly();
				mSwarm[i][j].setCounter(this);
			}
		}
		
		// Checking for neighbours...
		for(int i = 0; i < Config.NUMBER_OF_COLUMNS; i++) {
			for (int j = 0; j < Config.NUMBER_OF_ROWS; j++) {
				
				//////////////////////////
				// VonNeuman neighbourhood.
				/////////////////////////
				// i-1, j
				if(i-1 >= 0) {
					mSwarm[i][j].addNeighbour(mSwarm[i-1][j]);	
				}
				// i+1
				if(i+1 < Config.NUMBER_OF_COLUMNS) {
					mSwarm[i][j].addNeighbour(mSwarm[i+1][j]);
				}
				// i, j-1
				if(j-1 >= 0) {
					mSwarm[i][j].addNeighbour(mSwarm[i][j-1]);
				}
				// i, j+1
				if(j+1 < Config.NUMBER_OF_ROWS) {
					mSwarm[i][j].addNeighbour(mSwarm[i][j+1]);
				}
				
				//////////////////////////
				// Moore neighbourhood.
				/////////////////////////
				if(Config.MOORE_NEIGHBOURHOOD) {
					// i-1, j-1
					if(i-1 >= 0 && j-1 >= 0) {
						mSwarm[i][j].addNeighbour(mSwarm[i-1][j-1]);
					}
					// i-1, j+1
					if(i-1 >= 0 && j+1 < Config.NUMBER_OF_ROWS) {
						mSwarm[i][j].addNeighbour(mSwarm[i-1][j+1]);
					}
					// i-1, j-1
					if(i+1 < Config.NUMBER_OF_COLUMNS && j-1 >= 0) {
						mSwarm[i][j].addNeighbour(mSwarm[i+1][j-1]);
					}
					// i-1, j+1
					if(i+1 < Config.NUMBER_OF_COLUMNS && j+1 < Config.NUMBER_OF_ROWS) {
						mSwarm[i][j].addNeighbour(mSwarm[i+1][j+1]);
					}
				}
				//*/
			}
		}
	}
	
	private void launchSimu() {
		
		if(Config.BATCH_MODE) {
			System.out.println("Num of runs, Sensibility, AVG ticks, Std Dev, failures, max");
			for (int k = Config.SENSIBILITY_MAX; k >= Config.SENSIBILITY_MAX; k-=Config.SENSIBILITY_STEP) {
				CURRENT_SENSIBILITY = k;
				// Launch NUM_SIMULATION number of simulation.
				long max = 0;
				for (int i = 0; i < Config.NUM_SIMULATIONS; i++) {
					//System.out.println("Simulation n¡" + i);
					// Run One Time Simu.
					while(!simulation());
					mSimuEnd[i] = mNbTick;
					if(max < mNbTick && mNbTick != Config.UNFINISHED && Config.UNFINISHED != 0)
						max = mNbTick;
					// Reset.
					resetSimu();
					resetSwarm();
				}
				
				double average = 0;
				long nbUnFinished = 0;
				for (int j = 0; j < mSimuEnd.length; j++) {
					if(mSimuEnd[j] >= Config.UNFINISHED && Config.UNFINISHED != 0) {
						nbUnFinished++;
					} else {
						average += mSimuEnd[j];
					}
				}

				average /= mSimuEnd.length - nbUnFinished;
				
				double stdDev = 0;
				for (int i = 0; i < mSimuEnd.length; i++) {
					if(mSimuEnd[i] < Config.UNFINISHED && Config.UNFINISHED != 0) {
						stdDev += Math.pow(mSimuEnd[i] - average, 2);
					}
					
				}
				stdDev /= mSimuEnd.length-nbUnFinished;
				stdDev = Math.sqrt(stdDev);
				
				// Some tests output
				/*
				System.out.println("############## Global Results ################");
				System.out.println("Sensibility : " + Config.SENSIBILITY + ", Number of runs : " + Config.NUM_SIMULATIONS);
				System.out.println("Fail: " + nbUnFinished + ", Max : " + max);
				System.out.println("Average : " + average + ", Standard Deviation : " + stdDev);
				*/
				// Uncomment this if you are in the loop.
				System.out.println(Config.NUM_SIMULATIONS + ", " + k + ", " + average + ", " + stdDev + "," + nbUnFinished + "," + max );
				nbUnFinished = 0;
			}
		} else {
			/*final AppWindow window = */new AppWindow(mSwarm);
			mTimer = new Timer();
			mTask = new SwarmUpdateTask();
			mTimer.scheduleAtFixedRate(mTask, 0, Config.CYCLE_PERIOD);
		}
	}
	
	@Override
	public void reportFlash() {
		mNbFlashAtTick++;
		mMaxFlashAtTick = mNbFlashAtTick > mMaxFlashAtTick ? mNbFlashAtTick : mMaxFlashAtTick;
	}
	
	public static void main(String args[]) {
		final App app = new App();
		app.launchSimu();
	}

	

}
