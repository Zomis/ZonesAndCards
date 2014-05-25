package net.zomis.cards.wart.events;

import net.zomis.cards.wart.HStoneCard;

public class HStoneDoubleCardEvent extends HStoneCardEvent {

	private final HStoneCard target;
	private boolean	cancelled;

	public HStoneDoubleCardEvent(HStoneCard source, HStoneCard target) {
		super(source);
		this.target = target;
	}
	
	public HStoneCard getTarget() {
		return target;
	}
	
	public HStoneCard getSource() {
		return super.getCard();
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
