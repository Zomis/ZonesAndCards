package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneClass;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.HStoneRes;
import net.zomis.cards.wart.events.HStoneTurnEndEvent;
import net.zomis.cards.wart.factory.Battlecry;
import net.zomis.cards.wart.factory.HSFilters;
import net.zomis.cards.wart.factory.HStoneEffect;
import net.zomis.cards.wart.factory.HStoneMinionType;

public class WarlockCards implements CardSet<HStoneGame> {

	private static final HSFilters f = new HSFilters();
	private static final Battlecry e = new Battlecry();
	
	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 2,      FREE, 4, 3, "Succubus").battlecry(e.discardRandomCard()).forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 1,      FREE, 1, 3, "Voidwalker").taunt().forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 1,    COMMON, 0, 1, "Blood Imp").stealth().on(HStoneTurnEndEvent.class, e.toRandom(f.samePlayer().and(f.allMinions()).and(f.anotherCard()), e.otherPT(0, 1)), f.samePlayer()).forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 6,    COMMON, 6, 6, "Dread Infernal").battlecry(e.forEach(f.anotherCard(), null, e.damage(1))).forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 1,    COMMON, 3, 2, "Flame Imp").battlecry(e.damageMyHero(3)).forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 6,    COMMON, 6, 6, "Infernal").forClass(HStoneClass.WARLOCK).card());
//		game.addCard(minion( 4,    COMMON, 0, 4, "Summoning Portal").effect("Your minions cost (2) less, but not less than (1)").forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Worthless Imp").forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 5,      RARE, 5, 7, "Doomguard").charge().battlecry(e.combined(e.discardRandomCard(), e.discardRandomCard())).forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 3,      RARE, 3, 5, "Felguard").taunt().battlecry(e.destroyManaCrystal()).forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 3,      RARE, 3, 3, "Void Terror").battlecry(e.adjacents(destroyAndGainStats())).forClass(HStoneClass.WARLOCK).card());
		game.addCard(minion( 4,      EPIC, 5, 6, "Pit Lord").battlecry(e.damageMyHero(5)).forClass(HStoneClass.WARLOCK).card());
//		game.addCard(minion( 9, LEGENDARY, 3, 15, "Lord Jaraxxus").battlecry("Destroy your hero and replace him with Lord Jaraxxus").forClass(HStoneClass.WARLOCK).card());
		game.addCard(spell( 3,      FREE, "Drain Life").effect(e.toAny(e.combined(e.damage(2), e.healMyHero(2)))).forClass(HStoneClass.WARLOCK).card());
		game.addCard(spell( 4,      FREE, "Hellfire").effect(e.forEach(f.all(), null, e.damage(3))).forClass(HStoneClass.WARLOCK).card());
		game.addCard(spell( 3,      FREE, "Shadow Bolt").effect(e.toMinion(e.damage(4))).forClass(HStoneClass.WARLOCK).card());
		game.addCard(spell( 1,    COMMON, "Corruption").effect(e.to(f.opponentMinions(), e.addEnchantOnMyTurnStartDestroy)).forClass(HStoneClass.WARLOCK).card());
//		game.addCard(spell( 2,    COMMON, "Demonfire").effect("Deal 2 damage to a minion.   If it’s a friendly Demon, give it +2/+2 instead").forClass(HStoneClass.WARLOCK).card());
//		game.addCard(spell( 1,    COMMON, "Mortal Coil").effect("Deal 1 damage to a minion. If that kills it, draw a card").forClass(HStoneClass.WARLOCK).card());
//		game.addCard(spell( 1,    COMMON, "Power Overwhelming").effect("Give a friendly minion +4/+4 until end of turn. Then, it dies. Horribly").forClass(HStoneClass.WARLOCK).card());
		game.addCard(spell( 0,    COMMON, "Sacrificial Pact").effect(e.to(f.minionIs(HStoneMinionType.DEMON), e.combined(e.destroyTarget(), e.healMyHero(5)))).forClass(HStoneClass.WARLOCK).card());
//		game.addCard(spell( 3,    COMMON, "Sense Demons").effect("Put 2 random Demons from your deck into your hand").forClass(HStoneClass.WARLOCK).card()); // TODO: Does this remove them from the deck?
		game.addCard(spell( 0,    COMMON, "Soulfire").effect(e.toAny(e.combined(e.damage(4), e.discardRandomCard()))).forClass(HStoneClass.WARLOCK).card());
//		game.addCard(spell( 4,      RARE, "Shadowflame").effect("Destroy a friendly minion and deal its Attack damage to all enemy minions").forClass(HStoneClass.WARLOCK).card());
		game.addCard(spell( 6,      RARE, "Siphon Soul").effect(e.toMinion(e.combined(e.destroyTarget(), e.healMyHero(3)))).forClass(HStoneClass.WARLOCK).card());
//		game.addCard(spell( 5,      EPIC, "Bane of Doom").effect("Deal 2 damage to a character.  If that kills it, summon a random Demon").forClass(HStoneClass.WARLOCK).card());
		game.addCard(spell( 8,      EPIC, "Twisting Nether").effect(e.forEach(f.allMinions(), null, e.destroyTarget())).forClass(HStoneClass.WARLOCK).card());
		game.addCard(weapon( 3,      NONE, 3, 8, "Blood Fury").forClass(HStoneClass.WARLOCK).card());
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
