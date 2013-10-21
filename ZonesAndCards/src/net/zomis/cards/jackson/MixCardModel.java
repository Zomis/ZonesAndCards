package net.zomis.cards.jackson;

import net.zomis.cards.model.CardModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public abstract class MixCardModel extends CardModel {

	@JsonCreator
	public MixCardModel(@JsonProperty("name") String name) {
		super(name);
	}

}
