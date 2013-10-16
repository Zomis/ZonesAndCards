package net.zomis.cards.mdjq.cards;

import net.zomis.cards.mdjq.MDJQPermanent;
import net.zomis.cards.mdjq.MDJQStackAction;


public class PlayLandAction extends MDJQStackAction {

	private MDJQPermanent	card;

	public PlayLandAction(MDJQPermanent card) {
		super(MDJQStackAction.ActionType.LAND);
		this.card = card;
		this.useStack = false;
	}

	@Override
	public boolean isAllowed() {
		return card.getController().isAllowLandPlay() && card.getController().isActivePlayer();
	}
	
	@Override
	public void perform() {
		card.zoneMoveOnBottom(card.getGame().getBattlefield());
		card.getController().landPlayed();
	}
	
}
