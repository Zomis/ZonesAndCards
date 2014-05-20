package net.zomis.cards.hstone;

import static net.zomis.cards.hstone.factory.Battlecry.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.zomis.cards.hstone.factory.CardType;
import net.zomis.cards.hstone.factory.HStoneCardFactory;
import net.zomis.cards.hstone.factory.HStoneCardModel;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.model.CardZone;
import net.zomis.utils.ZomisList;

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
				return factory().effect(summon("Silver Hand Recruit")).card();
			case PRIEST:
				return factory().effect(heal(2)).card();
			case ROGUE:
				return factory().effect(equip("Wicked Knife")).card();
			case WARLOCK:
				return factory().effect(combined(damageMyHero(2), drawCard())).card();
			default:
				throw new UnsupportedOperationException();
		}
	}
	
	private HStoneEffect randomTotem() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				HStoneCardModel totemA = source.getGame().getCardModel("Healing Totem");
				HStoneCardModel totemB = source.getGame().getCardModel("Stoneclaw Totem");
				HStoneCardModel totemC = source.getGame().getCardModel("Searing Totem");
				HStoneCardModel totemD = source.getGame().getCardModel("Wrath of Air Totem");
				List<HStoneCardModel> totemsToChooseFrom = new ArrayList<>(Arrays.asList(totemA, totemB, totemC, totemD));
				
				for (HStoneCard card : source.getPlayer().getBattlefield()) {
					if (totemsToChooseFrom.contains(card.getModel())) {
						totemsToChooseFrom.remove(card.getModel());
					}
				}
				HStoneCardModel totem = ZomisList.getRandom(totemsToChooseFrom, source.getGame().getRandom());
				if (totem == null)
					return; // No totem available
				
				summon(totem.getName()).performEffect(source, target);
			}
		};
	}

	public HStoneCard heroPowerCard(CardZone<HStoneCard> specialZone) {
		return specialZone.createCardOnBottom(power());
	} 
}
