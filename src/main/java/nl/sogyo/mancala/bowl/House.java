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
			currentBowl = currentBowl.getNeighbor();
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
	
	public void startDistribute(Player distributor) throws IllegalArgumentException {
		if(distributor != owner) {
			throw new IllegalArgumentException("The distributor does not own the house to play");
		}
		
		int startingBeads = beads; //we cannot empty this bowl after the distribution in case the distribution makes a full circle
		beads = 0;
		neighbor.distribute(startingBeads, distributor);
	}
	
	@Override
	protected void endOfTurn(Player distributor) {
		if(wasEmpty() && distributor == owner) {
			stealBeads(distributor);
			((House)getOpposite()).stealBeads(distributor);
		}
		distributor.switchTurn();
		if(!playerHasTurn(distributor.getOpponent())) {
			distributor.endGame();
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
