package nl.sogyo.mancala.bowl;

import nl.sogyo.mancala.Player;

public class House extends Bowl {
	
	public static final int DEFAULT_HOUSES_PER_SIDE = 6;
	public static final int DEFAULT_INITIAL_BEADS = 4;
	
	/**
	 * Creates the houses and kalahas. The number of houses is determined by DEFAULT_HOUSES_PER_SIDE and the number of beads in each house is DEFAULT_INITIAL_BEADS.
	 * @param owner The owner of the houses on one side.
	 */
	public House(Player owner) {
		this(owner, DEFAULT_INITIAL_BEADS);
	}
	/**
	 * Creates the houses and kalahas. The number of houses is determined by DEFAULT_HOUSES_PER_SIDE.
	 * @param owner The owner of the houses on one side.
	 * @param beads The number of beads to be placed in each house.
	 */
	public House(Player owner, int beads) {
		this(owner, beads, DEFAULT_HOUSES_PER_SIDE);
	}
	/**
	 * Creates the houses and kalahas.
	 * @param owner The owner of the houses on one side.
	 * @param beads The number of beads to be placed in each house.
	 * @param housesPerSide How many houses each player should have.
	 */
	public House(Player owner, int beads, int housesPerSide) {
		this(owner, beads, null, housesPerSide);
	}
	/**
	 * Creates recursively the houses on one side and a kalaha; kalaha will initialize the creation of the other side of a board.
	 * @param owner The owner of the houses on one side.
	 * @param beads The number of beads to be placed in each house.
	 * @param neighbor The neighboring bowl of this house.
	 * @param housesPerSide How many houses each player should have.
	 */
	protected House(Player owner, int beads, Bowl neighbor, int housesPerSide) {
		super(owner, beads, neighbor);
		int numberOfHousesFromOwner = countHousesFromPlayer(owner);
		if(numberOfHousesFromOwner < housesPerSide) {
			new House(owner, beads, this, housesPerSide);
		} else {
			new Kalaha(owner.getOpponent(), beads, this, housesPerSide);
		}
		
		if(beads == 0) {
			owner.endGame();
		}
	}
	
	@Override
	protected Player findPlayerWithBiggestKalaha(Kalaha currentLargest) {
		return neighbor.findPlayerWithBiggestKalaha(currentLargest);
	}
	
	@Override
	protected boolean allHousesFromPlayerAreEmpty(Player player, int kalahasPassed) {
		if(owner == player && beads > 0) {
			return false;
		} else {
			return neighbor.allHousesFromPlayerAreEmpty(player, kalahasPassed);
		}
	}
	
	/**
	 * Play the current house. That is, take the beads from this and distribute them over the neighboring bowls.
	 * @param player The player who plays.
	 * @throws IllegalArgumentException When the player does not own the house to be played.
	 * @throws IllegalStateException When it is not the players turn to play a house.
	 */
	public void play(Player player) throws IllegalArgumentException, IllegalStateException {
		if(player != owner) {
			throw new IllegalArgumentException("The player does not own the house to play");
		}
		if(!player.hasTurn()) {
			throw new IllegalStateException("It is not the player's turn");
		}
		
		int beadsToDistribute = beads; //we cannot empty this bowl after the distributing in case it makes a full circle
		beads = 0;
		neighbor.distribute(beadsToDistribute, player);
	}
	
	@Override
	protected void endOfTurn(Player player) {
		if(wasEmpty() && player == owner) {
			stealBeads(player);
			((House)getOpposite()).stealBeads(player);
		}
		player.switchTurn();
		if(!playerHasTurn(player.getOpponent())) {
			player.endGame();
		}
	}
	
	private boolean wasEmpty() {
		return beads == 1;
	}
	
	/**
	 * Takes the beads from this house and places them in the kalaha of the thief.
	 * @param thief The player who steals the beads from this house.
	 */
	private void stealBeads(Player thief) {
		neighbor.putBeadsInKalaha(beads, thief);
		beads = 0;
	}
	
	@Override
	protected void putBeadsInKalaha(int beads, Player owner) {
		neighbor.putBeadsInKalaha(beads, owner);
	}
	
	@Override
	public Bowl getOpposite() {
		return neighbor.getOpposite().getNeighbor();
	}
	
	@Override
	public boolean isHouse() {
		return true;
	}
}
