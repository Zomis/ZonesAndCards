package net.zomis.cards.mdjq;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.ScoreParameters;
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
		for (CardModel card : params.getGame().getCards().values()) {
			list.add((MDJQCardModel) card);
		}
		return list;
	}

	@Override
	public boolean canScoreField(ScoreParameters<MDJQPlayer> parameters, MDJQCardModel field) {
		MDJQZone list = parameters.getParameters().getDeck();
		List<MDJQPermanent> list2 = ZomisList.getAll(list, new CardModelFilter(field));
		return list2.size() < field.getCardLimit();
	}

}
