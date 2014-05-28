package net.zomis.cards.systems;

import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.ResourceMWKComponent;
import net.zomis.cards.events.game.PhaseChangeEvent;
import net.zomis.cards.model.phases.PlayerPhase;

public class GainMWKResourcesSystem implements GameSystem {

	@Override
	public void onStart(FirstCompGame game) {
		game.registerHandler(PhaseChangeEvent.class, this::phaseChange);
	}
	
	public void phaseChange(PhaseChangeEvent event) {
		if (event.getTo() instanceof PlayerPhase) {
			PlayerPhase newPhase = (PlayerPhase) event.getTo();
			CompPlayer player = (CompPlayer) newPhase.getPlayer();
			
			ResourceMWKComponent resources = player.getRequiredComponent(ResourceMWKComponent.class);
			int increase = resources.getKings();
			resources.setMages(resources.getMages() + increase);
			resources.setWarriors(resources.getWarriors() + increase);
		}
	}

}
