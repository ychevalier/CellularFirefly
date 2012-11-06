package com.bham.naturalcomp.cellularfirefly.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.bham.naturalcomp.cellularfirefly.interfaces.FireflyDisplay;

public class Firefly {
	
	public static final int NUMBER_OF_PERIODS = 10;
	// In Milliseconds.
	public static final int DURATION_OF_PERIODS = 100;
	
	public static final int STATE_INIT = 0;
	public static final int STATE_TRANSITION_CHARGING_NONSENSITIVE = 7;
	public static final int STATE_FLASH = 9;
	
	/**
	 * Timer needs to be static,
	 * it is better and mandatory if you want a lot of fireflies.
	 * This design is more far from nature when every firefly has their own timer...
	 */
	private static Timer Timer;
	
	private ArrayList<Firefly> mNeighbours;
	private int mNextState;
	private FireflyTask mTask;
	private FireflyDisplay mDisplay;
	
	public Firefly() {
		mNeighbours = new ArrayList<Firefly>();
		if(Timer == null) {
			Timer = new Timer();
		}
	}
	
	public void addNeighbour(Firefly f) {
		mNeighbours.add(f);
	}
	
	public void setDisplay(FireflyDisplay display) {
		mDisplay = display;
	}
	
	public void launchFirefly() {
		killFirefly();
		mTask = new FireflyTask();
		//mCurrentState = STATE_INIT;
		Random gen = new Random();
		mNextState = Math.abs(gen.nextInt()) % NUMBER_OF_PERIODS;
		Timer.scheduleAtFixedRate(mTask, 0, DURATION_OF_PERIODS);
	}
	
	public void killFirefly() {
		if(mTask != null) {
			mTask.cancel();
		}
	}
	
	private class FireflyTask extends TimerTask {
		@Override
		public void run() { 
			if(mNextState == STATE_FLASH) {
				flash();
			}
			// Little hack for synchro
			else if(mNextState == STATE_INIT)
			{
				stopFlashing();
			}

			if(mDisplay != null) {
				mDisplay.updateState(mNextState);
			}
			
			mNextState = (mNextState + 1) % NUMBER_OF_PERIODS;
		}
	}
	
	protected void flash() {
		
		if(mDisplay != null) {
			mDisplay.displayFlash();
		}
		
		// Prevent Neighbours
		for (Firefly f : mNeighbours) {
			// Maybe give my id...
			if(f != null) {
				f.neighbourHasFlashed();
			}
		}
		
	}
	
	protected void stopFlashing() {
		if(mDisplay != null) {
			mDisplay.stopFlash();
		}
	}
	
	// Do I need synchronized???
	protected synchronized void neighbourHasFlashed() {
		int currentState = mNextState == 0 ? 9 : mNextState - 1;
		if(currentState >= STATE_INIT 
				&& currentState < STATE_TRANSITION_CHARGING_NONSENSITIVE) {
			mNextState = STATE_INIT;
		}
	}

}
