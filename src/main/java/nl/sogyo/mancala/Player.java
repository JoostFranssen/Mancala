package nl.sogyo.mancala;

public class Player {
	private Player opponent;
	private boolean turn;
	
	public Player() {
		turn = true;
		opponent = new Player(this);
	}
	public Player(Player opponent) {
		this.opponent = opponent;
		turn = !opponent.turn;
		opponent.opponent = this;
	}
	
	public void switchTurn() {
		turn = !turn;
		opponent.turn = !opponent.turn;
	}
	
	public Player getOpponent() {
		return opponent;
	}
	
	public boolean hasTurn() {
		return turn;
	}
	
	public void endGame() {
		turn = false;
		opponent.turn = false;
	}
	
	public boolean gameHasEnded() {
		return !turn && !opponent.turn;
	}
}
