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
	
	public int mancalaIndexToArrayIndex(int mancalaIndex) {
		return mancalaIndex - 1;
	}
	public int arrayIndexToMancalaIndex(int arrayIndex) {
		return arrayIndex + 1;
	}
	
	public void validatePlayerIndex(int playerIndex) throws IllegalStateException {
		if(playerIndex != 1 && playerIndex != 2) {
			throw new IllegalStateException("Invalid player index: " + playerIndex);
		}
	}
	
	@Override
	public String getPlayerName(int playerIndex) throws IllegalStateException {
		validatePlayerIndex(playerIndex);
		return playerNames[mancalaIndexToArrayIndex(playerIndex)];
	}

	/**
	 * @exception IllegalArgumentException if {@code name} is blank
	 */
	@Override
	public void setPlayerName(String name, int playerIndex) throws IllegalStateException {
		validatePlayerIndex(playerIndex);
		if(name.isBlank()) {
			throw new IllegalArgumentException("A player's name cannot be blank");
		}
		playerNames[mancalaIndexToArrayIndex(playerIndex)] = name;
	}

	@Override
	public boolean isToMovePlayer(int playerIndex) throws IllegalStateException {
		validatePlayerIndex(playerIndex);
		return players[mancalaIndexToArrayIndex(playerIndex)].hasTurn();
	}

	@Override
	public int[] playRecess(int index) {
		House house = (House)bowl.getNeighbor(mancalaIndexToArrayIndex(index));
		house.play(players[0].hasTurn() ? players[0] : players[1]);
		return exportGameState();
	}

	@Override
	public int getStonesForPit(int index) {
		return bowl.getNeighbor(mancalaIndexToArrayIndex(index)).getBeads();
	}

	@Override
	public int[] exportGameState() {
		int[] state = new int[15];
		for(int i = 0; i < 14; i++) {
			state[i] = getStonesForPit(arrayIndexToMancalaIndex(i));
		}
		state[14] = (players[mancalaIndexToArrayIndex(1)].hasTurn() ? 1 : 2);
		return state;
	}

	@Override
	public boolean isEndOfGame() {
		return players[0].gameHasEnded();
	}

	/**
	 * @return {@inheritDoc} In case of a tie an empty string is returned.
	 */
	@Override
	public String getWinnersName() {
		if(!isEndOfGame()) {
			return null;
		}
		Player winner = bowl.getWinner();
		if(winner == null) {
			return "";
		} else {
			return playerNames[winner == players[0] ? 0 : 1];
		}
	}
}
