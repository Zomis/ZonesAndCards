package net.zomis.cards.systems;

import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.HandComponent;
import net.zomis.cards.events2.FindUsableCardsEvent;

public class PlayHandSystem implements GameSystem {

	@Override
	public void onStart(FirstCompGame game) {
		game.registerHandler(FindUsableCardsEvent.class, this::addHandCards);
	}
	
	public void addHandCards(FindUsableCardsEvent event) {
		HandComponent hand = event.getPlayer().getRequiredComponent(HandComponent.class);
		event.addZoneToResult(hand.getHand());
	}

}
