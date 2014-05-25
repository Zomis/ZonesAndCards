package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.ench.HStoneEnchSpecificPT;
import net.zomis.cards.wart.events.HStoneMinionDiesEvent;
import net.zomis.cards.wart.factory.Battlecry;
import net.zomis.cards.wart.factory.HSAbility;
import net.zomis.cards.wart.factory.HSFilters;
import net.zomis.cards.wart.factory.HStoneEffect;

public class ManaFourCards implements CardSet<HStoneGame> {

	private static final HSFilters f = new HSFilters();
	private static final Battlecry e = new Battlecry();
	
	@Override
	public void addCards(HStoneGame game) {
		
		game.addCard(minion( 4,      NONE, 7, 6, "Emerald Drake").card());
		game.addCard(minion( 4,      FREE, 2, 7, "Oasis Snapjaw").card());
		game.addCard(minion( 4,      FREE, 3, 5, "Sen'jin Shieldmasta").taunt().card());
//		game.addCard(minion( 4,    COMMON, 5, 4, "Ancient Brewmaster").battlecry("Return a friendly minion from the battlefield to your hand").card());
		game.addCard(minion( 4,    COMMON, 4, 5, "Chillwind Yeti").card());
		game.addCard(minion( 4,    COMMON, 4, 2, "Cult Master").on(HStoneMinionDiesEvent.class, e.drawCard(), f.samePlayer().and(f.allMinions()).and(f.anotherCard())).card());
		game.addCard(minion( 4,    COMMON, 4, 4, "Dark Iron Dwarf").battlecry(e.tempBoost(f.allMinions(), 2, 0)).card());
		game.addCard(minion( 4,    COMMON, 2, 4, "Dragonling Mechanic").battlecry(e.summon("Mechanical Dragonling")).card());
//		game.addCard(minion( 4,    COMMON, 3, 3, "Dread Corsair").taunt().effect("Costs (1) less per Attack of your weapon").card());
		game.addCard(minion( 4,    COMMON, 2, 4, "Gnomish Inventor").battlecry(e.drawCard()).card());
		game.addCard(minion( 4,    COMMON, 1, 7, "Mogu'shan Warden").taunt().card());
		game.addCard(minion( 4,    COMMON, 4, 4, "Ogre Magi").spellDamage(1).card());
		game.addCard(minion( 4,    COMMON, 3, 3, "Silvermoon Guardian").shield().card());
		game.addCard(minion( 4,    COMMON, 4, 3, "Spellbreaker").battlecry(e.silencer()).card());
		game.addCard(minion( 4,    COMMON, 2, 5, "Stormwind Knight").charge().card());
		game.addCard(minion( 4,      RARE, 2, 5, "Ancient Mage").battlecry(e.adjacents(e.giveSpellDamage(1))).card());
		game.addCard(minion( 4, 	 RARE, 2, 3, "Defender of Argus").battlecry(e.adjacents(e.combined(e.otherPT(1, 1), e.giveAbility(HSAbility.TAUNT)))).card());
		game.addCard(minion( 4,      RARE, 4, 1, "Twilight Drake").battlecry(gainHealthForEachCardInHand()).card());
//		game.addCard(minion( 4,      RARE, 3, 5, "Violet Teacher").effect("Whenever you cast a spell, summon a 1/1 Violet Apprentice").card());
		game.addCard(minion( 4, LEGENDARY, 4, 5, "Baine Bloodhoof").card());
		game.addCard(minion( 4, LEGENDARY, 6, 2, "Leeroy Jenkins").charge().battlecry(e.oppSummon("Whelp", 2)).card());
//		game.addCard(minion( 4, LEGENDARY, 2, 4, "Old Murk-Eye").charge().effect("Has +1 Attack for each other Murloc on the battlefield").card());
		game.addCard(spell( 4,      NONE, "I Am Murloc").effect(e.iff(f.haveSpaceOnBattleField, e.evenChance(e.summon("Murloc", 3), e.summon("Murloc", 4), e.summon("Murloc", 5)))).card());
		game.addCard(spell( 4,      NONE, "Power of the Horde").effect(e.iff(f.haveSpaceOnBattleField, e.evenChance(e.summon("Frostwolf Grunt"), e.summon("Sen'jin Shieldmasta"),
				e.summon("Cairne Bloodhoof"), e.summon("Tauren Warrior"), e.summon("Thrallmar Farseer"), e.summon("Silvermoon Guardian")))).card());
		game.addCard(spell( 4,      NONE, "Rogues Do It...").effect(e.toAny(e.combined(e.damage(4), e.drawCard()))).card());
	}

	private HStoneEffect gainHealthForEachCardInHand() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				int health = source.getPlayer().getHand().size(); // TODO: Should not count the card itself.
				source.addEnchantment(new HStoneEnchSpecificPT(source, 0, health));
			}
		};
	}


}
