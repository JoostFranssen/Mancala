package nl.sogyo.mancala.bowl;

import nl.sogyo.mancala.Player;

public class Kalaha extends Bowl {
	public static final int NUMBER_OF_KALAHAS = 2;
	
	protected Kalaha(Player owner, int beads, Bowl neighbor, int housesPerSide) {
		super(owner, 0, neighbor);
		boolean foundKalaha = false;
		Bowl currentBowl = this;
		while(currentBowl.neighbor != null) {
			currentBowl = currentBowl.neighbor;
			foundKalaha |= (currentBowl instanceof Kalaha);
		}
		if(foundKalaha) {
			currentBowl.neighbor = this;
		} else {
			new House(owner, beads, this, housesPerSide);
		}
	}
	
	@Override
	protected Player findPlayerWithBiggestKalaha(Kalaha currentLargest) {
		if(currentLargest == null) {
			return neighbor.findPlayerWithBiggestKalaha(this);
		}
		if(currentLargest.beads == beads) {
			return null; //it is a draw, so there is no winner
		} else {
			return (currentLargest.getBeads() > beads ? currentLargest : this).owner;
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
	protected int takeBead(int totalBeads, Player beadsOriginalOwner) {
		if(owner == beadsOriginalOwner) {
			return super.takeBead(totalBeads, beadsOriginalOwner);
		} else {
			return totalBeads;
		}
	}
	
	@Override
	public Bowl getOpposite() {
		return this;
	}
}
