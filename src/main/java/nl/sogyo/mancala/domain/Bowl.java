package nl.sogyo.mancala.domain;

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
		if(neighborNumber > 0) {
			return neighbor.getNeighbor(neighborNumber - 1);
		}
		return this;
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
	 * Counts the number of bowls from and including the current bowl until it reaches itself again or null
	 * @return
	 */
	public int countBowlsFromPlayer(Player owner) {
		return countBowlsFromPlayerStartingAt(this, owner);
	}
	private int countBowlsFromPlayerStartingAt(Bowl initialBowl, Player owner) {
		int value = (this.owner == owner ? 1 : 0);
		
		if(neighbor == initialBowl || neighbor == null) {
			return value;
		} else {
			return neighbor.countBowlsFromPlayerStartingAt(initialBowl, owner) + value;
		}
	}
}
