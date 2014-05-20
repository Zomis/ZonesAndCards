package net.zomis.cards.hstone.events;

import net.zomis.cards.hstone.HStonePlayer;

public class HStoneTurnEndEvent extends HStoneCardEvent {

	public HStoneTurnEndEvent(HStonePlayer card) {
		super(card.getPlayerCard());
	}
	
}
