package nl.sogyo.mancala.bowl;

import nl.sogyo.mancala.Player;

public class House extends Bowl {

	protected House(Player owner) {
		super(owner);
	}
	protected House(Player owner, int beads) {
		super(owner, beads);
	}
	public House(Player owner, int beads, Bowl neighbor) {
		super(owner, beads, neighbor);
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
