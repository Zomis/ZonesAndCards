package net.zomis.cards.jackson;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public abstract class MixCard extends Card {

	MixCard(String name) {
		super(null);
	}
	
	@Override
	@JsonIgnore
	public abstract CardGame getGame();

}
