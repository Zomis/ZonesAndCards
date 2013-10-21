package net.zomis.cards.model.actions;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.StackAction;

public class ZoneMoveAction extends StackAction {

	private final Card card;
	private final CardZone source;
	private CardZone destination;
	private boolean sendToTop;
	
	public ZoneMoveAction(Card card) {
		this.card = card;
		this.source = card.getCurrentZone();
	}
	
	public Card getCard() {
		return card;
	}
	public CardZone getDestination() {
		return destination;
	}
	public CardZone getSource() {
		return source;
	}
	public void setDestination(CardZone destination) {
		this.destination = destination;
	}
	public void setSendToTop() {
		this.sendToTop = true;
	}
	public void setSendToBottom() {
		this.sendToTop = false;
	}
	
	@Override
	protected void onPerform() {
		if (this.sendToTop)
			card.zoneMoveOnTop(getDestination());
		else card.zoneMoveOnBottom(getDestination());
	}

	@Override
	public String toString() {
		return String.format("ZoneMove-%s-->%s", this.getCard(), this.getDestination());
	}
	
}
