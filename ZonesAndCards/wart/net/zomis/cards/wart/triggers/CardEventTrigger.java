package net.zomis.cards.wart.triggers;

import net.zomis.cards.wart.HSFilter;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.events.HStoneCardEvent;
import net.zomis.cards.wart.factory.HStoneEffect;
import net.zomis.events.EventHandlerGWT;

public class CardEventTrigger extends HStoneTrigger<HStoneCardEvent> {

	private final HSFilter filter;

	public CardEventTrigger(Class<? extends HStoneCardEvent> eventClass, HStoneEffect effect, HSFilter filter) {
		super(eventClass, effect);
		this.filter = filter;
	}

	@Override
	public EventHandlerGWT<HStoneCardEvent> forCard(final HStoneCard cardWithTrigger) {
		return new EventHandlerGWT<HStoneCardEvent>() {
			@Override
			public void executeEvent(HStoneCardEvent event) {
				if (filter.shouldKeep(cardWithTrigger, event.getCard())) {
					effect.performEffect(cardWithTrigger, event.getCard());
				}
			}
		};
	}

}
