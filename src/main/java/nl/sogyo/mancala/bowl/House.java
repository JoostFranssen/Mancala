package nl.sogyo.mancala.bowl;

import nl.sogyo.mancala.Player;

public class House extends Bowl {
	
	public static final int DEFAULT_HOUSES_PER_SIDE = 6;
	public static final int DEFAULT_INITIAL_BEADS = 4;
	
	public House(Player owner) {
		this(owner, DEFAULT_INITIAL_BEADS);
	}
	public House(Player owner, int beads) {
		this(owner, beads, DEFAULT_HOUSES_PER_SIDE);
	}
	public House(Player owner, int beads, int housesPerSide) {
		this(owner, beads, null, housesPerSide);
	}
	//creates housesPerSide houses for each player (owner and owner.getOpponent()), and one Kalaha for each player
	//the House object created here is the house owned by owner with neighbor the owner's Kalaha
	protected House(Player owner, int beads, Bowl neighbor, int housesPerSide) {
		super(owner, beads, neighbor);
		int neighboringHouses = 0;
		Bowl currentBowl = this;
		while(currentBowl instanceof House && neighboringHouses < housesPerSide) {
			currentBowl = currentBowl.neighbor;
			neighboringHouses++;
		}
		if(neighboringHouses < housesPerSide) {
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
	
	public void play(Player player) throws IllegalArgumentException, IllegalStateException {
		if(player != owner) {
			throw new IllegalArgumentException("The player does not own the house to play");
		}
		if(!player.isTurn()) {
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
}
