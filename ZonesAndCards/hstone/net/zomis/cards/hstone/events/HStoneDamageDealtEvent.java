package net.zomis.cards.hstone.events;

import net.zomis.cards.hstone.HStoneCard;

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
