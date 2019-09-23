package nl.sogyo.mancala.bowl;

import nl.sogyo.mancala.Player;

public abstract class Bowl {
	protected final Player owner;
	protected int beads;
	protected Bowl neighbor;
	
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
		return getNeighbor(1);
	}
	
	/**
	 * @param neighborNumber The how many-th neighbor (0 means this).
	 * @return The neighborNumber-th neighbor of this bowl.
	 */
	public Bowl getNeighbor(int neighborNumber) {
		Bowl currentBowl = this;
		for(int i = 0; i < neighborNumber; i++) {
			currentBowl = currentBowl.neighbor;
		}
		return currentBowl;
	}
	
	/**
	 * @return The player that won; null if it is a tie.
	 * @throws IllegalStateException When the game has not ended yet.
	 */
	public Player getWinner() throws IllegalStateException {
		if(!owner.gameHasEnded()) {
			throw new IllegalStateException("There is no winner yet as the game has not ended");
		}
		return findPlayerWithBiggestKalaha(null);
	}
	
	protected abstract Player findPlayerWithBiggestKalaha(Kalaha currentLargest);
	
	/**
	 * Distributes beads over the neighboring bowls one at a time.
	 * @param beadsToDistribute The amount of beads that are to be distributed over the same amount of bowls.
	 * @param distributor The player who owns the bowl to which the beads belong.
	 */
	protected void distribute(int beadsToDistribute, Player distributor) {
		int newBeadsToDistribute = takeBead(beadsToDistribute, distributor);
		if(newBeadsToDistribute > 0) {
			neighbor.distribute(newBeadsToDistribute, distributor);
		} else {
			endOfTurn(distributor);
		}
	}
	
	/**
	 * Takes one of the given beads and puts it in itself.
	 * @param totalBeads The number of beads to take one from.
	 * @param beadsOriginalOwner The player who owned the beads.
	 * @return The number of beads that remain after one has been taken.
	 */
	protected int takeBead(int totalBeads, Player beadsOriginalOwner) {
		beads++;
		return totalBeads - 1;
	}
	
	public boolean playerHasTurn(Player player) {
		return !allHousesFromPlayerAreEmpty(player, 0);
	}
	
	/**
	 * Checks whether all the houses from a particular player are empty to see if they still can make a move. This is done recursively by checking each house and passing to its neighbor
	 * @param player The player whose houses are to be checked.
	 * @param kalahasPassed The number of kalahas that have been passed. When two kalahas have been passed one can be sure that all houses have been checked.
	 * @return
	 */
	protected abstract boolean allHousesFromPlayerAreEmpty(Player player, int kalahasPassed);
	
	/**
	 * Transfer the beads to a kalaha.
	 * @param beads The number of beads to be transferred.
	 * @param owner The owner of the kalaha that the beads are to be placed in.
	 */
	protected abstract void putBeadsInKalaha(int beads, Player owner);
	
	/**
	 * Performs the check and corresponding actions for when a players turn ends.
	 * @param distributor The player who is performing the current turn.
	 */
	protected abstract void endOfTurn(Player distributor);
	
	public abstract Bowl getOpposite();
	
	/**
	 * Counts the number of houses from and including the current bowl until it reaches itself again or null.
	 * @param owner The player who owns the houses to be counted.
	 * @return The number of houses owned by owner.
	 */
	public int countHousesFromPlayer(Player owner) {
		return countHousesFromPlayerStartingAt(this, owner);
	}
	protected int countHousesFromPlayerStartingAt(Bowl initialBowl, Player owner) {
		int houseValue = (isHouse() && this.owner == owner ? 1 : 0); //if it is an owner's house we count it once, otherwise we don't count it
		
		if(neighbor == initialBowl || neighbor == null) {
			return houseValue;
		} else {
			return neighbor.countHousesFromPlayerStartingAt(initialBowl, owner) + houseValue;
		}
	}
	
	/**
	 * Counts the number of kalahas from and including the current Bowl until it reaches itself again or null.
	 * @param owner The player who owns the kalahas to be counted.
	 * @return The number of kalahas owned by owner.
	 */
	public int countKalahasFromPlayer(Player owner) {
		return countKalahasFromPlayerStartingAt(this, owner);
	}
	protected int countKalahasFromPlayerStartingAt(Bowl initialBowl, Player owner) {
		int kalahaValue = (isKalaha() && this.owner == owner ? 1 : 0); //if it is an owner's kalaha we count it once, otherwise we don't count it
		
		if(neighbor == initialBowl || neighbor == null) {
			return kalahaValue;
		} else {
			return neighbor.countKalahasFromPlayerStartingAt(initialBowl, owner) + kalahaValue;
		}
	}
	
	public boolean isHouse() {
		return false;
	}
	
	public boolean isKalaha() {
		return false;
	}
}
