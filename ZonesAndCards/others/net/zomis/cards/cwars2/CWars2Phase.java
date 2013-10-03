package net.zomis.cards.cwars2;

import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.phases.PlayerPhase;
import net.zomis.cards.util.ResourceMap;
import net.zomis.cards.util.ResourceType;

public class CWars2Phase extends PlayerPhase {

	public CWars2Phase(Player pl) {
		super(pl);
	}

	@Override
	public void onStart(CardGame gam) {
		CWars2Game game = (CWars2Game) gam;
		for (int i = 0; i < game.getProducers().length; i++) {
			ResourceType prod = game.getProducers()[i];
			ResourceType res = game.getRestypes()[i];
			ResourceMap resources = this.getPlayer().getResources();
			resources.changeResources(res, resources.getResources(prod));
		}
	}
	
	@Override
	public CWars2Player getPlayer() {
		return (CWars2Player) super.getPlayer();
	}
	
}
