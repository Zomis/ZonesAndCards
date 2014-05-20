package net.zomis.cards.hstone.triggers;

import net.zomis.cards.hstone.HSDoubleEventConsumer;
import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.events.HStoneDoubleCardEvent;
import net.zomis.events.EventHandlerGWT;

public class CardEventDoubleTrigger extends HStoneTrigger<HStoneDoubleCardEvent> {

	private final HSFilter filterSource;
	private final HSFilter filterTarget;
	private final HSDoubleEventConsumer	eventHandler;

	public CardEventDoubleTrigger(Class<? extends HStoneDoubleCardEvent> eventClass, HSDoubleEventConsumer effect, HSFilter filterSource, HSFilter filterTarget) {
		super(eventClass);
		this.filterSource = filterSource;
		this.filterTarget = filterTarget;
		this.eventHandler = effect;
	}

	@Override
	public EventHandlerGWT<HStoneDoubleCardEvent> forCard(final HStoneCard cardWithTrigger) {
		return new EventHandlerGWT<HStoneDoubleCardEvent>() {
			@Override
			public void executeEvent(HStoneDoubleCardEvent event) {
				boolean keepA = filterSource == null || filterSource.shouldKeep(cardWithTrigger, event.getSource());
				boolean keepB = filterTarget == null || filterTarget.shouldKeep(cardWithTrigger, event.getTarget());
				if (keepA && keepB) {
					eventHandler.handleEvent(cardWithTrigger, event);
				}
			}
		};
	}

}
