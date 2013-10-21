package net.zomis.cards.idiot;

import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.actions.ZoneMoveAction;

public class MoveAction extends ZoneMoveAction {

	public MoveAction(Card from, ClassicCardZone to) {
		super(from);
		setDestination(to);
		setSendToBottom();
	}

	@Override
	public boolean actionIsAllowed() {
		return getCard() != null && getDestination().cardList().isEmpty() &&
			getCard().getCurrentZone().cardList().peekLast() == getCard();
	}
	
}
