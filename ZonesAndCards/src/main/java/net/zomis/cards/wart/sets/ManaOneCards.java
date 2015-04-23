package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneMinionType.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HSFilter;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.events.HStoneCardPlayedEvent;
import net.zomis.cards.wart.events.HStoneHealEvent;
import net.zomis.cards.wart.events.HStoneMinionSummonedEvent;
import net.zomis.cards.wart.events.HStoneTurnEndEvent;
import net.zomis.cards.wart.events.HStoneTurnStartEvent;
import net.zomis.cards.wart.factory.Battlecry;
import net.zomis.cards.wart.factory.HSAbility;
import net.zomis.cards.wart.factory.HSFilters;

public class ManaOneCards implements CardSet<HStoneGame> {

	private static final HSFilters f = new HSFilters();
	private static final Battlecry e = new Battlecry();
	
	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 1,      NONE, 2, 1, "Flame of Azzinoth").card());
		game.addCard(minion( 1,      NONE, 1, 1, "Murloc").card());
		game.addCard(minion( 1,      NONE, 1, 1, "Whelp").card());
		game.addCard(minion( 1,      FREE, 2, 1, "Murloc Raider").card());
		game.addCard(minion( 1,      FREE, 1, 1, "Stonetusk Boar").charge().card());
		game.addCard(minion( 1,      FREE, 2, 1, "Voodoo Doctor").battlecry(e.heal(2)).card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Abusive Sergeant").battlecry(e.tempBoost(f.allMinions(), 2, 0)).card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Argent Squire").shield().card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Boar").card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Damaged Golem").card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Elven Archer").battlecry(e.damage(1)).card());
		game.addCard(minion( 1,    COMMON, 0, 4, "Emboldener 3000").on(HStoneTurnEndEvent.class, e.toRandom(f.allMinions(), e.otherPT(1, 1)), f.samePlayer()).card());
		game.addCard(minion( 1,    COMMON, 1, 2, "Goldshire Footman").taunt().card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Grimscale Oracle").staticEffectOtherMurlocsBonus(1, 0).is(MURLOC).card());
		game.addCard(minion( 1,    COMMON, 0, 1, "Homing Chicken").on(HStoneTurnStartEvent.class, e.combined(e.drawCards(3), e.selfDestruct()), f.samePlayer()).card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Leper Gnome").deathrattle(e.damageToEnemyHero(2)).card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Mechanical Dragonling").card());
		game.addCard(minion( 1,    COMMON, 0, 3, "Poultryizer").on(HStoneTurnStartEvent.class, e.toRandom(f.allMinions(), e.transform("Chicken")), f.samePlayer()).card());
		game.addCard(minion( 1,    COMMON, 0, 3, "Repair Bot").on(HStoneTurnEndEvent.class, e.toRandom(f.isDamaged(), e.heal(6)), f.samePlayer()).card());
		game.addCard(minion( 1,    COMMON, 0, 4, "Shieldbearer").taunt().card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Southsea Deckhand").staticAbility(f.samePlayer().and(f.targetPlayerHasWeapon()), HSAbility.CHARGE).card());
		game.addCard(minion( 1,    COMMON, 2, 2, "Squire").card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Squirrel").card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Worgen Infiltrator").stealth().card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Young Dragonhawk").windfury().card());
		game.addCard(minion( 1,      RARE, 1, 1, "Angry Chicken").staticPT(f.enrage(), 5, 0).card());
		game.addCard(minion( 1,      RARE, 1, 2, "Bloodsail Corsair").battlecry(e.removeDurabilityOppWeapon(1)).card());
		game.addCard(minion( 1,      RARE, 1, 1, "Imp").card());
		game.addCard(minion( 1,      RARE, 1, 2, "Lightwarden").on(HStoneHealEvent.class, e.selfPT(2, 0), f.all()).card());
		game.addCard(minion( 1,      RARE, 1, 2, "Murloc Tidecaller").on(HStoneMinionSummonedEvent.class, e.selfPT(1, 0), f.minionIsMurloc()).card());
		game.addCard(minion( 1,      RARE, 1, 2, "Secretkeeper").on(HStoneCardPlayedEvent.class, e.selfPT(1, 1), isSecret()).card());
		game.addCard(minion( 1,      RARE, 2, 1, "Young Priestess").on(HStoneTurnEndEvent.class, e.randomFriendlyMinion(e.otherPT(0, 1)), f.samePlayer()).card());
		game.addCard(minion( 1,      EPIC, 1, 2, "Hungry Crab").battlecry(e.to(f.minionIs(MURLOC), e.combined(e.destroyTarget(), e.selfPT(2, 2)))).card());
		game.addCard(spell( 1,      NONE, "Bananas").effect(e.toMinion(e.otherPT(1, 1))).card());
	}

	private HSFilter isSecret() {
		return (src, dst) -> dst.getModel().isSecret();
	}



}
