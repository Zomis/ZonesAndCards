package net.zomis.cards.components;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.iface.Component;

public class ChosenCardComponent implements Component {

	private CardWithComponents card;
	
	public CardWithComponents getCard() {
		return card;
	}
	
	public void setCard(CardWithComponents card) {
		this.card = card;
	}
	
}
