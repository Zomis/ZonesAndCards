package net.zomis.cards.hstone.triggers;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.events.EventHandlerGWT;
import net.zomis.events.IEvent;

public abstract class HStoneTrigger<T extends IEvent> {

	private Class<? extends T> clazz;
	protected final HStoneEffect	effect;

	public HStoneTrigger(Class<? extends T> eventClass, HStoneEffect effect) {
		this.clazz = eventClass;
		this.effect = effect;
	}
	
	public Class<? extends T> getClazz() {
		return clazz;
	}

	public EventHandlerGWT<T> createForCard(final HStoneCard card) {
		final EventHandlerGWT<T> forwarding = forCard(card);
		return new EventHandlerGWT<T>() {
			@Override
			public void executeEvent(T event) {
				if (card.hasTrigger(HStoneTrigger.this)) {
					forwarding.executeEvent(event);
				}
			}
		};
	}
	
	public abstract EventHandlerGWT<T> forCard(HStoneCard card);
	
}
