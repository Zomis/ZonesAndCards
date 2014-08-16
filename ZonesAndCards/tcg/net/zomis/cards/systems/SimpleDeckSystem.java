package net.zomis.cards.systems;

import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.DeckSourceComponent;
import net.zomis.cards.iface.GameSystem;
import net.zomis.cards.util.DeckBuilder;
import net.zomis.cards.util.DeckList;
import net.zomis.custommap.view.ZomisLog;

public class SimpleDeckSystem implements GameSystem {

	@Override
	public void onStart(FirstCompGame game) {
		ZomisLog.info(this.toString());
		for (CompPlayer pl : game.getPlayers()) {
			if (pl.compatibility(DeckSourceComponent.class).failsThenWarn())
				return;
			ZomisLog.info("Creating deck for " + pl);
			DeckList deck = new DeckList("Deck").add(52, "Random Card");
			DeckBuilder.createExact(pl.getComponent(DeckSourceComponent.class), deck.getCount(game));
		}
	}
	
}
