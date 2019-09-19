package nl.sogyo.mancala.domain;

import static org.junit.Assert.*;

import org.junit.*;

import nl.sogyo.mancala.Player;

public class PlayerTest {
	
	private static Player player;
	
	@BeforeClass
	public static void setUpPlayerPair() {
		player = Player.createPlayer();
	}
	
	@Test
	public void playerHasOpponent() {
		assertNotNull("Player has no opponent", player.getOpponent());
	}
	
	@Test
	public void playersAreEachOthersOpponents() {
		assertEquals("Players are not each other's opponent", player, player.getOpponent().getOpponent());
	}
	
	@Test
	public void exactlyOnePlayerHasTurn() {
		assertNotEquals("Players do not have mutually exclusive turn state", player.isTurn(), player.getOpponent().isTurn());
	}
	
	@Test
	public void switchPlayerTurn() {
		boolean turn = player.isTurn();
		boolean turnOpponent = player.getOpponent().isTurn();
		
		player.switchTurn();
		
		assertNotEquals("Turn of player was not changed", player.isTurn(), turn);
		assertNotEquals("Turn of player's opponent was not changed", player.getOpponent().isTurn(), turnOpponent);
	}
}
