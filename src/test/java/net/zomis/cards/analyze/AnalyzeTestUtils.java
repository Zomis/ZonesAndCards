package net.zomis.cards.analyze;

import net.zomis.cards.analyze2.CardSolution;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;

public class AnalyzeTestUtils {

	public static void outputSolution(CardSolution<CardZone<?>, Card<?>> solution) {
		System.out.println("Solution: " + solution);
		solution.getAssignments().forEach((zone, values) -> System.out.println(zone + " --> " + values));
		System.out.println();
	}

	
}
