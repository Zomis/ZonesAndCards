package net.zomis.cards.hstone.factory;

import net.zomis.cards.events.card.ZoneChangeEvent;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStonePlayer;
import net.zomis.events.EventHandlerGWT;

public class BattlecryTrigger extends HStoneTrigger {

	public BattlecryTrigger(HStoneEffect effect) {
		super(ZoneChangeEvent.class, effect);
	}

	@Override
	public EventHandlerGWT<ZoneChangeEvent> forCard(final HStoneCard card) {
		return new EventHandlerGWT<ZoneChangeEvent>() {
			@Override
			public void executeEvent(ZoneChangeEvent event) {
				if (event.getCard() != card)
					return;
				if (event.getToCardZone() == null)
					return;
				HStonePlayer owner = (HStonePlayer) event.getToCardZone().getOwner();
				if (event.getToCardZone() != owner.getBattlefield())
					return;
				owner.getGame().selectOrPerform(effect, card);
			}
		};
	}

	protected void applyTrigger(ZoneChangeEvent event) {
		
	}

}
