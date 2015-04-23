package net.zomis.cards.analyze;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import net.zomis.cards.analyze2.CardGroup;
import net.zomis.cards.analyze2.CardSolution;
import net.zomis.cards.analyze2.CardSolutions;
import net.zomis.cards.cbased.CompCardModel;
import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.count.CardCounter;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.GamePhase;

import org.junit.Before;
import org.junit.Test;

public class ZonesXYZCardsABCDTest {
	private CardSolutions<CardZone<?>, Card<?>>	solve;
	
	private CompCardModel a = new CompCardModel("a");
	private CompCardModel b = new CompCardModel("b");
	private CompCardModel c = new CompCardModel("c");
	private CompCardModel d = new CompCardModel("d");
	
	private CardZone<Card<?>> known;
	private CardZone<Card<?>> x;
	private CardZone<Card<?>> y;
	private CardZone<Card<?>> z;

	private CardGroup<Card<?>>	groupD;

	private CardGroup<Card<?>>	groupA;

	private CardGroup<Card<?>>	groupBCknown;

	private CardGroup<Card<?>>	groupBC;

	@Before
	public void xyzABCDtest() {
		FirstCompGame ga = new FirstCompGame();
		CompPlayer knownPlayer = new CompPlayer().setName("PL-Known");
		CompPlayer unknown = new CompPlayer().setName("PL-Unknown");
		ga.addPlayer(unknown);
		ga.addPlayer(knownPlayer);
		ga.addZone(known = new CardZone<>("Known", knownPlayer));
		ga.addZone(x = new CardZone<>("X____", unknown));
		ga.addZone(y = new CardZone<>("__Y__", unknown));
		ga.addZone(z = new CardZone<>("____Z", unknown));
		known.setKnown(knownPlayer, true);
		
		
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
		 * */
		
		System.out.println("XYZ");
		CardCounter counter = new CardCounter(ga);
		counter.setPerspective(knownPlayer);
		counter.addRule(x, 0, card -> card.getModel() == d);
		counter.addRule(y, 0, card -> card.getModel() == a);
		
		solve = counter.solve();// getProbabilityDistributionOf(y, card -> card.getModel() == d);
		groupD = solve.getGroups().stream().filter(grp -> grp.getCards().stream().anyMatch(card -> card.getModel() == d)).findFirst().get();
		groupA = solve.getGroups().stream().filter(grp -> grp.getCards().stream().anyMatch(card -> card.getModel() == a)).findFirst().get();
		Predicate<Card<?>> bc = card -> card.getModel() == b;
		Predicate<Card<?>> knownZone = card -> card.getCurrentZone() == known;
		groupBCknown = solve.getGroups().stream().filter(grp -> grp.getCards().stream().anyMatch(bc.and(knownZone))).findFirst().get();
		groupBC = solve.getGroups().stream().filter(grp -> grp.getCards().stream().anyMatch(bc.and(knownZone.negate()))).findFirst().get();
	}
	
	@Test
	public void assertSize() {
		assertEquals(6, solve.getSolutions().size());
	}
	
	@Test
	public void assertCombinations() {
		double[] combinations = solve.getSolutions().stream().mapToDouble(sol -> sol.getCombinations()).toArray();
		Arrays.sort(combinations);
		assertArrayEquals(new double[]{ 1.0, 1.0, 1.0, 4.0, 4.0, 8.0 }, combinations, 0.00001);
	}
	
	@Test
	public void assertKnownGroup() {
		solve.getSolutions().forEach(sol -> {
			assertEquals(2, sol.getAssignment(known, groupBCknown));
			assertEquals(0, sol.getAssignment(known, groupBC));
			assertEquals(0, sol.getAssignment(y, groupA));
			assertEquals(0, sol.getAssignment(x, groupD));
		});
	}
	
	@Test
	public void probabilityOfDinY() {
		double[] prob = solve.getProbabilityDistributionOf(y, card -> card.getModel() == d);
		System.out.println(Arrays.toString(prob));
		
		assertArrayEquals(new double[]{ 1.0 / 19.0, 12.0 / 19.0, 6.0 / 19.0  }, prob, 0.00001);
		
	}
	
	@Test
	public void assert_Y1d1bc_Z1d1a_X1bc1a() {
		List<CardSolution<CardZone<?>, Card<?>>> solutionss = solve.getSolutions();
		List<CardGroup<Card<?>>> groups = solve.getGroups();
		CardGroup<Card<?>> dGroup = groups.stream().filter(grp -> grp.getCards().stream().anyMatch(card -> card.getModel() == d)).findFirst().get();
		Collections.sort(solutionss, Comparator.comparingDouble(sol -> sol.getCombinations()));
		
		CardSolution<CardZone<?>, Card<?>> solution = solutionss.stream().filter(sol -> sol.getAssignments().get(y).getAssigns().get(dGroup) == 2).findFirst().get();
//		assertAssignment(solution, dGroup, );
		System.out.println("y = 2d:");
		AnalyzeTestUtils.outputSolution(solution);
		assertEquals(2, solution.getAssignment(z, groupA));
		assertEquals(2, solution.getAssignment(x, groupBC));
		assertEquals(2, solution.getAssignment(known, groupBCknown));
		assertEquals(2, solution.getAssignment(z, groupA));
		System.out.println("^^^^^^^^^ y = 2d:");
		
		solutionss.forEach(AnalyzeTestUtils::outputSolution);
		
	}

}
