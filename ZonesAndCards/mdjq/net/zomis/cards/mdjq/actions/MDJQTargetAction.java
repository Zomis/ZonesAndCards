package net.zomis.cards.mdjq.actions;

import net.zomis.cards.mdjq.MDJQGame;
import net.zomis.cards.mdjq.MDJQPermanent;
import net.zomis.cards.mdjq.MDJQPlayer;
import net.zomis.cards.mdjq.MDJQStackAction;
import net.zomis.cards.mdjq.targets.HasTargets;
import net.zomis.cards.mdjq.targets.Targets;

public class MDJQTargetAction extends MDJQStackAction implements HasTargets {

	private final Targets	targets;
	private final MDJQPlayer	targetChooser;
	private final MDJQPermanent	permanent;

	public MDJQTargetAction(ActionType type, MDJQPermanent permanent) {
		super(type);
		this.targets = new Targets();
		this.permanent = permanent;
		this.targetChooser = permanent.getController();
	}
	
	public MDJQPlayer getTargetChooser() {
		return targetChooser;
	}
	
	public MDJQPermanent getPermanent() {
		return permanent;
	}
	
	@Override
	public Targets getTargets() {
		return this.targets;
	}

	public MDJQGame getGame() {
		return this.permanent.getGame();
	}

}
