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
	public void eachPlayerHasOneKalaha() {
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
		
		assertEquals(1, playerKalahas, "The player does not have exactly one kalaha");
		assertEquals(1, opponentKalahas, "The opponent does not have exactly one kalaha");
	}
	
	@Test
	public void countKalahas() {
		//these methods were later implemented and thus are tested separately from the kalaha count
		assertEquals(1, house.countKalahasFromPlayer(player), "Player does not have one kalaha");
		assertEquals(1, house.countKalahasFromPlayer(player.getOpponent()), "Opponent does not have one kalaha");
	}
	
	@Test
	public void ownKalahaIsReachedButOpponentKalahaIsSkipped() {
		setUpBoard(4, 2);
		house.play(player); //start from last house, places one bead in Kalaha, 1 in both opponent's houses, and the last bead in the first own house
		
		Kalaha playerKalaha = (Kalaha)house.getNeighbor();
		Kalaha opponentKalaha = (Kalaha)playerKalaha.getNeighbor().getNeighbor().getNeighbor();
		
		assertEquals(1, playerKalaha.getBeads(), "No bead was placed in own Kalaha");
		assertEquals(0, opponentKalaha.getBeads(), "A bead was placed in opponent's Kalaha");
	}
	
	@Test
	public void turnDoesNotSwitchAfterEndInOwnKalaha() {
		setUpBoard(1);
		house.play(player); //distribution ends in own kalaha
		
		assertTrue(player.hasTurn(), "Player did not have another turn while finishing in their own kalaha");
	}
}
