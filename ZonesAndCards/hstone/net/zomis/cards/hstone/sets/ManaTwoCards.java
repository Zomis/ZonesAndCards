package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HSFilters.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.events.HStoneTurnEndEvent;
import net.zomis.cards.hstone.events.HStoneTurnStartEvent;
import net.zomis.cards.hstone.factory.HSAbility;
import net.zomis.cards.util.CardSet;

public class ManaTwoCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 2,      NONE, 2, 2, "Gnoll").taunt().card());
		game.addCard(minion( 2,      FREE, 3, 2, "Bloodfen Raptor").card());
		game.addCard(minion( 2,      FREE, 1, 1, "Novice Engineer").battlecry(drawCard()).card());
		game.addCard(minion( 2,      FREE, 2, 3, "River Crocolisk").card());
		game.addCard(minion( 2,    COMMON, 3, 2, "Acidic Swamp Ooze").battlecry(destroyOppWeapon()).card());
//		game.addCard(minion( 2,    COMMON, 2, 3, "Amani Berserker").effect("<b>Enrage:</b>").effect("+3 Attack").card());
//		game.addCard(minion( 2,    COMMON, 2, 3, "Bloodsail Raider").effect("<b>Battlecry:</b>").effect("Gain Attack equal to the Attack of your weapon").card());
		game.addCard(minion( 2,    COMMON, 2, 1, "Bluegill Warrior").charge().card());
//		game.addCard(minion( 2,    COMMON, 2, 2, "Dire Wolf Alpha").effect("Adjacent minions have +1 Attack").card());
		game.addCard(minion( 2,    COMMON, 3, 2, "Faerie Dragon").shroud().card());
		game.addCard(minion( 2,    COMMON, 2, 2, "Frostwolf Grunt").taunt().card());
		game.addCard(minion( 2,    COMMON, 2, 1, "Ironbeak Owl").battlecry(silencer()).card());
		game.addCard(minion( 2,    COMMON, 2, 2, "Kobold Geomancer").spellDamage(1).card());
		game.addCard(minion( 2,    COMMON, 2, 1, "Loot Hoarder").deathrattle(drawCard()).card());
		game.addCard(minion( 2,    COMMON, 3, 2, "Mad Bomber").battlecry(repeat(fixed(3), toRandom(canTakeDamage(1), damage(1)))).card());
		game.addCard(minion( 2,    COMMON, 2, 1, "Murloc Tidehunter").battlecry(summon("Murloc Scout")).card());
//		game.addCard(minion( 2,    COMMON, 3, 2, "Youthful Brewmaster").battlecry("Return a friendly minion from the battlefield to your hand").card());
		game.addCard(minion( 2,      RARE, 4, 5, "Ancient Watcher").noAttack().card());
//		game.addCard(minion( 2,      RARE, 2, 2, "Crazed Alchemist").battlecry("Swap the Attack and Health of a minion").card());
//		game.addCard(minion( 2,      RARE, 3, 2, "Knife Juggler").effect("After you summon a minion, deal 1 damage to a random enemy").card());
//		game.addCard(minion( 2,      RARE, 1, 3, "Mana Addict").effect("Whenever you cast a spell, gain +2 Attack this turn").card());
//		game.addCard(minion( 2,      RARE, 2, 2, "Mana Wraith").effect("ALL minions cost (1) more").card());
		game.addCard(minion( 2,      RARE, 1, 3, "Master Swordsmith").on(HStoneTurnEndEvent.class, toRandom(and(allMinions(), samePlayer()), otherPT(1, 0)), samePlayer()).card());
//		game.addCard(minion( 2,      RARE, 2, 2, "Pint-Sized Summoner").effect("The first minion you play each turn costs (1) less").card());
		game.addCard(minion( 2,      RARE, 2, 3, "Sunfury Protector").battlecry(adjacents(giveAbility(HSAbility.TAUNT))).card());
//		game.addCard(minion( 2,      RARE, 3, 2, "Wild Pyromancer").effect("After you cast a spell, deal 1 damage to ALL minions").card());
//		game.addCard(minion( 2,      EPIC, 1, 1, "Captain's Parrot").effect("<b>Battlecry:</b>").effect("Put a random Pirate from your deck into your hand").card());
		game.addCard(minion( 2,      EPIC, 0, 7, "Doomsayer").on(HStoneTurnStartEvent.class, destroyAllMinions(), samePlayer()).card());
		game.addCard(minion( 2, LEGENDARY, 1, 1, "Bloodmage Thalnos").spellDamage(1).deathrattle(drawCard()).card());
		game.addCard(minion( 2, LEGENDARY, 3, 3, "Finkle Einhorn").card());
//		game.addCard(minion( 2, LEGENDARY, 0, 4, "Lorewalker Cho").effect("Whenever a player casts a spell, put a copy into the other player’s hand").card());
//		game.addCard(minion( 2, LEGENDARY, 4, 4, "Millhouse Manastorm").effect("<b>Battlecry:</b>").effect("Enemy spells cost (0) next turn").card());
		game.addCard(minion( 2, LEGENDARY, 0, 4, "Nat Pagle").on(HStoneTurnStartEvent.class, evenChance(drawCard(), doNothing()), samePlayer()).card());
	}


}
