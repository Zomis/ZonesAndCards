package net.zomis.cards.hstone.actions;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.HStonePlayer;
import net.zomis.cards.hstone.HStoneRes;
import net.zomis.cards.hstone.factory.CardType;
import net.zomis.cards.hstone.factory.HSAbility;
import net.zomis.cards.hstone.factory.HStoneEffect;

public class AttackSelection extends HStoneEffect {

	private final HStoneCard source;

	public AttackSelection(HStoneCard source) {
		this.source = source;
	}

	@Override
	public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
		if (source.getPlayer() == target.getPlayer())
			return false;
		
		HStonePlayer cardOwner = target.getPlayer();
		if (target.isType(CardType.PLAYER)) {
			if (!cardOwner.hasTauntMinions())
				return true;
		}
		
		return target.hasAbility(HSAbility.TAUNT) || !cardOwner.hasTauntMinions();
	}

	@Override
	public void performEffect(HStoneCard source, HStoneCard target) {
		HStoneGame game = source.getGame();
		game.addAndProcessFight(source, target);
		if (source.isType(CardType.PLAYER)) {
			HStoneCard weapon = source.getPlayer().getWeapon();
			if (weapon != null) {
				weapon.getResources().changeResources(HStoneRes.HEALTH, -1);
				if (weapon.getHealth() <= 0) {
					weapon.destroy();
					source.getPlayer().destroyWeapon();
				}
			}
		}
		game.setTargetFilter(null, null);
		game.cleanup();
	}

}
