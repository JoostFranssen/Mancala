package nl.sogyo.mancala.bowl;

import nl.sogyo.mancala.Player;

public abstract class Bowl {
	private final Player owner;
	private int beads;
	protected Bowl neighbor;
	
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
	
	public abstract Bowl getOpposite();
	
	public static Bowl createBowls(Player owner, int initialBeads, int housesPerSide) {
		Bowl initialHouse = new House(owner, initialBeads);
		initialHouse.createNeighbor(owner, initialBeads, housesPerSide, housesPerSide - 1, 2, initialHouse);
		return initialHouse;
	}
	protected abstract void createNeighbor(Player owner, int initialBeads, int housesPerSide, int housesLeft, int kalahasLeft, Bowl initialHouse);
}
