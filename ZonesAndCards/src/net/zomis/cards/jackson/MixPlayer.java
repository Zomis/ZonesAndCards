package net.zomis.cards.jackson;

import java.util.List;

import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.Player;
import net.zomis.cards.resources.ResourceMap;
import net.zomis.cards.util.DeckPlayer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public abstract class MixPlayer extends Player implements DeckPlayer<CardModel> {

	@Override
	public abstract ResourceMap getResources();
	@JsonCreator
	public MixPlayer(@JsonProperty("name") String name) {}
	
	@Override
	@JsonIgnore
	public abstract List<Player> getOpponents();
	
	@JsonIgnore
	public abstract Player getNextPlayer();
	
	@Override
	@JsonIgnore
	public abstract int getCardCount();
	
}
