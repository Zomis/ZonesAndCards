package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.HStoneRes;
import net.zomis.cards.wart.ench.HStoneEnchForward;
import net.zomis.cards.wart.ench.HStoneEnchSpecificPT;
import net.zomis.cards.wart.events.HStoneCardPlayedEvent;
import net.zomis.cards.wart.events.HStoneDamagedEvent;
import net.zomis.cards.wart.factory.Battlecry;
import net.zomis.cards.wart.factory.HSFilters;
import net.zomis.cards.wart.factory.HSGetCounts;
import net.zomis.cards.wart.factory.HStoneEffect;

public class ManaFiveCards implements CardSet<HStoneGame> {

	private static final HSGetCounts c = new HSGetCounts();
	private static final HSFilters f = new HSFilters();
	private static final Battlecry e = new Battlecry();
	
	@Override
	public void addCards(HStoneGame game) {
		
		game.addCard(minion( 5,      FREE, 4, 4, "Nightblade").battlecry(e.damageToOppHero(3)).card());
		game.addCard(minion( 5,    COMMON, 5, 4, "Booty Bay Bodyguard").taunt().card());
		game.addCard(minion( 5,    COMMON, 4, 5, "Darkscale Healer").battlecry(e.forEach(f.samePlayer(), null, e.heal(2))).card());
		game.addCard(minion( 5,    COMMON, 5, 5, "Devilsaur").card());
		game.addCard(minion( 5,    COMMON, 3, 6, "Fen Creeper").taunt().card());
		game.addCard(minion( 5,    COMMON, 4, 4, "Frostwolf Warlord").battlecry(e.forEach(f.allMinions().and(f.anotherCard()), e.selfPT(1, 1), null)).card());
		game.addCard(minion( 5,    COMMON, 2, 7, "Gurubashi Berserker").on(HStoneDamagedEvent.class, e.selfPT(3, 0), f.thisCard()).card());
		game.addCard(minion( 5,    COMMON, 4, 4, "Silver Hand Knight").battlecry(e.summon("Squire")).card());
		game.addCard(minion( 5,    COMMON, 4, 6, "Spiteful Smith").effect(enrageWeaponTwoAttack()).card());
		game.addCard(minion( 5,    COMMON, 4, 2, "Stormpike Commando").battlecry(e.damage(2)).card());
		game.addCard(minion( 5,    COMMON, 5, 5, "Stranglethorn Tiger").stealth().card());
		game.addCard(minion( 5,    COMMON, 7, 6, "Venture Co. Mercenary").staticMana(f.allMinions().and(f.samePlayer()), 3).card());
		game.addCard(minion( 5,      RARE, 4, 4, "Abomination").taunt().deathrattle(e.forEach(f.all(), null, e.damage(2))).card());
		game.addCard(minion( 5,      RARE, 4, 4, "Azure Drake").spellDamage(1).battlecry(e.drawCard()).card());
		game.addCard(minion( 5,      RARE, 4, 4, "Gadgetzan Auctioneer").on(HStoneCardPlayedEvent.class, e.drawCard(), f.samePlayer().and(f.isSpell())).card());
		game.addCard(minion( 5,      RARE, 3, 5, "Stampeding Kodo").battlecry(e.toRandom(f.opponentMinions().and(f.withAttackLess(2)), e.destroyTarget())).card());
		game.addCard(minion( 5,      EPIC, 3, 3, "Faceless Manipulator").battlecry(e.pickAndCopy()).card());
		game.addCard(minion( 5, LEGENDARY, 5, 4, "Captain Greenskin").battlecry(giveWeapon(1, 1)).card());
		game.addCard(minion( 5, LEGENDARY, 5, 5, "Elite Tauren Chieftain").battlecry(e.bothPlayers(e.evenChance(e.giveCard("I Am Murloc"), e.giveCard("Power of the Horde"), e.giveCard("Rogues Do It...")))).card());
		game.addCard(minion( 5, LEGENDARY, 5, 4, "Harrison Jones").battlecry(e.combined(e.repeat(c.oppWeaponDurability(), e.drawCard()), e.destroyOppWeapon())).card());
	}

	private HStoneEffect giveWeapon(int attack, int durability) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				HStoneCard weapon = source.getPlayer().getWeapon();
				if (weapon == null)
					return;
				weapon.getResources().changeResources(HStoneRes.ATTACK, attack);
				weapon.getResources().changeResources(HStoneRes.HEALTH, durability);
			}
		};
	}

	private HStoneEffect enrageWeaponTwoAttack() {
		return new HStoneEffect() {

			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.addEnchantment(new HStoneEnchForward(new HStoneEnchSpecificPT(null, 2, 0)) {
					@Override
					public boolean isActive() {
						return source.isAlive();
					}

					@Override
					public boolean appliesTo(HStoneCard card) {
						return f.enrage().shouldKeep(source, source) && f.isWeapon.and(f.samePlayer()).shouldKeep(source, card);
					}
				});
			}
		};
	}


}
