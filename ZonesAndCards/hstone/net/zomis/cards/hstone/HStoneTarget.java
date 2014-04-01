package net.zomis.cards.hstone;

import net.zomis.cards.hstone.factory.HSAbility;
import net.zomis.cards.model.HasResources;
import net.zomis.cards.model.StackAction;

public interface HStoneTarget extends HasResources {

	StackAction clickAction();
	HStoneGame getGame();
	boolean isFrozen();
	boolean isAttackPossible();
	void addAbility(HSAbility frozen);
	void damage(int damage);
	void heal(int healing);
	HStonePlayer getPlayer();

}
