package net.zomis.cards.cwars2;

import java.util.Collection;
import java.util.LinkedList;

import net.zomis.ZomisList;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.cards.model.Card;
import net.zomis.cards.util.CardModelFilter;
import net.zomis.cards.util.DeckBuilder;

public class CWars2DeckBuilder extends DeckBuilder<CWars2Player, CWars2Card> {
	private final int maxPerDeck = 5;

	public CWars2DeckBuilder(ScoreConfigFactory<CWars2Player, CWars2Card> factory) {
		super(factory);
	}

	@Override
	public Collection<CWars2Card> getFieldsToScore(CWars2Player params) {
		// TODO: Hackish solution, see http://docs.oracle.com/javase/tutorial/java/generics/subtyping.html and http://stackoverflow.com/questions/933447/how-do-you-cast-a-list-of-objects-from-one-type-to-another-in-java
//		return new ArrayList<CWars2Card>(
//		    Arrays.asList (
//		        params.getGame().getAvailableCards().toArray(new CWars2Card[0])
//		    )
//		);
		return (Collection<CWars2Card>) (Collection<?>)params.getGame().getAvailableCards();
	}

	@Override
	public boolean canScoreField(ScoreParameters<CWars2Player> parameters, CWars2Card field) {
		LinkedList<Card> list = parameters.getParameters().getDeck().cardList();
		LinkedList<Card> list2 = ZomisList.filter2(list, new CardModelFilter(field));
		return list2.size() < this.maxPerDeck;
	}
	
}
