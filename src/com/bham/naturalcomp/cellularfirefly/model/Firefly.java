package com.bham.naturalcomp.cellularfirefly.model;

import java.util.ArrayList;
import java.util.Random;

import com.bham.naturalcomp.cellularfirefly.listener.FireflyDisplay;
import com.bham.naturalcomp.cellularfirefly.listener.FlashCounter;

public class Firefly {
	
	public static final int NUMBER_OF_PERIODS = 10;
	
	public static final int STATE_INIT = 0;
	public static final int STATE_TRANSITION_CHARGING_NONSENSITIVE = 7;
	public static final int STATE_FLASH = 9;
	
	private ArrayList<Firefly> mNeighbours;
	private int mCurrentState;
	private boolean mNeedReset;

	private FireflyDisplay mDisplay;
	private FlashCounter mCounter;
	
	public Firefly() {
		mNeighbours = new ArrayList<Firefly>();
		Random gen = new Random();
		mCurrentState = Math.abs(gen.nextInt()) % NUMBER_OF_PERIODS;
		mNeedReset = false;
	}
	
	public void addNeighbour(Firefly f) {
		mNeighbours.add(f);
	}
	
	public void setDisplay(FireflyDisplay display) {
		mDisplay = display;
	}
	
	public void setCounter(FlashCounter counter) {
		mCounter = counter;
	}
	
	public void nextStep() {
		mCurrentState = mNeedReset ? STATE_INIT : (mCurrentState + 1) % NUMBER_OF_PERIODS;
		mNeedReset = false;
	}
	
	public void act() {
		if(mCurrentState == STATE_FLASH) {
			flash();
		}
		// Little hack for synchro
		else if(mCurrentState == STATE_INIT) {
			stopFlashing();
		}
		
		if(mDisplay != null) {
			mDisplay.updateState(mCurrentState);
		}
	}
	
	protected void flash() {
		
		if(mDisplay != null) {
			mDisplay.displayFlash();
		}
		if(mCounter != null) {
			mCounter.reportFlash();
		}
		
		// Prevent Neighbours
		for (Firefly f : mNeighbours) {
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

	protected void neighbourHasFlashed() {
		if(mCurrentState >= STATE_INIT 
				&& mCurrentState < STATE_TRANSITION_CHARGING_NONSENSITIVE) {
			mNeedReset = true;
		}
	}

}
