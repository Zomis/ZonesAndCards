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
		return params.getGame().getCards();
	}

	@Override
	public boolean canScoreField(ScoreParameters<CWars2Player> parameters, CWars2Card field) {
		LinkedList<Card> list = parameters.getParameters().getDeck().cardList();
		LinkedList<Card> list2 = ZomisList.filter2(list, new CardModelFilter(field));
		return list2.size() < this.maxPerDeck;
	}
	
}
