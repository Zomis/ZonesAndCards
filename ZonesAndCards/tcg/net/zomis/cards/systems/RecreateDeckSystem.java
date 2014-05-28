package net.zomis.cards.systems;

import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.DeckComponent;
import net.zomis.cards.components.DeckSourceComponent;
import net.zomis.cards.events2.DrawCardEvent;
import net.zomis.custommap.view.ZomisLog;
import net.zomis.events.EventExecutorGWT;


public class RecreateDeckSystem implements GameSystem {

	@Override
	public void onStart(FirstCompGame game) {
		game.registerHandler(DrawCardEvent.class, this::onDrawCard, EventExecutorGWT.PRE);
	}
	
	private void onDrawCard(DrawCardEvent event) {
		ZomisLog.info("DrawCard Event");
		if (event.getPlayer().compatibility(DeckComponent.class).and(DeckSourceComponent.class).failsThenWarn())
			return;
		
		DeckComponent comp = event.getPlayer().getComponent(DeckComponent.class);
		if (!comp.getDeck().isEmpty())
			return;
		
		ZomisLog.info("Recreating deck");
		DeckSourceComponent source = event.getPlayer().getComponent(DeckSourceComponent.class);
		source.appendTo(comp.getDeck());
		comp.getDeck().shuffle();
		
	}

}