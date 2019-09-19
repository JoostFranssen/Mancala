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
	
	@Override
	public Bowl getOpposite() {
		return this;
	}
	
	@Override
	protected void createNeighbor(Player owner, int initialBeads, int housesPerSide, int housesLeft, int kalahasLeft, Bowl initialHouse) {
		if(kalahasLeft > 0) {
			neighbor = new House(owner.getOpponent(), initialBeads);
			neighbor.createNeighbor(owner.getOpponent(), initialBeads, housesPerSide, housesPerSide - 1, kalahasLeft, initialHouse);
		} else {
			neighbor = initialHouse;
		}
	}

}
