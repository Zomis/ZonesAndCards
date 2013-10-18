package net.zomis.cards.simple;

import java.util.Collection;
import java.util.LinkedList;

import net.zomis.aiscores.FieldScoreProducer;
import net.zomis.aiscores.ParamAndField;
import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.ScoreProducer;
import net.zomis.aiscores.ScoreStrategy;
import net.zomis.aiscores.ScoreUtils;
import net.zomis.aiscores.scorers.RandomScorer;
import net.zomis.cards.model.Card;

public class SimpleCardAI implements ScoreProducer<SimplePlayer, Card>, ScoreStrategy<SimplePlayer, Card> {

	private ScoreConfig<SimplePlayer, Card>	config;
	private SimplePlayer	player;

	public SimpleCardAI(SimplePlayer player) {
		config = new ScoreConfigFactory<SimplePlayer, Card>()
				.withScorer(new RandomScorer<SimplePlayer, Card>()).build();
		this.player = player;
	}
	
	@Override
	public ScoreConfig<SimplePlayer, Card> getConfig() {
		return config;
	}

	@Override
	public FieldScoreProducer<SimplePlayer, Card> createScoreProvider() {
		return new FieldScoreProducer<SimplePlayer, Card>(getConfig(), this);
	}

	@Override
	public ParamAndField<SimplePlayer, Card> play() {
		return ScoreUtils.pickBest(this.createScoreProvider(), this.player, this.player.getGame().getRandom());
	}

	@Override
	public Collection<Card> getFieldsToScore(SimplePlayer params) {
		Collection<Card> coll = new LinkedList<Card>();
		
		for (Card card : params.getHand().cardList())
			coll.add(card);
		
		return coll;
	}

	@Override
	public boolean canScoreField(ScoreParameters<SimplePlayer> parameters, Card field) {
		return true;
	}

}
