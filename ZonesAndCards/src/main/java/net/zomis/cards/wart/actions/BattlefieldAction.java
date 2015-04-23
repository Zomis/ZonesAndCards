package net.zomis.cards.wart.actions;

import net.zomis.cards.model.StackAction;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.factory.CardType;
import net.zomis.cards.wart.factory.HSAbility;
import net.zomis.cards.wart.factory.HStoneEffect;

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
		
		boolean hasAttack = card.getModel().isType(CardType.POWER) || card.getAttack() > 0;
		
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
		if (game.isTargetSelectionMode()) {
			HStoneCard cardSource = game.getTargetsFor();
			HStoneEffect effect = game.getTargetsForEffect();
			effect.performEffect(cardSource, card);
			game.setTargetFilter(null, null);
		}
		else card.getGame().setTargetFilter(new AttackSelection(card), card);
		
		game.cleanup();
	}
	
}
