package net.zomis.cards.model.actions;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.StackAction;

public class ZoneMoveAction extends StackAction {

	private final Card<?> card;
	private final CardZone<?> source;
	private CardZone<?> destination;
	private boolean sendToTop;
	
	public ZoneMoveAction(Card<?> card) {
		this.card = card;
		this.source = card.getCurrentZone();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((card == null) ? 0 : card.hashCode());
//		result = prime * result
//				+ ((destination == null) ? 0 : destination.hashCode());
//		result = prime * result + (sendToTop ? 1231 : 1237);
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	public Card<?> getCard() {
		return card;
	}
	public CardZone<?> getDestination() {
		return destination;
	}
	public CardZone<?> getSource() {
		return source;
	}
	public void setDestination(CardZone<?> destination) {
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
		return "ZoneMove-" + getCard() + "-->" + getDestination();
	}
	
}
