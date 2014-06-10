package net.zomis.cards.analyze2;

import java.util.List;
import java.util.function.Predicate;

import net.zomis.cards.model.CardZone;

public class CardSolutions<Z extends CardZone<?>, C> {

	private final List<CardSolution<Z, C>> solutions;

	public CardSolutions(List<CardSolution<Z, C>> solutions) {
		this.solutions = solutions;
	}
	
	public double[] getProbabilityDistributionFor(Z zone, Predicate<C> card) {
		return null;
	}
	
	public List<CardSolution<Z, C>> getSolutions() {
		return solutions;
	}
	
//	public CardProbabilities<C> getProbabilityDistributionFor(Z zone) {
//		return new CardProbabilities<C>(zone, solutions);
//	}

}
