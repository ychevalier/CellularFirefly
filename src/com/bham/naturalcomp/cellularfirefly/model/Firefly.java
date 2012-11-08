package com.bham.naturalcomp.cellularfirefly.model;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

import com.bham.naturalcomp.cellularfirefly.app.Config;
import com.bham.naturalcomp.cellularfirefly.listener.FireflyDisplay;
import com.bham.naturalcomp.cellularfirefly.listener.FlashCounter;

public class Firefly {
	
	private static final int NUMBER_OF_PERIODS = 10;
	
	private static final int STATE_INIT = 0;
	private static final int STATE_TRANSITION_CHARGING_NONSENSITIVE = 7;
	private static final int STATE_FLASH = 9;
	
	private ArrayList<Firefly> mNeighbours;
	private int mCurrentState;
	private boolean mNeedReset;
	private Random mGenerator;

	private WeakReference<FireflyDisplay> mDisplay;
	private WeakReference<FlashCounter> mCounter;
	
	public Firefly() {
		mNeighbours = new ArrayList<Firefly>();
		mGenerator = new Random();
		mCurrentState = Math.abs(mGenerator.nextInt()) % NUMBER_OF_PERIODS;
		mNeedReset = false;
	}
	
	public void reset() {
		mCurrentState = Math.abs(mGenerator.nextInt()) % NUMBER_OF_PERIODS;
		mNeedReset = false;
	}
	
	public void addNeighbour(Firefly f) {
		mNeighbours.add(f);
	}
	
	public void setDisplay(FireflyDisplay display) {
		mDisplay = new WeakReference<FireflyDisplay>(display);
	}
	
	public void setCounter(FlashCounter counter) {
		mCounter = new WeakReference<FlashCounter>(counter);
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
	}
	
	protected void flash() {
		
		if(mDisplay != null && mDisplay.get() != null) {
			mDisplay.get().displayFlash();
		}
		if(mCounter != null && mCounter.get() != null) {
			mCounter.get().reportFlash();
		}
		
		// Prevent Neighbours
		for (Firefly f : mNeighbours) {
			if(f != null) {
				f.neighbourHasFlashed();
			}
		}
		
	}
	
	protected void stopFlashing() {
		if(mDisplay != null && mDisplay.get() != null) {
			mDisplay.get().stopFlash();
		}
	}

	protected void neighbourHasFlashed() {
		if(mCurrentState >= STATE_INIT 
				&& mCurrentState < STATE_TRANSITION_CHARGING_NONSENSITIVE) {
			if((Math.abs(mGenerator.nextInt()) % 100) < Config.SENSIBILITY) {
				mNeedReset = true;
			}
			
		}
	}

}
