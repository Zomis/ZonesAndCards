package net.zomis.cards.systems;

import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.HandComponent;
import net.zomis.cards.events2.DetermineActionEvent;
import net.zomis.cards.events2.FindUsableCardsEvent;
import net.zomis.cards.iface.GameSystem;
import net.zomis.cards.iface.HasComponents;
import net.zomis.cards.model.CardZone;

public class PlayHandCostEffectSystem implements GameSystem {

	@Override
	public void onStart(FirstCompGame game) {
		game.registerHandler(FindUsableCardsEvent.class, this::addHandCards);
		game.registerHandler(DetermineActionEvent.class, this::determineAction);
	}
	
	public void addHandCards(FindUsableCardsEvent event) {
		HandComponent hand = event.getPlayer().getRequiredComponent(HandComponent.class);
		event.addZoneToResult(hand.getHand());
	}
	
	private void determineAction(DetermineActionEvent event) {
		CompPlayer owner = event.getSource().getOwner();
		if (owner == null || !owner.hasComponent(HandComponent.class))
			return;
		
		CardZone<?> zone = event.getSource().getCurrentZone();
		HasComponents currentPlayer = event.getSource().getGame().getCurrentPlayer();
		if (currentPlayer == null)
			return;
		
		if (zone == currentPlayer.getComponent(HandComponent.class).getHand()) {
			event.setAction(new CostAndEffectAction(event.getSource()));
		}
	}

}
