package net.zomis.cards.hstone.triggers;

import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.events.HStoneDamagedEvent;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.events.EventHandlerGWT;

public class DamageTrigger extends HStoneTrigger<HStoneDamagedEvent> {

	private HSFilter	filter;

	public DamageTrigger(HStoneEffect effect, HSFilter filter) {
		super(HStoneDamagedEvent.class, effect);
		this.filter = filter;
	}

	@Override
	public EventHandlerGWT<HStoneDamagedEvent> forCard(final HStoneCard card) {
		return new EventHandlerGWT<HStoneDamagedEvent>() {
			@Override
			public void executeEvent(HStoneDamagedEvent event) {
				if (filter.shouldKeep(card, event.getCard())) {
					effect.performEffect(card, event.getCard());
				}
			}
		};
	}

}
