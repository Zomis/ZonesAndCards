package net.zomis.cards.wart.actions;

import net.zomis.cards.model.StackAction;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStonePlayer;
import net.zomis.cards.wart.HStoneRes;
import net.zomis.cards.wart.events.HStoneCardPlayedEvent;
import net.zomis.cards.wart.factory.HStoneCardModel;
import net.zomis.cards.wart.factory.HStoneEffect;

public class PlayAction extends StackAction {

	private final HStoneCard	card;
	private final HStonePlayer owner;

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
		owner.getResources().changeResources(HStoneRes.CARDS_PLAYED, 1);
		card.getGame().callEvent(new HStoneCardPlayedEvent(card));
		
		switch (model.getType()) {
			case MINION:
				card.zoneMoveOnBottom(owner.getBattlefield());
				break;
			case SPELL:
				card.getGame().selectOrPerform(model.getEffect(), card);
				if (card.getCurrentZone() == owner.getHand()) // if the card is a secret it should have been placed in a different zone already
					card.zoneMoveOnBottom(owner.getDiscard());
				break;
			case WEAPON:
				card.getPlayer().equip(card);
				break;
			default:
				break;
		}
		card.getGame().cleanup();
	}
	
	private int getManaCost() {
		return card.getManaCost();
	}
	
	@Override
	public boolean actionIsAllowed() {
		int manaCost = getManaCost();
		if (!owner.getResources().hasResources(HStoneRes.MANA_AVAILABLE, manaCost)) {
			return setErrorMessage("Not enough resources, needs " + manaCost + " but has " + owner.getResources());
		}
		if (model().isMinion() && owner.getBattlefield().size() == HStonePlayer.MAX_BATTLEFIELD_SIZE) {
			return setErrorMessage("Battlefield is full");
		}
		HStoneEffect effect = card.getModel().getEffect();
		if (effect != null) {
			return effect.hasAnyAvailableTargets(card);
		}
		return true;
	}
	
	@Override
	public String toString() {
		return owner + " playing " + card;
	}

}
