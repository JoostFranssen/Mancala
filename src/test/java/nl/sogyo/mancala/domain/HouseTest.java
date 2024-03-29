package nl.sogyo.mancala.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nl.sogyo.mancala.domain.Player;

public class HouseTest {
	public static final int DEFAULT_NUMBER_OF_BOWLS = 2 * House.DEFAULT_HOUSES_PER_SIDE + Kalaha.NUMBER_OF_KALAHAS;
	
	private Player player;
	private House house, firstHouse;
	
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
		
		//find the first house owned by player
		Bowl currentBowl = house.getNeighbor();
		while(!(currentBowl instanceof House) || currentBowl.getOwner() != player) {
			currentBowl = currentBowl.getNeighbor();
		}
		firstHouse = (House)currentBowl;
	}
	
	@Test
	public void bowlsFormLoop() {
		Bowl bowl = house;
		for(int i = 0; i < DEFAULT_NUMBER_OF_BOWLS; i++) {
			bowl = bowl.getNeighbor();
		}
		assertEquals(house, bowl, "Neighbor of the last bowl is not the first bowl");
	}
	
	@Test
	public void oppositeOfOppositeIsSelf() {
		Bowl opposite = house.getOpposite();
		assertEquals(house, opposite.getOpposite(), "The opposite of its opposite is not itself");
	}
	
	@Test
	public void oppositeBelongsToOpponent() {
		assertEquals(house.getOwner().getOpponent(), house.getOpposite().getOwner(), "The opposite of a house does not belong to the opponent");
	}
	
	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3, 6})
	public void everyPlayerHasTheRightNumberOfHouses(int housesPerSide) {
		setUpBoard(House.DEFAULT_INITIAL_BEADS, housesPerSide);
		
		Bowl currentBowl = house;
		int ownHouses = 0;
		int opponentHouses = 0;
		do {
			if(currentBowl instanceof House && currentBowl.getOwner() == player) {
				ownHouses++;
			}
			if(currentBowl instanceof House && currentBowl.getOwner() == player.getOpponent()) {
				opponentHouses++;
			}
			currentBowl = currentBowl.getNeighbor();
		} while(currentBowl != house);
		
		assertEquals(housesPerSide, ownHouses, "The player does not have " + housesPerSide + " houses");
		assertEquals(housesPerSide, opponentHouses, "The opponent does not have " + housesPerSide + " houses");
	}
	
	@Test
	public void distributorOwnsStartingHouse() {
		Player distributor = player.getOpponent();
		
		//house belongs to player, not to opponent
		assertThrows(IllegalArgumentException.class, () -> house.play(distributor), "Opponent played the player's house, but no exception was thrown");
	}
	
	@Test
	public void startDistributeHouseGetsEmptied() {
		house.play(player);
		
		assertEquals(0, house.getBeads(), "House were distribution started was not emptied");
	}
	
	@Test
	public void distributeFirstHouseWithFourBeadsDistributesOneOverNexthouses() {
		firstHouse.play(player);
		House currentHouse = firstHouse;
		for(int i = 1; i <= 4; i++) {
			currentHouse = (House)currentHouse.getNeighbor();
			assertEquals(5, currentHouse.getBeads(), "Neightbor #" + i + " does not have five beads");
		}
	}
	
	@Test
	public void distributorHasTurn() {
		House opposite = (House)house.getOpposite();
		
		//at the start of the game the opponent does not have a turn
		assertThrows(IllegalStateException.class, () -> opposite.play(player.getOpponent()), "The opponent plays while not having a turn");
	}
	
	@Test
	public void distributionEndsInOwnEmptyHouseStealsFromOpposite() {
		setUpBoard(1, 2);
		house.play(player); //ends in Kalaha
		firstHouse.play(player); //ends in house, which was just emptied
		
		//steal from the house were we ended in
		assertEquals(0, house.getBeads(), "The second house was not stolen from");
		
		//steal from the opposite house
		assertEquals(0, house.getOpposite().getBeads(), "The opposite of the second house was not stolen from");
	}
	
	@Test
	public void playerHasATurnWithNoNonEmptyHouses() {
		assertTrue(house.playerHasTurn(player), "All houses are non-empty, but the player had no turn");
		assertTrue(house.playerHasTurn(player.getOpponent()), "All houses are non-empty, but the opponent had no turn");
	}
	
	@Test
	public void onlyPlayerWithEmptyHousesHasNoTurn() {
		setUpBoard(1, 1);
		
		//empty the first six houses
		house.play(player);
		
		assertFalse(house.playerHasTurn(player), "All houses of the player are empty, but they still had a turn");
		assertTrue(house.playerHasTurn(player.getOpponent()), "Opponent player has at least one non-empty house, but they had no turn");
	}
	
	@Test
	public void playerTurnSwitchesAfterDistributionNotInKalaha() {
		house.play(player);
		
		assertFalse(player.hasTurn(), "After distribution that did not end in a Kalaha it was still the players turn");
	}
	
	@Test
	public void getWinnerThrowsExceptionWhenGameHasNotEnded() {
		assertThrows(IllegalStateException.class, () -> house.getWinner(), "An initalized game with 4 beads in each house has a winnner, even though it did not end");
	}
	
	@Test
	public void initialEmptyBoardResultsInATie() {
		setUpBoard(0);
		
		assertNull(house.getWinner(), "Game initialized with an empty board does not result in a tie");
	}
	
	@Test
	public void playerWinsEndInHouse() {
		setUpBoard(3, 1);
		
		house.play(player);
		
		assertEquals(player, house.getWinner(), "Game ended with player in a winning state, but player did not win");
	}
	
	@Test
	public void playerWinsEndInKahala() {
		setUpBoard(1, 1);
		
		house.play(player);
		
		assertEquals(player, house.getWinner(), "Game ended with player in a winning state, but player did not win");
	}
	
	@Test
	public void zeroethNeighborIsSelf() {
		assertEquals(house, house.getNeighbor(0), "Zeroeth neighbor is not the house itself");
	}
	
	@Test
	public void secondNeighborIsNeighborOfNeighbor() {
		assertEquals(house.getNeighbor().getNeighbor(), house.getNeighbor(2), "Second neighbor is not the neighbor of its neighbor");
	}
	
	@ParameterizedTest
	@ValueSource(ints = {1, 6})
	public void numberOfHousesByPlayer(int housesPerSide) {
		setUpBoard(4, housesPerSide);
		
		//+1 is because each player has one kalaha
		assertEquals(housesPerSide + 1, house.countBowlsFromPlayer(player));
		assertEquals(housesPerSide + 1, house.countBowlsFromPlayer(player.getOpponent()));
	}
}
