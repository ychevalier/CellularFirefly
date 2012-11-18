package com.bham.naturalcomp.cellularfirefly.app;

public class Config {
	
	// Here is everything you need to tweak the program, no need to go in other files...
	
	// Hey this is not bullet proof, don't try too much or negative numbers...
	public static final int NUMBER_OF_COLUMNS = 3;
	public static final int NUMBER_OF_ROWS = 3;
	
	// The number of millisecond per cycle (there is 10 cycles before flashing)
	// useful only in non-batch mode
	public static final int CYCLE_PERIOD = 100;

	// If it is not moore (add diagonal neighbour) it is the standard
	// one, with edges, known as Von Neumann.
	public static final boolean MOORE_NEIGHBOURHOOD = true;
	
	// Probability (in percentage) that a firefly reset after a neighbour flashes.
	// The 3 values are used to create lot of simulation without having
	// to restart each time.
	public static final int SENSIBILITY_MAX = 100;
	public static final int SENSIBILITY_MIN = 100;
	public static final int SENSIBILITY_STEP = 1;
	
	// In batch mode, you can run several simulation with the same
	// parameters. You can choose below the number. The display is disabled.
	public static final boolean BATCH_MODE = true;
	public static final int NUM_SIMULATIONS = 1000;
	
	// Consider never ending after * iteration (0 is never)
	public static final int UNFINISHED = 100000;
}
