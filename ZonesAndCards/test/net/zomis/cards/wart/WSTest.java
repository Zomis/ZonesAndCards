package net.zomis.cards.wart;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.zomis.cards.CardsTest;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneClass;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.HStonePlayer;
import net.zomis.cards.wart.HStoneRes;
import net.zomis.cards.wart.factory.CardType;
import net.zomis.cards.wart.factory.HSAbility;
import net.zomis.cards.wart.factory.HSFilters;
import net.zomis.cards.wart.factory.HStoneCardModel;
import net.zomis.cards.wart.factory.HStoneChar;
import net.zomis.cards.wart.factory.HStoneDecks;
import net.zomis.cards.wart.factory.HStoneMinionType;
import net.zomis.cards.wart.factory.HStoneRarity;
import net.zomis.cards.wart.sets.DruidCards;
import net.zomis.cards.wart.sets.HStoneOption;
import net.zomis.utils.ZomisList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class WSTest extends CardsTest<HStoneGame> {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void noComboNoDamage() {
    	reachMana(8);
    	superPlayCard("SI:7 Agent");
    	assertFalse(game.isTargetSelectionMode());
    	
    	superPlayCard("SI:7 Agent");
    	assertTrue(game.isTargetSelectionMode());
    	target(game.getOpponent().getPlayerCard());
    	assertEquals(28, game.getOpponent().getHealth());
    }
    
    @Test
    public void aldorPeacekeeper() {
    	reachMana(5);
    	HStoneCard minion = superPlayCard("Bloodfen Raptor");
    	assertPTT(minion, 3, 2, 2);
    	nextTurn();
    	superPlayCard("Aldor Peacekeeper");
    	target(minion);
    	assertPTT(minion, 1, 2, 2);
    }
    
    @Test
    public void aldorPeacekeeperOnLightspawn() {
    	reachMana(5);
    	HStoneCard lightspawn = superPlayCard("Lightspawn");
    	assertPTT(lightspawn, 5, 5, 5);
    	nextTurn();
    	superPlayCard("Aldor Peacekeeper");
    	target(lightspawn);
    	assertPTT(lightspawn, 5, 5, 5);
    }
    
    @Test
    public void mountainGiant() {
    	HStoneCard giant = superCreateCard("Mountain Giant");
    	assertZoneSize(4, game.getCurrentPlayer().getHand());
    	assertSame(game.getCurrentPlayer().getHand(), giant.getCurrentZone());
    	assertEquals(9, giant.getManaCost());
    }
    
    @Test
    public void paladinBlessingofWisdom() {
    	reachMana(5);
    	HStoneCard minion = playCharge();
    	superPlayCard("Blessing of Wisdom");
    	target(minion);
    	nextTurnX2();
    	game.getCurrentPlayer().getHand().moveToBottomOf(null);
    	assertZoneSize(0, game.getCurrentPlayer().getHand());
    	assertDamageToOpponent(1, minion);
    	assertZoneSize(1, game.getCurrentPlayer().getHand());
    	
    	nextTurnX2();
    	assertZoneSize(2, game.getCurrentPlayer().getHand());
    	minion.silence();
    	assertDamageToOpponent(1, minion);
    	assertZoneSize(2, game.getCurrentPlayer().getHand());
    }
    
    @Test
    public void seaGiant() {
    	HStoneCard giant = superCreateCard("Sea Giant");
    	assertEquals(10, giant.getManaCost());
    	superPlayCard("Wisp");
    	assertEquals(9, giant.getManaCost());
    	reachMana(9);
    	assertEquals(9, giant.getManaCost());
    	use(giant);
    	assertEquals(game.getCurrentPlayer().getBattlefield(), giant.getCurrentZone());
    	assertEquals(9, giant.getManaCost());
    }
    
    @Test
    public void moltenGiant() {
    	HStoneCard giant = superCreateCard("Molten Giant");
    	assertEquals(20, giant.getManaCost());
    	game.getCurrentPlayer().getResources().set(HStoneRes.HEALTH, 15);
    	assertEquals(5, giant.getManaCost());
    	game.getCurrentPlayer().getResources().set(HStoneRes.HEALTH, 10);
    	assertEquals(0, giant.getManaCost());
    	game.getCurrentPlayer().getResources().set(HStoneRes.HEALTH, 8);
    	assertEquals(0, giant.getManaCost());
    }
	
	@Test
	public void searchCards() {
		System.out.println("--- SEARCH CARDS: ---");
		List<HStoneCardModel> matches = cards.values().stream()
			.filter(card -> false)
			.collect(Collectors.toList());
		matches.forEach(System.out::println);
		System.out.println("--- SEARCH END ---");
		System.out.println("Total cards: " + cards.size());
	}
	// TODO: "Dependency injection" using strings. Input string, lookup in a table, then you know what to do -- add support for parsing {target} {effect} {onAction} etc.
	// When silencing minion, health becomes Math.min(currentHealth, maximumHealth)
	
	// x If you silence a minion that has a static enchant from another minion, the static enchantment remains!
	// Apply temporary stealth to minions that already have stealth: Stealth remains
	// x Raging Worgen (windfury when enraged), + give windfury, then heal it: Has still windfury
	// Stealth + force a minion to deal it's damage: Not allowed, can't target a stealth unit
	// x Does spell damage affect secrets? -- YES
	// x Does silence remove frozen? -- YES
	// x If Shaman have all totems, he is unable to use hero power
	// Ancient of Lore (Druid) -- Effect does NOT trigger if it is cheated into the battlefield
	// x Nourish adds permanent mana
	// x Stealth minions are like hexproof
	
	// TEST: Does playing Warlock:Sense Demons remove the demons from the deck?
	// WIKI: Wild Pyromancer - what comes first, spell effect or Wild Pyro effect?
	// TEST: Rogue - Headcrack when you have a full hand already (by using Sprint). Expected: Card disappears
	// TEST: copyOppCardInHand when opp card in hand has modified playing cost
	// TEST: Mind Vision - when are you NOT allowed to play that card?
	// TEST: Silence Kirin Tor Mage (Mage)
	// TODO: Tests for which cards can be used on "shroud" minions (all where source is minion AFAIK)
	// TEST: Hunter look at deck card, is it allowed to play at end of game when no cards is in deck?
	// TESTED: Druid - Starfall when there are no minions. Not possible to choose the other target
	// TESTED: Crazed Alchemist - swapPT when you have Raid Leader or similar in game -- works by setting the new values, which gives you additional PT

	
	// TODO: NEED 400 DUST: Copy card that has been enchanted with draw card enchantment?
	// TODO: Inner Fire (Priest) with +1/+1 to other minions, +1 attack to adjacent minions... -- perhaps add an HStoneEnchantment?
	// x Mirror Entity (Mage) when battlefield is full already -- Result: Does not trigger
	// TODO: Warsong Commander (Warrior) --   "Whenever you summon a minion with 3 or less Attack, give it <b>Charge</b>" -- will it trigger for a 3/3 when you have Raid Leader in game? Expected: No.
	// TODO: Force of Nature (Druid) - how many minions can I have on the battlefield in order to play it?
	
	// TODO: Blade Flurry + Spell damage?
	
	// TODO: Test cards Hunter: Secretkeeper, Flare
	// TODO: Test cards Mage: Counterspell, Ethereal Arcanist, Kirin Tor Mage, Vaporize, Ice Block, Spellbender
	// TODO: Test cards Paladin: Eye for an Eye, Noble Sacrifice, Redemption, Repentance
	
	// A whole lot of stuff with Secrets: http://hearthstone.gamepedia.com/Secret
	// TODO: Hunter weapon - what comes first, secret effect or reveal secret trigger?
	// TODO: Secrets triggered by a minion being summoned will take place after any Battlecry. 
	// TODO: You can have up to five secrets at any one time, but you can't have more than one copy of the same secret active at any one time. 
	// TODO: If two secrets are triggered by the same effect, the order in which they will be activated is the same as the order in which they were played. 
	// TODO: Note that only one secret can trigger at a time, and each triggered secret will take effect before another secret can trigger.
	// TODO: If a secret removes the target for another secret which would have triggered, the second secret will not trigger, since it now lacks a target. For example, if Freezing Trap removes the minion which would have been the target of Misdirection, the Misdirection will not trigger, since it has no longer has a target. 
	// TODO: changing the state of events or removing the trigger itself without removing the target does not appear to be sufficient to prevent secrets from triggering. For example, a Misdirection may cause a minion which was attacking the hero to attack another target instead, however since the minion was originally attacking the hero, it will still trigger Freezing Trap after triggering Misdirection. This can make the order in which secrets are played extremely significant. 
	
	// TODO: Expert AI problems with Priest deck: Mage (2 loss). Shaman (1 loss). Priest (2 loss). Rogue (1 loss).
/*
SPELL [name=Shadow Word: Pain, descs=[Destroy a minion with 3 or less Attack], cost=2, rarity=FREE, clazz=PRIEST] --> Destroy {A MINION} with {3 or less Attack}
* ---> game.addCard(spell( 2,      FREE, "Shadow Word: Pain").effect(destroyMinion(withAttackLess(3))).card());
* Destroy a minion with 3 or less Attack --> destroyMinion(withAttackLess(3))
*/

	// 1/3, give +2 health and draw a card: Is now a 1/5
	// 1/3 has taken one damage, give +2 health and draw a card: Is now a 1/4, but 1/5 is max
	// Priest monster thingy. 5/5, has taken two damage -> 3/3. Double it's health: 6/6. Max is 5 + 3 = 8
	
	
	private final Predicate<HStoneCardModel> isMinion = HStoneCardModel::isMinion;
	
	private Map<String, HStoneCardModel> cards;
	
	/*
Attack - All strategies
Max Health - All strategies
Mana Cost - All strategies
Actual Health - Resource

HSAbilities - Set<HSAbility>, add and remove as needed. Even for stealth until next turn - add trigger to remove it
	
	Enchantment / Rule
	Applies to: Predicate<HStoneCard>
	Affects: Resource(s)
	 
	BaseRule:
	  Applies to all
	  Affects Health and Attack

	AddAttackOrHealth:
	  Applies to one specific, or some selected
	  Affects Health/Attack

	DoubleAttackOrHealth
	SetToZero
	SetToSameAs

!!!	global for entire game!!!!
	to determine health/attack, loop through the rules and apply the values


	Listeners/Triggers...

	 * give minion something (+x/+y, Taunt)
	 * Choose one
	 * Deathrattle
	 * Battlecry
	 * Choose where to place the minion
	 */

	// TESTED : Play minion 1/3, play Stormwind Champion, use Inner Fire on minion, remove Stormwind Champion. Theory: 	1/3 --> 2/4 --> 4/4 or 5/4 --> 3/3 or 4/4. It becomes 5/4 !!!
	// TESTED: Change a minion's attack to 1 and +2 to adjacent minions = Minion's attack is 3. i.e. change a minion's attack to 1 only changes the resmap!
	
	@Test
	public void enchants() {
		System.out.println("Testing enchants");
		reachMana(10);
		HStoneCard minion = superPlayCard("Northshire Cleric");
		assertPTT(minion, 1, 3, 3);
		HStoneCard stormwind = superPlayCard("Stormwind Champion");
		// minion.getHealth checks resource map directly... health needs to be increased when using Stormwind Champion
//		assertEquals(4, minion.getGame().getResources(minion, HStoneRes.HEALTH));
		assertPTT(minion, 2, 4, 4);
		nextTurnX2();
		superPlayCard("Inner Fire");
		target(minion);
		assertPTT(minion, 5, 4, 4);

		superPlayCard("Shadow Word: Death");
		target(stormwind);
		
		assertPTT(minion, 4, 3, 3);
		
	}
	
	
	private void assertPTT(HStoneCard minion, int attack, int health, int maxHealth) {
		assertEquals("Unexpected Attack", attack, minion.getAttack());
		assertEquals("Unexpected Health", health, minion.getHealth());
		assertEquals("Unexpected MaxHealth", health, minion.getHealthMax());
	}

	@Test
	public void thoughtsteal() {
		reachMana(3);
		int hand = game.getCurrentPlayer().getHand().size();
		superPlayCard("Thoughtsteal");
		assertZoneSize(hand + 2 - 1, game.getCurrentPlayer().getHand()); // You use a card to play it
		
		nextTurnX2();
		game.getCurrentPlayer().getHand().moveToBottomOf(null);
		
		hand = game.getCurrentPlayer().getHand().size();
		game.getOpponent().getDeck().moveToBottomOf(null);
		superPlayCard("Thoughtsteal");
		assertZoneSize(0, game.getOpponent().getDeck());
		assertZoneSize(hand, game.getCurrentPlayer().getHand());

	}
	
	@Test
	public void playBattlecryWithoutTarget() {
		reachMana(4);
		superPlayCard("Spellbreaker");
		assertFalse(game.isTargetSelectionMode());
	}
	
	@Test
	public void testAllCardsWithoutTargets() {
		reachMana(10);
		for (Entry<String, HStoneCardModel> model : this.cards.entrySet()) {
			assertFalse(game.isGameOver());
			assertEquals("Stack not empty", 0, game.stackSize());
			HStoneCard card = superCreateCard(model.getKey());
			
			assertEquals("Stack not empty", 0, game.stackSize());
			assertTrue(game.getTemporaryZone().isEmpty());
			assertFalse("Cannot play card while in target selection mode", game.isTargetSelectionMode());
			assertTrue("Card was not created to hand: " + card, card.getCurrentZone() == game.getCurrentPlayer().getHand());
			
			System.out.println("Trying to play card: " + card);
			if (!game.click(card)) {
				System.out.println("!!!!!!!!!!! Unable to play card: " + card);
				continue;
			}
//			System.out.println("Card played: " + card);
			assertEquals(0, game.stackSize());
			assertFalse("Card was not removed from hand: " + card, card.getCurrentZone() == game.getCurrentPlayer().getHand());
			
			if (!game.getTemporaryZone().isEmpty()) {
				List<Card<HStoneOption>> list = game.getTemporaryZone().cardList().stream().filter(option -> option.getModel().isAllowed(game.getTemporaryFor())).collect(Collectors.toList());
				Card<HStoneOption> random = ZomisList.getRandom(list, game.getRandom());
				System.out.println("Chosen: " + random);
				assertTrue("Option not allowed: " + random, game.click(random));
			}
			
			if (game.isTargetSelectionMode()) {
				List<HStoneCard> targets = game.findAll(game.getTargetsFor(), game.getTargetsForEffect());
				assertFalse("No targets for " + model.getKey(), targets.isEmpty());
				
				HStoneCard target = ZomisList.getRandom(targets, game.getRandom());
				target(target);
			}
			assertFalse(game.isGameOver());
			nextTurn();
			System.out.println("empty draws " + game.getCurrentPlayer().getResources().get(HStoneRes.EMPTY_DRAWS));
			surviveCheat();
			
			assertEquals("Stack not empty", 0, game.stackSize());
		}
	}
	
	@Test
	public void mageCheapSpells() {
		reachMana(2);
		HStoneCard fireball = superCreateCard("Fireball");
		assertEquals(4, fireball.getManaCost());
		
		superPlayCard("Sorcerer's Apprentice");
		nextTurnX2();
		assertEquals(3, fireball.getManaCost());
		assertResources(game.getCurrentPlayer().getResources(), HStoneRes.MANA_AVAILABLE, 3);
		use(fireball);
		target(game.getOpponent().getPlayerCard());
		assertEquals(24, game.getOpponent().getHealth());
		assertResources(game.getCurrentPlayer().getResources(), HStoneRes.MANA_AVAILABLE, 0);
	}
	
	private void surviveCheat() {
		assertFalse(game.isGameOver());
		new ArrayList<>(game.getCurrentPlayer().getBattlefield().cardList()).forEach(HStoneCard::destroy);
		new ArrayList<>(game.getOpponent().getBattlefield().cardList()).forEach(HStoneCard::destroy);
		game.cleanup();
		for (HStonePlayer pl : game.getPlayers()) {
			pl.getResources().set(HStoneRes.EMPTY_DRAWS, 0);
			pl.getResources().set(HStoneRes.HEALTH, 30);
		}
		assertFalse(game.isGameOver());
	}

	@Test
	public void mindControlSleep() {
		reachMana(10);
		HStoneCard minion = superPlayCard("Young Dragonhawk");
		nextTurn();
		superPlayCard("Mind Control");
		target(minion);
		assertEquals(game.getCurrentPlayer().getBattlefield(), minion.getCurrentZone());
		useFail(minion);
		
		nextTurn();
		
		HStoneCard withCharge = playCharge();
		nextTurn();
		superPlayCard("Mind Control");
		target(withCharge);
		assertEquals(game.getCurrentPlayer().getBattlefield(), withCharge.getCurrentZone());
		assertDamageToOpponent(1, withCharge);
		
		
	}
	
	@Test
	public void lightspawn() {
		game = testGame(HStoneClass.MAGE);
		reachMana(4);
		HStoneCard lightspawn = superPlayCard("Lightspawn");
		nextTurnX2();
		assertDamageToOpponent(5, lightspawn);
		nextTurn();
		useAndTarget(heroPower(), lightspawn);
		nextTurn();
		assertDamageToOpponent(4, lightspawn);
		lightspawn.silence();
		nextTurnX2();
		assertEquals(0, lightspawn.getAttack());
		assertEquals(4, lightspawn.getHealth());
		assertEquals(5, lightspawn.getHealthMax());
		useFail(lightspawn);
	}
	
	@Test
	public void warriorCleave() {
		reachMana(5);
		HStoneCard cleave = superCreateCard("Cleave");
		useFail(cleave);
		nextTurn();
		
		HStoneCard charge1 = playCharge();
		nextTurn();
				
		useFail(cleave);
		nextTurn();
		
		HStoneCard charge2 = playCharge();
		nextTurn();
		
		use(cleave);
		assertFalse(game.isTargetSelectionMode());
		assertFalse(charge1.getResources().toString(), charge1.isAlive());
		assertFalse(charge2.isAlive());
	}
	
	@Test
	public void cannotUseOpponentPower() {
		reachMana(5);
		useFail(game.getCurrentPlayer().getNextPlayer().getHeroPower());
	}
	
	@Test
	public void stealthIsRemovedWhenAttacking() {
		game = testGame(HStoneClass.MAGE);
		reachMana(7);
		HStoneCard worgen = superPlayCard("Worgen Infiltrator");
		assertTrue(worgen.hasAbility(HSAbility.STEALTH));
		nextTurnX2();
		assertDamageToOpponent(2, worgen);
		assertFalse(worgen.hasAbility(HSAbility.STEALTH));
	}
	
	@Test
	public void targetingStealth() {
		game = testGame(HStoneClass.MAGE);
		reachMana(7);
		HStoneCard worgen = superPlayCard("Worgen Infiltrator");
		use(game.getCurrentPlayer().getHeroPower());
		target(worgen);
		assertFalse(worgen.isAlive());
		
		worgen = superPlayCard("Worgen Infiltrator");
		nextTurn();
		use(game.getCurrentPlayer().getHeroPower());
		targetFail(worgen);
		
	}
	
	@Test
	public void roguePower() {
		game = testGame(HStoneClass.ROGUE);
		HStoneCard hero = game.getCurrentPlayer().getPlayerCard();
		reachMana(2);
		useFail(hero);
		assertNull(hero.getPlayer().getWeapon());
		use(hero.getPlayer().getHeroPower());
		assertNotNull(hero.getPlayer().getWeapon());
		assertEquals(2, hero.getPlayer().getWeapon().getHealth());
		assertDamageToOpponent(1, hero);
		nextTurnX2();
		assertEquals(1, hero.getPlayer().getWeapon().getHealth());
		assertDamageToOpponent(1, hero);
		nextTurnX2();
		assertNull(hero.getPlayer().getWeapon());
		useFail(hero);
	}
	
	@Test
	public void druidPower() {
		game = testGame(HStoneClass.DRUID);
		reachMana(2);
		assertResources(game.getCurrentPlayer().getResources(), HStoneRes.ARMOR, 0);
		assertEquals(0, game.getCurrentPlayer().getPlayerCard().getAttack());
		use(game.getCurrentPlayer().getHeroPower());
		assertResources(game.getCurrentPlayer().getResources(), HStoneRes.ARMOR, 1);
		assertEquals(1, game.getCurrentPlayer().getPlayerCard().getAttack());
		assertDamageToOpponent(1, game.getCurrentPlayer().getPlayerCard());
	}
	
	@Test
	public void druidClaw() {
		game = testGame(HStoneClass.DRUID);
		reachMana(5);
		superPlayCard("Claw");
		assertFalse(game.isTargetSelectionMode());
		assertResources(game.getCurrentPlayer().getResources(), HStoneRes.ARMOR, 2);
		assertEquals(2, game.getCurrentPlayer().getPlayerCard().getAttack());
		assertDamageToOpponent(2, game.getCurrentPlayer().getPlayerCard());

		nextTurn();
		
		assertResources(game.getCurrentPlayer().getNextPlayer().getResources(), HStoneRes.ARMOR, 2);
		assertEquals(0, game.getCurrentPlayer().getNextPlayer().getPlayerCard().getAttack());
		nextTurn();
		useFail(game.getCurrentPlayer().getPlayerCard());
	}
	
	@Test
	public void rogueDefiasRingleader() {
		nextTurn();
		useTheCoin();
		superPlayCard("Defias Ringleader");
		assertZoneSize(2, game.getCurrentPlayer().getBattlefield());
		nextTurn();
		superPlayCard("Defias Ringleader");
		assertZoneSize(1, game.getCurrentPlayer().getBattlefield());
	}
	
	@Test
	public void divineShieldFight() {
		reachMana(4);
		HStoneCard squire = superPlayCard("Argent Squire");
		assertTrue(squire.hasAbility(HSAbility.DIVINE_SHIELD));
		nextTurn();
		
		HStoneCard charge = playCharge();
		useAndTarget(charge, squire);
		assertFalse(squire.hasAbility(HSAbility.DIVINE_SHIELD));
		assertFalse(charge.isAlive());
		assertTrue(squire.isAlive());
	}
	
	@Test
	public void bloodKnight() { // TODO: Duplicate of another method? "forEachDivineShield"
		reachMana(8);
		HStoneCard squire = superPlayCard("Argent Squire");
		assertTrue(squire.hasAbility(HSAbility.DIVINE_SHIELD));
		HStoneCard bloodKnight = superPlayCard("Blood Knight");
		assertFalse(squire.hasAbility(HSAbility.DIVINE_SHIELD));
		assertEquals(6, bloodKnight.getAttack());
		assertEquals(6, bloodKnight.getHealthMax());
		assertEquals(6, bloodKnight.getHealth());
	}
	
	@Test
	public void cannotUseHeroPowerTwice() {
		game = testGame(HStoneClass.PALADIN);
		reachMana(5);
		use(game.getCurrentPlayer().getHeroPower());
		assertFalse(game.isTargetSelectionMode());
		useFail(game.getCurrentPlayer().getHeroPower());
	}
	
	@Test
	public void spellDamage() {
		reachMana(10);
		HStoneCard spellDamage = superPlayCard("Ogre Magi");
		assertResources(spellDamage.getResources(), HStoneRes.SPELL_DAMAGE, 1);
		superPlayCard("Fireball");
		target(game.getCurrentPlayer().getNextPlayer().getPlayerCard());
		assertEquals(23, game.getOpponent().getHealth());
	}
	
	@Test
	public void heroPowerCost() {
		game = testGame(HStoneClass.MAGE);
		reachMana(2);
		assertDamageToOpponent(1, game.getCurrentPlayer().getHeroPower());
		useFail(game.getCurrentPlayer().getHeroPower());
	}
	
	@Test
	public void silenceRemovesFreeze() {
		reachMana(10);
		HStoneCard golem = superPlayCard("War Golem");
		nextTurn();
		superPlayCard("Frost Nova");
		nextTurn();
		assertTrue(golem.hasAbility(HSAbility.FROZEN));
		superPlayCard("Ironbeak Owl");
		target(golem);
		assertFalse(golem.hasAbility(HSAbility.FROZEN));
		
		assertDamageToOpponent(7, golem);
	}
	
	@Test
	public void shamanPower() {
		game = testGame(HStoneClass.SHAMAN);
		playCharge();
		reachMana(2);
		use(game.getCurrentPlayer().getHeroPower());
		nextTurnX2();
		use(game.getCurrentPlayer().getHeroPower());
		nextTurnX2();
		use(game.getCurrentPlayer().getHeroPower());
		nextTurnX2();
		use(game.getCurrentPlayer().getHeroPower());
		
		nextTurnX2();
		assertZoneSize(5, game.getCurrentPlayer().getBattlefield());
		useFail(game.getCurrentPlayer().getHeroPower());
		assertZoneSize(5, game.getCurrentPlayer().getBattlefield());

		
	}
	
	@Test
	public void windfuryRemains() {
		game = testGame(HStoneClass.PRIEST);
		reachMana(10);
		HStoneCard enrager = superPlayCard("Raging Worgen");
		nextTurn();
		HStoneCard charge = playCharge();
		useAndTarget(charge, enrager);
		nextTurn();
		assertTrue(new HSFilters().isDamaged().shouldKeep(enrager, enrager));
		assertTrue("Does not have windfury when enraged", enrager.hasAbility(HSAbility.WINDFURY));
		assertDamageToOpponent(4, enrager);
		assertDamageToOpponent(4, enrager);
		nextTurnX2();
		superPlayCard("Windspeaker"); // give minion windfury
		target(enrager);
		assertTrue("Does not have windfury when being given it", enrager.hasAbility(HSAbility.WINDFURY));
		useAndTarget(game.getCurrentPlayer().getHeroPower(), enrager);
		assertTrue(enrager.hasAbility(HSAbility.WINDFURY));
		assertTrue("Does not have windfury after being healed", enrager.hasAbility(HSAbility.WINDFURY));
		assertDamageToOpponent(3, enrager);
		assertDamageToOpponent(3, enrager);
		
	}
	
	private void useAndTarget(HStoneCard use, HStoneCard target) {
		use(use);
		target(target);
	}

	@Test
	public void silenceStaticEnchant() {
		reachMana(10);
		HStoneCard wolf = superPlayCard("Dire Wolf Alpha");
		HStoneCard windfury = superPlayCard("Young Dragonhawk");
		nextTurnX2();
		assertDamageToOpponent(2, windfury); // +1 attack from Dire Wolf Alpha
		superPlayCard("Ironbeak Owl");
		target(windfury);
		nextTurnX2();
		assertDamageToOpponent(2, windfury);
		nextTurnX2();
		
		superPlayCard("Ironbeak Owl");
		target(wolf);
		assertDamageToOpponent(1, windfury);
	}
	
	private void nextTurnX2() {
		nextTurn();
		nextTurn();
	}

	@Test
	public void cannotHaveMoreThan7minionsFromSpell() {
		reachMana(10);
		superPlayCard("Wisp");
		superPlayCard("Wisp");
		superPlayCard("Argent Squire");
		superPlayCard("Argent Squire");
		superPlayCard("Stonetusk Boar");
		superPlayCard("Stonetusk Boar");
		superPlayCard("Goldshire Footman");
		assertZoneSize(7, game.getCurrentPlayer().getBattlefield());
		HStoneCard spellSummon = superCreateCard("Mirror Image");
		useFail(spellSummon);
		assertZoneSize(7, game.getCurrentPlayer().getBattlefield());
	}
	
	@Test
	public void cannotHaveMoreThan7minionsFromMinionCard() {
		reachMana(10);
		superPlayCard("Wisp");
		superPlayCard("Wisp");
		superPlayCard("Argent Squire");
		superPlayCard("Argent Squire");
		superPlayCard("Stonetusk Boar");
		superPlayCard("Stonetusk Boar");
		superPlayCard("Goldshire Footman");
		assertZoneSize(7, game.getCurrentPlayer().getBattlefield());
		HStoneCard card = superCreateCard("Goldshire Footman");
		useFail(card);
		assertZoneSize(7, game.getCurrentPlayer().getBattlefield());
	}
	
	@Test
	public void cannotUseSpellWithoutTarget() {
		reachMana(3);
		playCardFail("Power Word: Shield");
	}
	
	private void playCardFail(String name) {
		game.getCurrentPlayer().getHand().getBottomCard().zoneMoveOnBottom(null);
		HStoneCardModel cardModel = game.getCardModel(name);
		assertNotNull("Card Model not found: " + name, cardModel);
		HStoneCard card = game.getCurrentPlayer().getHand().createCardOnTop(cardModel);
		assertEquals(game.getCurrentPlayer().getHand(), card.getCurrentZone());
		assertNotNull(card.getPlayer());
		
		assertEquals("Stack not empty", 0, game.stackSize());
		assertFalse("Cannot play card while in target selection mode", game.isTargetSelectionMode());
		assertFalse("Expected card to fail: " + card, game.click(card));
		assertEquals(0, game.stackSize());
	}

	@Test
	public void noTargetAvailableForHungryCrab() {
		reachMana(10);
		superPlayCard("Hungry Crab");
		assertFalse(game.isTargetSelectionMode());
		
		HStoneCard murloc = superPlayCard("Grimscale Oracle"); // is a Murloc
		HStoneCard crab = superPlayCard("Hungry Crab");
		assertTrue(game.isTargetSelectionMode());
		target(murloc);
		nextTurn();
		nextTurn();
		assertDamageToOpponent(3, crab);
	}
	
	@Test
	public void acolyteOfPain() {
		reachMana(3);
		HStoneCard acolyte = superPlayCard("Acolyte of Pain");
		nextTurn();
		HStoneCard target = superPlayCard("Wisp");
		nextTurn();
		int cards = game.getCurrentPlayer().getHand().size();
		use(acolyte);
		target(target);
		assertZoneSize(cards + 1, game.getCurrentPlayer().getHand());
	}
	
	@Test
	public void warriorWeapon() {
		game = this.testGame(HStoneClass.WARRIOR);
		reachMana(4);
		HStoneCard weapon = superPlayCard("Fiery War Axe");
		assertSame(weapon, game.getCurrentPlayer().getWeapon());
		assertTrue(weapon.getModel().isWeapon());
		assertFalse(playCharge().getModel().isWeapon());
		assertTrue(weapon.getModel().isType(CardType.WEAPON));
		assertDamageToOpponent(3, game.getCurrentPlayer().getPlayerCard());
	}

	@Test
	public void cantFightWithOpponent() {
		reachMana(6);
		playWeapon();
		nextTurn();
		playWeapon();
		useFail(game.getOpponent().getPlayerCard());
	}
	
	@Test
	public void temporaryState() {
		reachMana(10);
		HStoneCard charge = playCharge();
		superPlayCard("Starfall");
		assertFalse(game.getTemporaryZone().isEmpty());
		HStoneCard card = superCreateCard("Voodoo Doctor");
		useFail(card);
		useFail(charge);
	}
	
	@Test
	public void testSouthseaDeckhand() {
		reachMana(10);
		HStoneCard minion = superPlayCard("Southsea Deckhand");
		assertFalse(minion.hasAbility(HSAbility.CHARGE));
		playWeapon();
		assertTrue(minion.hasAbility(HSAbility.CHARGE));
	}
	
	private void playWeapon() {
		superPlayCard("Fiery War Axe");
	}

	@Test
	public void beastTest() {
		reachMana(7);
		superPlayCard("Kill Command");
		target(game.getOpponent().getPlayerCard());
		assertEquals(27, game.getOpponent().getHealth());
		HStoneCard snake = superPlayCard("Snake");
		assertTrue(snake.getModel().isOfType(HStoneMinionType.BEAST));
		superPlayCard("Kill Command");
		target(game.getOpponent().getPlayerCard());
		assertEquals(22, game.getOpponent().getHealth());
	}
	
	private void assertDamageToOpponent(int damage, HStoneCard source) {
		use(source);
		HStonePlayer target = game.getCurrentPlayer().getNextPlayer();
		int previous = target.getHealth();
		target(target.getPlayerCard());
		assertEquals(previous - damage, target.getHealth());
	}

	@Test
	public void grimscaleOracleMurlocBonus() {
		HStoneCard firstOracle = superPlayCard("Grimscale Oracle"); // ALL other murlocs have +1 attack
		nextTurn();
		HStoneCard wisp = superPlayCard("Wisp");
		nextTurn();
		HStoneCard secondOracle = superPlayCard("Grimscale Oracle");
		
		System.out.println(game.getEnchantments());
		
		assertDamageToOpponent(2, firstOracle);
		nextTurn();
		use(wisp);
		target(secondOracle);
		assertZoneSize(0, game.getCurrentPlayer().getBattlefield());
		nextTurn();
		assertDamageToOpponent(1, firstOracle);
	}
	
	@Test
	public void onPlayCard() {
		reachMana(3);
		HStoneCard adv = superPlayCard("Questing Adventurer");
		assertEquals(2, adv.getAttack());
		nextTurn();
		nextTurn();
		superPlayCard("Wisp");
		superPlayCard("Wisp");
		assertDamageToOpponent(4, adv);
	}
	
	@Test
	public void adjacents() {
		reachMana(5);
		HStoneCard first = superPlayCard("Wisp");
		HStoneCard second = superPlayCard("Grimscale Oracle");
		HStoneCard third = superPlayCard("Acolyte of Pain");
		assertSame(null, first.getLeftAdjacent());
		assertSame(second, first.getRightAdjacent());
		
		assertSame(first, second.getLeftAdjacent());
		assertSame(third, second.getRightAdjacent());
		
		assertSame(second, third.getLeftAdjacent());
		assertSame(null, third.getRightAdjacent());
	}
	
	@Test
	public void silenceAndGiveTauntToAdjacent() {
		HStoneCard wisp = superPlayCard("Wisp");
		reachMana(4);
		assertSame(wisp, game.getCurrentPlayer().getBattlefield().getTopCard());
		superPlayCard("Defender of Argus");
		assertEquals(2, wisp.getAttack());
		assertTrue(wisp.hasAbility(HSAbility.TAUNT));
		nextTurn();
		superPlayCard("Ironbeak Owl");
		target(wisp);
		assertFalse(wisp.hasAbility(HSAbility.TAUNT));
		assertFalse(wisp.getPlayer().hasTauntMinions());
		assertEquals("Wisp attack was not restored when silenced.", 1, wisp.getAttack());
	}
	
	@Test
	public void Lay_on_Hands_heal8_draw3() {
		reachMana(8);
		superPlayCard("Fireball");
		target(game.getCurrentPlayer().getPlayerCard());
		nextTurnX2();
		superPlayCard("Fireball");
		target(game.getCurrentPlayer().getPlayerCard());
		nextTurnX2();
		assertEquals(30 - 12, game.getCurrentPlayer().getHealth());
		
		game.getCurrentPlayer().getHand().moveToBottomOf(null); // clear hand
		game.getCurrentPlayer().drawCard(); // need one card in hand
		game.cleanup();
		
		superPlayCard("Lay on Hands");
		target(game.getCurrentPlayer().getPlayerCard());
		assertEquals(30 - 12 + 8, game.getCurrentPlayer().getHealth());
		assertEquals(3, game.getCurrentPlayer().getHand().size());
	}
	
	@Test
	public void secretMirrorEntity() {
		game = testGame(HStoneClass.MAGE);
		reachMana(6);
		assertZoneSize(0, game.getCurrentPlayer().getSecrets());
		superPlayCard("Mirror Entity");
		assertZoneSize(1, game.getCurrentPlayer().getSecrets());
		nextTurn();
		playCharge();
		assertZoneSize(1, game.getCurrentPlayer().getBattlefield());
		nextTurn();
		assertZoneSize(1, game.getCurrentPlayer().getBattlefield());
		assertZoneSize(0, game.getCurrentPlayer().getSecrets());
		
		HStoneCard charge = null;
		while (game.getCurrentPlayer().getBattlefield().size() < HStonePlayer.MAX_BATTLEFIELD_SIZE) {
			charge = playCharge();
			nextTurnX2();
		}
		assertZoneSize(HStonePlayer.MAX_BATTLEFIELD_SIZE, game.getCurrentPlayer().getBattlefield());
		superPlayCard("Mirror Entity");
		assertNotNull(charge);
		
		// Make sure that Mirror Entity does not trigger when board is full
		nextTurn();
		assertZoneSize(1, game.getOpponent().getSecrets());
		assertZoneSize(HStonePlayer.MAX_BATTLEFIELD_SIZE, game.getOpponent().getBattlefield());
		playCharge();
		assertZoneSize(1, game.getOpponent().getSecrets());
		assertZoneSize(HStonePlayer.MAX_BATTLEFIELD_SIZE, game.getOpponent().getBattlefield());
	}
	
	@Test
	public void secretSnakeTrap() {
		game = testGame(HStoneClass.HUNTER);
		reachMana(6);
		HStoneCard myMinion = superPlayCard("Sen'jin Shieldmasta");
		assertZoneSize(0, game.getCurrentPlayer().getSecrets());
		superPlayCard("Snake Trap");
		assertZoneSize(1, game.getCurrentPlayer().getSecrets());
		nextTurn();
		useAndTarget(playCharge(), myMinion);
		nextTurn();
		assertZoneSize(0, game.getCurrentPlayer().getSecrets());
		assertZoneSize(4, game.getCurrentPlayer().getBattlefield());
	}
	
	@Test
	public void druidNourish() {
		reachMana(6);
		superPlayCard("Nourish");
		assertResources(game.getCurrentPlayer().getResources(), HStoneRes.MANA_TOTAL, 6);
		assertResources(game.getCurrentPlayer().getResources(), HStoneRes.MANA_AVAILABLE, 0);
		pickOption(DruidCards.EFFECT_INCREASE_MANA_TOTAL);
		assertResources(game.getCurrentPlayer().getResources(), HStoneRes.MANA_TOTAL, 8);
		assertResources(game.getCurrentPlayer().getResources(), HStoneRes.MANA_AVAILABLE, 2);
		nextTurn();
		
	}
	
	private void pickOption(HStoneOption option) {
		assertFalse(game.getTemporaryZone().isEmpty());
//		assertEquals(option.getCurrentZone(), game.getTemporaryZone());
		assertTrue(game.choose(option));
	}

	@Test
	public void druidForceOfNature() {
		reachMana(6);
		superPlayCard("Force of Nature");
		assertZoneSize(3, game.getCurrentPlayer().getBattlefield());
		nextTurn();
		assertZoneSize(0, game.getOpponent().getBattlefield());
	}
	
	@Test
	public void secretMisdirection() {
		game = testGame(HStoneClass.HUNTER);
		reachMana(2);
		assertZoneSize(0, game.getCurrentPlayer().getSecrets());
		superPlayCard("Misdirection");
		assertZoneSize(1, game.getCurrentPlayer().getSecrets());
		nextTurn();
		assertEquals(30, game.getCurrentPlayer().getHealth());
		HStoneCard charge = playCharge();
		useAndTarget(charge, game.getOpponent().getPlayerCard());
		assertZoneSize(0, game.getOpponent().getSecrets());
		assertTrue(charge.isAlive());
		assertEquals(29, game.getCurrentPlayer().getHealth());
		assertEquals(30, game.getOpponent().getHealth());
	}
	
	@Test
	public void secretFlare() {
		game = testGame(HStoneClass.HUNTER);
		reachMana(4);
		superPlayCard("Mirror Entity");
		assertZoneSize(1, game.getCurrentPlayer().getSecrets());
		nextTurn();
		superPlayCard("Flare");
		nextTurn();
		assertZoneSize(0, game.getCurrentPlayer().getSecrets());
	}
	
	@Test
	public void atteckedByWaterElemental() {
		reachMana(9);
		HStoneCard golem = superPlayCard("War Golem");
		nextTurn();
		HStoneCard water = superPlayCard("Water Elemental");
		nextTurnX2();
		assertFalse(golem.hasAbility(HSAbility.FROZEN));
		
		useAndTarget(water, golem);
		assertTrue("Golem was not frozen after attack", golem.hasAbility(HSAbility.FROZEN));
		nextTurn();
		assertTrue("Golem lost frozen at the end of my turn, directly after attack", golem.hasAbility(HSAbility.FROZEN));
		nextTurn();
		assertFalse("Golem is frozen way too long", golem.hasAbility(HSAbility.FROZEN));
		nextTurn();
		assertDamageToOpponent(7, golem);
	}
	
	@Test
	public void attackWaterElemental() {
		reachMana(9);
		HStoneCard golem = superPlayCard("War Golem");
		nextTurn();
		HStoneCard water = superPlayCard("Water Elemental");
		nextTurn();
		assertFalse(golem.hasAbility(HSAbility.FROZEN));
		
		useAndTarget(golem, water);
		assertTrue("Golem was not frozen after attack", golem.hasAbility(HSAbility.FROZEN));
		nextTurn();
		assertTrue("Golem lost frozen at the end of my turn, directly after attack", golem.hasAbility(HSAbility.FROZEN));
		
		nextTurn();
		assertTrue("Golem lost frozen at the end of opponents turn", golem.hasAbility(HSAbility.FROZEN));
		nextTurn();
		assertFalse("Golem is frozen way too long", golem.hasAbility(HSAbility.FROZEN));
		nextTurn();
		
		assertDamageToOpponent(7, golem);
	}
	
	@Test
	public void enrage() {
		reachMana(3);
		HStoneCard enrage = superPlayCard("Raging Worgen");
		nextTurn();
		HStoneCard charge = playCharge();
		use(charge);
		target(enrage);
		nextTurn();
		assertTrue("Does not have windfury when enraged", enrage.hasAbility(HSAbility.WINDFURY));
		assertEquals(4, enrage.getAttack());
		assertDamageToOpponent(4, enrage);
		assertDamageToOpponent(4, enrage);
	}
	
	private HStoneCard playCharge() {
		return superPlayCard(cards.values().stream()
				.filter(mc -> mc.getManaCost() <= 1)
				.filter(hasAbility(HSAbility.CHARGE))
				.filter(mc -> mc.getRarity() == HStoneRarity.FREE)
				.filter(mc -> mc.getAttack() == 1)
				.filter(isMinion)
				.findAny().get().getName());
	}

	@Test
	public void lightwarden() {
		game = testGame(HStoneClass.PRIEST);
		HStoneCard lightwarden = superPlayCard("Lightwarden");
		nextTurn();
		HStoneCard charge = playCharge();
		use(charge);
		target(game.getCurrentPlayer().getNextPlayer().getPlayerCard());
		assertTrue(game.getCurrentPlayer().getNextPlayer().getHealth() < 30);
		nextTurn();
		use(game.getCurrentPlayer().getHeroPower());
		target(game.getCurrentPlayer().getPlayerCard());
		assertEquals(30, game.getCurrentPlayer().getNextPlayer().getHealth());
		assertDamageToOpponent(3, lightwarden);
	}
	
	private HStoneCard superCreateCard(String name) {
		HStoneCard bottomCard = game.getCurrentPlayer().getHand().getBottomCard();
		if (bottomCard != null)
			bottomCard.zoneMoveOnBottom(null);
		return createCardWithoutDestroy(name);
	}
	
	private HStoneCard createCardWithoutDestroy(String name) {
		HStoneCardModel cardModel = game.getCardModel(name);
		assertNotNull("Card Model not found: " + name, cardModel);
		HStoneCard card = game.getCurrentPlayer().getHand().createCardOnTop(cardModel);
		assertEquals(game.getCurrentPlayer().getHand(), card.getCurrentZone());
		assertNotNull(card.getPlayer());
		return card;
	}

	private HStoneCard superPlayCard(String name) {
		superCreateCard(name);
		return playCard(name);
	}

	@Test
	public void cannotAttackHeroPower() {
		reachMana(5);
		HStoneCard charge = playCharge();
		use(charge);
		targetFail(game.getCurrentPlayer().getNextPlayer().getHeroPower());
	}
	
	@Test
	public void cannotAttackMyHeroPower() {
		reachMana(5);
		HStoneCard charge = playCharge();
		use(charge);
		targetFail(game.getCurrentPlayer().getHeroPower());
	}
	
	@Test
	public void deathrattle() {
		HStoneCard gnome = superPlayCard("Leper Gnome");
		nextTurn();
		HStoneCard wisp = superPlayCard("Wisp");
		nextTurn();
		use(gnome);
		target(wisp);
		assertFalse(gnome.isAlive());
		assertFalse(wisp.isAlive());
		assertZoneSize(0, game.getCurrentPlayer().getBattlefield());
		assertZoneSize(0, game.getCurrentPlayer().getNextPlayer().getBattlefield());
		assertEquals(28, game.getCurrentPlayer().getNextPlayer().getHealth());
	}
	
	@Test
	public void ancientWatcherNoAttack() {
		reachMana(5);
		HStoneCard watcher = superPlayCard("Ancient Watcher");
		nextTurnX2();
		assertTrue(watcher.hasAbility(HSAbility.NO_ATTACK));
		useFail(watcher);
	}
	
	@Test
	public void demolisher() {
		reachMana(3);
		superPlayCard("Demolisher");
		nextTurn();
		assertEquals(30, game.getCurrentPlayer().getHealth());
		nextTurn();
		assertEquals(28, game.getCurrentPlayer().getNextPlayer().getHealth());
		nextTurn();
		assertEquals(28, game.getCurrentPlayer().getHealth());
		nextTurn();
		assertEquals(26, game.getCurrentPlayer().getNextPlayer().getHealth());
		nextTurn();
		
		HStoneCard wisp = superPlayCard("Wisp");
		for (int i = 0; i < 50; i++) {
			nextTurn();
		}
		assertFalse(wisp.isAlive());
	}
	
	@Test
	public void testOverload() {
		reachMana(4);
		assertResources(game.getCurrentPlayer().getResources(), HStoneRes.MANA_TOTAL, 4);
		HStoneCard unboundElemental = superPlayCard("Unbound Elemental");
		assertEquals(2, unboundElemental.getAttack());
		assertEquals(4, unboundElemental.getHealth());
		
		nextTurnX2();
		
		assertResources(game.getCurrentPlayer().getResources(), HStoneRes.MANA_TOTAL, 5);
		superPlayCard("Earth Elemental");
		assertResources(game.getCurrentPlayer().getResources(), HStoneRes.MANA_OVERLOAD, 3);
		assertEquals(3, unboundElemental.getAttack());
		assertEquals(5, unboundElemental.getHealth());
		assertDamageToOpponent(3, unboundElemental);
		
		nextTurnX2();
		
		assertResources(game.getCurrentPlayer().getResources(), HStoneRes.MANA_OVERLOAD, 0);
		assertResources(game.getCurrentPlayer().getResources(), HStoneRes.MANA_AVAILABLE, 3);
		assertResources(game.getCurrentPlayer().getResources(), HStoneRes.MANA_TOTAL, 6);
	}
	
	@Test
	public void tempAttackBonus() {
		HStoneCard wisp = superPlayCard("Wisp");
		nextTurn();
		nextTurn();
		HStoneCard sergeant = superPlayCard("Abusive Sergeant"); // +2 attack to minion this turn
		assertTrue(wisp.isAlive());
		target(wisp);
		assertTrue("Wisp or sergeant died when performing battlecry enchant: " + wisp.getHealth() + ", " + sergeant.getHealth(), wisp.isAlive() && sergeant.isAlive());
		
		assertDamageToOpponent(3, wisp);
		nextTurn();
		nextTurn();
		assertDamageToOpponent(1, wisp);
	}
	
	@Test
	public void cannotUseOpponentCards() {
		HStoneCard card = superCreateCard("Murloc Raider");
		nextTurn();
		useFail(card);
	}
	
	
	@Test
	public void attack() {
		superPlayCard("Murloc Raider");
		HStoneCard card = (HStoneCard) game.getCurrentPlayer().getBattlefield().getTopCard();
		assertNotNull("Card did not enter the battlefield", card);
		nextTurn();
		nextTurn();

		HStoneCard card2 = (HStoneCard) game.getCurrentPlayer().getBattlefield().getTopCard();
		assertTrue(card == card2);
		assertNotNull("Card did not enter the battlefield", card);
		HStonePlayer target = game.getCurrentPlayer().getNextPlayer();
		assertEquals(30, target.getHealth());
		use(card);
		target(target.getPlayerCard());
		assertEquals(28, target.getHealth());
	}
	
	@Test
	public void battlecryDamage() {
		nextTurn();
		HStoneCard wisp = superPlayCard("Wisp");
		nextTurn();
		assertEquals(30, game.getCurrentPlayer().getNextPlayer().getHealth());
		HStoneCard archer = superPlayCard("Elven Archer");
		target(wisp);
		game.cleanup();
		assertTrue("Battlecry killed " + archer, archer.isAlive());
		assertFalse("Wisp is still alive", wisp.isAlive());
	}
	
	@Test
	public void battlecryDrawCard() {
		nextTurn();
		nextTurn();
		assertEquals(5, game.getCurrentPlayer().getHand().size());
		superPlayCard("Novice Engineer");
		assertEquals(5, game.getCurrentPlayer().getHand().size());
	}

	@Test
	public void battlecrySummon() {
		nextTurn();
		nextTurn();
		assertEquals(5, game.getCurrentPlayer().getHand().size());
		assertEquals(0, game.getCurrentPlayer().getBattlefield().size());
		superPlayCard("Murloc Tidehunter");
		assertEquals(4, game.getCurrentPlayer().getHand().size());
		assertEquals(2, game.getCurrentPlayer().getBattlefield().size());
		assertEquals("Murloc Tidehunter", game.getCurrentPlayer().getBattlefield().getTopCard().getModel().getName());
		assertEquals("Murloc Scout", game.getCurrentPlayer().getBattlefield().getBottomCard().getModel().getName());
		
	}

	@Test
	public void paladinCannotUseHeroPowerWhenBattlefieldIsFull() {
		game = testGame(HStoneClass.PALADIN);
		reachMana(2);
		for (int i = 0; i < 7; i++) {
			use(game.getCurrentPlayer().getHeroPower());
			nextTurnX2();
		}
		assertZoneSize(7, game.getCurrentPlayer().getBattlefield());
		useFail(game.getCurrentPlayer().getHeroPower());
	}
	
	@Test
	public void weaponIsNotCharacter() {
		reachMana(6);
		HStoneCard axe = superPlayCard("Fiery War Axe");
		HSFilters f = new HSFilters();
		assertTrue(f.isWeapon.shouldKeep(axe, axe));
		assertFalse(f.all().shouldKeep(axe, axe));
	}
	
	@Test
	public void twilightDrakeHealth() {
		HStoneCard drake = superCreateCard("Twilight Drake");
		reachMana(8);
		assertZoneSize(10, game.getCurrentPlayer().getHand());
		
		use(drake);
		assertZoneSize(9, game.getCurrentPlayer().getHand());
		assertEquals(10, drake.getHealth());
	}
	
	@Test
	public void cantAfford() {
		HStoneCard card = superCreateCard("Bloodfen Raptor");
		useFail(card);
		assertTrue(game.getCurrentPlayer().getBattlefield().isEmpty());
	}

	private HStoneCard cardToHand(final String cardName, final HStonePlayer player) {
		HStoneCardModel model = cards.get(cardName);
		assertNotNull("Card not found in game: " + cardName, model);
		Predicate<HStoneCard> isCard = c -> c.getModel().equals(model);
		CardZone<HStoneCard> search = player.getHand();
		assertFalse("No cards in hand", search.isEmpty());
		Optional<HStoneCard> card = search.cardList().stream().filter(isCard).findAny();
		if (!card.isPresent()) {
			search = game.getCurrentPlayer().getLibrary();
			assertFalse(search.isEmpty());
			card = search.cardList().stream().filter(isCard).findAny();
			player.getHand().getTopCard().zoneMoveOnBottom(search);
			assertTrue("Card exists in game but was not added to deck: " + cardName, card.isPresent());
			card.get().zoneMoveOnBottom(player.getHand());
		}
		assertTrue("Card not found: " + cardName, card.isPresent());
		
		return (HStoneCard) card.get();
	}
	
	@Test
	public void cardTypes() {
		HStoneCard minion = superPlayCard("Wisp");
		HStoneCard power = game.getCurrentPlayer().getHeroPower();
		HStoneCard player = game.getCurrentPlayer().getPlayerCard();
		assertTrue(player.getModel().isType(CardType.PLAYER));
		assertTrue(minion.getModel().isType(CardType.MINION));
		assertTrue(power.getModel().isType(CardType.POWER));
		nextTurn();
	}
	
	@Test
	public void chargeCanAttack() {
		superPlayCard("Stonetusk Boar");
		assertEquals(30, game.getCurrentPlayer().getNextPlayer().getHealth());
		use(game.getCurrentPlayer().getBattlefield().getTopCard());
		target(game.getCurrentPlayer().getNextPlayer().getPlayerCard());
		assertEquals(29, game.getCurrentPlayer().getNextPlayer().getHealth());
	}
	
	@Test
	public void cost() {
		assertEquals(1, game.getCurrentPlayer().getResources().getResources(HStoneRes.MANA_AVAILABLE));
		superPlayCard("Murloc Raider");
		assertEquals(0, game.getCurrentPlayer().getResources().getResources(HStoneRes.MANA_AVAILABLE));
		nextTurn();
		nextTurn();
		assertEquals(2, game.getCurrentPlayer().getResources().getResources(HStoneRes.MANA_AVAILABLE));
		assertEquals(2, game.getCurrentPlayer().getResources().getResources(HStoneRes.MANA_TOTAL));
		assertEquals(0, game.stackSize());
	}
	
	@Test
	public void drawCardsToDeath() {
		for (int i = 0; i < 1000; i++) {
			assertTrue(game.nextPhase());
			if (game.isGameOver()) {
				break;
			}
			game.getCurrentPlayer().getHand().moveToTopOf(null);
			assertNotNull("Current player was suddenly null at turn " + i, game.getCurrentPlayer());
		}
		
		game.getPlayers();
		Map<String, Integer> healths = game.getPlayers().stream()
				.map(pl -> (HStonePlayer) pl)
				.collect(Collectors.toMap(pl -> pl.getName(), pl -> pl.getHealth()));
		assertTrue("Game was not over after 1000 turns. Player healths is " + healths, game.isGameOver());
	}
	
	@Test
	public void entersBattlefield() {
		superPlayCard("Murloc Raider");
		HStoneCard card = (HStoneCard) game.getCurrentPlayer().getBattlefield().getTopCard();
		assertNotNull("Card did not enter the battlefield", card);
	}


	private void failClick(HStoneCard card) {
		assertFalse(game.click(card));
	}

	@Test
	public void cannotAttackHand() {
		HStoneCard taunt = superCreateCard("Shieldbearer");
		nextTurn();
		HStoneCard attacker = playCharge();
		use(attacker);
		targetFail(taunt);
	}

	private Predicate<? super HStoneCardModel> hasAbility(HSAbility ability) {
		return hs -> hs.getAbilities().contains(ability);
	}

	@Test
	public void forEachDivineShield() {
		HStoneCard shielded = superPlayCard("Argent Squire");
		assertZoneSize(1, game.getCurrentPlayer().getBattlefield());
		nextTurn();
		nextTurn();
		nextTurn();
		superPlayCard("The Coin");
		assertTrue(shielded.hasAbility(HSAbility.DIVINE_SHIELD));
		HStoneCard remover = superPlayCard("Blood Knight");
		assertZoneSize(1, game.getCurrentPlayer().getBattlefield());
		assertZoneSize(1, game.getCurrentPlayer().getNextPlayer().getBattlefield());
		assertFalse(shielded.hasAbility(HSAbility.DIVINE_SHIELD));
		assertEquals(6, remover.getAttack());
		nextTurn();
		nextTurn();
		use(remover);
		target(game.getCurrentPlayer().getNextPlayer().getPlayerCard());
		assertEquals(24, game.getCurrentPlayer().getNextPlayer().getHealth());
	}
	
	@Test
	public void frozenCantAttack() {
		superPlayCard("Wisp");
		nextTurn();
		superPlayCard("Ice Lance");
		assertTrue(game.getCurrentPlayer().getBattlefield().isEmpty());
		assertTrue("No target selection", game.isTargetSelectionMode());
		HStoneCard card = (HStoneCard) game.getCurrentPlayer().getNextPlayer().getBattlefield().getTopCard();
		target(card);
		assertTrue("Minion was not frozen", card.hasAbility(HSAbility.FROZEN));
		useFail(card);
		nextTurn();
		assertTrue("Minion was not frozen after end turn", card.hasAbility(HSAbility.FROZEN));
		useFail(card);
		nextTurn();
		assertFalse("Minion is still frozen", card.hasAbility(HSAbility.FROZEN));
		nextTurn();
		assertDamageToOpponent(1, card);
	}
	
	@Test
	public void fullHand() {
		for (int i = 0; i < 24; i++) {
			assertTrue(game.nextPhase());
			assertFalse("Player has more than 10 cards on hand at turn " + i, game.getCurrentPlayer().getHand().size() > 10);
		}
		assertFalse(game.isGameOver());
	}
	
	@Test
	public void giveOppCrystal() {
		nextTurn();
		nextTurn();
		nextTurn();
		assertEquals(2, game.getCurrentPlayer().getNextPlayer().getResources().getResources(HStoneRes.MANA_TOTAL));
		useTheCoin();
		superPlayCard("Arcane Golem");
		nextTurn();
		assertEquals(4, game.getCurrentPlayer().getResources().getResources(HStoneRes.MANA_TOTAL));
		assertEquals(4, game.getCurrentPlayer().getResources().getResources(HStoneRes.MANA_AVAILABLE));
		
	}
	
	@Test
	public void hunterHero() {
		game = testGame(HStoneClass.HUNTER);
		reachMana(2);
		use(game.getCurrentPlayer().getHeroPower());
		game.cleanup();
		assertEquals(28, game.getCurrentPlayer().getNextPlayer().getHealth());
	}
	
	@Test
	public void killEachOther() {
		superPlayCard("Wisp");
		nextTurn();
		superPlayCard("Wisp");
		nextTurn();
		assertEquals(0, game.stackSize());
		HStoneCard wisp = (HStoneCard) game.getCurrentPlayer().getBattlefield().getTopCard();
		HStoneCard oppWisp = (HStoneCard) game.getCurrentPlayer().getNextPlayer().getBattlefield().getTopCard();
		use(wisp);
		target(oppWisp);
		assertEquals(0, wisp.getResources().getResources(HStoneRes.HEALTH));
		assertEquals(0, oppWisp.getResources().getResources(HStoneRes.HEALTH));
		assertEquals(0, wisp.getHealth());
		assertEquals(0, oppWisp.getHealth());
		assertTrue("My  board is not empty", game.getCurrentPlayer().getBattlefield().isEmpty());
		assertTrue("Opp board is not empty", game.getCurrentPlayer().getNextPlayer().getBattlefield().isEmpty());
	}
	
	@Test
	public void mageHero() {
		game = testGame(HStoneClass.MAGE);
		useFail(game.getCurrentPlayer().getHeroPower());
		reachMana(4);
		assertEquals(4, game.getCurrentPlayer().getResources().getResources(HStoneRes.MANA_AVAILABLE));
		use(game.getCurrentPlayer().getHeroPower());
		assertTrue(game.isTargetSelectionMode());
		target(game.getCurrentPlayer().getPlayerCard());
		game.cleanup();
		assertEquals(29, game.getCurrentPlayer().getHealth());
	}
	
	@Override
	protected HStoneGame newTestObject() {
		return testGame(HStoneClass.MAGE, HStoneClass.MAGE);
	}
	
	private void nextTurn() {
		if (game.isGameOver()) {
			System.out.println("No reason for Next turn, game has ended.");
			return;
		}
		assertFalse("Cannot go to next turn while selecting target", game.isTargetSelectionMode());
		assertZoneSize(0, game.getTemporaryZone());
		assertEquals("Game stack not empty!", 0, game.stackSize());
		assertTrue(game.nextPhase());
	}
	
	@Test
	public void noFightSamePlayer() {
		superPlayCard("Murloc Raider");
		nextTurn();
		nextTurn();
		superPlayCard("Murloc Raider");
		CardZone<HStoneCard> battlefield = game.getCurrentPlayer().getBattlefield();
		assertEquals(2, battlefield.size());
		
		HStoneCard attacker = (HStoneCard) battlefield.getTopCard();
		use(attacker);
		failClick(battlefield.getBottomCard());
		
		assertTrue(game.getCurrentPlayer() == attacker.getPlayer());
		assertFalse(game.getCurrentPlayer().getPlayerCard().clickAction().actionIsAllowed());
		use(attacker);
		target(game.getCurrentPlayer().getNextPlayer().getPlayerCard());
		assertEquals(28, game.getCurrentPlayer().getNextPlayer().getHealth());
	}
	
	@Test
	public void onlyAttackOnce() {
		superPlayCard("Wisp");
		nextTurn();
		nextTurn();
		HStoneCard wisp = (HStoneCard) game.getCurrentPlayer().getBattlefield().getTopCard();
		HStonePlayer opponent = game.getCurrentPlayer().getNextPlayer();
		assertEquals(30, opponent.getHealth());
		use(wisp);
		target(opponent.getPlayerCard());
		assertEquals(29, opponent.getHealth());
		useFail(wisp);
	}

	private void useTheCoin() {
		playCard("The Coin");
	}
	
	private HStoneCard playCard(String cardName) {
		assertEquals("Stack not empty", 0, game.stackSize());
		assertTrue(game.getTemporaryZone().isEmpty());
		assertFalse("Cannot play card while in target selection mode", game.isTargetSelectionMode());
//		int cardsOnHand = game.getCurrentPlayer().getHand().size();
		HStoneCard card = cardToHand(cardName, game.getCurrentPlayer());
		assertTrue("Card was not created to hand: " + cardName, card.getCurrentZone() == game.getCurrentPlayer().getHand());
		assertTrue("Unable to play card: " + card, game.click(card));
		assertEquals(0, game.stackSize());
		assertFalse("Card was not removed from hand: " + cardName, card.getCurrentZone() == game.getCurrentPlayer().getHand());
//		assertEquals("Card was not removed from hand: " + cardName, cardsOnHand - 1, game.getCurrentPlayer().getHand().size());
		return card;
	}
	
	@Test
	public void playerAndHandSize() {
		assertSame(game.getFirstPlayer(), game.getCurrentPlayer());
		assertEquals(4, game.getCurrentPlayer().getHand().size());
		nextTurn();
		assertSame(game.getFirstPlayer().getNextPlayer(), game.getCurrentPlayer());
		assertEquals(6, game.getCurrentPlayer().getHand().size()); // 4 start cards + the ring + a new card
	}
	
	@Test
	public void priestHero() {
		game = testGame(HStoneClass.PRIEST);
		HStoneCard attacker = superPlayCard("Murloc Raider");
		HStonePlayer opponent = game.getCurrentPlayer().getNextPlayer();
		nextTurn();
		nextTurn();
		use(attacker);
		target(game.getCurrentPlayer().getNextPlayer().getPlayerCard());
		assertEquals(28, opponent.getHealth());
		nextTurn();
		use(opponent.getHeroPower());
		target(opponent.getPlayerCard());
		game.cleanup();
		assertEquals(30, opponent.getHealth());
	}
	
	private void reachMana(int mana) {
		while (!game.getCurrentPlayer().getResources().hasResources(HStoneRes.MANA_AVAILABLE, mana)
				&& !game.isGameOver()) {
			nextTurn();
		}
	}


	@Test
	public void repeatPlayers() {
		HStonePlayer first = game.getFirstPlayer();
		HStonePlayer second = first.getNextPlayer();
		
		assertEquals(first, game.getCurrentPlayer());
		nextTurn();
		assertEquals(second, game.getCurrentPlayer());
		nextTurn();
		assertEquals(first, game.getCurrentPlayer());
		nextTurn();
		assertEquals(second, game.getCurrentPlayer());
		nextTurn();
		assertEquals(first, game.getCurrentPlayer());
	}


	@Test
	public void summoningSickness() {
		superPlayCard("Murloc Raider");
		HStoneCard card = (HStoneCard) game.getCurrentPlayer().getBattlefield().getTopCard();
		assertFalse("Minion acts as if it doesn't have summoning sickness", game.click(card));
		nextTurn();
		nextTurn();
		assertTrue("Minion is unable to attack even after sleeping", game.click(card));
	}
	
	private void target(HStoneCard card) {
		assertTrue("Target selection mode is not active", game.isTargetSelectionMode());
		assertTrue(game.getTemporaryZone().isEmpty());
		assertTrue(game.click(card));
		assertFalse(game.isTargetSelectionMode());
	}
	
	private StackAction targetFail(HStoneCard card) {
		assertTrue(game.isTargetSelectionMode());
		StackAction action = card.clickAction();
		game.addAndProcessStackAction(action);
		assertFalse("Action was performed: " + action, action.actionIsPerformed());
		return action;
	}
	
	@Test
	public void tauntMustBeAttacked() {
		superPlayCard("Wisp");
		nextTurn();
		HStoneCard taunter = superPlayCard("Shieldbearer");
		assertTrue("Don't have taunt minions", game.getCurrentPlayer().hasTauntMinions());
		nextTurn();
		HStoneCard wisp = (HStoneCard) game.getCurrentPlayer().getBattlefield().getTopCard();
		game.addAndProcessStackAction(wisp.clickAction());
		
		targetFail(game.getCurrentPlayer().getNextPlayer().getPlayerCard());
		assertEquals(30, game.getCurrentPlayer().getNextPlayer().getHealth());
		assertFalse("We're still in target selection even though it was not allowed", game.isTargetSelectionMode());
		
		use(wisp);
		target(taunter);
		assertEquals(30, game.getCurrentPlayer().getNextPlayer().getHealth());
		assertEquals(3, taunter.getHealth());
	}
	
	@Test
	public void megaHeal() {
		game = testGame(HStoneClass.PRIEST);
		HStoneCard wisp = superPlayCard("Wisp");
		reachMana(5);
		use(game.getCurrentPlayer().getHeroPower());
		target(wisp);
		assertEquals(1, wisp.getHealth());
	}
	
	@Test
	public void onlyDrawCardsIfImAlive() {
		game = testGame(HStoneClass.MAGE);
		HStoneCard wisp = superPlayCard("Wisp");
		reachMana(4);
		superPlayCard("Cult Master");
		assertZoneSize(2, game.getCurrentPlayer().getBattlefield());
		nextTurnX2();
		int size = game.getCurrentPlayer().getHand().size();
		useAndTarget(heroPower(), wisp);
		assertZoneSize(size + 1, game.getCurrentPlayer().getHand());
		superPlayCard(wisp.getModel().getName());
		nextTurn();
		
		size = game.getOpponent().getHand().size();
		superPlayCard("Holy Nova");
		assertZoneSize(size + 1, game.getOpponent().getHand());
	}
	
	private HStoneCard heroPower() {
		return game.getCurrentPlayer().getHeroPower();
	}

	@Test
	public void temporaryStealth() {
		HStoneCard wisp = superPlayCard("Wisp");
		assertFalse(wisp.hasAbility(HSAbility.STEALTH));
		
		superPlayCard("Conceal");
		assertTrue(wisp.hasAbility(HSAbility.STEALTH));
		
		HStoneCard wispWithoutStealth = superPlayCard("Wisp");
		assertFalse(wispWithoutStealth.hasAbility(HSAbility.STEALTH));
		
		nextTurn();
		nextTurn();
		assertFalse(wisp.hasAbility(HSAbility.STEALTH));
		
		HStoneCard alwaysStealth = superPlayCard("Worgen Infiltrator");
		assertTrue(alwaysStealth.hasAbility(HSAbility.STEALTH));
		superPlayCard("Conceal");
		assertTrue(alwaysStealth.hasAbility(HSAbility.STEALTH));
		nextTurn();
		assertTrue(alwaysStealth.hasAbility(HSAbility.STEALTH));
		nextTurn();
		assertTrue(alwaysStealth.hasAbility(HSAbility.STEALTH));
	}
	
	private HStoneGame testGame(HStoneClass hstoneClass) {
		return testGame(hstoneClass, hstoneClass);
	}
	
	protected HStoneGame testGame(HStoneClass player1, HStoneClass player2) {
		HStoneChar p1 = new HStoneChar("Test1", player1, HStoneDecks.testDeck());
		HStoneChar p2 = new HStoneChar("Test2", player2, HStoneDecks.testDeck());
		HStoneGame game = new HStoneGame(p1, p2);
		HStonePlayer pl1 = (HStonePlayer) game.getPlayers().get(0);
		HStonePlayer pl2 = (HStonePlayer) game.getPlayers().get(1);
		game.startGame();
		assertNull(game.getCurrentPlayer());
		assertEquals(3, pl1.getHand().size());
		assertEquals(4, pl2.getHand().size());
//		assertTrue(game.nextPhase()); // Pre-start game phase where players can switch cards, once.
		game.click(game.getActionZone().getTopCard());
		cards = game.getCards();
		return game;
	}
	
	private StackAction use(HStoneCard card) {
		assertFalse(game.isTargetSelectionMode());
		if (card.getCurrentZone() == card.getPlayer().getBattlefield()) {
			assertTrue(card.isAlive());
		}
		StackAction action = card.clickAction();
		game.addAndProcessStackAction(action);
		assertTrue("Cannot use " + card + " with action " + action + " mess " + action.getMessage(), action.actionIsPerformed());
		return action;
	}
	
	@Test
	public void nightmare() {
		HStoneCard minion = playCharge();
		superPlayCard("Nightmare");
		target(minion);
		assertDamageToOpponent(6, minion);
		nextTurn();
		assertTrue(minion.isAlive());
		nextTurn();
		assertFalse(minion.isAlive());
	}
	
	@Test
	public void usableCards() {
		game.getCurrentPlayer().getHand().moveToBottomOf(null);
		HStoneCard coin = createCardWithoutDestroy("The Coin");
		HStoneCard wisp = createCardWithoutDestroy("Wisp");
		HStoneCard cannotUse = createCardWithoutDestroy("Lightspawn"); // not usable
		HStoneCard murloc = createCardWithoutDestroy("Murloc Raider");
		
		List<Card<?>> usable = game.getUseableCards(game.getCurrentPlayer());
		
		assertZoneSize(1, game.getActionZone());
		assertTrue(usable.contains(game.getActionZone().getTopCard()));
		assertZoneSize(0, game.getCurrentPlayer().getBattlefield());
		
		assertZoneSize(2, game.getCurrentPlayer().getSpecialZone());
		assertTrue(usable.contains(game.getCurrentPlayer().getSpecialZone().getTopCard()));
		assertTrue(usable.contains(game.getCurrentPlayer().getSpecialZone().getBottomCard()));
		
		assertZoneSize(4, game.getCurrentPlayer().getHand());
		assertTrue(usable.contains(coin));
		assertTrue(usable.contains(wisp));
		assertTrue(usable.contains(cannotUse));
		assertTrue(usable.contains(murloc));
		assertEquals(usable.toString(), 7, usable.size());
	}
	
	@Test
	public void useCoin() {
		nextTurn();
		assertEquals(1, game.getCurrentPlayer().getResources().getResources(HStoneRes.MANA_AVAILABLE));
		useTheCoin();
		assertFalse("Coin triggered selection mode", game.isTargetSelectionMode());
		assertEquals("Coin did not give mana", 2, game.getCurrentPlayer().getResources().getResources(HStoneRes.MANA_AVAILABLE));
		assertTrue (game.getCurrentPlayer().getBattlefield().isEmpty());
		superPlayCard("Bloodfen Raptor");
		assertFalse(game.getCurrentPlayer().getBattlefield().isEmpty());
	}

	private StackAction useFail(HStoneCard card) {
		assertFalse(game.isTargetSelectionMode());
		StackAction action = card.clickAction();
		game.addAndProcessStackAction(action);
		assertFalse("Action was performed: " + action, action.actionIsPerformed());
		return action;
	}

	@Test
	public void warlockHero() {
		game = testGame(HStoneClass.WARLOCK);
		reachMana(2);
		int cards = game.getCurrentPlayer().getHand().size();
		use(game.getCurrentPlayer().getHeroPower());
		game.cleanup();
		assertZoneSize(cards + 1, game.getCurrentPlayer().getHand());
		assertEquals(28, game.getCurrentPlayer().getHealth());
	}
	
	@Test
	public void warriorHero() {
		game = testGame(HStoneClass.WARRIOR);
		reachMana(2);
		HStonePlayer armorPlayer = game.getCurrentPlayer();
		use(armorPlayer.getHeroPower());
		assertEquals(2, armorPlayer.getResources().getResources(HStoneRes.ARMOR));
		nextTurn();
		
		HStoneCard attacker = superPlayCard("Bloodfen Raptor");
		nextTurnX2();
		useAndTarget(attacker, armorPlayer.getPlayerCard());
		assertEquals(0, armorPlayer.getArmor());
		assertEquals(29, armorPlayer.getHealth());
	}

	@Test
	public void windfury() {
		superPlayCard("Young Dragonhawk");
		nextTurn();
		nextTurn();
		HStoneCard attacker = (HStoneCard) game.getCurrentPlayer().getBattlefield().getTopCard();
		assertEquals(30, game.getCurrentPlayer().getNextPlayer().getHealth());
		use(attacker);
		target(game.getCurrentPlayer().getNextPlayer().getPlayerCard());
		assertEquals(29, game.getCurrentPlayer().getNextPlayer().getHealth());
		use(attacker);
		target(game.getCurrentPlayer().getNextPlayer().getPlayerCard());
		assertEquals(28, game.getCurrentPlayer().getNextPlayer().getHealth());
	}
	
}
