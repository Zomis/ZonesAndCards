package net.zomis.cards.hstone.events;

import net.zomis.cards.events.CardEvent;
import net.zomis.cards.hstone.HStoneCard;

public class HStoneCardEvent extends CardEvent {
	
	public HStoneCardEvent(HStoneCard card) {
		super(card);
	}
	
	@Override
	public HStoneCard getCard() {
		return (HStoneCard) super.getCard();
	}


}
