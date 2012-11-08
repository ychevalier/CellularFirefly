package com.bham.naturalcomp.cellularfirefly.app;

import java.util.Timer;
import java.util.TimerTask;

import com.bham.naturalcomp.cellularfirefly.io.FileHelper;
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
	
	private class SwarmUpdateTask extends TimerTask {
		@Override
		public void run() {
			// Run the simulation until the boolean says its over.
			// Then cancel it...
			if(simulation()) {
				if(Config.WRITE_IN_FILE) {
					FileHelper.closeCurrentFile();
				}
				cancel();
			}
		}
	}
	
	private boolean simulation() {
		float percentageFlash = (float)mNbFlashAtTick/((float)Config.NUMBER_OF_COLUMNS * (float)Config.NUMBER_OF_ROWS) * 100;
		
		if(Config.WRITE_IN_FILE) {
			FileHelper.writeNextLine(mNbTick + ", " + percentageFlash + "%");
		} else if(!Config.REMOVE_ZEROS || mNbFlashAtTick > 0) {
			System.out.println(mNbTick + ", " + percentageFlash + "%");
		}
		
		if(percentageFlash >= Config.STOP_CRITERION) {
			mCurrentTimesCriterion++;
		} else if(percentageFlash > 0) {
			// Obviously, it has to be consecutive except when the percentage is 0.0 (non flashing)
			mCurrentTimesCriterion = 0;
		}
		
		if(mCurrentTimesCriterion >= Config.NUM_TIMES_CRITERION) {
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
			
			// Launch NUM_SIMULATION number of simulation.
			for (int i = 0; i < Config.NUM_SIMULATIONS; i++) {
				if(!Config.WRITE_IN_FILE) {
					System.out.println("Simulation " + i);
					System.out.println("Tick, % of Sync");
				} else {
					if(Config.FILE_PATH != null && !Config.FILE_PATH.equals("")) {
						Config.WRITE_IN_FILE = FileHelper.createFile(Config.FILE_PATH + "_" + i + ".csv");
					} else {
						Config.WRITE_IN_FILE = FileHelper.createFile("Simulation_" + i + ".csv");
					}
					FileHelper.writeNextLine("Tick, % of Sync");
				}
				// Run One TIme Simu.
				while(!simulation());
				// Reset.
				resetSimu();
				resetSwarm();
				if(Config.WRITE_IN_FILE) {
					FileHelper.closeCurrentFile();
				}
			}

		} else {
			/*final AppWindow window = */new AppWindow(mSwarm);
			if(!Config.WRITE_IN_FILE) {
				System.out.println("Tick, % of Sync");
			} else {
				if(Config.FILE_PATH != null && !Config.FILE_PATH.equals("")) {
					Config.WRITE_IN_FILE = FileHelper.createFile(Config.FILE_PATH);
				} else {
					Config.WRITE_IN_FILE = FileHelper.createFile("Simulation");
				}
				FileHelper.writeNextLine("Tick, % of Sync");
			}
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
