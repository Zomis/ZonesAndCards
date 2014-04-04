package net.zomis.cards.hstone.factory;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.events.EventHandlerGWT;
import net.zomis.events.IEvent;

public abstract class HStoneTrigger {

	private Class<? extends IEvent> clazz;
	protected final HStoneEffect	effect;

	public <T extends IEvent> HStoneTrigger(Class<T> eventClass, HStoneEffect effect) {
		this.clazz = eventClass;
		this.effect = effect;
	}
	
	public Class<? extends IEvent> getClazz() {
		return clazz;
	}

	public abstract EventHandlerGWT<? extends IEvent> forCard(HStoneCard card);
	
}
