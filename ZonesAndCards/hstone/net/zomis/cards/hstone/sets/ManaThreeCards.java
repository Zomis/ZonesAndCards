package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;

import java.util.List;

import net.zomis.cards.hstone.FightModule;
import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.events.HStoneDamageDealtEvent;
import net.zomis.cards.hstone.events.HStoneMinionDiesEvent;
import net.zomis.cards.hstone.events.HStoneTurnStartEvent;
import net.zomis.cards.hstone.factory.HSFilters;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.util.CardSet;
import net.zomis.utils.ZomisList;

public class ManaThreeCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion(3, EPIC  , 3, 3, "Blood Knight").battlecry(forEach(HSFilters.minionWithDivineShield(), selfPT(3, 3), removeShield())).card());
		
		game.addCard(minion(3, RARE  , 2, 2, "Coldlight Oracle").battlecry(allDraw(2)).card());
		game.addCard(minion(3, RARE  , 2, 3, "Coldlight Seer").battlecry(forEach(HSFilters.minionIsMurloc(), null, otherPT(0, 2))).card());
		game.addCard(minion(3, FREE  , 1, 4, "Dalaran Mage").spellDamage(1).card());
		game.addCard(minion(3, COMMON, 1, 3, "Acolyte Of Pain").on(HStoneDamageDealtEvent.class, drawCard(), HSFilters.thisCard()).card());
		
		game.addCard(minion(3, RARE  , 1, 4, "Demolisher").on(HStoneTurnStartEvent.class, damageToRandom(enemy(), 2), HSFilters.samePlayer()).card());
		game.addCard(minion(3, COMMON, 3, 3, "Earthen Ring Farseer").battlecry(heal(3)).card());
//		hStoneGame.addCard(minion(3, RARE  , 2, 3, "Emperor Cobra").on(thisDealsDamage(), destroyThatOne()).card());
		game.addCard(minion(3, COMMON, 2, 3, "Flesheating Ghoul").on(HStoneMinionDiesEvent.class, selfPT(1, 0), HSFilters.all()).card());
		

//		hStoneGame.addCard(minion(3, RARE  , 0, 3, "Alarm-o-Bot").on(myTurnStart, exchange(thisCard, randomMinionHand())).card());
		game.addCard(minion(3, RARE  , 4, 2, "Arcane Golem").charge().battlecry(giveOpponentManaCrystal()).card());
//		hStoneGame.addCard(minion(3, RARE  , 4, 2, "Big Game Hunter").battlecry(destroyMinion(targetHasPT(7, 0))).card());
	}

	private HStoneEffect damageToRandom(final HSFilter filter, final int damage) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				List<HStoneCard> all = source.getGame().findAll(source, filter);
				HStoneCard random = ZomisList.getRandom(all, source.getGame().getRandom());
				FightModule.damage(random, damage);
			}
		};
	}

	private HSFilter enemy() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return searcher.getPlayer() != target.getPlayer();
			}
		};
	}

}
