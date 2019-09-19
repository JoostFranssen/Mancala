package nl.sogyo.mancala.bowl;

import nl.sogyo.mancala.Player;

public class Kalaha extends Bowl {
	public static final int NUMBER_OF_KALAHAS = 2;

	protected Kalaha(Player owner) {
		super(owner);
	}
	protected Kalaha(Player owner, int beads) {
		super(owner, beads);
	}
	public Kalaha(Player owner, int beads, Bowl neighbor) {
		super(owner, beads, neighbor);
	}
	
	@Override
	protected Player findPlayerWithBiggestKalaha(Kalaha currentLargest) {
		if(currentLargest == null) {
			return neighbor.findPlayerWithBiggestKalaha(this);
		} else {
			if(currentLargest.getBeads() == beads) {
				return null;
			} else {
				return currentLargest.getBeads() > beads ? currentLargest.owner : this.owner;
			}
		}
	}
	
	@Override
	protected boolean allHousesFromPlayerAreEmpty(Player player, int kalahasPassed) {
		if(kalahasPassed >= 2) {
			return true;
		}
		return neighbor.allHousesFromPlayerAreEmpty(player, kalahasPassed + 1);
	}
	
	@Override
	protected void putBeadsInKalaha(int beads, Player owner) {
		if(owner == this.owner) {
			this.beads += beads;
		} else {
			neighbor.putBeadsInKalaha(beads, owner);
		}
	}
	
	@Override
	protected void endOfTurn(Player distributor) {
		if(!playerHasTurn(distributor)) {
			distributor.endGame();
		}
	}
	
	@Override
	protected int putBead(int totalBeads, Player beadsOrigin) {
		if(owner == beadsOrigin) {
			return super.putBead(totalBeads, beadsOrigin);
		} else {
			return totalBeads;
		}
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
