package com.bham.naturalcomp.cellularfirefly.app;

import java.util.Timer;
import java.util.TimerTask;

import com.bham.naturalcomp.cellularfirefly.listener.FlashCounter;
import com.bham.naturalcomp.cellularfirefly.model.Firefly;
import com.bham.naturalcomp.cellularfirefly.view.AppWindow;

public class App implements FlashCounter {
	
	private static final int NUMBER_OF_COLUMNS = Config.NUMBER_OF_COLUMNS;
	private static final int NUMBER_OF_ROWS = Config.NUMBER_OF_ROWS;
	
	private static final int CYCLE_PERIOD = Config.CYCLE_PERIOD;
	
	private static final boolean MOORE_NEIGHBOURHOOD = Config.MOORE_NEIGHBOURHOOD;
	
	// If we want the 0.0% (non flashing) state in the final csv.
	private static final boolean REMOVE_ZEROS = Config.REMOVE_ZEROS;
	
	// The percentage of synchronized firefly before stopping.
	private static final float STOP_CRITERION = Config.STOP_CRITERION;
	
	// The number of time we want to have our criterion consecutively.
	private static final int NUM_TIMES_CRITERION = Config.NUM_TIMES_CRITERION;
	
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
	
	private class SwarmUpdateTask extends TimerTask {
		@Override
		public void run() {
			if(simulation()) {
				cancel();
			}
		}
	}
	
	private boolean simulation() {
		float percentageFlash = (float)mNbFlashAtTick/((float)NUMBER_OF_COLUMNS * (float)NUMBER_OF_ROWS) * 100;
		
		if(!REMOVE_ZEROS || mNbFlashAtTick > 0) {
				System.out.println(mNbTick + ", " + percentageFlash + "%");
		}
		
		if(percentageFlash >= STOP_CRITERION) {
			mCurrentTimesCriterion++;
		} else if(percentageFlash > 0) {
			// Obviously, it has to be consecutive except when the percentage is 0.0 (non flashing)
			mCurrentTimesCriterion = 0;
		}
		
		if(mCurrentTimesCriterion >= NUM_TIMES_CRITERION) {
			return SIMU_IS_OVER;
		}
		
		mNbTick++;
		mNbFlashAtTick = 0;
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
		
		return SIMU_IS_RUNNING;
	}
	
	private void resetSimu() {
		mNbFlashAtTick = 0;
		mMaxFlashAtTick = 0;
		mNbTick = 0;
		mCurrentTimesCriterion = 0;
	}
	
	private void resetSwarm() {
		for(int i = 0; i < NUMBER_OF_COLUMNS; i++) {
			for (int j = 0; j < NUMBER_OF_ROWS; j++) {
				mSwarm[i][j].reset();
			}
		}
	}
	
	protected App() {
		resetSimu();
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
				if(MOORE_NEIGHBOURHOOD) {
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
				//*/
			}
		}
	}
	
	private void launchSimu() {
		if(Config.BATCH_MODE) {
			for (int i = 0; i < Config.NUM_SIMULATIONS; i++) {
				System.out.println("Simulation " + i);
				System.out.println("Tick, % of Sync");
				while(!simulation());
				resetSimu();
				resetSwarm();
			}
		} else {
			/*final AppWindow window = */new AppWindow(mSwarm);
			System.out.println("Tick, % of Sync");
			mTimer = new Timer();
			mTask = new SwarmUpdateTask();
			mTimer.scheduleAtFixedRate(mTask, 0, CYCLE_PERIOD);
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
