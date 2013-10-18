package net.zomis.cards.mdjq.effects;

import net.zomis.cards.mdjq.cards.TriggerData;
import net.zomis.cards.mdjq.cards.TriggeredEffect;


public class LifeToMe implements TriggeredEffect {
	
	private int	life;

	public LifeToMe(int life) {
		this.life = life;
	}
	@Override
	public void apply(TriggerData event) {
		event.getPlayer().changeLife(life);
	}

}
