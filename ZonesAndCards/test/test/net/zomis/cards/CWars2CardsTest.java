package test.net.zomis.cards;

import static org.junit.Assert.*;

import java.util.Map.Entry;

import net.zomis.cards.cwars2.CWars2CardFactory;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Game.Producers;
import net.zomis.cards.cwars2.CWars2Game.Resources;
import net.zomis.cards.cwars2.CWars2Player;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.util.IResource;
import net.zomis.cards.util.ResourceMap;
import net.zomis.custommap.CustomFacade;

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
		
		skipTurn();
		assertResourceDifference(values, getResources(), res, prod);
		for (Entry<IResource, Integer> ee : values.getMapData().entrySet()) {
			if (ee.getKey() != res) {
				assertResourceDifference(values, getResources(), ee.getKey(), 0);
			}
		}
		assertResourceMapNoStrategies(game.getCurrentPlayer().getResources());
		assertResourceMapNoListeners(game.getCurrentPlayer().getResources());
	}
	
	@Test
	public void magicWeapons() {
		int total = getLife();
		skipTurn();
		cheat();
		performCard("Magic Weapons");
		skipTurn();
		performCard("Archer");
		int newTotal = getLife();
		assertEquals(total - 4, newTotal);
	}
	private void cheat() {
		for (Resources res : Resources.values()) {
			game.getCurrentPlayer().getResources().set(res, 42);
		}
	}

	private int getLife() {
		return game.getCurrentPlayer().getResources().getResources(CWars2Game.CASTLE) + game.getCurrentPlayer().getResources().getResources(CWars2Game.WALL);
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
			int cost = 0;
			if (res == Resources.WEAPONS) cost = 17;
			assertResourceDifference(old, newVal, res, Math.min(opponent.getResources(res), 8) - cost);
		}
	}
	@Test
	public void protectResources() {
		game.getCurrentPlayer().getResources().set(Resources.WEAPONS, 8);
		game.getCurrentPlayer().getResources().set(Resources.BRICKS, 7);
		ResourceMap oldRes = getResources();
		performCard("Protect Resources");
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
		performCard("Archer");
		assertEquals(old, getLife());
		skipTurn();
		performCard("Fire Archer");
		assertEquals(old - 5, getLife());
	}
	@Test
	public void magicDefenseThenWeapons() {
		int old = getLife();
		cheat();
		performCard("Magic Defense");
		cheat();
		performCard("Magic Weapons");
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
		cheat();
		performCard("Magic Defense");
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
		for (Entry<IResource, Integer> ee : game.getCurrentPlayer().getResources().getMapData().entrySet()) {
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
		StackAction act = game.getAIHandler().click(findCard(name));
		CustomFacade.getLog().i(game.getActivePhase().toString() + " Perform " + act);
		assertFalse(game.isDiscardMode());
		assertEquals(0, game.getDiscarded());
		game.addAndProcessStackAction(act);
		CustomFacade.getLog().i(game.getActivePhase().toString());
		assertTrue("Action could not be performed: " + act + " for card " + name + " because of " + act.getMessage(), act.isPerformed());
		assertNotSame("Current player did not change, was " + currPlayer + " action " + act + " message is " + act.getMessage(), currPlayer, game.getCurrentPlayer());
	}
	
	public Card findCard(String name) {
		for (Card card : game.getCurrentPlayer().getHand().cardList()) {
			if (card.getModel().getName().equals(name)) {
				return card;
			}
		}
		throw new IllegalArgumentException("Card not found: " + name + " in hand of " + game.getCurrentPlayer().getName());
	}
	
	
	@Override
	protected void onBefore() {
		game = new CWars2Game();
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
			
			player.getResources().set(CWars2Game.CASTLE, 5);
			player.getResources().set(CWars2Game.WALL, 5);
			
			for (CardModel c : game.getAvailableCards()) {
				if (c.getName().equals("Dummy")) {
					player.getHand().createCardOnBottom(c);
					player.getHand().createCardOnBottom(c);
					player.getHand().createCardOnBottom(c);
				}
				player.getHand().createCardOnBottom(c);
			}
		}
	}
	
}
