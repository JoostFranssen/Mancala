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
	
	@Override
	public Bowl getOpposite() {
		return neighbor.getOpposite().getNeighbor();
	}
	
	@Override
	protected void createNeighbor(Player owner, int initialBeads, int housesPerSide, int housesLeft, int kalahasLeft, Bowl initialHouse) {
		if(housesLeft > 0) {
			neighbor = new House(owner, initialBeads);
			neighbor.createNeighbor(owner, initialBeads, housesPerSide, housesLeft - 1, kalahasLeft, initialHouse);
		} else {
			neighbor = new Kalaha(owner, 0);
			neighbor.createNeighbor(owner, initialBeads, housesPerSide, housesLeft, kalahasLeft - 1, initialHouse);
		}
	}
}
