package nl.sogyo.mancala.bowl;

import nl.sogyo.mancala.Player;

public abstract class Bowl {
	private final Player owner;
	private int beads;
	private Bowl neighbor;
	
	protected Bowl(Player owner) {
		this(owner, 0, null);
	}
	protected Bowl(Player owner, int beads) {
		this(owner, beads, null);
	}
	protected Bowl(Player owner, int beads, Bowl neighbor) {
		this.owner = owner;
		this.beads = beads;
		this.neighbor = neighbor;
	}
	
	public int getBeads() {
		return beads;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public Bowl getNeighbor() {
		return neighbor;
	}
	
	public static Bowl createBowls(Player owner, int initialBeads, int housesPerSide) {
		return new House(owner, initialBeads);
	}
}
