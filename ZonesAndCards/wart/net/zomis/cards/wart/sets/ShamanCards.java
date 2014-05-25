package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.Battlecry.*;
import static net.zomis.cards.wart.factory.HSFilters.*;
import static net.zomis.cards.wart.factory.HSGetCounts.*;
import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HSFilter;
import net.zomis.cards.wart.HStoneClass;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.events.HStoneCardPlayedEvent;
import net.zomis.cards.wart.events.HStoneTurnEndEvent;
import net.zomis.cards.wart.factory.HSAbility;
import net.zomis.cards.wart.factory.HStoneMinionType;

public class ShamanCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 1,      FREE, 0, 2, "Healing Totem").on(HStoneTurnEndEvent.class, forEach(allMinions().and(samePlayer()), null, heal(1)), samePlayer()).forClass(HStoneClass.SHAMAN).card());
		game.addCard(minion( 1,      FREE, 1, 1, "Searing Totem").forClass(HStoneClass.SHAMAN).card());
		game.addCard(minion( 1,      FREE, 0, 2, "Stoneclaw Totem").taunt().forClass(HStoneClass.SHAMAN).card());
		game.addCard(minion( 1,      FREE, 0, 2, "Wrath of Air Totem").spellDamage(1).forClass(HStoneClass.SHAMAN).card());
		game.addCard(minion( 1,    COMMON, 3, 1, "Dust Devil").windfury().overload(2).forClass(HStoneClass.SHAMAN).card());
		game.addCard(minion( 6,    COMMON, 6, 5, "Fire Elemental").battlecry(damage(3)).forClass(HStoneClass.SHAMAN).card());
		game.addCard(minion( 2,    COMMON, 0, 3, "Flametongue Totem").staticPT(adjacents(), 2, 0).forClass(HStoneClass.SHAMAN).card());
		game.addCard(minion( 3,    COMMON, 2, 4, "Unbound Elemental").on(HStoneCardPlayedEvent.class, selfPT(1, 1), hasOverload()).forClass(HStoneClass.SHAMAN).card());
		game.addCard(minion( 4,    COMMON, 3, 3, "Windspeaker").battlecry(giveAbility(HSAbility.WINDFURY)).forClass(HStoneClass.SHAMAN).card());
		game.addCard(minion( 3,      RARE, 0, 3, "Mana Tide Totem").on(HStoneTurnEndEvent.class, drawCard(), samePlayer()).forClass(HStoneClass.SHAMAN).card());
		game.addCard(minion( 2,      RARE, 2, 3, "Spirit Wolf").taunt().forClass(HStoneClass.SHAMAN).card());
		game.addCard(minion( 5,      EPIC, 7, 8, "Earth Elemental").taunt().overload(3).forClass(HStoneClass.SHAMAN).card());
		game.addCard(minion( 8, LEGENDARY, 3, 5, "Al'Akir the Windlord").windfury().charge().shield().taunt().forClass(HStoneClass.SHAMAN).card());
		game.addCard(spell( 0,      FREE, "Ancestral Healing").effect(toMinion(combined(heal(1000000), giveAbility(HSAbility.TAUNT)))).forClass(HStoneClass.SHAMAN).card());
		game.addCard(spell( 1,      FREE, "Frost Shock").effect(to(all().and(opponent()), combined(damage(1), freeze()))).forClass(HStoneClass.SHAMAN).card());
		game.addCard(spell( 3,      FREE, "Hex").effect(toMinion(transform("Frog"))).forClass(HStoneClass.SHAMAN).card());
		game.addCard(spell( 1,      FREE, "Rockbiter Weapon").effect(to(samePlayer(), tempBoost(samePlayer(), 3, 0))).forClass(HStoneClass.SHAMAN).card());
		game.addCard(spell( 2,      FREE, "Windfury").effect(giveAbility(HSAbility.WINDFURY)).forClass(HStoneClass.SHAMAN).card());
		game.addCard(spell( 5,    COMMON, "Bloodlust").effect(forEach(samePlayer().and(allMinions()), null, tempBoost(all(), 3, 0))).forClass(HStoneClass.SHAMAN).card());
		game.addCard(spell( 1,    COMMON, "Earth Shock").effect(toMinion(combined(silencer(), damage(1)))).forClass(HStoneClass.SHAMAN).card());
		game.addCard(spell( 1,    COMMON, "Forked Lightning").effect(iff(opponentHasMinions(2), toMultipleRandom(fixed(2), allMinions().and(enemy()), damage(2)))).overload(2).forClass(HStoneClass.SHAMAN).card());
		game.addCard(spell( 1,    COMMON, "Lightning Bolt").overload(1).effect(damage(3)).forClass(HStoneClass.SHAMAN).card());
		game.addCard(spell( 0,    COMMON, "Totemic Might").effect(forEach(samePlayer().and(minionIs(HStoneMinionType.TOTEM)), null, otherPT(0, 2))).forClass(HStoneClass.SHAMAN).card());
//		game.addCard(spell( 2,      RARE, "Ancestral Spirit").effect("Choose a minion. When that minion is destroyed, return it to the battlefield").forClass(HStoneClass.SHAMAN).card());
		game.addCard(spell( 3,      RARE, "Feral Spirit").effect(iff(haveSpaceOnBattleField, summon("Spirit Wolf", 2))).overload(2).forClass(HStoneClass.SHAMAN).card());
		game.addCard(spell( 3,      RARE, "Lava Burst").effect(damage(5)).overload(2).forClass(HStoneClass.SHAMAN).card());
		game.addCard(spell( 3,      RARE, "Lightning Storm").effect(forEach(allMinions().and(enemy()), null, evenChance(damage(2), damage(3)))).overload(2).forClass(HStoneClass.SHAMAN).card());
//		game.addCard(spell( 3,      EPIC, "Far Sight").effect("Draw a card. That card costs (3) less").forClass(HStoneClass.SHAMAN).card());
		game.addCard(weapon( 2,    COMMON, 2, 3, "Stormforged Axe").overload(1).forClass(HStoneClass.SHAMAN).card());
		game.addCard(weapon( 5,      EPIC, 2, 8, "Doomhammer").windfury().overload(2).forClass(HStoneClass.SHAMAN).card());
	}

	private HSFilter hasOverload() {
		return (src, dst) -> dst.getModel().getOverload() > 0;
	}

}
