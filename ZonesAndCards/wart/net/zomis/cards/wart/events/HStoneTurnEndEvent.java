package net.zomis.cards.wart.events;

import net.zomis.cards.wart.HStonePlayer;

public class HStoneTurnEndEvent extends HStoneCardEvent {

	public HStoneTurnEndEvent(HStonePlayer card) {
		super(card.getPlayerCard());
	}
	
}
