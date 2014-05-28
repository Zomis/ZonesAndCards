package net.zomis.cards.systems;

import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.events.game.PhaseChangeEvent;
import net.zomis.cards.helpers.DrawCardHelper;
import net.zomis.cards.model.phases.PlayerPhase;

public class DrawCardAtBeginningOfTurnSystem implements GameSystem {

	public DrawCardAtBeginningOfTurnSystem() {
	}

	@Override
	public void onStart(FirstCompGame game) {
		game.registerHandler(PhaseChangeEvent.class, this::phaseChange);
	}
	
	public void phaseChange(PhaseChangeEvent event) {
		if (event.getTo() instanceof PlayerPhase) {
			PlayerPhase newPhase = (PlayerPhase) event.getTo();
			CompPlayer player = (CompPlayer) newPhase.getPlayer();
			DrawCardHelper.drawcard(player);
		}
	}
	
	
}