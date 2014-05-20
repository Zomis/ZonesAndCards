package net.zomis.cards.hstone.actions;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.HStoneRes;
import net.zomis.cards.hstone.factory.CardType;
import net.zomis.cards.hstone.factory.HSAbility;
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
		
		boolean hasAttack = card.getModel().isType(CardType.POWER) || card.getResources().hasResources(HStoneRes.ATTACK, 1);
		if (!hasAttack)
			return setErrorMessage("No attack value");
		
		if (!card.hasActionPoints())
			return setErrorMessage("No action points available");
		
		if (card.hasAbility(HSAbility.FROZEN))
			return setErrorMessage("Frozen");
		if (card.hasAbility(HSAbility.NO_ATTACK))
			return setErrorMessage("Has ability NO_ATTACK");
		return true;
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
			if (cardSource == null)
				throw new NullPointerException("Card source is null");
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
