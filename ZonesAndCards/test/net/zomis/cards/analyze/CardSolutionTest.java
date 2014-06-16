package net.zomis.cards.analyze;

import static org.junit.Assert.*;

import java.util.Arrays;

import net.zomis.cards.analyze2.CardGroup;
import net.zomis.cards.analyze2.CardSolution;
import net.zomis.cards.analyze2.CountStyle;
import net.zomis.cards.analyze2.ZoneRule;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;

import org.junit.Test;

public class CardSolutionTest {

	@Test
	public void cardGroupCreatedSuccessfullyInsideRule() {
		CardZone<Card<?>> zoneX = new CardZone<>("X");
		ZoneRule<CardZone<Card<?>>, String> x = new ZoneRule<>(zoneX, CountStyle.EQUAL, 1, Arrays.asList("A", "B"));
		CardGroup<String> group = x.getAssignments().getGroups().iterator().next();
		assertTrue(group.getCards().contains("A"));
		assertTrue(group.getCards().contains("B"));
		assertFalse(group.getCards().contains("C"));
	}
	
	@Test
	public void solutionWithOneRule() {
		CardZone<Card<?>> zoneX = new CardZone<>("X");
		ZoneRule<CardZone<Card<?>>, String> x = new ZoneRule<>(zoneX, CountStyle.EQUAL, 1, Arrays.asList("A", "B"));
		CardGroup<String> group = x.getAssignments().getGroups().iterator().next();
		CardSolution<CardZone<Card<?>>, String> solution = new CardSolution<>(Arrays.asList(x));
		assertEquals(2, solution.getCombinations(), 0.0001);
	}
	
	@Test
	public void solutionWithTwoRules() {
		CardZone<Card<?>> zoneX = new CardZone<>("X");
		CardZone<Card<?>> zoneY = new CardZone<>("Y");
		ZoneRule<CardZone<Card<?>>, String> x = new ZoneRule<>(zoneX, CountStyle.EQUAL, 1, Arrays.asList("A", "B"));
		CardGroup<String> group = x.getAssignments().getGroups().iterator().next();
		assertTrue(group.getCards().contains("A"));
		assertTrue(group.getCards().contains("B"));
		assertFalse(group.getCards().contains("C"));
		CardSolution<CardZone<Card<?>>, String> solution = new CardSolution<>(Arrays.asList(x));
		assertEquals(2, solution.getCombinations(), 0.0001);
	}
	
	
}
