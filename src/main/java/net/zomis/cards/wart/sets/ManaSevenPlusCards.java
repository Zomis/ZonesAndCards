package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HSFilter;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.HStonePlayer;
import net.zomis.cards.wart.HStoneRes;
import net.zomis.cards.wart.events.HStoneTurnEndEvent;
import net.zomis.cards.wart.events.HStoneTurnStartEvent;
import net.zomis.cards.wart.factory.Battlecry;
import net.zomis.cards.wart.factory.HSFilters;
import net.zomis.cards.wart.factory.HSGetCounts;
import net.zomis.cards.wart.factory.HStoneCardModel;
import net.zomis.cards.wart.factory.HStoneEffect;
import net.zomis.cards.wart.triggers.CardEventTrigger;

public class ManaSevenPlusCards implements CardSet<HStoneGame> {

	private static final HSGetCounts c = new HSGetCounts();
	private static final HSFilters f = new HSFilters();
	private static final Battlecry e = new Battlecry();
	
	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 7,    COMMON, 9, 5, "Core Hound").card());
		game.addCard(minion( 7,    COMMON, 6, 6, "Stormwind Champion").staticPT(f.allMinions().and(f.samePlayer()).and(f.anotherCard()), 1, 1).card());
		game.addCard(minion( 7,    COMMON, 7, 7, "War Golem").card());
		game.addCard(minion( 7,      RARE, 7, 5, "Ravenholdt Assassin").stealth().card());
		game.addCard(minion(20,      EPIC, 8, 8, "Molten Giant").staticSelfCost(c.oneLessPerHeroDamage).card());
		game.addCard(minion(12,      EPIC, 8, 8, "Mountain Giant").staticSelfCost(c.oneLessPerOtherCardInHand).card());
		game.addCard(minion(10,      EPIC, 8, 8, "Sea Giant").staticSelfCost(c.oneLessPerOtherMinionOnBattlefield).card());
		game.addCard(minion( 9, LEGENDARY, 8, 8, "Alexstrasza").battlecry(e.to(f.allPlayers(), e.set(HStoneRes.HEALTH, 15))).card());
		game.addCard(minion( 7, LEGENDARY, 7, 5, "Baron Geddon").on(HStoneTurnEndEvent.class, e.forEach(f.anotherCard(), null, e.damage(2)), f.samePlayer()).card());
		game.addCard(minion(10, LEGENDARY, 12, 12, "Deathwing").battlecry(e.combined(e.forEach(f.allMinions().and(f.anotherCard()), null, e.destroyTarget()), discardAllCards())).card());
		game.addCard(minion( 8, LEGENDARY, 7, 7, "Gruul").on(HStoneTurnEndEvent.class, e.selfPT(1, 1), f.all()).card());
		game.addCard(minion( 9, LEGENDARY, 4, 12, "Malygos").spellDamage(5).card());
//		game.addCard(minion( 9, LEGENDARY, 8, 8, "Nozdormu").effect("Players only have 15 seconds to take their turns").card());
		game.addCard(minion( 9, LEGENDARY, 8, 8, "Onyxia").battlecry(e.summon("Whelp", HStonePlayer.MAX_BATTLEFIELD_SIZE)).card());
		game.addCard(minion( 8, LEGENDARY, 8, 8, "Ragnaros the Firelord").noAttack().on(HStoneTurnEndEvent.class, e.toRandom(f.opponent(), e.damage(8)), f.samePlayer()).card());
		HStoneCardModel YSERA = minion( 9, LEGENDARY, 4, 12, "Ysera").on(HStoneTurnEndEvent.class, drawDreamCard(), f.samePlayer()).card();
		game.addCard(YSERA);
		
		game.addCard(spell( 0, COMMON, "Dream").effect(e.toMinion(e.unsummon())).card());
		game.addCard(spell( 0, COMMON, "Nightmare").effect(e.toMinion(e.combined(e.otherPT(5, 5), destroyAtStartOfMyNextTurn()))).card());
		game.addCard(spell( 2, COMMON, "Ysera Awakens").effect(e.forEach(f.all().and(f.not(is(YSERA))), null, e.damage(8))).card());
	}

	public HStoneEffect destroyAtStartOfMyNextTurn() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				HSFilter filter = new HSFilter() {
					@Override
					public boolean shouldKeep(HStoneCard cardWithTrigger, HStoneCard whoCausedEvent) {
						return source.getPlayer() == whoCausedEvent.getPlayer();
					}
				};
				target.addTrigger(new CardEventTrigger(HStoneTurnStartEvent.class, e.destroy(target), filter));
			}
		};
	}

	public HSFilter is(HStoneCardModel model) {
		return (src, dst) -> dst.getModel() == model;
	}

	private HStoneEffect drawDreamCard() {
		return e.evenChance(e.giveCard("Dream"), e.giveCard("Emerald Drake"), e.giveCard("Laughing Sister"), e.giveCard("Nightmare"), e.giveCard("Ysera Awakens"));
	}

	private HStoneEffect discardAllCards() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				for (HStoneCard card : source.getPlayer().getHand())
					if (card != source)
						card.zoneMoveOnBottom(source.getPlayer().getDiscard());
			}
		};
	}

}
