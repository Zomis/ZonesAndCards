package net.zomis.cards.wart.triggers;

import net.zomis.cards.wart.HSDoubleEventConsumer;
import net.zomis.cards.wart.HSFilter;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.events.HStoneDoubleCardEvent;
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
