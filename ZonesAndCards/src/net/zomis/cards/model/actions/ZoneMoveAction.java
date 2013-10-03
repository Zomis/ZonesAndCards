package net.zomis.cards.model.actions;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.phases.GamePhase;
import net.zomis.cards.model.phases.IPlayerPhase;

public class ZoneMoveAction extends StackAction {

	private Card	card;
	private CardZone	destination;
	
	public ZoneMoveAction(Card card) {
		this.card = card;
	}
	
	public Card getCard() {
		return card;
	}
	public CardZone getDestination() {
		return destination;
	}
	public void setDestination(CardZone destination) {
		this.destination = destination;
	}
	@Override
	protected void perform() {
//		CustomFacade.getLog().i("Zone Move Action: " + this);
		card.zoneMove(this.getDestination(), this.getPlayer());
	}

	protected Player getPlayer() {
		GamePhase phase = card.getCurrentZone().getGame().getActivePhase();
		if (phase instanceof IPlayerPhase) {
			return ((IPlayerPhase)phase).getPlayer();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return String.format("ZoneMove-%s-->%s by %s", this.getCard(), this.getDestination(), this.getPlayer());
	}
	
}
