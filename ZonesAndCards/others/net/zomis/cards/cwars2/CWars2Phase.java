package net.zomis.cards.cwars2;

import net.zomis.cards.cwars2.CWars2Res.Producers;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.phases.PlayerPhase;
import net.zomis.cards.resources.IResource;
import net.zomis.cards.resources.ResourceMap;

public class CWars2Phase extends PlayerPhase {

	CWars2Phase() {
		super(null);
	}
	public CWars2Phase(Player pl) {
		super(pl);
	}

	@Override
	public void onStart(CardGame<?, ?> gam) {
		for (Producers producer : Producers.values()) {
			IResource res = producer.getResource();
			ResourceMap resources = this.getPlayer().getResources();
			resources.changeResources(res, resources.getResources(producer));
			resources.setResourceStrategy(producer, null);
		}
	}
	
	@Override
	public CWars2Player getPlayer() {
		return (CWars2Player) super.getPlayer();
	}
	
}
