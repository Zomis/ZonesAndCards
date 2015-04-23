package net.zomis.cards.mdjq.phases;

import net.zomis.cards.mdjq.MDJQPermanent;
import net.zomis.cards.mdjq.MDJQPhase;
import net.zomis.cards.mdjq.MDJQPlayer;
import net.zomis.cards.model.CardGame;

public class MDJQUntapPhase extends MDJQPhase {

	public MDJQUntapPhase(MDJQPlayer pl) {
		super(pl, "Untap");
	}
	
	@Override
	public void onStart(CardGame<?, ?> game) {
		this.getPlayer().newTurn();
	}
	
	@Override
	public void onEnd(CardGame<?, ?> game) {
		for (MDJQPermanent perm : this.getPlayer().getGame().getBattlefield()) {
			if (perm.getController() == this.getPlayer())
				perm.untap();
		}
	}
	
}
