package nl.sogyo.mancala.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nl.sogyo.mancala.Player;
import nl.sogyo.mancala.bowl.Bowl;

public class PlayerTest {
	
	private Player player;
	
	@BeforeEach
	public void setUpPlayerPair() {
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
	
	@ParameterizedTest
	@ValueSource(ints = {0, 1, 4})
	public void gameEndsOnlyWhenCurrentPlayerHasNoTurn(int initialBeads) {
		Bowl bowl = Bowl.createBowls(player, initialBeads);
		
		boolean playersTurnAndCannotPlay = player.isTurn() && !bowl.playerHasTurn(player);
		boolean opponentsTurnAndCannotPlay = player.getOpponent().isTurn() && !bowl.playerHasTurn(player.getOpponent());
		boolean noOneHasATurn = !bowl.playerHasTurn(player) && !bowl.playerHasTurn(player.getOpponent());
		
		assertEquals(player.gameHasEnded(), playersTurnAndCannotPlay || opponentsTurnAndCannotPlay || noOneHasATurn);
	}
}
