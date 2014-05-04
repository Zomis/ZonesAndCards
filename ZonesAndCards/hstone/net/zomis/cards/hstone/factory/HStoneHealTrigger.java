package net.zomis.cards.hstone.factory;

import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.events.HStoneHealEvent;
import net.zomis.cards.hstone.triggers.HStoneTrigger;
import net.zomis.events.EventHandlerGWT;

public class HStoneHealTrigger extends HStoneTrigger<HStoneHealEvent> {

	private HSFilter	filter;

	public HStoneHealTrigger(HStoneEffect effect, HSFilter filter) {
		super(HStoneHealEvent.class, effect);
		this.filter = filter;
	}

	@Override
	public EventHandlerGWT<HStoneHealEvent> forCard(final HStoneCard card) {
		return new EventHandlerGWT<HStoneHealEvent>() {
			@Override
			public void executeEvent(HStoneHealEvent event) {
				if (filter.shouldKeep(null, event.getCard())) {
					effect.performEffect(card, event.getCard());
				}
			}
		};
	}

}
