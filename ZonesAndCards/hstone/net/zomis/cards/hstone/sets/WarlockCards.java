package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HSFilters.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;
import net.zomis.cards.hstone.HSAction;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneClass;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.HStoneRes;
import net.zomis.cards.hstone.events.HStoneTurnEndEvent;
import net.zomis.cards.hstone.events.HStoneTurnStartEvent;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.hstone.factory.HStoneMinionType;
import net.zomis.cards.hstone.triggers.CardEventTrigger;
import net.zomis.cards.util.CardSet;

public class WarlockCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 2,      FREE, 4, 3, "Succubus").battlecry(discardRandomCard()).forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 1,      FREE, 1, 3, "Voidwalker").taunt().forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 1,    COMMON, 0, 1, "Blood Imp").stealth().on(HStoneTurnEndEvent.class, toRandom(samePlayer().and(allMinions()).and(anotherCard()), otherPT(0, 1)), samePlayer()).forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 6,    COMMON, 6, 6, "Dread Infernal").battlecry(forEach(not(thisCard()), null, damage(1))).forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 1,    COMMON, 3, 2, "Flame Imp").battlecry(damageMyHero(3)).forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 6,    COMMON, 6, 6, "Infernal").forClass(HStoneClass.WARLOCK).card());
//		game.addCard(minion( 4,    COMMON, 0, 4, "Summoning Portal").effect("Your minions cost (2) less, but not less than (1)").forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Worthless Imp").forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 5,      RARE, 5, 7, "Doomguard").charge().battlecry(combined(discardRandomCard(), discardRandomCard())).forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 3,      RARE, 3, 5, "Felguard").taunt().battlecry(destroyManaCrystal()).forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 3,      RARE, 3, 3, "Void Terror").battlecry(adjacents(destroyAndGainStats())).forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 4,      EPIC, 5, 6, "Pit Lord").battlecry(damageMyHero(5)).forClass(HStoneClass.WARLOCK).card());
//		game.addCard(minion( 9, LEGENDARY, 3, 15, "Lord Jaraxxus").battlecry("Destroy your hero and replace him with Lord Jaraxxus").forClass(HStoneClass.WARLOCK).card());
		game.addCard(spell( 3,      FREE, "Drain Life").effect(toAny(combined(damage(2), healMyHero(2)))).forClass(HStoneClass.WARLOCK).card());
		game.addCard(spell( 4,      FREE, "Hellfire").effect(forEach(all(), null, damage(3))).forClass(HStoneClass.WARLOCK).card());
		game.addCard(spell( 3,      FREE, "Shadow Bolt").effect(toMinion(damage(4))).forClass(HStoneClass.WARLOCK).card());
		game.addCard(spell( 1,    COMMON, "Corruption").effect(to(opponentMinions(), addEnchantOnMyTurnStartDestroy())).forClass(HStoneClass.WARLOCK).card());
//		game.addCard(spell( 2,    COMMON, "Demonfire").effect("Deal 2 damage to a minion.   If it’s a friendly Demon, give it +2/+2 instead").forClass(HStoneClass.WARLOCK).card());
//		game.addCard(spell( 1,    COMMON, "Mortal Coil").effect("Deal 1 damage to a minion. If that kills it, draw a card").forClass(HStoneClass.WARLOCK).card());
//		game.addCard(spell( 1,    COMMON, "Power Overwhelming").effect("Give a friendly minion +4/+4 until end of turn. Then, it dies. Horribly").forClass(HStoneClass.WARLOCK).card());
		game.addCard(spell( 0,    COMMON, "Sacrificial Pact").effect(to(minionIs(HStoneMinionType.DEMON), combined(destroyTarget(), healMyHero(5)))).forClass(HStoneClass.WARLOCK).card());
//		game.addCard(spell( 3,    COMMON, "Sense Demons").effect("Put 2 random Demons from your deck into your hand").forClass(HStoneClass.WARLOCK).card()); // TODO: Does this remove them from the deck?
		game.addCard(spell( 0,    COMMON, "Soulfire").effect(toAny(combined(damage(4), discardRandomCard()))).forClass(HStoneClass.WARLOCK).card());
//		game.addCard(spell( 4,      RARE, "Shadowflame").effect("Destroy a friendly minion and deal its Attack damage to all enemy minions").forClass(HStoneClass.WARLOCK).card());
		game.addCard(spell( 6,      RARE, "Siphon Soul").effect(toMinion(combined(destroyTarget(), healMyHero(3)))).forClass(HStoneClass.WARLOCK).card());
//		game.addCard(spell( 5,      EPIC, "Bane of Doom").effect("Deal 2 damage to a character.  If that kills it, summon a random Demon").forClass(HStoneClass.WARLOCK).card());
//		game.addCard(spell( 8,      EPIC, "Twisting Nether").effect("Destroy all minions").forClass(HStoneClass.WARLOCK).card());
		game.addCard(weapon( 3,      NONE, 3, 8, "Blood Fury").forClass(HStoneClass.WARLOCK).card());
	}

	public static HSAction addEnchantOnMyTurnStartDestroy() {
		return (src, dst) -> dst.addTrigger(new CardEventTrigger(HStoneTurnStartEvent.class, selfDestruct(), (src2, dst2) -> src2.getPlayer() == src.getPlayer()));
	}

	public static HStoneEffect destroyAndGainStats() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getResources().changeResources(HStoneRes.ATTACK, target.getAttack());
				source.getResources().changeResources(HStoneRes.MAX_HEALTH, target.getHealth());
				source.getResources().changeResources(HStoneRes.HEALTH, target.getHealth());
				target.destroy();
			}
		};
	}


}
