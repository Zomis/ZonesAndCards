package net.zomis.cards.hstone;

import net.zomis.cards.resources.ResourceMap;

public class FightModule {

	public static void attack(HStoneGame game, HStoneTarget source, HStoneTarget target) {
		damage(target, source.getResources().getResources(HStoneRes.ATTACK));
		damage(source, target.getResources().getResources(HStoneRes.ATTACK));
		source.getResources().changeResources(HStoneRes.ACTION_POINTS, -1);
		
		cleanup(game, source);
		cleanup(game, target);
		
		game.cleanup();
	}

	private static void cleanup(HStoneGame game, HStoneTarget source) {
		ResourceMap res = source.getResources();
		int damage = res.getResources(HStoneRes.AWAITING_DAMAGE);
		int heal = res.getResources(HStoneRes.AWAITING_HEAL);
		
		res.changeResources(HStoneRes.HEALTH, heal - damage);
		res.set(HStoneRes.AWAITING_DAMAGE, 0);
		res.set(HStoneRes.AWAITING_HEAL, 0);
	}

	public static void damage(HStoneTarget target, int damage) {
		target.getResources().changeResources(HStoneRes.AWAITING_DAMAGE, damage);
	}

	public static void heal(HStoneTarget target, int healing) {
		target.getResources().changeResources(HStoneRes.AWAITING_HEAL, healing);
	}

}
