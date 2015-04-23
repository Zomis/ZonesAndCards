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
import net.zomis.cards.ai.CGController.AI;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;

public class CardAIGeneric<P extends Player, C extends Card<?>> implements ScoreStrategy<P, C>, AI {

	private ScoreConfig<P, C> mConfig;
	private double minScore = Integer.MIN_VALUE;
	protected FieldScoreProducer<P, C> scoreProd;
	protected boolean buffered;
	
	public void setMinScore(double minScore) {
		this.minScore = minScore;
	}
	
	public void setConfig(ScoreConfigFactory<P, C> config) {
		this.setConfig(config.build());
	}
	
	public void setConfig(ScoreConfig<P, C> config) {
		if (this.mConfig != null)
			throw new IllegalStateException("Config already set.");
		this.mConfig = config;
	}
	
	public ScoreConfig<P, C> getConfig() {
		return this.mConfig;
	}

	private FieldScoreProducer<P, C> createScoreProvider() {
		if (buffered)
			scoreProd = new BufferedScoreProducer<P, C>(mConfig, this);
		else scoreProd = new FieldScoreProducer<P, C>(mConfig, this);
		return scoreProd;
	}

	public ParamAndField<P, C> play(P player) {
		if (mConfig == null)
			throw new IllegalStateException("Config not initialized for AI " + this);
		if (player == null)
			throw new IllegalArgumentException("Player is null");
		if (player.getGame() == null)
			throw new IllegalArgumentException("Player does not have a game");
		
		ParamAndField<P, C> best = ScoreUtils.pickBest(this.createScoreProvider(), 
				player, player.getGame().getRandom());
		if (best == null || best.getFieldScore().getScore() < this.minScore) {
//			CustomFacade.getLog().w("Best is " + best + " which was not more than " + minScore + " by " + this);
			return new ParamAndField<P, C>(player, nullAction(player));
		}
		return best;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<C> getFieldsToScore(P params) {
		return (Collection<C>) params.getGame().getUseableCards(params);
	}

	@Override
	public boolean canScoreField(ScoreParameters<P> parameters, C field) {
		StackAction act = field.clickAction();
		if (act == null)
			throw new NullPointerException("Action is null for " + field);
		return field.clickAction().actionIsAllowed();
	}

	@Deprecated
	protected FieldScore<C> nullAction(Player player) { // TODO: Avoid using nullAction in CardAIs
		return new FieldScore<C>(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Card<?> decideMove(Player player) {
		return play((P) player).getField();
	}
	
}
