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
	public void setUp() {
		setUp(4);
	}
	public void setUp(int initialBeads) {
		player = new Player();
		house = (House)((new House(player.getOpponent(), initialBeads, House.DEFAULT_HOUSES_PER_SIDE)).getNeighbor().getNeighbor());
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
	public void turnDoesNotSwitchAfterEndInOwnKalaha() {
		setUp(6); //6 beads in each house
		house.startDistribute(player); //distribution ends in own kalaha
		
		assertTrue(player.isTurn(), "Player did not have another turn while finishing in their own kalaha");
	}
}
