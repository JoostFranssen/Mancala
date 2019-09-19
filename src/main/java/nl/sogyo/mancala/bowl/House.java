package nl.sogyo.mancala.bowl;

import nl.sogyo.mancala.Player;

public class House extends Bowl {

	public House(Player owner) {
		super(owner);
	}
	public House(Player owner, int beads) {
		super(owner, beads);
	}
	public House(Player owner, int beads, Bowl neighbor) {
		super(owner, beads, neighbor);
	}
}
