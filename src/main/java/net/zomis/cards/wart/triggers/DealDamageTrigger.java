package net.zomis.cards.wart.triggers;

import net.zomis.cards.wart.HSFilter;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.events.HStoneDamageDealtEvent;
import net.zomis.cards.wart.factory.HStoneEffect;
import net.zomis.events.EventHandlerGWT;

/**
 * When deal damage, use filter (cardWithTrigger, cardThatDealtDamage), and effect (cardThatDealtDamage, cardThatDamageWasDealtTo)
 */
public class DealDamageTrigger extends HStoneTrigger<HStoneDamageDealtEvent> {

	private final HSFilter	filter;

	public DealDamageTrigger(HStoneEffect effect, HSFilter filter) {
		super(HStoneDamageDealtEvent.class, effect);
		this.filter = filter;
	}

	@Override
	public EventHandlerGWT<HStoneDamageDealtEvent> forCard(final HStoneCard cardWithTrigger) {
		return new EventHandlerGWT<HStoneDamageDealtEvent>() {
			@Override
			public void executeEvent(HStoneDamageDealtEvent event) {
				if (filter.shouldKeep(cardWithTrigger, event.getCard())) {
					effect.performEffect(event.getCard(), event.getTarget());
				}
			}
		};
	}

}
