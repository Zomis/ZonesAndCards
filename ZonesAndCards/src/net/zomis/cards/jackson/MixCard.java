package net.zomis.cards.jackson;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public abstract class MixCard extends Card<CardModel> {

	MixCard(String name) {
		super(null);
	}
	
	@Override
	@JsonIgnore
	public abstract CardGame<?, ?> getGame();

}
