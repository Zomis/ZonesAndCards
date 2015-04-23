package net.zomis.cards.crgame;

import net.zomis.cards.model.actions.ZoneMoveAction;

public class CRPlayAction extends ZoneMoveAction {

	private final CRCard card;

	public CRPlayAction(CRCard card) {
		super(card);
		this.card = card;
		if (card.getModel().isUser()) {
			setDestination(card.getPlayer().getUserZone());
		}
		else if (card.getModel().isZombie()) {
			setDestination(card.getPlayer().getZombieZone());
		}
		else setDestination(card.getPlayer().getDiscard());
	}
	
	private boolean needsTargets() {
		return card.getModel().needsTargets();
	}
	
	@Override
	public boolean actionIsAllowed() {
		if (needsTargets()) {
			if (card.getGame().findAll(card, card.getModel().getTargets()).isEmpty())
				return setErrorMessage("No valid targets");
		}
		boolean enoughResources = card.getPlayer().getResources().hasResources(CRRes.HOURS_AVAILABLE, card.getModel().getCost());
		if (!enoughResources)
			setErrorMessage("Not enough resources");
		return enoughResources;
	}
	
	@Override
	protected void onPerform() {
		CRCardModel model = card.getModel();
		card.getPlayer().getResources().changeResources(CRRes.HOURS_AVAILABLE, -card.getModel().getCost());
		if (model.isSpell()) {
			if (needsTargets()) {
				card.getGame().setTargets(card, model.getTargets(), model.getEffect());
			}
			else card.getModel().getEffect().apply(card, null);
		}
		super.onPerform();
	}
	
	public CRCard getCard() {
		return card;
	}

}
