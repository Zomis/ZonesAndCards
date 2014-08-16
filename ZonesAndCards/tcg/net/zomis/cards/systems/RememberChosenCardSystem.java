package net.zomis.cards.systems;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.ChosenCardComponent;
import net.zomis.cards.events.card.CardPlayedEvent;
import net.zomis.cards.iface.GameSystem;
import net.zomis.custommap.view.ZomisLog;
import net.zomis.events.EventExecutorGWT;

public class RememberChosenCardSystem implements GameSystem {

	@Override
	public void onStart(FirstCompGame game) {
		for (CompPlayer pl : game.getPlayers()) {
			pl.compatibility(ChosenCardComponent.class).required();
		}
		game.registerHandler(CardPlayedEvent.class, this::cardPlayed, EventExecutorGWT.PRE);
	}
	
	private void cardPlayed(CardPlayedEvent event) {
		CardWithComponents played = (CardWithComponents) event.getCard();
		ChosenCardComponent component = played.getOwner().getComponent(ChosenCardComponent.class);
		ZomisLog.info("Set card played for " + played.getOwner() + " to " + played);
		component.setCard(played);
	}

}
