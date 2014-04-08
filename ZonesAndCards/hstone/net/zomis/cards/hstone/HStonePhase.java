package net.zomis.cards.hstone;

import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.phases.PlayerPhase;
import net.zomis.cards.resources.ResourceMap;

public class HStonePhase extends PlayerPhase {

	public HStonePhase(HStonePlayer pl) {
		super(pl);
	}
	
	@Override
	public HStonePlayer getPlayer() {
		return (HStonePlayer) super.getPlayer();
	}

	@Override
	public void onStart(CardGame<?, ?> game) {
		ResourceMap res = getPlayer().getResources();
		
		if (!res.hasResources(HStoneRes.MANA_TOTAL, 10))
			res.changeResources(HStoneRes.MANA_TOTAL, 1);
		
		res.set(HStoneRes.MANA_AVAILABLE, res.getResources(HStoneRes.MANA_TOTAL) - res.getResources(HStoneRes.MANA_OVERLOAD));
		res.set(HStoneRes.MANA_OVERLOAD, 0);
		
		getPlayer().drawCard();
	}
	
	@Override
	public void onEnd(CardGame<?, ?> game) {
		HStonePlayer pl = (HStonePlayer) getPlayer();
		for (HStoneCard card : pl.getBattlefield()) {
			card.onEndTurn();
		}
		for (HStoneCard card : pl.getSpecialZone()) {
			card.onEndTurn();
		}
		
		super.onEnd(game);
	}
}
