package net.zomis.cards.util;

import net.zomis.aiscores.FieldScoreProducer;
import net.zomis.aiscores.ParamAndField;
import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.ScoreStrategy;
import net.zomis.aiscores.ScoreUtils;
import net.zomis.cards.model.CardModel;

public abstract class DeckBuilder<PlayerType extends DeckPlayer<CardModelType>, CardModelType extends CardModel> implements ScoreStrategy<PlayerType, CardModelType> {
	private ScoreConfig<PlayerType, CardModelType> config;

	public DeckBuilder(ScoreConfigFactory<PlayerType, CardModelType> factory) {
		this.config = factory.build();
	}

	public void createDeck(PlayerType player, int cards) {
		FieldScoreProducer<PlayerType, CardModelType> scorer = new FieldScoreProducer<PlayerType, CardModelType>(config, this);
		
		while (player.getCardCount() < cards) {
			ParamAndField<PlayerType, CardModelType> result = ScoreUtils.pickBest(scorer, player, player.getGame().getRandom());
			if (result == null)
				break;
			player.addCard(result.getField());
		}
	}
}
