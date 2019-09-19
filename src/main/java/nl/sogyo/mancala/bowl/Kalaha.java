package nl.sogyo.mancala.bowl;

import nl.sogyo.mancala.Player;

public class Kalaha extends Bowl {

	public Kalaha(Player owner) {
		super(owner);
	}
	public Kalaha(Player owner, int beads) {
		super(owner, beads);
	}
	public Kalaha(Player owner, int beads, Bowl neighbor) {
		super(owner, beads, neighbor);
	}

}
