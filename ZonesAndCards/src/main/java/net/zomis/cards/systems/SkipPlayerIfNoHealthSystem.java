package net.zomis.cards.systems;

import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.HealthComponent;
import net.zomis.cards.events.game.PhaseChangeEvent;
import net.zomis.cards.iface.GameSystem;

public class SkipPlayerIfNoHealthSystem implements GameSystem {

	@Override
	public void onStart(FirstCompGame game) {
		game.registerHandler(PhaseChangeEvent.class, this::phaseChange);
	}
	
	private void phaseChange(PhaseChangeEvent event) {
		FirstCompGame game = (FirstCompGame) event.getGame();
		CompPlayer player = game.getCurrentPlayer();
		if (!player.compatibility(HealthComponent.class).fails()) {
			return;
		}
		
		if (!player.getComponent(HealthComponent.class).isAlive()) {
			game.nextPhase();
		}
		
	}
	
	

}
