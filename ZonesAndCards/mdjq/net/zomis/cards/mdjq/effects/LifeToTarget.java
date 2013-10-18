package net.zomis.cards.mdjq.effects;

import net.zomis.cards.mdjq.MDJQPlayer;
import net.zomis.cards.mdjq.cards.TriggerData;
import net.zomis.cards.mdjq.cards.TriggeredEffect;
import net.zomis.cards.mdjq.targets.HasTargets;
import net.zomis.cards.mdjq.targets.SingleTarget;
import net.zomis.cards.mdjq.targets.TargetStrategy;
import net.zomis.cards.mdjq.targets.Targets;

public class LifeToTarget implements TriggeredEffect, HasTargets {

	private int	life;

	private Targets targets;
	
	public LifeToTarget(TargetStrategy target, int life) {
		this.life = life;
		this.targets = new SingleTarget(target);
	}
	
	@Override
	public Targets getTargets() {
		return targets;
	}
	
	@Override
	public void apply(TriggerData data) {
		MDJQPlayer player = (MDJQPlayer) this.targets.getTargets().get(0).getChosenTarget();
		player.changeLife(life);
	}

}
