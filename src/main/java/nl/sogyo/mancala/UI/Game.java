package nl.sogyo.mancala.UI;

import java.util.Scanner;

import nl.sogyo.mancala.domain.Bowl;
import nl.sogyo.mancala.domain.House;
import nl.sogyo.mancala.domain.Player;

public class Game {
	private Player player;
	private House house;
	
	public Game() {
		player = new Player();
		house = new House(player);
		
		Scanner scanner = new Scanner(System.in);
		
		while(!player.gameHasEnded()) {
			Player currentPlayer = (player.hasTurn() ? player : player.getOpponent());
			
			System.out.println(String.format("It is %s's turn:", playerName(currentPlayer)));
			printBoard(currentPlayer);
			String playableHousesString = "";
			boolean[] playableHouses = getPlayableHouseIndices(currentPlayer);
			for(int i = 0; i < House.DEFAULT_HOUSES_PER_SIDE; i++) {
				if(playableHouses[i]) {
					playableHousesString += String.format("%d, ", i + 1); // + 1 as house indices start at 0 internally
				}
			}
			System.out.print(String.format("Choose one of the following houses to play, numbered from left to right: %s: ", playableHousesString.substring(0, playableHousesString.lastIndexOf(','))));
			
			while(true) {
				if(scanner.hasNextInt()) {
					int houseChoiceIndex = scanner.nextInt() - 1; //- 1 one as house indices start at 0 internally
					if(houseChoiceIndex >= 0 && houseChoiceIndex < House.DEFAULT_HOUSES_PER_SIDE) {
						if(playableHouses[houseChoiceIndex]) {
							House houseChoice = (House)firstHouseFrom(currentPlayer).getNeighbor(houseChoiceIndex);
							houseChoice.play(currentPlayer);
							break;
						}
					}
				} else {
					scanner.next();
				}
				System.out.print("Invalid input. Please choose again: ");
			}
		}
		
		Player winner = house.getWinner();
		if(winner == null) {
			printBoard(player);
			System.out.println("It's a tie!");
		} else {
			printBoard(winner);
			System.out.println(String.format("%s has won!", playerName(winner)));
		}
		
		scanner.close();
	}
	
	private String playerName(Player player) {
		if(this.player == player) {
			return "Player 1";
		}
		if(this.player == player.getOpponent()) {
			return "Player 2"; 
		}
		return "";
	}
	
	private void printBoard(Player perspective) {
		String bottomRow = "    ";
		String topRow = "";
		Bowl currentBowl = firstHouseFrom(perspective);
		for(int i = 0; i < House.DEFAULT_HOUSES_PER_SIDE + 1; i++) {
			bottomRow += String.format("[%02d]", currentBowl.getBeads());
			currentBowl = currentBowl.getNeighbor();
		}
		for(int i = 0; i < House.DEFAULT_HOUSES_PER_SIDE + 1; i++) {
			topRow = String.format("[%02d]", currentBowl.getBeads()) + topRow;
			currentBowl = currentBowl.getNeighbor();
		}
		
		System.out.println(topRow + "     - " + playerName(perspective.getOpponent()));
		System.out.println(bottomRow + " - " + playerName(perspective));
	}
	
	private House firstHouseFrom(Player player) {
		Bowl currentBowl = house.getNeighbor();
		while(!(currentBowl instanceof House) || currentBowl.getOwner() != player) {
			currentBowl = currentBowl.getNeighbor();
		}
		return (House)currentBowl;
	}
	
	private boolean[] getPlayableHouseIndices(Player player) {
		boolean[] playableHouses = new boolean[House.DEFAULT_HOUSES_PER_SIDE];
		House firstHouse = firstHouseFrom(player);
		for(int i = 0; i < House.DEFAULT_HOUSES_PER_SIDE; i++) {
			playableHouses[i] = (firstHouse.getNeighbor(i).getBeads() != 0);
		}
		return playableHouses;
	}
}
