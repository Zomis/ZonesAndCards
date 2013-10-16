package net.zomis.cards.mdjq;

import net.zomis.cards.mdjq.cards.TargetStrategy;

public class MDJQTarget {
	private TargetStrategy strategy;
	private boolean required;
	private MDJQObject chosenTarget;
	
	public MDJQTarget(TargetStrategy strategy) {
		this(strategy, true);
	}
	public MDJQTarget(TargetStrategy strategy, boolean required) {
		this.strategy = strategy;
		this.required = required;
	}
	
	public boolean isTargetChosen() {
		return !this.required || this.chosenTarget != null;
	}
	public boolean isValidTarget(MDJQPermanent card) {
		return this.strategy.isValidTarget(card, this.chosenTarget);
	}
	
	public boolean isRequired() {
		return required;
	}
	public MDJQObject getChosenTarget() {
		return chosenTarget;
	}
	public TargetStrategy getStrategy() {
		return strategy;
	}
	
}
