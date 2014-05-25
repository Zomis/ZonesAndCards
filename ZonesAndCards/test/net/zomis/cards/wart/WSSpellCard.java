package net.zomis.cards.wart;

import net.zomis.cards.wart.factory.CardType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WSSpellCard extends WSBaseCard {

	@JsonCreator
	public WSSpellCard(@JsonProperty("name") String name, @JsonProperty("manaCost") int manaCost) {
		super(name, manaCost);
	}
	
	@Override
	protected CardType getCardType() {
		return CardType.SPELL;
	}	
	
}
