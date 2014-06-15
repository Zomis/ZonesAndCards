package net.zomis.cards.events.card;

import net.zomis.cards.events.CardEvent;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;

public class CardPlayedEvent extends CardEvent {

	private final StackAction	action;
	private final Player whoPlayed;

	public CardPlayedEvent(Card<?> card, Player player, CardGame<?, ?> cardGame, StackAction action) {
		super(card, cardGame);
		this.action = action;
		this.whoPlayed = player;
	}
	
	public StackAction getAction() {
		return action;
	}
	
	public Player getWhoPlayed() {
		return whoPlayed;
	}

}
