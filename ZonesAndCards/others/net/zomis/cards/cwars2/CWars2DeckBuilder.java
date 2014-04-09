package net.zomis.cards.cwars2;

import java.util.Collection;
import java.util.List;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.util.CardModelFilter;
import net.zomis.cards.util.DeckBuilder;
import net.zomis.utils.ZomisList;

public class CWars2DeckBuilder extends DeckBuilder<CWars2Player, CWars2Card> {
	public static final int MAX_PER_DECK = 5;

	public CWars2DeckBuilder(ScoreConfigFactory<CWars2Player, CWars2Card> factory) {
		super(factory);
	}

	@Override
	public Collection<CWars2Card> getFieldsToScore(CWars2Player params) {
		return params.getGame().getCards().values();
	}

	@Override
	public boolean canScoreField(ScoreParameters<CWars2Player> parameters, CWars2Card field) {
		CardZone<Card<CWars2Card>> list = parameters.getParameters().getDeck();
		List<Card<CWars2Card>> list2 = ZomisList.getAll(list, new CardModelFilter(field));
		return list2.size() < CWars2DeckBuilder.MAX_PER_DECK;
	}

	
}
