package net.zomis.cards;

import static org.junit.Assert.*;

import java.util.List;

import net.zomis.cards.analyze2.CardSolution;
import net.zomis.cards.analyze2.CardsAnalyze;
import net.zomis.cards.cbased.CompCardModel;
import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.Suite;
import net.zomis.cards.count.CardCounter;
import net.zomis.cards.hearts.HeartsGame;
import net.zomis.cards.hearts.HeartsGiveDirection;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.GamePhase;

import org.junit.Test;

public class HeartsProbabilityTest {

	@Test
	public void xyzABCDtest() {
		FirstCompGame ga = new FirstCompGame();
		CompPlayer knownPlayer = new CompPlayer();
		CompPlayer unknown = new CompPlayer();
		CardZone<Card<?>> known;
		CardZone<Card<?>> x;
		CardZone<Card<?>> y;
		CardZone<Card<?>> z;
		ga.addZone(known = new CardZone<>("Known", knownPlayer));
		ga.addZone(x = new CardZone<>("X", unknown));
		ga.addZone(y = new CardZone<>("Y", unknown));
		ga.addZone(z = new CardZone<>("Z", unknown));
		
		CompCardModel a = new CompCardModel("a");
		CompCardModel b = new CompCardModel("b");
		CompCardModel c = new CompCardModel("c");
		CompCardModel d = new CompCardModel("d");
		
		known.createCardOnBottom(b);
		known.createCardOnBottom(c);
		x.createCardOnBottom(a);
		x.createCardOnBottom(a);
		y.createCardOnBottom(b);
		y.createCardOnBottom(c);
		z.createCardOnBottom(d);
		z.createCardOnBottom(d);
		
		ga.addPhase(new GamePhase(knownPlayer));
		ga.startGame();
		/* 4 zones: x, y, z, +1 known
		 * 4 card models, 2 cards of each: a,b,c,d
		 * known zone contains b,c
		 * y does not contain any a
		 * x does not contain any d
		 * 
		 * Combine Sudoku with MFE
		 * MFE: Categorize cards into groups - FieldGroups
		 * Sudoku: Keep track on what things are possible. Which cards are possible in which zones, and which zones can have which cards.
		 * MFE: FieldRule, Zone = cards. Example: 2y = b + c
		 * MFE: Combinatorics
		 * 
		 * 
		 * 2x = 0d
		 * 2y = 0a
		 * 
		 * 2x + 2y + 2z = 2a + b + c + 2d
		 * 
		 * 2y = 0d:
		 * 2y = 0a!
		 * 2y = b + c
		 * 2x = 0d!
		 * 2x = 2a
		 * 2z = 2d
		 * 1 combination
		 * 
		 * 2y = 1d:
		 * 2z = 1d
		 * 2y = 0a!
		 * 2y = 1bc <--- same "isolation" of bc
		 * 
		 * z = 0bc? --> x = 1bc + a, z = 1a
		 * z = 1bc? --> x = 2a
		 * 2 combinations
		 * 
		 * 
		 * 
		 * 2y = 2d:
		 * z = 0-2a + 0-2bc
		 * x = 0-2a + 0-2bc
		 * 1 + 2 + 1 combinations
		 * 
		 * 
		 * 
		 * */
		
		
		CardCounter counter = new CardCounter(ga);
		counter.setPerspective(knownPlayer);
		counter.addRule(x, 0, card -> card.getModel() == d);
		counter.addRule(y, 0, card -> card.getModel() == a);
		
		counter.getProbabilityDistributionOf(y, card -> card.getModel() == d);
		
		CardsAnalyze<CardZone<?>, Card<?>> analyze = new CardsAnalyze<>();
		analyze.addZone(known).addZone(x).addZone(y).addZone(z);
		analyze.addCards(known).addCards(x).addCards(y).addCards(z);
		analyze.addRule(known, 2, card -> card.getModel() == b || card.getModel() == c);
//		analyze.addRule(known, 1, card -> card.getModel() == b);
//		analyze.addRule(known, 1, card -> card.getModel() == c);
		
		analyze.addRule(x, 0, card -> card.getModel() == d);
		analyze.addRule(y, 0, card -> card.getModel() == a);
		
		List<CardSolution<CardZone<?>, Card<?>>> solutions = analyze.solve();
		analyze.outputRules();
		
//		assertEquals();
		System.out.println("Solutions:");
		for (CardSolution<CardZone<?>, Card<?>> sol : solutions) {
			System.out.println("Solution: " + sol);
			sol.getAssignments().forEach((zone, values) -> System.out.println(zone + " --> " + values));
			if (!sol.validCheck()) {
				System.out.println("Solution is not valid!!");
			}
			System.out.println();
			
			
		}
		
		
		
	}
	
	@Test
	public void gs() {
		
		HeartsGame game = new HeartsGame(HeartsGiveDirection.NONE);
		game.setRandomSeed(42L);
		game.addPlayer("A");
		game.addPlayer("B");
		game.addPlayer("C");
		game.addPlayer("D");
		
		game.startGame();
		CardCounter counter = new CardCounter(game);
		counter.setPerspective(game.getFirstPlayer());
		
		for (CardPlayer pl : game.getPlayers()) {
			System.out.println(pl.getHand().cardList());
			
		}
		System.out.println();
		
		CardPlayer startPlayer = game.getCurrentPlayer();
		assertEquals(1, game.getPlayers().indexOf(startPlayer));
		CardPlayer unknownPlayer = game.getPlayers().get(game.getPlayers().size() - 1);
/*
CSP
39 variables (one for each card-spot)
39 values (one for each card)
All must be unique


??? Use Monte Carlo Method? (bad idea when things start to get complex)




*/
		
		
		
		counter.getProbabilityDistributionOf(unknownPlayer.getHand(), card -> card.getModel().getSuite() == Suite.CLUBS);
		game.click(game.getCurrentPlayer().getHand().getTopCard());
		assertEquals(1, counter.getInformationCounter());
		
		
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


3 f�nsterbr�dor
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
		for (CardPlayer pl : game.getPlayers()) {
			System.out.println(pl.getHand().cardList());
			
		}
	}
	
	
}