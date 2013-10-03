package net.zomis.cards.events;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.events.IEvent;

public class ZoneChangeEvent implements IEvent {
	
	private final CardZone fromCardZone;
	private CardZone toCardZone;
	private final Card card;
	private final Player player;

	public ZoneChangeEvent(CardZone fromCardZone, CardZone toCardZone, Card card, Player player) {
		this.fromCardZone = fromCardZone;
		this.toCardZone = toCardZone;
		this.card = card;
		this.player = player;
	}
	
	public Card getCard() {
		return card;
	}
	public CardZone getFromCardZone() {
		return fromCardZone;
	}
	public CardZone getToCardZone() {
		return toCardZone;
	}
	public void setToCardZone(CardZone toCardZone) {
		this.toCardZone = toCardZone;
	}
	public Player getPlayer() {
		return player;
	}
	@Override
	public String toString() {
		return String.format("ZoneChange{%s by %s. %s-->%s}", this.card, this.player, this.getFromCardZone(), this.getToCardZone());
	}
}
