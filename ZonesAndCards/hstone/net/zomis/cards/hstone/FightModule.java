package net.zomis.cards.hstone;

public class FightModule {

	public static void attack(HStoneGame game, HStoneCard source, HStoneCard target) {
		int attack = source.getResources().getResources(HStoneRes.ATTACK);
		int counterAttack = target.getResources().getResources(HStoneRes.ATTACK);
		
		if (source instanceof HStoneCard) {
			HStoneCard card = (HStoneCard) source;
			attack += card.getAttackBonus();
		}
		if (target instanceof HStoneCard) {
			HStoneCard card = (HStoneCard) target;
			counterAttack += card.getAttackBonus();
		}
		
		damage(target, attack);
		damage(source, counterAttack);
		source.getResources().changeResources(HStoneRes.ACTION_POINTS, -1);
		
		source.cleanup();
		target.cleanup();
		
		game.cleanup();
	}

	public static void damage(HStoneCard target, int damage) {
		target.getResources().changeResources(HStoneRes.AWAITING_DAMAGE, damage);
	}

	public static void heal(HStoneCard target, int healing) {
		target.getResources().changeResources(HStoneRes.AWAITING_HEAL, healing);
	}

}
