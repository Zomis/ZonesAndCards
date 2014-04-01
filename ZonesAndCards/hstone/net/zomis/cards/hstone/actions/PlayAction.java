package net.zomis.cards.hstone.actions;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStonePlayer;
import net.zomis.cards.hstone.HStoneRes;
import net.zomis.cards.hstone.factory.HStoneCardModel;
import net.zomis.cards.model.StackAction;

public class PlayAction extends StackAction {

	private HStoneCard	card;
	private HStonePlayer owner;

	public PlayAction(HStoneCard card) {
		this.card = card;
		this.owner = (HStonePlayer) card.getCurrentZone().getOwner();
	}
	
	private HStoneCardModel model() {
		return (HStoneCardModel) card.getModel();
	}
	
	@Override
	protected void onPerform() {
		owner.getResources().changeResources(HStoneRes.MANA_AVAILABLE, -model().getManaCost());
		HStoneCardModel model = model();
		if (model.isMinion()) {
			card.zoneMoveOnBottom(owner.getBattlefield());
		}
		else if (model.isSpell()) {
			card.getGame().selectOrPerform(model.getEffect(), card);
			card.zoneMoveOnBottom(null);
		}
	}
	
	@Override
	public boolean actionIsAllowed() {
		if (!owner.getResources().hasResources(HStoneRes.MANA_AVAILABLE, model().getManaCost())) {
			return setErrorMessage("Not enough resources, needs " + model().getManaCost() + " but has " + owner.getResources());
		}
		return true;
	}
	
	@Override
	public String toString() {
		return owner + " playing " + card;
	}

}
