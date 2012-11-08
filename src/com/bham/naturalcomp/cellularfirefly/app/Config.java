package com.bham.naturalcomp.cellularfirefly.app;

public class Config {
	
	// Here is everything you need to tweak the program, ne need to go in other files...
	
	public static final int NUMBER_OF_COLUMNS = 3;
	public static final int NUMBER_OF_ROWS = 3;
	
	// The number of millisecond per cycle (there is 10 cycles before flashing)
	public static final int CYCLE_PERIOD = 50;
	
	public static final boolean MOORE_NEIGHBOURHOOD = true;
	
	// If we want the 0.0% (non flashing) state in the final csv.
	public static final boolean REMOVE_ZEROS = true;
	
	// The percentage of synchronized firefly before stopping.
	public static final float STOP_CRITERION = (float) 100.0;
	
	// The number of time we want to have our criterion consecutively.
	public static final int NUM_TIMES_CRITERION = 3;
	
	// Probability (in percentage) that a firefly reset after a neighbour flashes.
	public static final int SENSIBILITY = 70;
	
	// In batch mode, you can run several simulation with the same
	// parameters. You can choose below the number. The display is disabled.
	public static final boolean BATCH_MODE = true;
	
	public static final int NUM_SIMULATIONS = 10;
	
	// If you want to have the output written
	// in a file instead of console output (better if multiple simulations...)
	// THe ouptput is always CSV style.
	// Not final in case you mess up with the name, or can't write on the file...
	public static boolean WRITE_IN_FILE = true;
	// the CSV extension will be added automatically.
	public static final String FILE_PATH = "Simu";
	
	

}
