package net.zomis.cards.hearts;

import java.util.List;

import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicGame;

public enum HeartsGiveDirection {
	LEFT(1), RIGHT(-1), AHEAD(2), NONE(null);
	
	private Integer	give;

	private HeartsGiveDirection(Integer give) {
		this.give = give;
	}
	
	public CardPlayer getGiveToPlayer(CardPlayer player) {
		if (give == null)
			return player; // To avoid a null check, return self.
		
		ClassicGame game = player.getGame();
		List<CardPlayer> players = game.getPlayers();
		int index = players.indexOf(player);
		if (index == -1)
			throw new IllegalArgumentException("Player is not in list");
		
		index = (index + this.give + players.size()) % players.size();
		return (CardPlayer) players.get(index);
	}

	public boolean isGive() {
		return this.give != null;
	}
}
