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
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.InvalidStackAction;

public class CardAI implements ScoreStrategy<Player, StackAction> {

	private ScoreConfig<Player, StackAction> mConfig;
	private double minScore = Integer.MIN_VALUE;
	protected FieldScoreProducer<Player, StackAction> scoreProd;
	protected boolean buffered;
	
	public void setMinScore(double minScore) {
		this.minScore = minScore;
	}
	
	public void setConfig(ScoreConfigFactory<Player, StackAction> config) {
		this.setConfig(config.build());
	}
	public void setConfig(ScoreConfig<Player, StackAction> config) {
		if (this.mConfig != null)
			throw new IllegalStateException("Config already set.");
		this.mConfig = config;
	}
	
	public ScoreConfig<Player, StackAction> getConfig() {
		return this.mConfig;
	}

	private FieldScoreProducer<Player, StackAction> createScoreProvider() {
		if (buffered)
			scoreProd = new BufferedScoreProducer<Player, StackAction>(mConfig, this);
		else scoreProd = new FieldScoreProducer<Player, StackAction>(mConfig, this);
		return scoreProd;
	}

	public ParamAndField<Player, StackAction> play(Player player) {
		if (mConfig == null)
			throw new IllegalStateException("Config not initialized for AI " + this);
		if (player == null)
			throw new IllegalArgumentException("Player is null");
		if (player.getGame() == null)
			throw new IllegalArgumentException("Player does not have a game");
		
		ParamAndField<Player, StackAction> best = ScoreUtils.pickBest(this.createScoreProvider(), 
				player, player.getGame().getRandom());
		if (best == null || best.getFieldScore().getScore() < this.minScore) {
//			CustomFacade.getLog().w("Best is " + best + " which was not more than " + minScore + " by " + this);
			return new ParamAndField<Player, StackAction>(player, nullAction(player));
		}
		return best;
	}
	
	@Override
	public Collection<StackAction> getFieldsToScore(Player params) {
		return params.getGame().getAvailableActions(params);
	}

	@Override
	public boolean canScoreField(ScoreParameters<Player> parameters, StackAction field) {
		if (!field.actionIsAllowed()) {
//			CustomFacade.getLog().w("AI removing action: " + field);
		}
		return field.actionIsAllowed();
	}

	protected FieldScore<StackAction> nullAction(Player player) {
		return new FieldScore<StackAction>(new InvalidStackAction("AI Null Action"));
	}
	
}
