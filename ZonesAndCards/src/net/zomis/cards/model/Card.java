package net.zomis.cards.model;

import net.zomis.cards.events.ZoneChangeEvent;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class Card {
	private final CardModel model;
	
	@JsonBackReference
	CardZone currentZone;

	Card() { this.model = null; }
	Card(CardModel model) {
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

	public void zoneMove(CardZone destination, Player player) {
		ZoneChangeListener listener = this.getModel().getOnZoneChange();
		ZoneChangeEvent event = new ZoneChangeEvent(this.currentZone, destination, this, player);
		if (listener != null) {
			listener.onZoneChange(event);
		}
		CardZone zone = this.getCurrentZone();
		CardGame game = zone.getGame();
		if (game == null)
			throw new NullPointerException("Zone is not initialized correctly: " + zone);
		game.executeEvent(event);
		event.getFromCardZone().remove(this);
		CardZone dest = event.getToCardZone();
		if (dest != null) {
			dest.add(this);
		}
	}
	
	public CardZone getCurrentZone() {
		return currentZone;
	}
	
	public CardGame getGame() {
		return currentZone.getGame();
	}
	
}
