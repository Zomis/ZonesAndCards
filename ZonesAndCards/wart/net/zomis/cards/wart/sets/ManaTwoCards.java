package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.model.CardZone;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HSFilter;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.HStoneRes;
import net.zomis.cards.wart.events.HStoneCardPlayedEvent;
import net.zomis.cards.wart.events.HStoneMinionSummonedEvent;
import net.zomis.cards.wart.events.HStoneTurnEndEvent;
import net.zomis.cards.wart.events.HStoneTurnStartEvent;
import net.zomis.cards.wart.factory.Battlecry;
import net.zomis.cards.wart.factory.HSAbility;
import net.zomis.cards.wart.factory.HSFilters;
import net.zomis.cards.wart.factory.HSGetCounts;
import net.zomis.cards.wart.factory.HStoneEffect;
import net.zomis.cards.wart.factory.HStoneMinionType;
import net.zomis.utils.ZomisList;

public class ManaTwoCards implements CardSet<HStoneGame> {

	private static final HSGetCounts c = new HSGetCounts();
	private static final HSFilters f = new HSFilters();
	private static final Battlecry e = new Battlecry();
	
	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 2,      NONE, 2, 2, "Gnoll").taunt().card());
		game.addCard(minion( 2,      FREE, 3, 2, "Bloodfen Raptor").card());
		game.addCard(minion( 2,      FREE, 1, 1, "Novice Engineer").battlecry(e.drawCard()).card());
		game.addCard(minion( 2,      FREE, 2, 3, "River Crocolisk").card());
		game.addCard(minion( 2,    COMMON, 3, 2, "Acidic Swamp Ooze").battlecry(e.destroyOppWeapon()).card());
		game.addCard(minion( 2,    COMMON, 2, 3, "Amani Berserker").staticPT(f.enrage(), 3, 0).card());
		game.addCard(minion( 2,    COMMON, 2, 3, "Bloodsail Raider").battlecry(e.selfPT(c.calcWeaponDamage, c.fixed(0))).card());
		game.addCard(minion( 2,    COMMON, 2, 1, "Bluegill Warrior").charge().card());
		game.addCard(minion( 2,    COMMON, 2, 2, "Dire Wolf Alpha").staticPT(f.adjacents(), 1, 0).card());
		game.addCard(minion( 2,    COMMON, 3, 2, "Faerie Dragon").shroud().card());
		game.addCard(minion( 2,    COMMON, 2, 2, "Frostwolf Grunt").taunt().card());
		game.addCard(minion( 2,    COMMON, 2, 1, "Ironbeak Owl").battlecry(e.silencer()).card());
		game.addCard(minion( 2,    COMMON, 2, 2, "Kobold Geomancer").spellDamage(1).card());
		game.addCard(minion( 2,    COMMON, 2, 1, "Loot Hoarder").deathrattle(e.drawCard()).card());
		game.addCard(minion( 2,    COMMON, 3, 2, "Mad Bomber").battlecry(e.repeat(c.fixed(3), e.toRandom(f.canTakeDamage(1), e.damage(1)))).card());
		game.addCard(minion( 2,    COMMON, 2, 1, "Murloc Tidehunter").battlecry(e.summon("Murloc Scout")).card());
		game.addCard(minion( 2,    COMMON, 3, 2, "Youthful Brewmaster").battlecry(e.to(f.samePlayer().and(f.allMinions()), e.unsummon())).card());
		game.addCard(minion( 2,      RARE, 4, 5, "Ancient Watcher").noAttack().card());
		game.addCard(minion( 2,      RARE, 2, 2, "Crazed Alchemist").battlecry(e.toMinion(swapPT())).card());
		game.addCard(minion( 2,      RARE, 3, 2, "Knife Juggler").on(HStoneMinionSummonedEvent.class, e.toRandom(f.all().and(f.opponent()), e.damage(1)), f.samePlayer()).card());
		game.addCard(minion( 2,      RARE, 1, 3, "Mana Addict").on(HStoneCardPlayedEvent.class, e.tempBoost(f.thisCard(), 2, 0), f.samePlayer().and(f.isSpell())).card());
//		game.addCard(minion( 2,      RARE, 2, 2, "Mana Wraith").effect("ALL minions cost (1) more").card());
		game.addCard(minion( 2,      RARE, 1, 3, "Master Swordsmith").on(HStoneTurnEndEvent.class, e.toRandom(f.allMinions().and(f.samePlayer()), e.otherPT(1, 0)), f.samePlayer()).card());
//		game.addCard(minion( 2,      RARE, 2, 2, "Pint-Sized Summoner").effect("The first minion you play each turn costs (1) less").card());
		game.addCard(minion( 2,      RARE, 2, 3, "Sunfury Protector").battlecry(e.adjacents(e.giveAbility(HSAbility.TAUNT))).card());
//		game.addCard(minion( 2,      RARE, 3, 2, "Wild Pyromancer").effect("After you cast a spell, deal 1 damage to ALL minions").card());
		game.addCard(minion( 2,      EPIC, 1, 1, "Captain's Parrot").battlecry(moveRandomFromDeckToHand(f.allMinions().and(f.minionIs(HStoneMinionType.PIRATE)))).card());
		game.addCard(minion( 2,      EPIC, 0, 7, "Doomsayer").on(HStoneTurnStartEvent.class, e.destroyAllMinions(), f.samePlayer()).card());
		game.addCard(minion( 2, LEGENDARY, 1, 1, "Bloodmage Thalnos").spellDamage(1).deathrattle(e.drawCard()).card());
		game.addCard(minion( 2, LEGENDARY, 3, 3, "Finkle Einhorn").card());
//		game.addCard(minion( 2, LEGENDARY, 0, 4, "Lorewalker Cho").effect("Whenever a player casts a spell, put a copy into the other player’s hand").card());
//		game.addCard(minion( 2, LEGENDARY, 4, 4, "Millhouse Manastorm").effect("<b>Battlecry:</b>").effect("Enemy spells cost (0) next turn").card());
		game.addCard(minion( 2, LEGENDARY, 0, 4, "Nat Pagle").on(HStoneTurnStartEvent.class, e.evenChance(e.drawCard(), e.doNothing()), f.samePlayer()).card());
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
