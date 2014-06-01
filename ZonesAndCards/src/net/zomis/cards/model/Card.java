package net.zomis.cards.model;

import java.util.List;

import net.zomis.cards.events.card.ZoneChangeEvent;
import net.zomis.custommap.view.ZomisLog;

public class Card<M extends CardModel> {
	private final M model;
	
	protected CardZone<?> currentZone;

	Card() { this(null); }
	
	protected Card(M model) {
		this.model = model;
	}
	
	public StackAction clickAction() {
		CardGame<?, ?> game = this.getGame();
		return game.getActionFor(this);
	}
	
	public M getModel() {
		return model;
	}

	@Override
	public String toString() {
		return "Card:" + model.toString();
	}

	public void moveAndReplaceWith(CardZoneLocation location, Card<M> card) {
		CardZone<?> destination = location.getZone();
		CardZone<?> zone = this.getCurrentZone();
		CardGame<?, ?> game = zone.getGame();
		if (game == null)
			game = destination.getGame();
		if (game == null)
			throw new NullPointerException("Neither zone is connected to a game: " + zone + " --> " + destination);
		
		ZoneChangeEvent event = new ZoneChangeEvent(this.currentZone, destination, this);
		game.executeEvent(event);
		game.executeEvent(new ZoneChangeEvent(card.getCurrentZone(), this.currentZone, card));
		
		@SuppressWarnings("unchecked")
		CardZone<Card<M>> dest = (CardZone<Card<M>>) event.getToCardZone();
		
		int myIndex = getCurrentZone().cardList().indexOf(this);
		int newCardOldIndex = card.getCurrentZone().cardList().indexOf(card);
		
		@SuppressWarnings("unchecked")
		List<Card<M>> list = (List<Card<M>>) this.getCurrentZone().cardList();
		list.set(myIndex, card);
		ZomisLog.info(newCardOldIndex + " " + myIndex);
		card.getCurrentZone().cardList().remove(newCardOldIndex);
		
		if (dest != null) {
			if (location.isTop()) {
				dest.cardList().addFirst(this);
			}
			else if (location.isBottom()) {
				dest.cardList().addLast(this);
			}
			else dest.cardList().add(location.getIndex(), this);
		}
		card.currentZone = this.currentZone;
		this.currentZone = dest;
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
	
	public String getDescription() {
		return getModel().getName();
	}

	public boolean isKnown() {
		return getCurrentZone().isKnown(getGame().getCurrentPlayer());
	}

	public Player getOwner() {
		if (currentZone == null)
			throw new NullPointerException("Card is not within a zone: " + this);
		return currentZone.getOwner();
	}
	
}
