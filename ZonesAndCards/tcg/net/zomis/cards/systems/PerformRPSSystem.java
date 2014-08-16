package net.zomis.cards.systems;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.ChosenCardComponent;
import net.zomis.cards.components.HealthComponent;
import net.zomis.cards.components.RPSComponent;
import net.zomis.cards.events.game.PhaseChangeEvent;
import net.zomis.cards.iface.GameSystem;
import net.zomis.cards.systems.RPSCardsSystem.RPS;
import net.zomis.custommap.view.ZomisLog;
import net.zomis.fight.ext.WinResult;

public class PerformRPSSystem implements GameSystem {

	@Override
	public void onStart(FirstCompGame game) {
		game.registerHandler(PhaseChangeEvent.class, this::turnChange);
	}
	
	private void turnChange(PhaseChangeEvent event) {
		FirstCompGame game = (FirstCompGame) event.getGame();
		if (game.getCurrentPlayer() == game.getPlayers().get(0)) {
			
			for (CompPlayer pl : game.getPlayers()) {
				CardWithComponents card = pl.getRequiredComponent(ChosenCardComponent.class).getCard();
				if (card == null)
					return;
				RPS rpsValue = card.getModel().getRequiredComponent(RPSComponent.class).getRps();
				for (CompPlayer opponent : game.getPlayers()) {
					CardWithComponents card2 = opponent.getRequiredComponent(ChosenCardComponent.class).getCard();
					RPS oppValue = card2.getModel().getRequiredComponent(RPSComponent.class).getRps();
					WinResult fight = RPSCardsSystem.fight(rpsValue, oppValue);
					ZomisLog.info(fight + ": " + rpsValue + " vs. " + oppValue);
					pl.getRequiredComponent(HealthComponent.class).heal(fight.winValueInt());
//					opponent.getRequiredComponent(HealthComponent.class).heal(fight.reversed().winValueInt());
				}
			}
		}
	}

}
