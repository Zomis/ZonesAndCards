package net.zomis.cards.systems;

import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.DeckComponent;
import net.zomis.cards.components.HandComponent;
import net.zomis.cards.events.game.PhaseChangeEvent;
import net.zomis.cards.events2.DrawCardEvent;
import net.zomis.cards.model.phases.PlayerPhase;
import net.zomis.custommap.view.ZomisLog;

public class DrawCardSystem implements GameSystem {

	public DrawCardSystem() {
	}

	@Override
	public void onStart(FirstCompGame game) {
		game.registerHandler(PhaseChangeEvent.class, this::phaseChange);
	}
	
	public void phaseChange(PhaseChangeEvent event) {
		if (event.getTo() instanceof PlayerPhase) {
			PlayerPhase newPhase = (PlayerPhase) event.getTo();
			CompPlayer player = (CompPlayer) newPhase.getPlayer();
			
			if (player.compatibility(HandComponent.class).and(DeckComponent.class).fails())
				return;
			
			HandComponent hand = player.getComponent(HandComponent.class);
			DeckComponent deck = player.getComponent(DeckComponent.class);
			
			ZomisLog.info("Draw a card"); // TODO: Create a method somewhere that draws a card, given a HasComponents object.
			player.getGame().executeEvent(new DrawCardEvent(player, deck.getDeck().getTopCard()), () -> deck.getDeck().getTopCard().zoneMoveOnBottom(hand.getHand()));
			
		}
	}
	
	
}
