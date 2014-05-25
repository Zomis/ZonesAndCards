package net.zomis.cards.wart.events;

import net.zomis.cards.wart.HStoneCard;

public class HStonePreAttackEvent extends HStoneDoubleCardEvent {

	private boolean cancelled;
	
	public HStonePreAttackEvent(HStoneCard card, HStoneCard target) {
		super(card, target);
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
