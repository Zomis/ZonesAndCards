package net.zomis.cards.model;

import net.zomis.cards.events.card.ZoneChangeEvent;

public class Card<M extends CardModel> {
	private final M model;
	
	@Override
	public int hashCode() {
		final int prime = 41;
		int result = 1;
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		return result;
	}

	protected CardZone<?> currentZone;

	Card() { this(null); }
	
	protected Card(M model) {
		this.model = model;
	}
	
	public StackAction clickAction() {
		return this.getGame().getActionHandler().click(this);
	}
	public M getModel() {
		return model;
	}

	@Override
	public String toString() {
		return model.toString();
//		return super.toString();
	}

	public void zoneMoveOnBottom(CardZone<?> destination) {
		this.zoneMoveInternal(destination, false);
	}
	public void zoneMoveOnTop(CardZone<?> destination) {
		this.zoneMoveInternal(destination, true);
	}
	private void zoneMoveInternal(CardZone<?> destination, boolean top) {
		ZoneChangeEvent event = new ZoneChangeEvent(this.currentZone, destination, this);
		CardZone<?> zone = this.getCurrentZone();
		CardGame<?, ?> game = zone.getGame();
		if (game == null)
			game = destination.getGame();
		if (game == null)
			throw new NullPointerException("Neither zone is connected to a game: " + zone + " --> " + destination);
		game.executeEvent(event);
		event.getFromCardZone().cardList().remove(this);
		@SuppressWarnings("unchecked")
		CardZone<Card<M>> dest = (CardZone<Card<M>>) event.getToCardZone();
		if (dest != null) {
			if (top) dest.cardList().addFirst(this);
			else dest.cardList().addLast(this);
		}
		this.currentZone = dest;
	}
	
	public CardZone<?> getCurrentZone() {
		return currentZone;
	}
	
	public CardGame<?, ?> getGame() {
		if (currentZone == null)
			throw new NullPointerException("Card is not within a zone: " + this + ". Is it possible that this card has been moved to /dev/null?");
		return currentZone.getGame();
	}
	
}
