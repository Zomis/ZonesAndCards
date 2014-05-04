package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneMinionType.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;

import java.util.LinkedList;

import net.zomis.cards.hstone.FightModule;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.HStonePlayer;
import net.zomis.cards.hstone.events.HStoneHealEvent;
import net.zomis.cards.hstone.events.HStoneTurnEndEvent;
import net.zomis.cards.hstone.factory.HSFilters;
import net.zomis.cards.hstone.factory.HSTargetType;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.util.CardSet;
import net.zomis.utils.ZomisList;

public class ManaOneCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion(1, RARE  , 2, 1, "Young Priestess").on(HStoneTurnEndEvent.class, randomFriendlyMinion(otherPT(0, 1)), HSFilters.samePlayer()).card());
//		game.addCard(minion(1, COMMON, 2, 1, "Southsea Deckhand").staticEffect(chargeIfHasWeapon()).card());
//		game.addCard(minion(1, RARE  , 1, 2, "Secretkeeper").on(SecretPlayed, PT(1, 1)).card());
//		game.addCard(minion(1, RARE  , 1, 2, "Murloc Tidecaller").on(Summoned(Murloc), PT(1, 0)).card());
		game.addCard(minion(1, RARE  , 1, 2, "Hungry Crab").battlecry(forEach(HSFilters.minionIsMurloc(), null, otherDestroy())).card());
		game.addCard(minion(1, COMMON, 2, 1, "Leper Gnome").deathrattle(damageToEnemyHero(2)).card());
		game.addCard(minion(1, RARE, 1, 2, "Lightwarden").on(HStoneHealEvent.class, selfPT(2, 0), HSFilters.all()).card());
		game.addCard(spell(1, COMMON, "Ice Lance").effect(freezeOrDamage(4)).card());

		game.addCard(minion(1, COMMON, 2, 1, "Abusive Sergeant").battlecry(tempBoost(HSTargetType.MINION, 2, 0)).card());
		game.addCard(minion(1, RARE  , 1, 1, "Angry Chicken").enrage(selfPT(5, 0)).card());
		game.addCard(minion(1, COMMON, 1, 1, "Argent Squire").shield().card());
		game.addCard(minion(1, RARE  , 1, 2, "Bloodsail Corsair").battlecry(removeDurabilityOppWeapon(1)).card());
		game.addCard(minion(1, FREE  , 1, 1, "Elven Archer").battlecry(damage(1)).card());
		game.addCard(minion(1, FREE  , 1, 1, "Grimscale Oracle").is(MURLOC).staticEffectOtherMurlocsBonus(1, 0).card());
		game.addCard(minion(1, FREE  , 1, 2, "Goldshire Footman").taunt().card());
		game.addCard(minion(1, FREE  , 2, 1, "Murloc Raider").is(MURLOC).card());
		game.addCard(minion(1, COMMON, 0, 4, "Shieldbearer").taunt().card());
		game.addCard(minion(1, FREE  , 1, 1, "Stonetusk Boar").charge().card());
		game.addCard(minion(1, FREE  , 2, 1, "Voodoo Doctor").battlecry(heal(2)).card());
		game.addCard(minion(1, COMMON, 2, 1, "Worgen Infiltrator").stealth().card());
		game.addCard(minion(1, COMMON, 1, 1, "Young Dragonhawk").windfury().card());
		
	}

	private HStoneEffect randomFriendlyMinion(final HStoneEffect effect) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				LinkedList<HStoneCard> list = source.getPlayer().getBattlefield().cardList();
				list.remove(source);
				HStoneCard random = ZomisList.getRandom(list, source.getGame().getRandom());
				effect.performEffect(source, random);
			}
		};
	}

	private HStoneEffect damageToEnemyHero(final int damage) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				HStonePlayer player = source.getPlayer();
				HStonePlayer opponent = player.getNextPlayer();
				FightModule.damage(opponent.getPlayerCard(), damage);
			}
		};
	}

}
