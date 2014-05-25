package net.zomis.cards.classic;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.zomis.cards.CardsTest;
import net.zomis.cards.ai.CGController;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.events.card.ZoneChangeEvent;
import net.zomis.cards.events.game.GameOverEvent;
import net.zomis.cards.hearts.HeartsGame;
import net.zomis.cards.hearts.HeartsSuperGame;
import net.zomis.cards.hearts.SimpleHeartsAI;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.custommap.CustomFacade;
import net.zomis.events.Event;
import net.zomis.events.EventListener;
import net.zomis.utils.ZomisList;

import org.junit.Test;

public class HeartsSuperTest extends CardsTest<HeartsSuperGame> implements EventListener {

	@Override
	protected void onBefore() {
		game = new HeartsSuperGame(new String[]{ "BUBU", "Minken", "Tejpbit", "Zomis" });
		game.setRandomSeed(42);
	}
	
	private int counter;
	
	@Event(priority=1000)
	public void onZoneChange(ZoneChangeEvent event) {
		if (event.getFromCardZone().getGame() == null) {
			counter--;
//			ZomisLog.info(event.toString());
		}
		if (event.getToCardZone() == null) {
			counter++;
		}
	}
	
	private CGController controller;
	
	@Test
	public void playHeartsSuperGame() {
		assertEquals(ClassicCard.RANK_2, game.getAceConfig().getMinRank());
		assertEquals(ClassicCard.RANK_ACE_HIGH, game.getAceConfig().getMaxRank());
		
		SimpleHeartsAI ai = new SimpleHeartsAI(new Random(42));
		controller = new CGController(game);
		for (Player pl : game.getPlayers()) {
			controller.setAI(pl, ai);
		}
		game.registerHandler(GameOverEvent.class, this::onGameOver);
		game.startGame();
		
		for (Player pl : game.getPlayers()) {
			CardPlayer player = (CardPlayer) pl;
			assertEquals(HeartsGame.RANKS_PER_SUITE, player.getHand().size());
			ClassicCard randomCard = (ClassicCard) ZomisList.getRandom(player.getHand().cardList()).getModel();
			assertNotSame(randomCard.getSuite().isBlack(), randomCard.getSuite().isRed());
		}
		
		int i = 0;
		while (!game.isGameOver() && i < 4000) {
			makeMove(i);
			++i;
		}
		
		assertTrue(i + " -- " + Arrays.toString(game.getScores()), game.isGameOver());
//		CustomFacade.getLog().i("Stopped after i " + i + Arrays.toString(game.getScores()));
	}

	private void onGameOver(GameOverEvent event) {
		CustomFacade.getLog().i("HeartsSuperGame - onGameOver: " + counter + event.toString());
		counter = 0;
	}
	
	private void makeMove(int iteration) {
		StackAction sa = null;
		if (game.getCurrentPlayer() == null) {
			for (Player pl : game.getPlayers()) {
				sa = controller.play(pl);
				assertTrue(iteration + " -- " + pl.toString() + " tried to do illegal action: " + sa, sa.actionIsPerformed());
//				ZomisLog.info(iteration + "- Action By Player: " + pl + sa);
				if (game.getCurrentPlayer() != null)
					break;
			}
		}
		else {
			CardPlayer pl = game.getCurrentPlayer();
			sa = controller.play();
//			ZomisLog.info(iteration + "--- Action By Player: " + pl + sa);
			assertTrue("Current player is " + game.getCurrentPlayer() + " old is " + pl + " pile is " + game.getPile(), game.getCurrentPlayer() != pl || game.getPile().isEmpty());
			assertTrue(iteration + sa.toString(), sa.actionIsPerformed());
//			CustomFacade.getLog().i("Action: " + pl + sa);
			if (game.getPile().isEmpty())
				verifyHands(iteration);
		}
	}
	private final Set<CardModel> cards = new HashSet<CardModel>();
	
	private void verifyHands(int iteration) {
		cards.clear();
		int i = -1;
		for (Player pl : game.getPlayers()) {
			CardPlayer player = (CardPlayer) pl;
			if (i == -1) {
				i = player.getHand().size();
//				ZomisLog.info("Initializing expected to " + player + " = " + player.getHand().size());
			}
			assertEquals("Unexpected zone size: " + player.toString(), i, player.getHand().size());
			for (Card<ClassicCard> card : player.getHand()) {
				assertTrue(cards.add(card.getModel()));
			}
		}
	}

	
	
}
