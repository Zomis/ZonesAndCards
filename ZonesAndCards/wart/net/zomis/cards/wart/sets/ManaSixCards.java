package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.Battlecry.*;
import static net.zomis.cards.wart.factory.HSFilters.*;
import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.events.HStoneCardPlayedEvent;
import net.zomis.cards.wart.events.HStoneTurnEndEvent;
import net.zomis.cards.wart.factory.HSAbility;

public class ManaSixCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		
		game.addCard(minion( 6,      FREE, 6, 7, "Boulderfist Ogre").card());
		game.addCard(minion( 6,      FREE, 5, 2, "Reckless Rocketeer").charge().card());
		game.addCard(minion( 6,    COMMON, 4, 7, "Archmage").spellDamage(1).card());
		game.addCard(minion( 6,    COMMON, 5, 5, "Frost Elemental").battlecry(toAny(freeze())).card());
		game.addCard(minion( 6,    COMMON, 6, 5, "Lord of the Arena").taunt().card());
		game.addCard(minion( 6,    COMMON, 5, 4, "Priestess of Elune").battlecry(healMyHero(4)).card());
		game.addCard(minion( 6,    COMMON, 4, 5, "Windfury Harpy").windfury().card());
		game.addCard(minion( 6,      RARE, 4, 2, "Argent Commander").charge().shield().card());
		game.addCard(minion( 6,      RARE, 4, 5, "Sunwalker").taunt().shield().card());
		game.addCard(minion( 6, LEGENDARY, 4, 5, "Cairne Bloodhoof").deathrattle(summon("Baine Bloodhoof")).card());
		game.addCard(minion( 6, LEGENDARY, 6, 6, "Gelbin Mekkatorque").battlecry(evenChance(summon("Emboldener 3000"), summon("Homing Chicken"), summon("Poultryizer"), summon("Repair Bot"))).card());
		game.addCard(minion( 6, LEGENDARY, 4, 4, "Hogger").on(HStoneTurnEndEvent.class, summon("Gnoll"), samePlayer()).card());
		game.addCard(minion( 6, LEGENDARY, 7, 5, "Illidan Stormrage").on(HStoneCardPlayedEvent.class, summon("Flame of Azzinoth"), samePlayer()).card());
		game.addCard(minion( 6, LEGENDARY, 5, 5, "Sylvanas Windrunner").deathrattle(toRandom(opponentMinions(), stealMinion(allMinions()))).card());
		game.addCard(minion( 6, LEGENDARY, 9, 7, "The Beast").deathrattle(oppSummon("Finkle Einhorn", 1)).card());
		game.addCard(minion( 6, LEGENDARY, 4, 5, "The Black Knight").battlecry(to(opponentMinions().and(minionHasAbility(HSAbility.TAUNT)), destroyTarget())).card());
	}

}
