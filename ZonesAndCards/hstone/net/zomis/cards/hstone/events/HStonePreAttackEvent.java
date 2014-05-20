package net.zomis.cards.hstone.events;

import net.zomis.cards.hstone.HStoneCard;

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
