package net.zomis.cards.systems;

import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.events2.DetermineActionEvent;

public class CostAndEffectSystem implements GameSystem {

	@Override
	public void onStart(FirstCompGame game) {
		game.registerHandler(DetermineActionEvent.class, this::determineAction);
	}
	
	private void determineAction(DetermineActionEvent event) {
		if (event.getAction() == null)
			event.setAction(new CostAndEffectAction(event.getCard()));
	}

}
