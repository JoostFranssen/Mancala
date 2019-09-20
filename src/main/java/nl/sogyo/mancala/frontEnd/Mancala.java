package nl.sogyo.mancala.frontEnd;

import nl.sogyo.mancala.Player;
import nl.sogyo.mancala.bowl.Bowl;
import nl.sogyo.mancala.bowl.House;

public class Mancala {
	private static Player player;
	private static House house;
	
	public static void main(String[] args) {
		player = new Player();
		house = new House(player);
		System.out.println(playerName(player.getOpponent()));
		printBoard(player);
		System.out.println(playerName(player));
		
		house.play(player);
		
		System.out.println(playerName(player));
		printBoard(player.getOpponent());
		System.out.println(playerName(player.getOpponent()));
	}
	
	private static String playerName(Player player) {
		if(Mancala.player == player) {
			return "Player 1";
		}
		if(Mancala.player == player.getOpponent()) {
			return "Player 2";
		}
		return "";
	}
	
	private static void printBoard(Player perspective) {
		String bottomRow = "   ";
		String topRow = "";
		Bowl currentBowl = firstHouseFrom(perspective);
		for(int i = 0; i < House.DEFAULT_HOUSES_PER_SIDE + 1; i++) {
			bottomRow += String.format("[%d]", currentBowl.getBeads());
			currentBowl = currentBowl.getNeighbor();
		}
		for(int i = 0; i < House.DEFAULT_HOUSES_PER_SIDE + 1; i++) {
			topRow = String.format("[%d]", currentBowl.getBeads()) + topRow;
			currentBowl = currentBowl.getNeighbor();
		}
		
		System.out.println(topRow);
		System.out.println(bottomRow);
	}
	
	private static House firstHouseFrom(Player player) {
		Bowl currentBowl = house.getNeighbor();
		while(!(currentBowl instanceof House) || currentBowl.getOwner() != player) {
			currentBowl = currentBowl.getNeighbor();
		}
		return (House)currentBowl;
	}
}
