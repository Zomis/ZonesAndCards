package net.zomis.cards.model.ai;

import java.util.Collection;

import net.zomis.aiscores.FieldScore;
import net.zomis.aiscores.FieldScoreProducer;
import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.ScoreStrategy;
import net.zomis.aiscores.extra.BufferedScoreProducer;
import net.zomis.aiscores.extra.ParamAndField;
import net.zomis.aiscores.extra.ScoreUtils;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;

public class CardAI implements ScoreStrategy<Player, Card<?>> {

	private ScoreConfig<Player, Card<?>> mConfig;
	private double minScore = Integer.MIN_VALUE;
	protected FieldScoreProducer<Player, Card<?>> scoreProd;
	protected boolean buffered;
	
	public void setMinScore(double minScore) {
		this.minScore = minScore;
	}
	
	public void setConfig(ScoreConfigFactory<Player, Card<?>> config) {
		this.setConfig(config.build());
	}
	public void setConfig(ScoreConfig<Player, Card<?>> config) {
		if (this.mConfig != null)
			throw new IllegalStateException("Config already set.");
		this.mConfig = config;
	}
	
	public ScoreConfig<Player, Card<?>> getConfig() {
		return this.mConfig;
	}

	private FieldScoreProducer<Player, Card<?>> createScoreProvider() {
		if (buffered)
			scoreProd = new BufferedScoreProducer<Player, Card<?>>(mConfig, this);
		else scoreProd = new FieldScoreProducer<Player, Card<?>>(mConfig, this);
		return scoreProd;
	}

	public ParamAndField<Player, Card<?>> play(Player player) {
		if (mConfig == null)
			throw new IllegalStateException("Config not initialized for AI " + this);
		if (player == null)
			throw new IllegalArgumentException("Player is null");
		if (player.getGame() == null)
			throw new IllegalArgumentException("Player does not have a game");
		
		ParamAndField<Player, Card<?>> best = ScoreUtils.pickBest(this.createScoreProvider(), 
				player, player.getGame().getRandom());
		if (best == null || best.getFieldScore().getScore() < this.minScore) {
//			CustomFacade.getLog().w("Best is " + best + " which was not more than " + minScore + " by " + this);
			return new ParamAndField<Player, Card<?>>(player, nullAction(player));
		}
		return best;
	}
	
	@Override
	public Collection<Card<?>> getFieldsToScore(Player params) {
		return params.getGame().getUseableCards(params);
	}

	@Override
	public boolean canScoreField(ScoreParameters<Player> parameters, Card<?> field) {
		return field.clickAction().actionIsAllowed();
	}

	protected FieldScore<Card<?>> nullAction(Player player) {
		return new FieldScore<Card<?>>(null);
	}
	
}
