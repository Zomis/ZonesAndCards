package net.zomis.cards.hstone.actions;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.model.StackAction;

public class BattlefieldAction extends StackAction {

	private HStoneCard	card;

	public BattlefieldAction(HStoneCard card) {
		this.card = card;
	}
	
	@Override
	public boolean actionIsAllowed() {
		if (card.getGame().isTargetSelectionMode()) {
			if (!card.getGame().isTargetAllowed(card))
				return setErrorMessage("Target not allowed: " + card);
			return true;
		}
		return card.isAttackPossible();
	}
	
	@Override
	protected void onFailedPerform() {
		card.getGame().setTargetFilter(null, null);
	}

	@Override
	protected void onPerform() {
		HStoneGame game = card.getGame();
		// TODO: Cleanup this method. If my assertions are correct, the if (effect != null) check is useless.
		if (game.isTargetSelectionMode()) {
			HStoneCard cardSource = game.getTargetsFor();
			HStoneEffect effect = game.getTargetsForEffect();
			if (effect != null) {
				effect.performEffect(cardSource, card);
				game.setTargetFilter(null, null);
				game.cleanup();
				return;
				
//				if (cardSource.getModel().isSpell() || cardSource.getModel().isType(CardType.POWER)) {
//					cardSource.getModel().getEffect().performEffect(cardSource, card);
//					game.setTargetFilter(null, null);
//					game.cleanup();
//					return;
//				}
			}
			else throw new AssertionError("Expected isTargetSelectionMode to match getTargetsForEffect != null");
		}
		else card.getGame().setTargetFilter(new AttackSelection(card), card);
		
		game.cleanup();
	}
	
}
