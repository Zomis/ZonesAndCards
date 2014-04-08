package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneMinionType.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.util.CardSet;

public class ManaOneCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
//		game.addCard(minion(1, RARE  , 2, 1, "Young Priestess").on(EndTurn, randomTarget().health(+1)).card());
//		game.addCard(minion(1, COMMON, 2, 1, "Southsea Deckhand").staticEffect(chargeIfHasWeapon()).card());
//		game.addCard(minion(1, RARE  , 1, 2, "Secretkeeper").on(SecretPlayed, PT(1, 1)).card());
//		game.addCard(minion(1, RARE  , 1, 2, "Murloc Tidecaller").on(Summoned(Murloc), PT(1, 0)).card());
		game.addCard(minion(1, RARE  , 1, 2, "Hungry Crab").battlecry(forEach(minionIsMurloc(), null, otherDestroy())).card());
//		game.addCard(minion(1, COMMON, 2, 1, "Leper Gnome").death(damageTo(2, Target.enemyHero())).card());
//		game.addCard(minion(1, RARE, 1, 2, "Lightwarden").on(Heal, Effect(triggerCard, PT(2, 0))).card());
		game.addCard(spell(1, COMMON, "Ice Lance").effect(freezeOrDamage(4)).card());

		game.addCard(minion(1, COMMON, 2, 1, "Abusive Sergeant").battlecry(tempBoost(2, 0)).card());
		game.addCard(minion(1, RARE  , 1, 1, "Angry Chicken").enrage(selfPT(5, 0)).card());
		game.addCard(minion(1, COMMON, 1, 1, "Argent Squire").shield().card());
		game.addCard(minion(1, RARE  , 1, 2, "Bloodsail Corsair").battlecry(removeDurabilityOppWeapon(1)).card());
		game.addCard(minion(1, BASE  , 1, 1, "Elven Archer").battlecry(damage(1)).card());
		game.addCard(minion(1, BASE  , 1, 1, "Grimscale Oracle").staticEffect(typePTBonus(MURLOC, 1, 0)).card());
		game.addCard(minion(1, BASE  , 1, 2, "Goldshire Footman").taunt().card());
		game.addCard(minion(1, BASE  , 2, 1, "Murloc Raider").is(MURLOC).card());
		game.addCard(minion(1, COMMON, 0, 4, "Shieldbearer").taunt().card());
		game.addCard(minion(1, BASE  , 1, 1, "Stonetusk Boar").charge().card());
		game.addCard(minion(1, BASE  , 2, 1, "Voodoo Doctor").battlecry(heal(2)).card());
		game.addCard(minion(1, COMMON, 2, 1, "Worgen Infiltrator").stealth().card());
		game.addCard(minion(1, COMMON, 1, 1, "Young Dragonhawk").windfury().card());
		
	}

}
