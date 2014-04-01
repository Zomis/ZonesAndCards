package net.zomis.cards.mdjq;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.util.CardModelFilter;
import net.zomis.cards.util.DeckBuilder;
import net.zomis.utils.ZomisList;

public class MDJQDeckBuilder extends DeckBuilder<MDJQPlayer, MDJQCardModel> {

	public MDJQDeckBuilder(ScoreConfigFactory<MDJQPlayer, MDJQCardModel> factory) {
		super(factory);
	}

	@Override
	public Collection<MDJQCardModel> getFieldsToScore(MDJQPlayer params) {
		HashSet<MDJQCardModel> list = new HashSet<MDJQCardModel>();
		for (CardModel card : params.getGame().getAvailableCards()) {
			list.add((MDJQCardModel) card);
		}
		return list;
	}

	@Override
	public boolean canScoreField(ScoreParameters<MDJQPlayer> parameters, MDJQCardModel field) {
		LinkedList<Card> list = parameters.getParameters().getDeck().cardList();
		LinkedList<Card> list2 = ZomisList.filter2(list, new CardModelFilter(field));
		return list2.size() < field.getCardLimit();
	}

}
