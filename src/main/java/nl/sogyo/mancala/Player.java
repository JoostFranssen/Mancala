package nl.sogyo.mancala;

public class Player {
	private Player opponent;
	private boolean turn;
	
	private Player() {}
	
	public static Player createPlayer() {
		Player player1 = new Player();
		Player player2 = new Player();
		
		player1.opponent = player2;
		player2.opponent = player1;
		
		player1.turn = true;
		player2.turn = false;
		
		return player1;
	}
	
	public void switchTurn() {
		turn = !turn;
		opponent.turn = !opponent.turn;
	}
	
	public Player getOpponent() {
		return opponent;
	}
	
	public boolean isTurn() {
		return turn;
	}
}
