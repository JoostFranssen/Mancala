package nl.sogyo.mancala.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nl.sogyo.mancala.Player;
import nl.sogyo.mancala.bowl.Bowl;
import nl.sogyo.mancala.bowl.House;

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
		
		assertNotEquals(player.hasTurn(), turn, "Turn of player was not changed");
		assertNotEquals(player.getOpponent().hasTurn(), turnOpponent, "Turn of player's opponent was not changed");
	}
	
	@ParameterizedTest
	@ValueSource(ints = {0, 1, 4})
	public void gameEndsOnlyWhenCurrentPlayerHasNoTurn(int initialBeads) {
		Bowl bowl = new House(player, initialBeads, House.DEFAULT_HOUSES_PER_SIDE);
		
		boolean playersTurnAndCannotPlay = player.hasTurn() && !bowl.playerHasTurn(player);
		boolean opponentsTurnAndCannotPlay = player.getOpponent().hasTurn() && !bowl.playerHasTurn(player.getOpponent());
		boolean noOneHasATurn = !bowl.playerHasTurn(player) && !bowl.playerHasTurn(player.getOpponent());
		
		assertEquals(player.gameHasEnded(), playersTurnAndCannotPlay || opponentsTurnAndCannotPlay || noOneHasATurn);
	}
}
