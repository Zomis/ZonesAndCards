package net.zomis.cards.jackson;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.ai.CardAI;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class MixAI extends CardAI {
	
	@Override
	@JsonIgnore
	public abstract void setConfig(ScoreConfigFactory<Player, Card<?>> config);
}

