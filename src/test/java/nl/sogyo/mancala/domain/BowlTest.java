package nl.sogyo.mancala.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import nl.sogyo.mancala.Player;
import nl.sogyo.mancala.bowl.House;

public class BowlTest {
	
	private Player player;
	private House house;
	
	@Before
	public void setUp() {
		player = Player.createPlayer();
		house = new House(player);
	}
	
	@Test
	public void bowlHasNonNegativeBeads() {
		assertTrue("House has negative amount of beads", house.getBeads() >= 0);
	}
	
	@Test
	public void bowlHasOwner() {
		assertNotNull("House does not have an owner", house.getOwner());
	}
	
	@Test
	public void bowlHasNeighbor() {
		assertNotNull("House does not have a neighbor", house.getNeighbor());
	}
}
