package net.zomis.cards.wart;

import net.zomis.cards.model.CardZone;
import net.zomis.cards.wart.factory.Battlecry;
import net.zomis.cards.wart.factory.CardType;
import net.zomis.cards.wart.factory.HSFilters;
import net.zomis.cards.wart.factory.HStoneCardFactory;
import net.zomis.cards.wart.factory.HStoneCardModel;
import net.zomis.cards.wart.factory.HStoneEffect;

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
	
	private static final HSFilters f = new HSFilters();
	private static final Battlecry e = new Battlecry();
	
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
				return factory().effect(e.damage(1)).card();
			case HUNTER:
				return factory().effect(e.damageToOppHero(2)).card();
			case WARRIOR:
				return factory().effect(e.armor(2)).card();
			case SHAMAN:
				return factory().effect(e.iff(f.shamanCanPlayTotem(), e.randomTotem())).card(); // TODO: Is this ever disallowed to use? (when having all possibles, or battlefield full)
			case PALADIN:
				return factory().effect(e.iff(f.haveSpaceOnBattleField, e.summon("Silver Hand Recruit"))).card(); // TODO: Is this ever disallowed to use? (when having all possibles, or battlefield full)
			case PRIEST:
				return factory().effect(e.heal(2)).card();
			case ROGUE:
				return factory().effect(e.equip("Wicked Knife")).card();
			case WARLOCK:
				return factory().effect(e.combined(e.damageMyHero(2), e.drawCard())).card();
			default:
				throw new UnsupportedOperationException();
		}
	}
	
	public HStoneCard heroPowerCard(CardZone<HStoneCard> specialZone) {
		return specialZone.createCardOnBottom(power());
	} 
}
