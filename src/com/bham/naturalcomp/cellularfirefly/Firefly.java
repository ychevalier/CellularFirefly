package com.bham.naturalcomp.cellularfirefly;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Firefly {
	
	public static final int NUMBER_OF_PERIODS = 10;
	// In Milliseconds.
	public static final int DURATION_OF_PERIODS = 100;
	
	public static final int STATE_INIT = 0;
	public static final int STATE_TRANSITION_CHARGING_NONSENSITIVE = 7;
	public static final int STATE_FLASH = 9;
	
	private ArrayList<Firefly> mNeighbours;
	private int mCurrentState;
	private Timer mTimer;
	private FireflyTask mTask;
	
	public Firefly() {
		mNeighbours = new ArrayList<Firefly>();
		mCurrentState = STATE_INIT;
		mTimer = new Timer();
	}
	
	public void addNeighbour(Firefly f) {
		mNeighbours.add(f);
	}
	
	public void launchFirefly() {
		killFirefly();
		mTask = new FireflyTask();
		//mCurrentState = STATE_INIT;
		Random gen = new Random();
		mCurrentState = Math.abs(gen.nextInt()) % NUMBER_OF_PERIODS;
		mTimer.scheduleAtFixedRate(mTask, 0, DURATION_OF_PERIODS);
	}
	
	public void killFirefly() {
		if(mTask != null) {
			mTask.cancel();
		}
	}
	
	private class FireflyTask extends TimerTask {
		@Override
		public void run() {
			switch(mCurrentState) { 
			case STATE_FLASH:
				flash();
				break;
			case STATE_INIT:
				// If we are here, it means that we were flashing, 
				// so we need to stop the last flash...
				stopFlashing();
				break;
			}
			
			System.out.println("### " + mCurrentState);
			mCurrentState = (mCurrentState + 1) % NUMBER_OF_PERIODS;
		}
	}
	
	protected void flash() {
		// Prevent Neighbours
		for (Firefly f : mNeighbours) {
			// Maybe give my id...
			if(f != null) {
				f.neighbourHasFlashed();
			}
		}
		//System.out.println("Flashiing");
		// Display the actual flash...
	}
	
	protected void stopFlashing() {
		//System.out.println("Stop Flashiing");
		// Stop the flash from displaying.
	}
	
	protected void neighbourHasFlashed() {
		if(mCurrentState >= STATE_INIT 
				&& mCurrentState < STATE_TRANSITION_CHARGING_NONSENSITIVE) {
			mCurrentState = STATE_INIT;
		}
	}

}
