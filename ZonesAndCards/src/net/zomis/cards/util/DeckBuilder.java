package net.zomis.cards.util;

import java.util.Collection;
import java.util.Map;

import net.zomis.aiscores.FieldScoreProducer;
import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.ScoreStrategy;
import net.zomis.aiscores.extra.ParamAndField;
import net.zomis.aiscores.extra.ScoreUtils;
import net.zomis.cards.model.CardModel;
import net.zomis.oldstuff.IntegerMap;

public abstract class DeckBuilder<PlayerType extends DeckPlayer<CardModelType>, CardModelType extends CardModel> implements ScoreStrategy<PlayerType, CardModelType> {
	private ScoreConfig<PlayerType, CardModelType> config;

	public DeckBuilder(ScoreConfigFactory<PlayerType, CardModelType> factory) {
		this.config = factory.build();
	}

	public void createDeck(PlayerType player, int cards) {
		FieldScoreProducer<PlayerType, CardModelType> scorer = new FieldScoreProducer<PlayerType, CardModelType>(config, this);
		
		while (player.getCardCount() < cards) {
			ParamAndField<PlayerType, CardModelType> result = ScoreUtils.pickBest(scorer, player, player.getGame().getRandom());
			if (result == null) {
				throw new IllegalStateException("Error adding cards to deck player: " + player + ", " + player.getCardCount() + "/" + cards);
			}
			player.addCard(result.getField());
		}
	}
	
	public Map<CardModelType, Integer> createDeckMap(PlayerType player, int cards) {
		IntegerMap<CardModelType> map = new IntegerMap<CardModelType>();
		
		FieldScoreProducer<PlayerType, CardModelType> scorer = new FieldScoreProducer<PlayerType, CardModelType>(config, this);
		int count = 0;
		while (count < cards) {
			ParamAndField<PlayerType, CardModelType> result = ScoreUtils.pickBest(scorer, player, player.getGame().getRandom());
			if (result == null) {
				throw new IllegalStateException("Error adding cards to deck player: " + player + ", " + count + "/" + cards);
			}
			count++;

			map.addCount(result.getField());
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public static <E extends CardModel> void createExact(DeckPlayer<E> player, Collection<? extends CardCount> cards) {
		player.clearCards();
		for (CardCount cc : cards) {
			for (int i = 0; i < cc.getCount(); ++i)
				player.addCard((E) cc.getModel());
		}
	}
	
}
