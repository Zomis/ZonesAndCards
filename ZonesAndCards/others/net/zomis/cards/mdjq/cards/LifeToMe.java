package net.zomis.cards.mdjq.cards;

import net.zomis.cards.mdjq.MDJQEvent;

public class LifeToMe implements TriggeredEffect {
	
	private int	life;

	public LifeToMe(int life) {
		this.life = life;
	}
	@Override
	public void apply(MDJQEvent event) {
		event.getGame().getCurrentPlayer().changeLife(life);
	}

}
