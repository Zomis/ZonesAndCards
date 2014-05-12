package net.zomis.cards.crgame;

import net.zomis.cards.model.StackAction;

public class CRTargetChosenAction extends StackAction {

	private final CRCard card;

	public CRTargetChosenAction(CRCard card) {
		this.card = card;
	}
	
	@Override
	public boolean actionIsAllowed() {
		return card.getGame().getTargetParameters().isValidTarget(card);
	}
	
	@Override
	protected void onPerform() {
		CRTargetParameters targetData = card.getGame().getTargetParameters();
		targetData.apply(card);
		card.getGame().clearTargets();
	}
	
}
