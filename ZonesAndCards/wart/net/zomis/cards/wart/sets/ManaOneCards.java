package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.Battlecry.*;
import static net.zomis.cards.wart.factory.HSFilters.*;
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
import net.zomis.cards.wart.factory.HSAbility;
import net.zomis.cards.wart.factory.HSFilters;

public class ManaOneCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 1,      NONE, 2, 1, "Flame of Azzinoth").card());
		game.addCard(minion( 1,      NONE, 1, 1, "Murloc").card());
		game.addCard(minion( 1,      NONE, 1, 1, "Whelp").card());
		game.addCard(minion( 1,      FREE, 2, 1, "Murloc Raider").card());
		game.addCard(minion( 1,      FREE, 1, 1, "Stonetusk Boar").charge().card());
		game.addCard(minion( 1,      FREE, 2, 1, "Voodoo Doctor").battlecry(heal(2)).card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Abusive Sergeant").battlecry(tempBoost(allMinions(), 2, 0)).card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Argent Squire").shield().card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Boar").card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Damaged Golem").card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Elven Archer").battlecry(damage(1)).card());
		game.addCard(minion( 1,    COMMON, 0, 4, "Emboldener 3000").on(HStoneTurnEndEvent.class, toRandom(allMinions(), otherPT(1, 1)), samePlayer()).card());
		game.addCard(minion( 1,    COMMON, 1, 2, "Goldshire Footman").taunt().card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Grimscale Oracle").staticEffectOtherMurlocsBonus(1, 0).is(MURLOC).card());
		game.addCard(minion( 1,    COMMON, 0, 1, "Homing Chicken").on(HStoneTurnStartEvent.class, combined(drawCards(3), selfDestruct()), samePlayer()).card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Leper Gnome").deathrattle(damageToEnemyHero(2)).card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Mechanical Dragonling").card());
		game.addCard(minion( 1,    COMMON, 0, 3, "Poultryizer").on(HStoneTurnStartEvent.class, toRandom(allMinions(), transform("Chicken")), samePlayer()).card());
		game.addCard(minion( 1,    COMMON, 0, 3, "Repair Bot").on(HStoneTurnEndEvent.class, toRandom(isDamaged(), heal(6)), samePlayer()).card());
		game.addCard(minion( 1,    COMMON, 0, 4, "Shieldbearer").taunt().card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Southsea Deckhand").staticAbility(samePlayer().and(targetPlayerHasWeapon()), HSAbility.CHARGE).card());
		game.addCard(minion( 1,    COMMON, 2, 2, "Squire").card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Squirrel").card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Worgen Infiltrator").stealth().card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Young Dragonhawk").windfury().card());
		game.addCard(minion( 1,      RARE, 1, 1, "Angry Chicken").staticPT(enrage(), 5, 0).card());
		game.addCard(minion( 1,      RARE, 1, 2, "Bloodsail Corsair").battlecry(removeDurabilityOppWeapon(1)).card());
		game.addCard(minion( 1,      RARE, 1, 1, "Imp").card());
		game.addCard(minion( 1,      RARE, 1, 2, "Lightwarden").on(HStoneHealEvent.class, selfPT(2, 0), all()).card());
		game.addCard(minion( 1,      RARE, 1, 2, "Murloc Tidecaller").on(HStoneMinionSummonedEvent.class, selfPT(1, 0), minionIsMurloc()).card());
		game.addCard(minion( 1,      RARE, 1, 2, "Secretkeeper").on(HStoneCardPlayedEvent.class, selfPT(1, 1), isSecret()).card());
		game.addCard(minion( 1,      RARE, 2, 1, "Young Priestess").on(HStoneTurnEndEvent.class, randomFriendlyMinion(otherPT(0, 1)), HSFilters.samePlayer()).card());
		game.addCard(minion( 1,      EPIC, 1, 2, "Hungry Crab").battlecry(to(minionIs(MURLOC), combined(destroyTarget(), selfPT(2, 2)))).card());
		game.addCard(spell( 1,      NONE, "Bananas").effect(toMinion(otherPT(1, 1))).card());
	}

	private HSFilter isSecret() {
		return (src, dst) -> dst.getModel().isSecret();
	}



}
