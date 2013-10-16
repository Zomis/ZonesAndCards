package net.zomis.cards.mdjq.cards;

import net.zomis.cards.mdjq.MDJQEvent;
import net.zomis.cards.mdjq.MDJQZoneChangeEvent;

public class ETB implements TriggeredAbility {

	private TriggeredEffect[]	effects;

	public ETB(TriggeredEffect... effect) {
		this.effects = effect;
	}
	
	@Override
	public void trigger(MDJQEvent event) {
		if (event instanceof MDJQZoneChangeEvent) {
			MDJQZoneChangeEvent ev = (MDJQZoneChangeEvent) event;
			if (ev.getToZone() == event.getGame().getBattlefield()) {
				event.getGame().addStackAction(new CombinedEffect(event, effects));
			}
		}
	}

}
