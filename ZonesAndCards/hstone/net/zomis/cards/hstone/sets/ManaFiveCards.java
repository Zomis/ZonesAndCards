package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HSFilters.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.events.HStoneDamagedEvent;
import net.zomis.cards.util.CardSet;

public class ManaFiveCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		
		game.addCard(minion( 5,      FREE, 4, 4, "Nightblade").battlecry(damageToOppHero(3)).card());
		game.addCard(minion( 5,    COMMON, 5, 4, "Booty Bay Bodyguard").taunt().card());
		game.addCard(minion( 5,    COMMON, 4, 5, "Darkscale Healer").battlecry(forEach(samePlayer(), null, heal(2))).card());
		game.addCard(minion( 5,    COMMON, 5, 5, "Devilsaur").card());
		game.addCard(minion( 5,    COMMON, 3, 6, "Fen Creeper").taunt().card());
		game.addCard(minion( 5,    COMMON, 4, 4, "Frostwolf Warlord").battlecry(forEach(allMinions().and(not(thisCard())), selfPT(1, 1), null)).card());
		game.addCard(minion( 5,    COMMON, 2, 7, "Gurubashi Berserker").on(HStoneDamagedEvent.class, selfPT(3, 0), thisCard()).card());
		game.addCard(minion( 5,    COMMON, 4, 4, "Silver Hand Knight").battlecry(summon("Squire")).card());
//		game.addCard(minion( 5,    COMMON, 4, 6, "Spiteful Smith").effect("<b>Enrage:</b>").effect("Your weapon has +2 Attack").card());
		game.addCard(minion( 5,    COMMON, 4, 2, "Stormpike Commando").battlecry(damage(2)).card());
		game.addCard(minion( 5,    COMMON, 5, 5, "Stranglethorn Tiger").stealth().card());
//		game.addCard(minion( 5,    COMMON, 7, 6, "Venture Co. Mercenary").effect("Your minions cost (3) more").card());
		game.addCard(minion( 5,      RARE, 4, 4, "Abomination").taunt().deathrattle(forEach(all(), null, damage(2))).card());
		game.addCard(minion( 5,      RARE, 4, 4, "Azure Drake").spellDamage(1).battlecry(drawCard()).card());
//		game.addCard(minion( 5,      RARE, 4, 4, "Gadgetzan Auctioneer").effect("Whenever you cast a spell, draw a card").card());
		game.addCard(minion( 5,      RARE, 3, 5, "Stampeding Kodo").battlecry(toRandom(opponentMinions().and(withAttackLess(2)), destroyTarget())).card());
//		game.addCard(minion( 5,      EPIC, 3, 3, "Faceless Manipulator").battlecry("Choose a minion and become a copy of it").card());
//		game.addCard(minion( 5, LEGENDARY, 5, 4, "Captain Greenskin").effect("<b>Battlecry:</b>").effect("Give your weapon +1/+1").card());
		game.addCard(minion( 5, LEGENDARY, 5, 5, "Elite Tauren Chieftain").battlecry(bothPlayers(evenChance(giveCard("I Am Murloc"), giveCard("Power of the Horde"), giveCard("Rogues Do It...")))).card());
		game.addCard(minion( 5, LEGENDARY, 5, 4, "Harrison Jones").battlecry(combined(repeat(oppWeaponDurability(), drawCard()), destroyOppWeapon())).card());
	}


}
