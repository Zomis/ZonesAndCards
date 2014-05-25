package net.zomis.cards.wart;

import net.zomis.cards.wart.factory.CardType;
import net.zomis.cards.wart.factory.HStoneMinionType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WSMinionCard extends WSWeaponCard {
	public HStoneMinionType subtype;
	
	@JsonCreator
	public WSMinionCard(@JsonProperty("name") String name, @JsonProperty("manaCost") int manaCost) {
		super(name, manaCost);
	}

	@Override
	protected CardType getCardType() {
		return CardType.MINION;
	}
	
	public void setSubtype(String subtype) {
		if (subtype.isEmpty())
			return;
		this.subtype = HStoneMinionType.valueOf(subtype.toUpperCase());
	}
	
	@Override
	public String toString() {
		return getCardType() + " [subtype=" + subtype + ", " + super.toString() + "]";
	}
	
}
