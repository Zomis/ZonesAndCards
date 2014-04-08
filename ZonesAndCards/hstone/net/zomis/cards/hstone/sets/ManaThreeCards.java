package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.util.CardSet;

public class ManaThreeCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion(3, EPIC  , 3, 3, "Blood Knight").battlecry(forEach(minionWithDivineShield(), selfPT(3, 3), removeShield())).card());
		
		game.addCard(minion(3, RARE  , 2, 2, "Coldlight Oracle").battlecry(allDraw(2)).card());
		game.addCard(minion(3, RARE  , 2, 3, "Coldlight Seer").battlecry(forEach(minionIsMurloc(), null, otherPT(0, 2))).card());
		game.addCard(minion(3, BASE  , 1, 4, "Dalaran Mage").spellDamage(1).card());
//		hStoneGame.addCard(minion(3, COMMON, 1, 3, "Acolyte of Pain").on(takesDamage, drawCard()).card());
		
//		hStoneGame.addCard(minion(3, RARE  , 1, 4, "Demolisher").on(myStartTurn, damageToRandom(enemy, 2).card());
		game.addCard(minion(3, COMMON, 3, 3, "Earthen Ring Farseer").battlecry(heal(3)).card());
//		hStoneGame.addCard(minion(3, RARE  , 2, 3, "Emperor Cobra").on(thisDealsDamage(), destroyThatOne()).card());
//		hStoneGame.addCard(minion(3, COMMON, 2, 3, "Flesheating Ghoul").on(minionDies(), myPT(1, 0)).card());
		

//		hStoneGame.addCard(minion(3, RARE  , 0, 3, "Alarm-o-Bot").on(startTurn, exchange(thisCard, randomMinionHand())).card());
		game.addCard(minion(3, RARE  , 4, 2, "Arcane Golem").charge().battlecry(giveOpponentManaCrystal()).card());
//		hStoneGame.addCard(minion(3, RARE  , 4, 2, "Big Game Hunter").battlecry(destroyMinion(targetHasPT(7, 0))).card());
	}

}
