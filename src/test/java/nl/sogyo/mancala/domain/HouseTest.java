package nl.sogyo.mancala.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import nl.sogyo.mancala.Player;
import nl.sogyo.mancala.bowl.Bowl;
import nl.sogyo.mancala.bowl.House;

class HouseTest {

	private static final int NUMBER_OF_HOUSES_PER_SIDE = 6;
	private static final int NUMBER_OF_KALAHAS = 2;
	private static final int NUMBER_OF_BOWLS = 2 * NUMBER_OF_HOUSES_PER_SIDE + NUMBER_OF_KALAHAS;
	
	private static Player player;
	private static House house;
	
	@BeforeAll
	public static void setUp() {
		player = Player.createPlayer();
		house = (House)Bowl.createBowls(player, 4, NUMBER_OF_HOUSES_PER_SIDE);
	}
	
	@Test
	public void bowlsFormLoop() {
		Bowl bowl = house;
		for(int i = 0; i < NUMBER_OF_BOWLS; i++) {
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
		
		assertEquals(ownHouses, NUMBER_OF_HOUSES_PER_SIDE, "The player does not have " + NUMBER_OF_HOUSES_PER_SIDE + " houses");
		assertEquals(opponentHouses, NUMBER_OF_HOUSES_PER_SIDE, "The opponent does not have " + NUMBER_OF_HOUSES_PER_SIDE + " houses");
	}
}
