package nl.sogyo.mancala.bowl;

import nl.sogyo.mancala.Player;

public abstract class Bowl {
	protected final Player owner;
	protected int beads;
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
	
	//returns null for a draw, throws IllegalStateException when the game has not yet ended
	public Player getWinner() throws IllegalStateException {
		if(!owner.gameHasEnded()) {
			throw new IllegalStateException("There is no winner yet as the game has not ended");
		}
		
		return findPlayerWithBiggestKalaha(null);
	}
	
	protected abstract Player findPlayerWithBiggestKalaha(Kalaha currentLargest);
	
	protected void distribute(int beadsToDistribute, Player distributor) {
		int newBeadsToDistribute = putBead(beadsToDistribute, distributor);
		if(newBeadsToDistribute > 0) {
			neighbor.distribute(newBeadsToDistribute, distributor);
		} else {
			endOfTurn(distributor);
		}
	}
	
	protected int putBead(int totalBeads, Player beadsOrigin) {
		beads++;
		return totalBeads - 1;
	}
	
	public boolean playerHasTurn(Player player) {
		return !allHousesFromPlayerAreEmpty(player, 0);
	}
	
	protected abstract boolean allHousesFromPlayerAreEmpty(Player player, int kalahasPassed);
	
	protected abstract void putBeadsInKalaha(int beads, Player owner);
	
	protected abstract void endOfTurn(Player distributor);
	
	public abstract Bowl getOpposite();
}
