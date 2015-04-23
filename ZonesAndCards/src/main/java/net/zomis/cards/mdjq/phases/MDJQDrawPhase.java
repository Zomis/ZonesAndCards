package net.zomis.cards.mdjq.phases;

import net.zomis.cards.mdjq.MDJQPhase;
import net.zomis.cards.mdjq.MDJQPlayer;
import net.zomis.cards.model.CardGame;

public class MDJQDrawPhase extends MDJQPhase {

	public MDJQDrawPhase(MDJQPlayer pl) {
		super(pl, "Draw");
	}

	@Override
	public void onEnd(CardGame<?, ?> game) {
		this.getPlayer().getLibrary().getTopCard().zoneMoveOnBottom(this.getPlayer().getHand());
	}
	
}
