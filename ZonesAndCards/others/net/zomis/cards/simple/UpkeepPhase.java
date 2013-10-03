package net.zomis.cards.simple;

import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.phases.PlayerPhase;


public class UpkeepPhase extends PlayerPhase {

	public UpkeepPhase(Player player) {
		super(player);
	}
	
	@Override
	public void onStart(CardGame game) {
		SimplePlayer pl = (SimplePlayer) this.getPlayer();
		pl.changeResources(pl.getResourcesPerTurn());
		if (!pl.library.cardList().isEmpty())
			pl.library.cardList().element().zoneMove(pl.hand, pl);
	}

	@Override
	public String toString() {
		return "Upkeep-" + super.toString();
	}
	
}
