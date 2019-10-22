package nl.sogyo.mancala.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nl.sogyo.mancala.domain.Player;

public class PlayerTest {
	
	private Player player;
	
	@BeforeEach
	public void setUpPlayerPair() {
		player = new Player();
	}
	
	@Test
	public void playerHasOpponent() {
		assertNotNull(player.getOpponent(), "Player has no opponent");
	}
	
	@Test
	public void playersAreEachOthersOpponents() {
		assertEquals(player, player.getOpponent().getOpponent(), "Players are not each other's opponent");
	}
	
	@Test
	public void exactlyOnePlayerHasTurn() {
		assertNotEquals(player.hasTurn(), player.getOpponent().hasTurn(), "Players do not have mutually exclusive turn state");
	}
	
	@Test
	public void switchPlayerTurn() {
		boolean turn = player.hasTurn();
		boolean turnOpponent = player.getOpponent().hasTurn();
		
		player.switchTurn();
		
		assertNotEquals(turn, player.hasTurn(), "Turn of player was not changed");
		assertNotEquals(turnOpponent, player.getOpponent().hasTurn(), "Turn of player's opponent was not changed");
	}
	
	@Test
	public void gameEndsWhenPlayerHasNoTurn() {
		House house = new House(player, 1, 1);
		house.play(player);
		
		assertTrue(player.gameHasEnded(), "It was the player turn but they could not make a move, yet the game did not end");
	}
	
	@ParameterizedTest
	@ValueSource(ints = {0, House.DEFAULT_INITIAL_BEADS})
	public void gameEndsOnlyWhenPlayersHaveATurn(int initialBeads) {
		new House(player, initialBeads);
		
		assertEquals(initialBeads == 0, player.gameHasEnded(), "A new game did not have the correct state when there were " + initialBeads + "initial beads");
	}
}
