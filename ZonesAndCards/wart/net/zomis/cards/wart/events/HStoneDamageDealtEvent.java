package net.zomis.cards.wart.events;

import net.zomis.cards.wart.HStoneCard;

public class HStoneDamageDealtEvent extends HStoneCardEvent {

	private final HStoneCard	target;

	public HStoneDamageDealtEvent(HStoneCard source, HStoneCard target) {
		super(source);
		this.target = target;
	}
	
	public HStoneCard getTarget() {
		return target;
	}
}
