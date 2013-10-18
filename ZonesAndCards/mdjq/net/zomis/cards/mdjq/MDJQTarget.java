package net.zomis.cards.mdjq;

import java.util.LinkedList;
import java.util.List;

import net.zomis.cards.mdjq.actions.MDJQTargetAction;
import net.zomis.cards.mdjq.targets.TargetStrategy;

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
	/**
	 * For use when something is about to resolve. Check if target is still valid.
	 * @param card
	 * @return
	 */
	public boolean isValidTarget(MDJQTargetAction action) {
		return this.strategy.isValidTarget(action, this.chosenTarget);
	}
	
	public void setChosenTarget(MDJQObject chosenTarget) {
		if (this.isTargetChosen()) {
			throw new IllegalStateException("Target already chosen");
		}
		this.chosenTarget = chosenTarget;
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
	public List<MDJQObject> findLegalTargets(MDJQTargetAction action) {
		List<MDJQObject> possible = new LinkedList<MDJQObject>();
		for (MDJQObject obj : action.getGame().getObjects()) {
			if (this.strategy.isValidTarget(action, obj)) {
				possible.add(obj);
			}
		}
		return possible;
	}
	
}
