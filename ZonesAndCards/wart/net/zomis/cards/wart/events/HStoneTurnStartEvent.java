package net.zomis.cards.wart.events;

import net.zomis.cards.wart.HStonePlayer;

public class HStoneTurnStartEvent extends HStoneCardEvent {

	public HStoneTurnStartEvent(HStonePlayer card) {
		super(card.getPlayerCard());
	}
	
}
