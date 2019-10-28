package nl.sogyo.mancala.domain;

public class MancalaDomain implements Mancala {
	
	private final Player[] players = new Player[2];
	private final String[] playerNames = new String[2];
	private final Bowl bowl; //bowl with index 1 according to the index from the Mancala interface
	
	{
		Player player = new Player();
		players[0] = player;
		players[1] = player.getOpponent();
		bowl = (new House(player).getNeighbor(9));
	}
	
	public void validatePlayerIndex(int playerIndex) throws IllegalStateException {
		if(playerIndex != 1 && playerIndex != 2) {
			throw new IllegalStateException("Invalid player index: " + playerIndex);
		}
	}
	
	@Override
	public String getPlayerName(int playerIndex) throws IllegalStateException {
		validatePlayerIndex(playerIndex);
		return playerNames[playerIndex - 1];
	}

	@Override
	public void setPlayerName(String name, int playerIndex) throws IllegalStateException {
		validatePlayerIndex(playerIndex);
		playerNames[playerIndex - 1] = name;
	}

	@Override
	public boolean isToMovePlayer(int playerIndex) throws IllegalStateException {
		validatePlayerIndex(playerIndex);
		return players[playerIndex - 1].hasTurn();
	}

	@Override
	public int[] playRecess(int index) {
		House house = (House)bowl.getNeighbor(index - 1);
		house.play(players[0].hasTurn() ? players[0] : players[1]);
		return exportGameState();
	}

	@Override
	public int getStonesForPit(int index) {
		return bowl.getNeighbor(index - 1).getBeads();
	}

	@Override
	public int[] exportGameState() {
		int[] state = new int[15];
		for(int i = 0; i < 14; i++) {
			state[i] = getStonesForPit(i);
		}
		state[14] = (players[0].hasTurn() ? 1 : 2);
		return state;
	}

	@Override
	public boolean isEndOfGame() {
		return players[0].gameHasEnded();
	}

	@Override
	public String getWinnersName() {
		return playerNames[bowl.getWinner() == players[0] ? 1: 2];
	}
}
