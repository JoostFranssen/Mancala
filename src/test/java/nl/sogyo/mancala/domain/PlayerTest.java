package nl.sogyo.mancala.domain;



import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import nl.sogyo.mancala.Player;

public class PlayerTest {
	
	private static Player player;
	
	@BeforeAll
	public static void setUpPlayerPair() {
		player = Player.createPlayer();
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
		assertNotEquals(player.isTurn(), player.getOpponent().isTurn(), "Players do not have mutually exclusive turn state");
	}
	
	@Test
	public void switchPlayerTurn() {
		boolean turn = player.isTurn();
		boolean turnOpponent = player.getOpponent().isTurn();
		
		player.switchTurn();
		
		assertNotEquals(player.isTurn(), turn, "Turn of player was not changed");
		assertNotEquals(player.getOpponent().isTurn(), turnOpponent, "Turn of player's opponent was not changed");
	}
}
