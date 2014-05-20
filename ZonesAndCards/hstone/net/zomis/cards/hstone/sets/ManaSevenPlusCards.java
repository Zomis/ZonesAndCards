package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HSFilters.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.HStonePlayer;
import net.zomis.cards.hstone.HStoneRes;
import net.zomis.cards.hstone.events.HStoneTurnEndEvent;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.util.CardSet;

public class ManaSevenPlusCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 7,    COMMON, 9, 5, "Core Hound").card());
//		game.addCard(minion( 7,    COMMON, 6, 6, "Stormwind Champion").effect("Your other minions have +1/+1").card());
		game.addCard(minion( 7,    COMMON, 7, 7, "War Golem").card());
		game.addCard(minion( 7,      RARE, 7, 5, "Ravenholdt Assassin").stealth().card());
//		game.addCard(minion(20,      EPIC, 8, 8, "Molten Giant").effect("Costs (1) less for each damage your hero has taken").card());
//		game.addCard(minion(12,      EPIC, 8, 8, "Mountain Giant").effect("Costs (1) less for each other card in your hand").card());
//		game.addCard(minion(10,      EPIC, 8, 8, "Sea Giant").effect("Costs (1) less for each other minion on the battlefield").card());
		game.addCard(minion( 9, LEGENDARY, 8, 8, "Alexstrasza").battlecry(to(allPlayers(), set(HStoneRes.HEALTH, 15))).card());
		game.addCard(minion( 7, LEGENDARY, 7, 5, "Baron Geddon").on(HStoneTurnEndEvent.class, forEach(not(thisCard()), null, damage(2)), samePlayer()).card());
		game.addCard(minion(10, LEGENDARY, 12, 12, "Deathwing").battlecry(combined(forEach(allMinions().and(not(thisCard())), null, destroyTarget()), discardAllCards())).card());
		game.addCard(minion( 8, LEGENDARY, 7, 7, "Gruul").on(HStoneTurnEndEvent.class, selfPT(1, 1), all()).card());
		game.addCard(minion( 9, LEGENDARY, 4, 12, "Malygos").spellDamage(5).card());
//		game.addCard(minion( 9, LEGENDARY, 8, 8, "Nozdormu").effect("Players only have 15 seconds to take their turns").card());
		game.addCard(minion( 9, LEGENDARY, 8, 8, "Onyxia").battlecry(summon("Whelp", HStonePlayer.MAX_BATTLEFIELD_SIZE)).card());
		game.addCard(minion( 8, LEGENDARY, 8, 8, "Ragnaros the Firelord").noAttack().on(HStoneTurnEndEvent.class, toRandom(not(samePlayer()), damage(8)), samePlayer()).card());
		game.addCard(minion( 9, LEGENDARY, 4, 12, "Ysera").on(HStoneTurnEndEvent.class, drawDreamCard(), samePlayer()).card());
	}

	private HStoneEffect drawDreamCard() {
		return evenChance(giveCard("Dream"), giveCard("Emerald Drake"), giveCard("Laughing Sister"), giveCard("Nightmare"), giveCard("Ysera Awakens"));
	}

	private HStoneEffect discardAllCards() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getPlayer().getHand().moveToBottomOf(source.getPlayer().getDiscard()); // TODO: Not the same card!
			}
		};
	}

}
