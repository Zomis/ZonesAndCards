package net.zomis.cards.hstone.events;

import net.zomis.cards.hstone.HStonePlayer;

public class HStoneTurnStartEvent extends HStoneCardEvent {

	public HStoneTurnStartEvent(HStonePlayer card) {
		super(card.getPlayerCard());
	}
	
	public HStonePlayer getPlayer() {
		return getCard().getPlayer();
	}

}
