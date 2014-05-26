package net.zomis.cards.util;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import net.zomis.aiscores.FieldScoreProducer;
import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.ScoreStrategy;
import net.zomis.aiscores.extra.ParamAndField;
import net.zomis.aiscores.extra.ScoreUtils;
import net.zomis.cards.model.CardModel;
import net.zomis.oldstuff.CountingMap;

public abstract class DeckBuilder<P extends DeckPlayer<M>, M extends CardModel> implements ScoreStrategy<P, M> {
	private ScoreConfig<P, M> config;

	public DeckBuilder(ScoreConfigFactory<P, M> factory) {
		this.config = factory.build();
	}

	public void createDeck(P player, int cards, Random random) {
		FieldScoreProducer<P, M> scorer = new FieldScoreProducer<P, M>(config, this);
		
		while (player.getCardCount() < cards) {
			ParamAndField<P, M> result = ScoreUtils.pickBest(scorer, player, random);
			if (result == null) {
				throw new IllegalStateException("Error adding cards to deck player: " + player + ", " + player.getCardCount() + "/" + cards);
			}
			player.addCard(result.getField());
		}
	}
	
	public Map<M, Integer> createDeckMap(P player, int cards, Random random) {
		CountingMap<M> map = new CountingMap<M>();
		
		FieldScoreProducer<P, M> scorer = new FieldScoreProducer<P, M>(config, this);
		int count = 0;
		while (count < cards) {
			ParamAndField<P, M> result = ScoreUtils.pickBest(scorer, player, random);
			if (result == null) {
				throw new IllegalStateException("Error adding cards to deck player: " + player + ", " + count + "/" + cards);
			}
			count++;

			map.addCount(result.getField());
		}
		return map;
	}
	
	public static <E extends CardModel> void createExact(DeckPlayer<E> player, Collection<? extends CardCount<E>> cards) {
		for (CardCount<E> cc : cards) {
			for (int i = 0; i < cc.getCount(); ++i)
				player.addCard((E) cc.getModel());
		}
//		player.getDeck().shuffle();
	}
	
}
