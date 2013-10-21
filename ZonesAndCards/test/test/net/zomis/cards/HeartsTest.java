package test.net.zomis.cards;

import junit.framework.Assert;
import net.zomis.ZomisList;
import net.zomis.aiscores.ParamAndField;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCardFilter;
import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.classics.Suite;
import net.zomis.cards.hearts.HeartsGame;
import net.zomis.cards.hearts.HeartsGiveAction;
import net.zomis.cards.hearts.HeartsGiveDirection;
import net.zomis.cards.hearts.SimpleHeartsAI;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.NextTurnAction;
import net.zomis.cards.util.CardCalculation;
import net.zomis.cards.util.CardCounter;

import org.junit.Test;

public class HeartsTest extends CardsTest<HeartsGame> {

	@Test
	public void countCards() {
		game = new HeartsGame(HeartsGiveDirection.NONE);
		game.setRandomSeed(42);
		game.addPlayer("BUBU");
		game.addPlayer("Minken");
		game.addPlayer("Tejpbit");
		game.addPlayer("Zomis");
		
		CardCounter counter = new CardCounter(game);
		counter.setPerspective(game.getPlayers().get(0));
		ClassicCardZone zone = new ClassicCardZone("Zone");
		zone.addDeck(game, 0);
		counter.addAvailableCards(zone);
		CardCalculation calc = counter.calculate(counter.getAvailable(), new ClassicCardFilter(Suite.HEARTS));
		// Library 52, Lands/Hearts 13, Hand 13, Probability for 3 lands is 28.63296%
		
		Assert.assertEquals("Incorrect probability 52/13/13/3", 0.2863296, calc.calcProbabilities(HeartsGame.MAGIC_NUMBER)[3], 0.000001);
		
		calc = counter.calculate(counter.getAvailable(), new ZomisList.FilterOR<Card>()
			.addFilter(new ClassicCardFilter(Suite.HEARTS))
			.addFilter(new ClassicCardFilter(Suite.SPADES, 12))
		);
		Assert.assertEquals("Incorrect probability 52/14/13/3", 0.2709786, calc.calcProbabilities(HeartsGame.MAGIC_NUMBER)[3], 0.000001);
		// Library 52, Lands/Points 14, Hand 13, Probability for 3 lands is 27.09786%
		
		// x Inform about the initial existing cards (52)
		game.startGame();
		Assert.assertEquals("Wrong information counter", HeartsGame.MAGIC_NUMBER, counter.getInformationCounter());
		
		// TODO: Inform about the known existing cards
		// TODO: When a player plays a card of incorrect suite, assume that they don't have any card of the real suite
		// TODO: When players play cards, inform about them
		// TODO: In the give card step, inform about cards that was given away
		// TODO: Calculate: Probability that a player after me **is able** to pick this stick
/*
 * BUBU has 10 cards.
 * 6 clubs, 2 diamonds, and 5 spades has been played.
 * Current suite is clubs
 * BUBU plays diamond
 * --> BUBU doesn't have any clubs
 * BUBU now has 9 cards. We know that they are only diamonds, spades and hearts.
 * How is the other probabilities affected?
 * 
 * We now know that the other clubs that we don't know about are either in Minken's hand or Tejpbit's hand.
 * 
 **/		
	}
	
	@Test
	public void play() {
		game.startGame();
		Assert.assertEquals(4, game.getPlayers().size());
		Assert.assertNotNull(game.getCurrentPlayer());
		for (int i = 0; i < 52; i++) {
			game.callPlayerAI();
		}
		Assert.assertTrue(game.isGameOver());
	}
	
	@Test
	public void playWithGive() {
		this.createGame(HeartsGiveDirection.LEFT);
		Assert.assertEquals(4, game.getPlayers().size());
		game.startGame();
		Assert.assertNull(game.getCurrentPlayer());
		
		for (int i = 0; i < HeartsGiveAction.GIVE_COUNT; i++) {
			Assert.assertFalse("Failed on i " + i, game.isNextPhaseAllowed());
			game.callPlayerAI();
		}
		
		Assert.assertNull(game.getCurrentPlayer());
		Player player = game.getPlayers().get(0);
		ParamAndField<Player, StackAction> saScore = player.getAI().play(player);
		StackAction sa = saScore.getField();
		Assert.assertFalse("Player is allowed to do something: " + sa + " with score " + saScore.getFieldScore().getScore(), sa.actionIsAllowed()
				&& !(sa instanceof NextTurnAction));
		
		Assert.assertTrue(game.isNextPhaseAllowed());
		Assert.assertTrue(game.nextPhase()); // This actually does a lot!!!
		
		Assert.assertNotNull(game.getCurrentPlayer());
		for (int i = 0; i < 52; i++) {
			game.callPlayerAI();
		}
		Assert.assertTrue(game.isGameOver());
	}
	
	@Test
	public void notEnoughPlayers() {
		for (int i = 0; i < 5; i++) {
			try {
				game = new HeartsGame(HeartsGiveDirection.NONE);
				for (int pl = 0; pl <= i; pl++)
					game.addPlayer("BUBU_" + pl);
				game.startGame();
			}
			catch (IllegalStateException e) {
				Assert.assertTrue(game.getPlayers().size() != 4);
			}
			if (game.getPlayers().size() == 4) {
				CardPlayer cp = (CardPlayer) game.getPlayers().get(0);
				Assert.assertEquals(HeartsGame.MAGIC_NUMBER, cp.getHand().cardList().size());
			}
		}
	}
	
	@Override
	protected void onBefore() {
		createGame(HeartsGiveDirection.NONE);
	}

	private void createGame(HeartsGiveDirection dir) {
		game = new HeartsGame(dir);
		game.setRandomSeed(42);
		game.addPlayer("BUBU");
		game.addPlayer("Minken");
		game.addPlayer("Tejpbit");
		game.addPlayer("Zomis");
		for (Player pl : game.getPlayers())
			pl.setAI(new SimpleHeartsAI(game.getRandom()));
	}
	

}
