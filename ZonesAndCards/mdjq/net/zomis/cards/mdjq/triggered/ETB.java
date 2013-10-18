package net.zomis.cards.mdjq.triggered;

import net.zomis.cards.mdjq.MDJQPermanent;
import net.zomis.cards.mdjq.actions.CombinedEffect;
import net.zomis.cards.mdjq.cards.TriggeredAbility;
import net.zomis.cards.mdjq.cards.TriggeredEffect;
import net.zomis.cards.mdjq.events.MDJQEvent;
import net.zomis.cards.mdjq.events.MDJQZoneChangeEvent;

public class ETB implements TriggeredAbility {

	private TriggeredEffect[]	effects;

	public ETB(TriggeredEffect... effect) {
		this.effects = effect;
	}
	
	@Override
	public void trigger(MDJQPermanent triggeredWhere, MDJQEvent event) {
		if (event instanceof MDJQZoneChangeEvent) {
			MDJQZoneChangeEvent ev = (MDJQZoneChangeEvent) event;
			if (triggeredWhere == ev.getPermanent() && ev.getToZone() == event.getGame().getBattlefield()) {
				event.getGame().addStackAction(new CombinedEffect(triggeredWhere, effects));
			}
		}
	}

}
