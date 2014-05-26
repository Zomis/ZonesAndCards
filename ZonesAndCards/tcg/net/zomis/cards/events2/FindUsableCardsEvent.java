package net.zomis.cards.events2;

import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.events.IEvent;

public class FindUsableCardsEvent implements IEvent {

	private final FirstCompGame	game;
	private final CompPlayer	player;

	public FindUsableCardsEvent(FirstCompGame game, CompPlayer player) {
		this.game = game;
		this.player = player;
	}
	
	public FirstCompGame getGame() {
		return game;
	}
	
	public CompPlayer getPlayer() {
		return player;
	}

}
