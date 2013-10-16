package net.zomis.cards.mdjq.phases;

import net.zomis.cards.mdjq.MDJQPermanent;
import net.zomis.cards.mdjq.MDJQPhase;
import net.zomis.cards.mdjq.MDJQPlayer;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.custommap.model.CastedIterator;

public class MDJQUntapPhase extends MDJQPhase {

	public MDJQUntapPhase(MDJQPlayer pl) {
		super(pl, "Untap");
	}
	
	@Override
	public void onStart(CardGame game) {
		this.getPlayer().newTurn();
	}
	
	@Override
	public void onEnd(CardGame game) {
		for (MDJQPermanent perm : new CastedIterator<Card, MDJQPermanent>(this.getPlayer().getGame().getBattlefield().cardList())) {
			if (perm.getController() == this.getPlayer())
				perm.untap();
		}
	}
	
}
