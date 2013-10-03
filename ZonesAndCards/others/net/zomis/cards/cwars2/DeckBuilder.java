package net.zomis.cards.cwars2;

import java.util.Collection;
import java.util.LinkedList;

import net.zomis.ZomisList;
import net.zomis.aiscores.FieldScoreProducer;
import net.zomis.aiscores.ParamAndField;
import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.ScoreStrategy;
import net.zomis.aiscores.ScoreUtils;
import net.zomis.cards.model.Card;
import net.zomis.cards.util.CardModelFilter;

public class DeckBuilder implements ScoreStrategy<CWars2Player, CWars2Card> {
	private ScoreConfig<CWars2Player, CWars2Card>	config;
	private final int maxPerDeck = 5;

	public DeckBuilder(ScoreConfigFactory<CWars2Player, CWars2Card> factory) {
		this.config = factory.build();
	}

	public void createDeck(CWars2Player player, int cards) {
		FieldScoreProducer<CWars2Player, CWars2Card> scorer = new FieldScoreProducer<CWars2Player, CWars2Card>(config, this);
		
		while (player.getCardCount() < cards) {
			ParamAndField<CWars2Player, CWars2Card> result = ScoreUtils.pickBest(scorer, new CWars2Player[]{ player });
			if (result == null)
				break;
			
			player.addCard(result.getField());
		}
		
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
