package net.zomis.cards.systems;

import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.DeckComponent;
import net.zomis.cards.components.DeckSourceComponent;
import net.zomis.cards.iface.GameSystem;

public class CreateDeckOnceFromSourceSystem implements GameSystem {

	@Override
	public void onStart(FirstCompGame game) {
		for (CompPlayer player : game.getPlayers()) {
			DeckComponent comp = player.getComponent(DeckComponent.class);
			DeckSourceComponent source = player.getComponent(DeckSourceComponent.class);
			source.appendTo(comp.getDeck());
			comp.getDeck().shuffle();
		}
	}

}
