package nl.sogyo.mancala.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.sogyo.mancala.Player;
import nl.sogyo.mancala.bowl.Bowl;
import nl.sogyo.mancala.bowl.House;
import nl.sogyo.mancala.bowl.Kalaha;

class HouseTest {
	
	private static final int DEFAULT_HOUSES_PER_SIDE = 6;
	private static final int DEFAULT_NUMBER_OF_BOWLS = 2 * DEFAULT_HOUSES_PER_SIDE + Kalaha.NUMBER_OF_KALAHAS;
	
	private Player player;
	private House house;
	
	@BeforeEach
	public void setUpBoard() {
		setUpBoard(4, DEFAULT_HOUSES_PER_SIDE);
	}
	public void setUpBoard(int initialBeads) {
		setUpBoard(initialBeads, DEFAULT_HOUSES_PER_SIDE);
	}
	public void setUpBoard(int initialBeads, int housesPerSide) {
		player = new Player();
		house = (House)Bowl.createBowls(player, initialBeads, housesPerSide);
	}
	
	@Test
	public void bowlsFormLoop() {
		Bowl bowl = house;
		for(int i = 0; i < DEFAULT_NUMBER_OF_BOWLS; i++) {
			bowl = bowl.getNeighbor();
		}
		assertEquals(bowl, house, "Neighbor of the last bowl is not the first bowl");
	}
	
	@Test
	public void oppositeOfOppositeIsSelf() {
		Bowl opposite = house.getOpposite();
		assertEquals(house, opposite.getOpposite(), "The opposite of its opposite is not itself");
	}
	
	@Test
	public void oppositeBelongsToOpponent() {
		assertEquals(house.getOpposite().getOwner(), house.getOwner().getOpponent(), "The opposite of a house does not belong to the opponent");
	}
	
	@Test
	public void everyPlayerHasTheRightNumberOfHouses() {
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
		
		assertEquals(ownHouses, DEFAULT_HOUSES_PER_SIDE, "The player does not have " + DEFAULT_HOUSES_PER_SIDE + " houses");
		assertEquals(opponentHouses, DEFAULT_HOUSES_PER_SIDE, "The opponent does not have " + DEFAULT_HOUSES_PER_SIDE + " houses");
	}
	
	@Test
	public void distributorOwnsStartingHouse() {
		Player distributor = player.getOpponent();
		
		if(house.getOwner() != distributor) {
			assertThrows(IllegalArgumentException.class, () -> house.startDistribute(distributor), "Opponent played the other's house, but no exception was thrown");
		}
	}
	
	@Test
	public void distributeFirstHouseWithFourBeadsGetsEmptied() {
		house.startDistribute(player);
		
		assertEquals(house.getBeads(), 0, "House were distribution started was not emptied");
	}
	
	@Test
	public void distributeFirstHouseWithFourBeadsDistributesOneOverNexthouses() {
		house.startDistribute(player);
		House currentHouse = house;
		for(int i = 1; i <= 4; i++) {
			currentHouse = (House)currentHouse.getNeighbor();
			assertEquals(currentHouse.getBeads(), 5, "Neightbor #" + i + " does not have five beads");
		}
	}
	
	@Test
	public void distributionEndsInOwnEmptyHouseStealsFromOpposite() {
		setUpBoard(1);
		
		//empty the second house by distributing its one bead
		House secondHouse = (House)house.getNeighbor();
		secondHouse.startDistribute(player);
		
		//start distributing from the first house, which shall end in the now empty second house
		house.startDistribute(player);
		
		//steal from the house were we ended in
		assertEquals(secondHouse.getBeads(), 0, "The second house was not stolen from");
		
		//steal from the opposite house
		assertEquals(secondHouse.getOpposite().getBeads(), 0, "The opposite of the second house was not stolen from");
	}
	
	@Test
	public void playerHasATurnWithNoNonEmptyHouses() {
		assertTrue(house.playerHasTurn(player), "All houses are non-empty, but the player had no turn");
		assertTrue(house.playerHasTurn(player.getOpponent()), "All houses are non-empty, but the opponent had no turn");
	}
	
	@Test
	public void onlyPlayerWithEmptyHousesHasNoTurn() {
		setUpBoard(1);
		
		//empty the first six houses
		Bowl currentHouse = house;
		for(int i = 0; i < DEFAULT_HOUSES_PER_SIDE; i++) {
			((House)currentHouse).startDistribute(player);
			currentHouse = currentHouse.getNeighbor();
		}
		
		assertFalse(house.playerHasTurn(player), "All houses of the player are empty, but they still had a turn");
		assertTrue(house.playerHasTurn(player.getOpponent()), "Opponent player has at least one non-empty house, but they had no turn");
	}
	
	@Test
	public void playerTurnSwitchesAfterDistributionNotInKalaha() {
		house.startDistribute(player);
		
		assertFalse(player.isTurn(), "After distribution that did not end in a Kalaha it was still the players turn");
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
		setUpBoard(2, 1);
		
		house.startDistribute(player);
		((House)house.getNeighbor().getNeighbor()).startDistribute(player.getOpponent());
		
		assertEquals(house.getWinner(), player.getOpponent(), "Game ended with opponent in a winning state, but opponent did not win");
	}
	
	@Test
	public void playerWinsEndInKahala() {
		setUpBoard(1, 1);
		
		house.startDistribute(player);
		
		assertEquals(house.getWinner(), player, "Game ended with player in a winning state, but player did not win");
	}
}
