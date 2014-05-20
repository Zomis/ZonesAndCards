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
	public void onStart(CardGame<?, ?> gm) {
		ResourceMap res = getPlayer().getResources();
		
		if (!res.hasResources(HStoneRes.MANA_TOTAL, 10))
			res.changeResources(HStoneRes.MANA_TOTAL, 1);
		
		res.set(HStoneRes.MANA_AVAILABLE, res.getResources(HStoneRes.MANA_TOTAL) - res.getResources(HStoneRes.MANA_OVERLOAD));
		res.set(HStoneRes.MANA_OVERLOAD, 0);
		
		HStoneGame game = getPlayer().getGame();
		getPlayer().drawCard();
		getPlayer().onStartTurn();
		game.increaseTurnCounter();
		game.executeTurnStartEvent();
		game.cleanup();
	}
	
	@Override
	public void onEnd(CardGame<?, ?> gm) {
		HStonePlayer pl = (HStonePlayer) getPlayer();
		for (HStoneCard card : pl.getNextPlayer().getBattlefield()) {
			card.onEndTurnOpponent();
		}
		for (HStoneCard card : pl.getBattlefield()) {
			card.onEndTurn();
		}
		for (HStoneCard card : pl.getSpecialZone()) {
			card.onEndTurn();
		}
		HStoneGame game = pl.getGame();
		game.executeTurnEndEvent();
		game.cleanup();
	}
}
