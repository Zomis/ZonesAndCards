package net.zomis.cards.analyze2;

import java.util.List;

import net.zomis.cards.model.CardZone;

public interface CardSolutionCallback<Z extends CardZone<?>, C> {

	void onSolved(List<ZoneRule<Z, C>> results);
	
}
