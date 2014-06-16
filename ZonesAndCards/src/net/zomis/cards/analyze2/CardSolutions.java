package net.zomis.cards.analyze2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import net.zomis.cards.model.CardZone;

public class CardSolutions<Z extends CardZone<?>, C> {

	private final List<CardSolution<Z, C>> solutions;

	public CardSolutions(List<CardSolution<Z, C>> solutions) {
		this.solutions = solutions;
	}
	
	public double[] getProbabilityDistributionOf(Z zone, Predicate<C> predicate) {
		double[] dbl = new double[zone.size() + 1];
		for (CardSolution<Z, C> sol : solutions) {
			double[] result = sol.getProbabilityDistributionOf(zone, predicate);
			for (int i = 0; i < result.length; i++) {
				dbl[i] += result[i];
			}
		}
		
		return dbl;
	}
	
	public List<CardSolution<Z, C>> getSolutions() {
		return new ArrayList<>(solutions);
	}

	/**
	 * 
	 * @return A list of all the {@link CardGroup}s available in this solution set.
	 */
	public List<CardGroup<C>> getGroups() {
		CardSolution<Z, C> firstSolution = solutions.get(0);
		Map<Z, CardAssignment<Z, C>> assignments = firstSolution.getAssignments();
		return new ArrayList<>(assignments.values().iterator().next().getGroups());
	}
	
	public CardSolutions<Z, C> getSolutionsWithAssignment(Z zone, CardGroup<C> group, int value) {
		List<CardSolution<Z, C>> solutions = new ArrayList<>();
		
		for (CardSolution<Z, C> sol : this.solutions) {
			if (sol.getAssignment(zone, group) == value) {
				solutions.add(sol);
			}
		}
		
		return new CardSolutions<>(solutions);
	}
	

}
