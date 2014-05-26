package net.zomis.cards.cwars;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.cards.CardsTest;
import net.zomis.cards.cwars2.CWars2Card;
import net.zomis.cards.cwars2.CWars2CardFactory;
import net.zomis.cards.cwars2.CWars2DeckBuilder;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Player;
import net.zomis.cards.cwars2.CWars2Res;
import net.zomis.cards.cwars2.CWars2Res.Producers;
import net.zomis.cards.cwars2.CWars2Res.Resources;
import net.zomis.cards.cwars2.CWars2Setup;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.Player;
import net.zomis.cards.resources.IResource;
import net.zomis.cards.resources.ResourceData;
import net.zomis.cards.resources.ResourceMap;

import org.junit.Test;

public class CWars2CardsTest extends CardsTest<CWars2Game> {

	private void testAllThingy(Resources res) {
		int prod = totalProducers();
		CWars2Player player = game.getCurrentPlayer();
		ResourceMap values = getResources();
		performCard("All " + res.toString());
		values.changeResources(res, -1);
		assertResourcesEqual(values, player.getResources(), new IResource[]{ Resources.WEAPONS.getProducer(), Resources.BRICKS.getProducer(), Resources.CRYSTALS.getProducer() });
		for (Resources r : Resources.values()) {
			if (r == res)
				assertEquals(prod, player.getResources().getResources(r.getProducer()));
			else assertEquals(0, player.getResources().getResources(r.getProducer()));
		}
//		loadSave();
		skipTurn();
		assertResourceDifference(values, getResources(), res, prod);
		for (Entry<IResource, Integer> ee : values.getValues()) {
			if (ee.getKey() != res) {
				assertResourceDifference(values, getResources(), ee.getKey(), 0);
			}
		}
		assertResourceMapNoStrategies(game.getCurrentPlayer().getResources());
		assertResourceMapNoListeners(game.getCurrentPlayer().getResources());
	}

	@Test
	public void reverseMagic() {
		game.getCurrentPlayer().getResources().set(Resources.CRYSTALS, 42);
		game.getCurrentPlayer().getResources().set(Resources.BRICKS, 42);
		performCard("Magic Defense");
		skipTurn();
		ResourceMap old = getResources();
		performCard("Reverse");
		assertResourceDifference(old, game.getCurrentPlayer().getNextPlayer().getResources(), CWars2Res.WALL, -4);
		
		skipTurn();
		performCard("Magic Weapons");
		game.getCurrentPlayer().getResources().set(Resources.BRICKS, 42);
		game.getCurrentPlayer().getResources().set(CWars2Res.WALL, 42);
		old = getResources();
		performCard("Reverse");
		assertResourceDifference(old, game.getCurrentPlayer().getNextPlayer().getResources(), CWars2Res.WALL, -4);
//		game.getCurrentPlayer().getResources().set(CWars2Res.WALL, 20);
//		skipTurn();
//		performCard("Battering Ram");
	}
	private void assertResources() {
		for (Player player : game.getPlayers()) {
			for (Entry<IResource, ResourceData> ee : player.getResources().getData().entrySet()) {
				if (ee.getKey() instanceof Resources)
					assertTrue(ee.getValue() + " of " + ee.getKey() + player.getResources(), ee.getValue().getRealValueOrDefault() >= 0);
				if (ee.getKey() instanceof Producers)
					assertTrue(ee.getValue() + " of " + ee.getKey() + player.getResources(), ee.getValue().getRealValueOrDefault() >= 1);
				if (ee.getKey() == CWars2Res.WALL)
					assertTrue(ee.getValue() + " of " + ee.getKey() + player.getResources(), ee.getValue().getRealValueOrDefault() >= 0);
			}
		}
	}
	@Test
	public void createDeckMap() {
		CWars2DeckBuilder builder = new CWars2DeckBuilder(new ScoreConfigFactory<CWars2Player, CWars2Card>());
		int expected = 75;
		Map<CWars2Card, Integer> map = builder.createDeckMap(game.getCurrentPlayer(), expected, new Random());
		int total = 0;
		for (Entry<CWars2Card, Integer> ee : map.entrySet()) {
			total += ee.getValue();
		}
		assertEquals(expected, total);
	}
	@Test
	public void magicWeapons() {
		int total = getLife();
		skipTurn();
		cheat();
		performCard("Magic Weapons");
		loadSave();
		skipTurn();
		performCard("Archer");
		int newTotal = getLife();
		assertEquals(total - 4, newTotal);
	}
	@Test
	public void magicWeaponsDouble() {
		int total = getLife();
		skipTurn();
		cheat();
		performCard("Magic Weapons");
		skipTurn();
		performCard("Magic Weapons");
		loadSave();
		skipTurn();
		performCard("Archer");
		int newTotal = getLife();
		assertEquals("Failed: " + getResources(), total - 4, newTotal);
	}
	@Test
	public void magicWeapons2() {
		int total = getLife();
		skipTurn();
		cheat();
		performCard("Magic Weapons");
		skipTurn();
		performCard("Fire Archer");
		int newTotal = getLife();
		assertEquals("Failed: " + getResources(), total - 10, newTotal);
	}
	private void cheat() {
		for (Resources res : Resources.values()) {
			game.getCurrentPlayer().getResources().set(res, 42);
		}
	}

	private int getLife() {
		return game.getCurrentPlayer().getResources().getResources(CWars2Res.CASTLE) + game.getCurrentPlayer().getResources().getResources(CWars2Res.WALL);
	}
	
	@Test
	public void gest() {
		game.getCurrentPlayer().getResources().set(Resources.CRYSTALS, 2);
		game.getCurrentPlayer().getResources().set(Resources.CRYSTALS.getProducer(), 1);
		skipTurn();
		game.getCurrentPlayer().getResources().set(Resources.CRYSTALS, 5);
		performCard("Remove " + Resources.CRYSTALS);
		assertEquals(1, getResources().getResources(Resources.CRYSTALS));
	}
	
	@Test
	public void thief() {
		game.getCurrentPlayer().getResources().set(Resources.WEAPONS, 17);
		game.getCurrentPlayer().getNextPlayer().getResources().set(Resources.BRICKS, 42);
		ResourceMap old = getResources();
		ResourceMap opponent = new ResourceMap(game.getCurrentPlayer().getNextPlayer().getResources());
		performCard("Thief");
		ResourceMap newVal = new ResourceMap(game.getCurrentPlayer().getNextPlayer().getResources());
		for (Resources res : Resources.values()) {
			int cost = (res == Resources.WEAPONS ? 17 : 0);
			assertResourceDifference(old, newVal, res, Math.min(opponent.getResources(res), 8) - cost);
		}
	}
	@Test
	public void doNotAllowSacrifice() {
		Resources res = Resources.BRICKS;
		game.getCurrentPlayer().getResources().set(res, 8);
		game.getCurrentPlayer().getResources().set(res.getProducer(), 1);
		assertFalse(game.click(findCard("Sacrifice " + res.getProducer())));
	}
	@Test
	public void curse() {
		ResourceMap old = getResources();
		skipTurn();
		cheat();
		performCard("Curse");
		for (Resources res : Resources.values()) {
			assertResourceDifference(old, getResources(), res, getResources().getResources(res.getProducer()) - 1);
			assertResourceDifference(old, getResources(), res.getProducer(), -1);
		}
	}
	@Test
	public void sacrificeBuilder() {
		final Resources res = Resources.BRICKS;
		final Producers prod = res.getProducer();
		game.getCurrentPlayer().getResources().set(res, 8);
		ResourceMap mine = getResources();
		ResourceMap opp = game.getCurrentPlayer().getNextPlayer().getResources();
		assertEquals(2, mine.getResources(prod));
		assertEquals(2, opp.getResources(prod));
		performCard("Sacrifice " + prod);
		assertEquals(1, getResources().getResources(prod));
		skipTurn();
		assertEquals(1, getResources().getResources(prod));
	}
	@Test
	public void protectResources() {
		game.getCurrentPlayer().getResources().set(Resources.WEAPONS, 8);
		game.getCurrentPlayer().getResources().set(Resources.BRICKS, 7);
		ResourceMap oldRes = getResources();
		performCard("Protect Resources");
		loadSave();
		game.getCurrentPlayer().getResources().set(Resources.WEAPONS, 20);
		ResourceMap beforeThief = getResources();
		performCard("Thief");
		for (Resources res : Resources.values()) {
			int cost = res == Resources.WEAPONS ? -8 : 0;
			assertResourceDifference(oldRes, game.getCurrentPlayer().getResources(), res, oldRes.getResources(res.getProducer()) + cost);
		}
		skipTurn();
		
		assertResourceDifference(beforeThief, getResources(), Resources.BRICKS, beforeThief.getResources(Resources.BRICKS.getProducer()));
		assertResourceDifference(beforeThief, getResources(), Resources.WEAPONS, -17 + beforeThief.getResources(Resources.WEAPONS.getProducer()));
		assertResourceDifference(beforeThief, getResources(), Resources.CRYSTALS, beforeThief.getResources(Resources.CRYSTALS.getProducer()));
	}
	@Test
	public void magicDefense() {
		int old = getLife();
		game.getCurrentPlayer().getResources().set(Resources.CRYSTALS, 10);
		performCard("Magic Defense");
		loadSave();
		performCard("Archer");
		assertEquals(old, getLife());
		skipTurn();
		performCard("Fire Archer");
		assertEquals(old - 5, getLife());
	}
	@Test
	public void babylonEffect() {
		CWars2Card cardModel = (CWars2Card) findCard("Babylon").getModel();
		
		for (Entry<IResource, Integer> ee : cardModel.getOpponentEffects().getValues()) {
			assertEquals(null, ee.getValue());
		}
		
		ResourceMap map = new ResourceMap();
		map.change(cardModel.getOpponentEffects(), 1);
		assertEquals(null, map.dataFor(CWars2Res.CASTLE).getRealValue());
		assertEquals(30, cardModel.getEffects().getResources(CWars2Res.CASTLE));
		map.change(cardModel.getEffects(), 1);
		assertEquals((int) 30, (int) map.dataFor(CWars2Res.CASTLE).getRealValue());
		
		ResourceMap map2 = new ResourceMap(cardModel.getEffects());
		map2.change(cardModel.getOpponentEffects(), 1);
		
		ResourceMap map3 = new ResourceMap(cardModel.getOpponentEffects());
		map3.change(cardModel.getEffects(), 1);
		assertResourcesEqual(map2, map3);
		
		assertEquals(cardModel.getEffects().getResources(CWars2Res.CASTLE), map2.getResources(CWars2Res.CASTLE));
		
		assertResourcesEqual(map2, map);
	}
	@Test
	public void magicDefenseThenWeapons() {
		int old = getLife();
		cheat();
		performCard("Magic Defense");
		loadSave();
		cheat();
		performCard("Magic Weapons");
		loadSave();
		skipTurn();
		performCard("Archer");
		int newV = getLife();
		assertEquals(old, newV);
	}
	@Test
	public void magicCombo() {
		int old = getLife();
		skipTurn();
		cheat();
		performCard("Magic Weapons");
		loadSave();
		cheat();
		performCard("Magic Defense");
		loadSave();
		performCard("Archer");
		int newV = getLife();
		assertEquals(old, newV);
	}
	@Test
	public void rewardRoadblock() {
		ResourceMap oldRes = getResources();
		performCard("Reward Workers");
		game.getCurrentPlayer().getResources().set(Resources.WEAPONS, 12);
		performCard("Roadblock");
		for (Resources res : Resources.values())
			assertResourceDifference(oldRes, game.getCurrentPlayer().getResources(), res, -1);
	}
	@Test
	public void rewardWorkers() {
		ResourceMap oldRes = getResources();
		performCard("Reward Workers");
		skipTurn();
		ResourceMap newRes = getResources();
		for (Resources res : Resources.values())
			assertResourceDifference(oldRes, newRes, res, oldRes.getResources(res.getProducer()) * 2 - 1);
	}

	@Test
	public void sacrifice() {
		cheat();
		game.getCurrentPlayer().getResources().set(Producers.WIZARDS, 4);
		performCard("Sacrifice Wizards");
		skipTurn();
		performCard("Sacrifice Wizards");
		assertEquals(1, getResources().getResources(Producers.WIZARDS));
	}
	@Test
	public void roadblock() {
		ResourceMap oldResources = getResources();
		skipTurn();
		game.getCurrentPlayer().getResources().set(Resources.WEAPONS, 42);
		performCard("Roadblock");
		assertResourcesEqual(oldResources, getResources());
		skipTurn();
		assertResourceMapNoStrategies(game.getCurrentPlayer().getResources());
		assertResourceMapNoListeners(game.getCurrentPlayer().getResources());
		skipTurn();
		assertResourceDifference(oldResources, getResources(), Resources.WEAPONS, oldResources.getResources(Producers.RECRUITS));
	}
	@Test
	public void allWeapons() {
		testAllThingy(Resources.WEAPONS);
		testAllThingy(Resources.BRICKS);
		testAllThingy(Resources.CRYSTALS);
	}
	
	
	private ResourceMap getResources() {
		return new ResourceMap(game.getCurrentPlayer().getResources());
	}
	private void skipTurn() {
		performCard("Dummy");
	}
	private int totalProducers() {
		int total = 0;
		for (Entry<IResource, Integer> ee : game.getCurrentPlayer().getResources().getValues()) {
			if (ee.getKey() instanceof Producers) {
				total += ee.getValue();
			}
		}
		return total;
	}
	
	public void performCard(String name) {
		CWars2Player currPlayer = game.getCurrentPlayer();
		assertFalse(game.isDiscardMode());
		assertEquals(0, game.getDiscarded());
		assertFalse(game.isDiscardMode());
		assertEquals(0, game.getDiscarded());
		assertResources();
		assertTrue(game.click(findCard(name)));
		assertResources();
		assertNotSame("Current player did not change, was " + currPlayer, currPlayer, game.getCurrentPlayer());
	}
	
	public Card<CWars2Card> findCard(String name) {
		for (Card<CWars2Card> card : game.getCurrentPlayer().getHand()) {
			if (card.getModel().getName().equals(name)) {
				return card;
			}
		}
		throw new IllegalArgumentException("Card not found: " + name + " in hand of " + game.getCurrentPlayer().getName());
	}
	
	
	@Override
	protected void onBefore() {
		game = CWars2Setup.defaultGame(null);
		new CWars2CardFactory("Dummy").addTo(game);
		game.startGame();
		for (Player p : game.getPlayers()) {
			CWars2Player player = (CWars2Player) p;
			player.getResources().set(Resources.BRICKS, 2);
			player.getResources().set(Resources.WEAPONS, 2);
			player.getResources().set(Resources.CRYSTALS, 2);
			
			player.getResources().set(Producers.BUILDERS, 2);
			player.getResources().set(Producers.RECRUITS, 2);
			player.getResources().set(Producers.WIZARDS, 2);
			
			player.getResources().set(CWars2Res.CASTLE, 5);
			player.getResources().set(CWars2Res.WALL, 5);
			
			for (CardModel c : game.getCards().values()) {
				if (c.getName().equals("Dummy")) {
					player.getHand().createCardOnBottom(c);
					player.getHand().createCardOnBottom(c);
				}
				player.getHand().createCardOnBottom(c);
				player.getHand().createCardOnBottom(c);
			}
		}
	}
	
	private void loadSave() {
		if (true)
			return;
		ResourceMap res = game.getCurrentPlayer().getResources();

//		String data = CardsIO.save(game);
//		CWars2Game loaded = CardsIO.load(data, CWars2Game.class);
		CWars2Game loaded = null;

		assertNotNull("Failed loading game. See StackTrace", loaded);
		assertEquals(game.getCurrentPlayer().getName(), loaded.getCurrentPlayer().getName());
		assertEquals(game.getCards().size(), loaded.getCards().size());
		assertEquals(game.getActivePhase().getClass(), loaded.getActivePhase().getClass());
		assertNotSame(game.getActivePhase(), loaded.getActivePhase());
		assertNotSame(game.getCurrentPlayer(), loaded.getCurrentPlayer());
		assertNotSame(res, loaded.getCurrentPlayer().getResources());
		assertResourcesEqual(res, loaded.getCurrentPlayer().getResources());
	}
	
}
