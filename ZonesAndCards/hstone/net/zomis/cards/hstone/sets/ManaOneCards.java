package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HSFilters.*;
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
import net.zomis.cards.hstone.events.HStoneTurnStartEvent;
import net.zomis.cards.hstone.factory.HSFilters;
import net.zomis.cards.hstone.factory.HStoneCardModel;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.util.CardSet;
import net.zomis.utils.ZomisList;

public class ManaOneCards implements CardSet<HStoneGame> {

	public enum CardEnumTest {
		FLAME_OF_AZZINOTH(minion( 1,      NONE, 2, 1, "Flame of Azzinoth").card());
		
		private final HStoneCardModel model;

		private CardEnumTest(HStoneCardModel model) {
			this.model = model;
		}
		
		public HStoneCardModel getModel() {
			return model;
		}
	}
	
	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 1,      NONE, 2, 1, "Flame of Azzinoth").card());
		game.addCard(minion( 1,      NONE, 1, 1, "Murloc").card());
		game.addCard(minion( 1,      NONE, 1, 1, "Whelp").card());
		game.addCard(minion( 1,      FREE, 2, 1, "Murloc Raider").card());
		game.addCard(minion( 1,      FREE, 1, 1, "Stonetusk Boar").charge().card());
		game.addCard(minion( 1,      FREE, 2, 1, "Voodoo Doctor").battlecry(heal(2)).card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Abusive Sergeant").battlecry(tempBoost(allMinions(), 2, 0)).card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Argent Squire").shield().card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Boar").card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Damaged Golem").card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Elven Archer").battlecry(damage(1)).card());
		game.addCard(minion( 1,    COMMON, 0, 4, "Emboldener 3000").on(HStoneTurnEndEvent.class, toRandom(allMinions(), otherPT(1, 1)), samePlayer()).card());
		game.addCard(minion( 1,    COMMON, 1, 2, "Goldshire Footman").taunt().card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Grimscale Oracle").staticEffectOtherMurlocsBonus(1, 0).is(MURLOC).card());
		game.addCard(minion( 1,    COMMON, 0, 1, "Homing Chicken").on(HStoneTurnStartEvent.class, combined(drawCards(3), selfDestruct()), samePlayer()).card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Leper Gnome").deathrattle(damageToEnemyHero(2)).card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Mechanical Dragonling").card());
		game.addCard(minion( 1,    COMMON, 0, 3, "Poultryizer").on(HStoneTurnStartEvent.class, toRandom(allMinions(), transform("Chicken")), samePlayer()).card());
		game.addCard(minion( 1,    COMMON, 0, 3, "Repair Bot").on(HStoneTurnEndEvent.class, toRandom(not(undamaged()), heal(6)), samePlayer()).card());
		game.addCard(minion( 1,    COMMON, 0, 4, "Shieldbearer").taunt().card());
//		game.addCard(minion( 1,    COMMON, 2, 1, "Southsea Deckhand").effect("Has").effect("<b>Charge</b>").effect("while you have a weapon equipped").card());
		game.addCard(minion( 1,    COMMON, 2, 2, "Squire").card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Squirrel").card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Worgen Infiltrator").stealth().card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Young Dragonhawk").windfury().card());
//		game.addCard(minion( 1,      RARE, 1, 1, "Angry Chicken").effect("<b>Enrage:</b>").effect("+5 Attack").card());
		game.addCard(minion( 1,      RARE, 1, 2, "Bloodsail Corsair").battlecry(removeDurabilityOppWeapon(1)).card());
		game.addCard(minion( 1,      RARE, 1, 1, "Imp").card());
		game.addCard(minion( 1,      RARE, 1, 2, "Lightwarden").on(HStoneHealEvent.class, selfPT(2, 0), all()).card());
//		game.addCard(minion( 1,      RARE, 1, 2, "Murloc Tidecaller").effect("Whenever a Murloc is summoned, gain +1 Attack").card());
//		game.addCard(minion( 1,      RARE, 1, 2, "Secretkeeper").effect("Whenever a").effect("<b>Secret</b>").effect("is played, gain +1/+1").card());
		game.addCard(minion( 1,      RARE, 2, 1, "Young Priestess").on(HStoneTurnEndEvent.class, randomFriendlyMinion(otherPT(0, 1)), HSFilters.samePlayer()).card());
		game.addCard(minion( 1,      EPIC, 1, 2, "Hungry Crab").battlecry(to(minionIs(MURLOC), selfPT(2, 2))).card()); // TODO: Only perform battlecry if there is a target available
		game.addCard(spell( 1,      NONE, "Bananas").effect(toMinion(otherPT(1, 1))).card());
	}

	private HStoneEffect transform(final String cardName) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				// TODO Auto-generated method stub
			}
		};
	}

	private HStoneEffect selfDestruct() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.destroy();
			}
		};
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
