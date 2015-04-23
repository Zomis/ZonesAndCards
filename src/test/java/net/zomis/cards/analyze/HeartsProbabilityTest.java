package net.zomis.cards.analyze;

import static org.junit.Assert.*;

import java.util.Arrays;

import net.zomis.cards.analyze2.CardSolutions;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.Suite;
import net.zomis.cards.count.CardCounter;
import net.zomis.cards.events.card.CardPlayedEvent;
import net.zomis.cards.hearts.HeartsGame;
import net.zomis.cards.hearts.HeartsGiveDirection;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.utils.ZomisUtils;

import org.junit.Before;
import org.junit.Test;

public class HeartsProbabilityTest {

	private HeartsGame	game;
	private CardCounter	counter;
	private CardSolutions<CardZone<?>, Card<?>>	solve;

	@Before
	public void setup() {
		game = new HeartsGame(HeartsGiveDirection.NONE);
		game.setRandomSeed(42L);
		game.addPlayer("Counting Player");
		game.addPlayer("B");
		game.addPlayer("C");
		game.addPlayer("D");
		
		game.startGame();
		counter = new CardCounter(game);
		counter.setPerspective(game.getFirstPlayer());
		
		game.getPlayers().forEach(pl -> System.out.println(pl.getHand().cardList()));
		game.registerHandler(CardPlayedEvent.class, ev -> System.out.println("Card played: " + ev.getCard() + " by " + ev.getCard().getOwner()));
		solve = counter.solve();
		
		
		CardPlayer startPlayer = game.getCurrentPlayer();
		assertEquals(1, game.getPlayers().indexOf(startPlayer));
		
	}
	
	@Test
	public void hearts() {
		CardPlayer unknownPlayer = game.getPlayers().get(game.getPlayers().size() - 1);
		
		double[] calculated = solve.getProbabilityDistributionOf(unknownPlayer.getHand(), card -> ((ClassicCard) card.getModel()).getSuite() == Suite.CLUBS);
		
		solve.getSolutions().forEach(AnalyzeTestUtils::outputSolution);
		
		double[] nnkkArray = ZomisUtils.NNKKdistribution(39, 9, 13);
		System.out.println(Arrays.toString(nnkkArray));
		
		assertArrayEquals(nnkkArray, calculated, 0.00001);
		
//		game.click(game.getCurrentPlayer().getHand().getTopCard());
//		assertEquals(1, counter.getInformationCounter());

//		for (CardPlayer pl : game.getPlayers()) {
//			System.out.println(pl.getHand().cardList());
//		}
/*
NnKk
52 - 13
39
9
----------
13
x

11! / 2! / 9! = 55

2 kort till en --- 9 nCr 2 *
8! / 1! / 7! = 8


3 fönsterbrädor
4 blomkrukor
---------
004
013
022
031
040

103
112
121
130

202
211
220

301
310

400

13+12+11+10+9+8+7+6+5+4+3+2+1
*/
	}
	
	
}
