package net.zomis.cards.systems;

import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.ManaComponent;
import net.zomis.cards.events.game.PhaseChangeEvent;
import net.zomis.cards.iface.GameSystem;
import net.zomis.cards.model.GamePhase;

public class IncreaseManaSystem implements GameSystem {

	private final int increase;
	private final int max;

	public IncreaseManaSystem(int increase, int max) {
		this.increase = increase;
		this.max = max;
	}

	@Override
	public void onStart(FirstCompGame game) {
		game.registerHandler(PhaseChangeEvent.class, this::phaseChange);
	}
	
	public void phaseChange(PhaseChangeEvent event) {
		GamePhase newPhase = event.getTo();
		CompPlayer player = (CompPlayer) newPhase.getPlayer();
		
		ManaComponent mana = player.getRequiredComponent(ManaComponent.class);
		int currentMax = mana.getMaxMana();
		
		if (currentMax + increase >= max) {
			mana.setMaxMana(max);
		}
		else {
			mana.setMaxMana(currentMax + increase);
		}
	}
	
	
}
