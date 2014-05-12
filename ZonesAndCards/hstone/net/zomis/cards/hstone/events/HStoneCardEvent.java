package net.zomis.cards.hstone.events;

import net.zomis.cards.events.CardEvent;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneGame;

public class HStoneCardEvent extends CardEvent {
	
	public HStoneCardEvent(HStoneCard card) {
		super(card);
	}
	
	@Override
	public HStoneCard getCard() {
		return (HStoneCard) super.getCard();
	}
	
	@Override
	public HStoneGame getGame() {
		return (HStoneGame) super.getGame();
	}


}
