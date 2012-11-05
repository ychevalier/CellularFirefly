package com.bham.naturalcomp.cellularfirefly;

public class Swarm {
	
	public static void main(String [] args) {
		Firefly f = new Firefly();

		Firefly f2 = new Firefly();
		
		f.addNeighbour(f2);
		f2.addNeighbour(f);
		
		f.launchFirefly();
		f2.launchFirefly();
		
	}

}
