package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HSFilters.*;
import static net.zomis.cards.hstone.factory.HSGetCounts.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.HStoneRes;
import net.zomis.cards.hstone.events.HStoneCardPlayedEvent;
import net.zomis.cards.hstone.events.HStoneMinionSummonedEvent;
import net.zomis.cards.hstone.events.HStoneTurnEndEvent;
import net.zomis.cards.hstone.events.HStoneTurnStartEvent;
import net.zomis.cards.hstone.factory.HSAbility;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.hstone.factory.HStoneMinionType;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.util.CardSet;
import net.zomis.utils.ZomisList;

public class ManaTwoCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 2,      NONE, 2, 2, "Gnoll").taunt().card());
		game.addCard(minion( 2,      FREE, 3, 2, "Bloodfen Raptor").card());
		game.addCard(minion( 2,      FREE, 1, 1, "Novice Engineer").battlecry(drawCard()).card());
		game.addCard(minion( 2,      FREE, 2, 3, "River Crocolisk").card());
		game.addCard(minion( 2,    COMMON, 3, 2, "Acidic Swamp Ooze").battlecry(destroyOppWeapon()).card());
		game.addCard(minion( 2,    COMMON, 2, 3, "Amani Berserker").staticPT(enrage(), 3, 0).card());
		game.addCard(minion( 2,    COMMON, 2, 3, "Bloodsail Raider").battlecry(selfPT(calcWeaponDamage(), fixed(0))).card());
		game.addCard(minion( 2,    COMMON, 2, 1, "Bluegill Warrior").charge().card());
		game.addCard(minion( 2,    COMMON, 2, 2, "Dire Wolf Alpha").staticPT(adjacents(), 1, 0).card());
		game.addCard(minion( 2,    COMMON, 3, 2, "Faerie Dragon").shroud().card());
		game.addCard(minion( 2,    COMMON, 2, 2, "Frostwolf Grunt").taunt().card());
		game.addCard(minion( 2,    COMMON, 2, 1, "Ironbeak Owl").battlecry(silencer()).card());
		game.addCard(minion( 2,    COMMON, 2, 2, "Kobold Geomancer").spellDamage(1).card());
		game.addCard(minion( 2,    COMMON, 2, 1, "Loot Hoarder").deathrattle(drawCard()).card());
		game.addCard(minion( 2,    COMMON, 3, 2, "Mad Bomber").battlecry(repeat(fixed(3), toRandom(canTakeDamage(1), damage(1)))).card());
		game.addCard(minion( 2,    COMMON, 2, 1, "Murloc Tidehunter").battlecry(summon("Murloc Scout")).card());
//		game.addCard(minion( 2,    COMMON, 3, 2, "Youthful Brewmaster").battlecry("Return a friendly minion from the battlefield to your hand").card());
		game.addCard(minion( 2,      RARE, 4, 5, "Ancient Watcher").noAttack().card());
		game.addCard(minion( 2,      RARE, 2, 2, "Crazed Alchemist").battlecry(toMinion(swapPT())).card());
		game.addCard(minion( 2,      RARE, 3, 2, "Knife Juggler").on(HStoneMinionSummonedEvent.class, toRandom(all().and(not(samePlayer())), damage(1)), samePlayer()).card());
		game.addCard(minion( 2,      RARE, 1, 3, "Mana Addict").on(HStoneCardPlayedEvent.class, tempBoost(thisCard(), 2, 0), samePlayer().and(isSpell())).card());
//		game.addCard(minion( 2,      RARE, 2, 2, "Mana Wraith").effect("ALL minions cost (1) more").card());
		game.addCard(minion( 2,      RARE, 1, 3, "Master Swordsmith").on(HStoneTurnEndEvent.class, toRandom(allMinions().and(samePlayer()), otherPT(1, 0)), samePlayer()).card());
//		game.addCard(minion( 2,      RARE, 2, 2, "Pint-Sized Summoner").effect("The first minion you play each turn costs (1) less").card());
		game.addCard(minion( 2,      RARE, 2, 3, "Sunfury Protector").battlecry(adjacents(giveAbility(HSAbility.TAUNT))).card());
//		game.addCard(minion( 2,      RARE, 3, 2, "Wild Pyromancer").effect("After you cast a spell, deal 1 damage to ALL minions").card());
		game.addCard(minion( 2,      EPIC, 1, 1, "Captain's Parrot").battlecry(moveRandomFromDeckToHand(allMinions().and(minionIs(HStoneMinionType.PIRATE)))).card());
		game.addCard(minion( 2,      EPIC, 0, 7, "Doomsayer").on(HStoneTurnStartEvent.class, destroyAllMinions(), samePlayer()).card());
		game.addCard(minion( 2, LEGENDARY, 1, 1, "Bloodmage Thalnos").spellDamage(1).deathrattle(drawCard()).card());
		game.addCard(minion( 2, LEGENDARY, 3, 3, "Finkle Einhorn").card());
//		game.addCard(minion( 2, LEGENDARY, 0, 4, "Lorewalker Cho").effect("Whenever a player casts a spell, put a copy into the other player’s hand").card());
//		game.addCard(minion( 2, LEGENDARY, 4, 4, "Millhouse Manastorm").effect("<b>Battlecry:</b>").effect("Enemy spells cost (0) next turn").card());
		game.addCard(minion( 2, LEGENDARY, 0, 4, "Nat Pagle").on(HStoneTurnStartEvent.class, evenChance(drawCard(), doNothing()), samePlayer()).card());
	}

	public static HStoneEffect moveRandomFromDeckToHand(HSFilter filter) {
//		 "Put a random Pirate from your deck into your hand"
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				CardZone<HStoneCard> deck = source.getPlayer().getDeck();
				List<HStoneCard> cardOptions = new ArrayList<>();
				for (HStoneCard card : deck) {
					if (filter.shouldKeep(source, card)) {
						cardOptions.add(card);
					}
				}
				HStoneCard chosen = ZomisList.getRandom(cardOptions, source.getGame().getRandom());
				if (chosen != null)
					chosen.zoneMoveOnBottom(source.getPlayer().getHand());
			}
		};
	}

	public static HStoneEffect swapPT() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				int attack = target.getAttack();
				int health = target.getHealth();
				target.getResources().set(HStoneRes.ATTACK, health);
				target.getResources().set(HStoneRes.HEALTH, attack);
				target.getResources().set(HStoneRes.MAX_HEALTH, attack);
			}
		};
	}



}
