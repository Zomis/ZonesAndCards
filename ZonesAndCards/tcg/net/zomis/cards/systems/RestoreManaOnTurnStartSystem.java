package net.zomis.cards.systems;

import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.ManaComponent;
import net.zomis.cards.events.game.PhaseChangeEvent;
import net.zomis.cards.iface.GameSystem;
import net.zomis.cards.model.GamePhase;

public class RestoreManaOnTurnStartSystem implements GameSystem {

	public RestoreManaOnTurnStartSystem() {
	}

	@Override
	public void onStart(FirstCompGame game) {
		game.registerHandler(PhaseChangeEvent.class, this::phaseChange);
	}
	
	public void phaseChange(PhaseChangeEvent event) {
		GamePhase newPhase = event.getTo();
		CompPlayer player = (CompPlayer) newPhase.getPlayer();
		ManaComponent mana = player.getRequiredComponent(ManaComponent.class);
		mana.restoreAllMana();
	}
	
	
}
