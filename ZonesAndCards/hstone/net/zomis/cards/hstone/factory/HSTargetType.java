package net.zomis.cards.hstone.factory;

import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStonePlayer;
import net.zomis.cards.hstone.HStoneTarget;

public enum HSTargetType implements HSFilter {
	PLAYER, MINION;

	@Override
	public boolean shouldKeep(HStoneTarget obj) {
		switch (this) {
			case PLAYER:
				return obj instanceof HStonePlayer;
			case MINION:
				if (obj instanceof HStoneCard) {
					HStoneCard card = (HStoneCard) obj;
					return card.getModel().isMinion();
				}
				return false;
		}
		return false;
	}
}
