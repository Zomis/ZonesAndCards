package net.zomis.cards.jackson;

import net.zomis.cards.util.UnmodifiableResource;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public abstract class MixUnmodifiable extends UnmodifiableResource {

	public MixUnmodifiable() {
		super(null, 0);
	}

	@JsonProperty
	private final int	mDefault = 0;
	@JsonProperty
	private final String	name = null;

	@Override
	@JsonIgnore
	public abstract int getDefault();
	
}
