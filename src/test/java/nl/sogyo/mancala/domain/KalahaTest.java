package nl.sogyo.mancala.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.sogyo.mancala.Player;
import nl.sogyo.mancala.bowl.Bowl;
import nl.sogyo.mancala.bowl.House;
import nl.sogyo.mancala.bowl.Kalaha;

class KalahaTest {

	private Player player;
	private House house;
	
	@BeforeEach
	public void setUpBoard() {
		setUpBoard(House.DEFAULT_INITIAL_BEADS, House.DEFAULT_HOUSES_PER_SIDE);
	}
	public void setUpBoard(int initialBeads) {
		setUpBoard(initialBeads, House.DEFAULT_HOUSES_PER_SIDE);
	}
	public void setUpBoard(int initialBeads, int housesPerSide) {
		player = new Player();
		house = new House(player, initialBeads, housesPerSide);
	}
	
	@Test
	public void everyPlayerHasOneKalaha() {
		Bowl currentBowl = house;
		int playerKalahas = 0;
		int opponentKalahas = 0;
		
		do {
			if(currentBowl instanceof Kalaha) {
				if(currentBowl.getOwner() == player) {
					playerKalahas++;
				}
				if(currentBowl.getOwner() == player.getOpponent()) {
					opponentKalahas++;
				}
			}
			currentBowl = currentBowl.getNeighbor();
		} while(currentBowl != house);
		
		assertEquals(playerKalahas, 1, "The player does not have exactly one kalaha");
		assertEquals(opponentKalahas, 1, "The opponent does not have exactly one kalaha");
	}
	
	@Test
	public void ownKalahaIsReachedButOpponentKalahaIsSkipped() {
		setUpBoard(4, 2);
		house.startDistribute(player); //start from last house, places one bead in Kalaha, 1 in both opponent's houses, and the last bead in the first own house
		
		Kalaha playerKalaha = (Kalaha)house.getNeighbor();
		Kalaha opponentKalaha = (Kalaha)playerKalaha.getNeighbor().getNeighbor().getNeighbor();
		
		assertEquals(playerKalaha.getBeads(), 1, "No bead was placed in own Kalaha");
		assertEquals(opponentKalaha.getBeads(), 0, "A bead was placed in opponent's Kalaha");
	}
	
	@Test
	public void turnDoesNotSwitchAfterEndInOwnKalaha() {
		setUpBoard(1);
		house.startDistribute(player); //distribution ends in own kalaha
		
		assertTrue(player.isTurn(), "Player did not have another turn while finishing in their own kalaha");
	}
}
