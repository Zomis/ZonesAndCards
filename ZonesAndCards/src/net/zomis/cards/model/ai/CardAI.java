package net.zomis.cards.model.ai;

import java.util.Collection;

import net.zomis.aiscores.FieldScoreProducer;
import net.zomis.aiscores.ParamAndField;
import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.ScoreProducer;
import net.zomis.aiscores.ScoreStrategy;
import net.zomis.aiscores.ScoreUtils;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;

public class CardAI implements ScoreStrategy<Player, StackAction>, ScoreProducer<Player, StackAction> {

	private ScoreConfig<Player, StackAction> config;
	private CardGame game;
	
	public CardAI(CardGame game) {
		this.game = game;
	}
	
	public void setConfig(ScoreConfig<Player, StackAction> config) {
		if (this.config != null)
			throw new IllegalStateException("Config already set.");
		this.config = config;
	}
	
	@Override
	public ScoreConfig<Player, StackAction> getConfig() {
		return this.config;
	}

	@Override
	public FieldScoreProducer<Player, StackAction> createScoreProvider() {
		return new FieldScoreProducer<Player, StackAction>(config, this);
	}

	@Override
	public ParamAndField<Player, StackAction> play() {
		return ScoreUtils.pickBest(this.createScoreProvider(), new Player[]{ game.getCurrentPlayer() }, this.game.getRandom());
	}

	@Override
	public Collection<StackAction> getFieldsToScore(Player params) {
		return game.getAIHandler().getAvailableActions(params);
	}

	@Override
	public boolean canScoreField(ScoreParameters<Player> parameters, StackAction field) {
		return field.isAllowed();
	}

}
