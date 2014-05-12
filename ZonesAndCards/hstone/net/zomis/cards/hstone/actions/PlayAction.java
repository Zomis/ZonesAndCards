package net.zomis.cards.hstone.actions;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStonePlayer;
import net.zomis.cards.hstone.HStoneRes;
import net.zomis.cards.hstone.events.HStoneCardPlayedEvent;
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
		HStoneCardModel model = model();
		owner.getResources().changeResources(HStoneRes.MANA_AVAILABLE, -getManaCost());
		card.getGame().callEvent(new HStoneCardPlayedEvent(card));
		if (model.isMinion()) {
			card.zoneMoveOnBottom(owner.getBattlefield());
		}
		else if (model.isSpell()) {
			card.getGame().selectOrPerform(model.getEffect(), card);
			card.zoneMoveOnBottom(owner.getDiscard());
		}
	}
	
	private int getManaCost() {
		return card.getGame().getResources(card, HStoneRes.MANA_COST);
	}
	
	@Override
	public boolean actionIsAllowed() {
		int manaCost = model().getManaCost();
		if (!owner.getResources().hasResources(HStoneRes.MANA_AVAILABLE, manaCost)) {
			return setErrorMessage("Not enough resources, needs " + manaCost + " but has " + owner.getResources());
		}
		// TODO: Make sure that there is at least one available target for the action, if action requires a target
		return true;
	}
	
	@Override
	public String toString() {
		return owner + " playing " + card;
	}

}
