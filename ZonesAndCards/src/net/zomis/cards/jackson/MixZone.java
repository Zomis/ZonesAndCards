package net.zomis.cards.jackson;

import java.util.LinkedList;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public abstract class MixZone<E extends Card<?>> extends CardZone<E> {

	@JsonProperty
	private final LinkedList<Card<?>> cards = new LinkedList<Card<?>>();
	
	public MixZone(@JsonProperty(value="name") String name) {
		super(name);
	}
	
	@Override
	@JsonIgnore
	public abstract boolean isEmpty();

	@Override
	@JsonIgnore
	public abstract E getBottomCard();
	
	@Override
	@JsonIgnore
	public abstract E getTopCard();
	
}
