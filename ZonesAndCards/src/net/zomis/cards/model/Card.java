package net.zomis.cards.model;

import net.zomis.cards.events.ZoneChangeEvent;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class Card {
	private final CardModel model;
	
	@JsonBackReference
	protected CardZone currentZone;

	Card() { this.model = null; }
	protected Card(CardModel model) {
		this.model = model;
	}
	
	public CardModel getModel() {
		return model;
	}

	@Override
	public String toString() {
		return model.toString();
//		return super.toString();
	}

	public void zoneMoveOnBottom(CardZone destination) {
		this.zoneMoveInternal(destination, false);
	}
	public void zoneMoveOnTop(CardZone destination) {
		this.zoneMoveInternal(destination, true);
	}
	@Deprecated
	public void zoneMove(CardZone destination) {
		this.zoneMoveOnBottom(destination);
	}
	private void zoneMoveInternal(CardZone destination, boolean top) {
		ZoneChangeEvent event = new ZoneChangeEvent(this.currentZone, destination, this);
		CardZone zone = this.getCurrentZone();
		CardGame game = zone.getGame();
		if (game == null)
			throw new NullPointerException("Zone is not initialized correctly: " + zone);
		game.executeEvent(event);
		event.getFromCardZone().cardList().remove(this);
		CardZone dest = event.getToCardZone();
		if (dest != null) {
			if (top) dest.cardList().addFirst(this);
			else dest.cardList().addLast(this);
		}
		this.currentZone = dest;
	}
	
	public CardZone getCurrentZone() {
		return currentZone;
	}
	
	public CardGame getGame() {
		return currentZone.getGame();
	}
	
}
