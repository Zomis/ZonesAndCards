package net.zomis.cards.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.ai.CardAI;

public abstract class MixAI extends CardAI {
	
	@Override
	@JsonIgnore
	public abstract void setConfig(ScoreConfigFactory<Player, StackAction> config);
}

