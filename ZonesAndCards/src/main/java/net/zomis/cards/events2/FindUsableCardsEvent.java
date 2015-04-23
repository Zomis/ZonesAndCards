package net.zomis.cards.events2;

import java.util.List;

import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.events.IEvent;

public class FindUsableCardsEvent implements IEvent {

	private final FirstCompGame	game;
	private final CompPlayer	player;
	private List<Card<?>>	result;

	public FindUsableCardsEvent(FirstCompGame game, CompPlayer player, List<Card<?>> result) {
		this.game = game;
		this.player = player;
		this.result = result;
	}
	
	public void addCardToResult(Card<?> card) {
		this.result.add(card);
	}
	
	public FirstCompGame getGame() {
		return game;
	}
	
	public CompPlayer getPlayer() {
		return player;
	}
	
	public List<Card<?>> getResult() {
		return result;
	}

	public void addZoneToResult(CardZone<?> zone) {
		result.addAll(zone.cardList());
	}

}
