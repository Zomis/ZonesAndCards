package net.zomis.cards.mdjq.cards;

import net.zomis.cards.mdjq.MDJQEvent;
import net.zomis.cards.mdjq.MDJQPlayer;

public class LifeToTarget implements TriggeredEffect, HasTargets {

	private int	life;

	private Targets targets;
	
	public LifeToTarget(TargetStrategy target, int life) {
		this.life = life;
		this.targets = new SingleTarget(target);
	}
	public Targets getTargets() {
		return targets;
	}
	
	@Override
	public void apply(MDJQEvent event) {
		MDJQPlayer player = (MDJQPlayer) this.getTargets().getTargets().get(0).getChosenTarget();
		player.changeLife(life);
	}

}
