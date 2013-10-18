package net.zomis.cards.mdjq.actions;

import net.zomis.cards.mdjq.MDJQPermanent;
import net.zomis.cards.mdjq.MDJQRes;
import net.zomis.cards.mdjq.MDJQStackAction;
import net.zomis.cards.util.ResourceMap;

public class PlaySpellAction extends MDJQStackAction {

	private MDJQPermanent card;

	public PlaySpellAction(MDJQPermanent card) {
		super(ActionType.SPELL);
		this.card = card;
	}
	
	@Override
	public boolean isAllowed() {
		ResourceMap manaPool = card.getController().getManaPool();
		ResourceMap manaCost = card.getModel().getManaCost();
		return card.getController().hasPriority() && MDJQRes.hasResources(manaPool, manaCost);
	}
	
	@Override
	public void onAddToStack() {
		ResourceMap manaPool = card.getController().getManaPool();
		ResourceMap manaCost = card.getModel().getManaCost();
		MDJQRes.changeResources(manaPool, manaCost, -1);
	}
	
	@Override
	public void onPerform() {
//		CustomFacade.getLog().i("Card move to battlefield " + card);
		if (card.getModel().isPermanent()) {
			card.zoneMoveOnBottom(card.getGame().getBattlefield());
		}
		else throw new UnsupportedOperationException("Non-Permanents are not implemented yet.");
	}

}
