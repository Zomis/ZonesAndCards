package net.zomis.cards.hstone.actions;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.factory.CardType;
import net.zomis.cards.model.StackAction;

public class BattlefieldAction extends StackAction {

	private HStoneCard	card;

	public BattlefieldAction(HStoneCard card) {
		this.card = card;
	}
	
	@Override
	public boolean actionIsAllowed() {
		if (card.getGame().isTargetSelectionMode()) {
			return card.getGame().isTargetAllowed(card);
		}
		return card.isAttackPossible();
	}
	
	@Override
	protected void onFailedPerform() {
		card.getGame().setTargetFilter(null, null);
	}

	@Override
	protected void onPerform() {
		if (card.getGame().isTargetSelectionMode()) {
			if (card.getGame().getTargetsFor() instanceof HStoneCard) {
				HStoneCard cardSource = (HStoneCard) card.getGame().getTargetsFor();
				if (cardSource.getModel().isSpell() || cardSource.getModel().isType(CardType.POWER)) {
					cardSource.getModel().getEffect().performEffect(cardSource, card);
					card.getGame().setTargetFilter(null, null);
					return;
				}
			}
			HStoneGame game = card.getGame();
			card.getGame().addAndProcessFight(card.getGame().getTargetsFor(), card);
			game.setTargetFilter(null, null);
		}
		else card.getGame().setTargetFilter(new AttackSelection(card), card);
		
	}
	
}
