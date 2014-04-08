package net.zomis.cards.hstone;

import net.zomis.cards.hstone.factory.CardType;
import net.zomis.cards.hstone.factory.HStoneCardFactory;
import net.zomis.cards.hstone.factory.HStoneCardModel;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.model.CardZone;
import static net.zomis.cards.hstone.factory.Battlecry.*;

public enum HStoneClass {
	DRUID,
	MAGE,
	HUNTER,
	WARRIOR,
	SHAMAN,
	PALADIN,
	PRIEST,
	ROGUE,
	WARLOCK;

	private HStoneCardFactory factory() {
		return new HStoneCardFactory(name(), 2, CardType.POWER).charge();
	}
	
	private HStoneCardModel power() {
		switch (this) {
			case DRUID:
				return factory().effect(new HStoneEffect() {
					@Override
					public void performEffect(HStoneCard source, HStoneCard target) {
						source.getPlayer().getResources().changeResources(HStoneRes.ARMOR, 1);
						source.getPlayer().getResources().changeResources(HStoneRes.ATTACK, 1);
					}
				}).card();
			case MAGE:
				return factory().effect(damage(1)).card();
			case HUNTER:
				return factory().effect(damageToOppHero(2)).card();
			case WARRIOR:
				return factory().effect(armor(2)).card();
			case SHAMAN:
				return factory().effect(randomTotem()).card();
			case PALADIN:
				return factory().effect(summon("Soldier")).card();
			case PRIEST:
				return factory().effect(heal(1)).card();
			case ROGUE:
				return factory().effect(equip("Rogue Weapon")).card();
			case WARLOCK:
				return factory().effect(combined(selfPlayerDamage(2), drawCard())).card();
			default:
				throw new UnsupportedOperationException();
		}
	}
	
	private HStoneEffect randomTotem() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				summon("Totem").performEffect(source, target);
			}
		};
	}

	public HStoneCard heroPowerCard(CardZone<HStoneCard> specialZone) {
		return specialZone.createCardOnBottom(power());
	} 
}
