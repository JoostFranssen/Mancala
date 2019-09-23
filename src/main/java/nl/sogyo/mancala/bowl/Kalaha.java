package nl.sogyo.mancala.bowl;

import nl.sogyo.mancala.player.Player;

public class Kalaha extends Bowl {
	public static final int NUMBER_OF_KALAHAS = 2;
	
	/**
	 * Creates a kalaha and continue to creating more houses, if necessary, or connects the last house to itself to finish the board.
	 * @param owner The owner of this kalaha.
	 * @param beads The number of beads to be placed in each house. The kalaha always received zero beads.
	 * @param neighbor The neighbor of this kalaha.
	 * @param housesPerSide The number of houses each player should have.
	 */
	protected Kalaha(Player owner, int beads, Bowl neighbor, int housesPerSide) {
		super(owner, 0, neighbor);
		int numberOfKalahas = countKalahasFromPlayer(owner.getOpponent());
		if(numberOfKalahas > 0) { //there are enough kalahas, so we can connect the last house to this kalaha
			Bowl currentBowl = this;
			while(currentBowl.neighbor != null) {
				currentBowl = currentBowl.neighbor;
			}
			currentBowl.neighbor = this;
		} else { //we still need another kalaha and therefore also another row of houses
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
			return (currentLargest.beads > beads ? currentLargest : this).owner;
		}
	}
	
	@Override
	protected boolean allHousesFromPlayerAreEmpty(Player player, int kalahasPassed) {
		if(kalahasPassed >= NUMBER_OF_KALAHAS) {
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
		if(!playerHasTurn(distributor)) { //player ends in their own kalaha, so they have another turn and we need only check whether they can play
			distributor.endGame();
		}
	}
	
	@Override
	protected int takeBead(int totalBeads, Player beadsOriginalOwner) {
		if(owner == beadsOriginalOwner) { //only take a bead when the kalaha belongs to the player who is distributing the beads
			return super.takeBead(totalBeads, beadsOriginalOwner);
		} else {
			return totalBeads;
		}
	}
	
	@Override
	public Bowl getOpposite() {
		return this;
	}
	
	@Override
	public boolean isKalaha() {
		return true;
	}
}
