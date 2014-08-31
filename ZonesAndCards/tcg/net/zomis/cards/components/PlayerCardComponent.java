package net.zomis.cards.components;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.CompCardModel;
import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.iface.Component;

public class PlayerCardComponent implements Component {

	private final CompCardModel cardModel;
	private final CardWithComponents card;

	public PlayerCardComponent(CompPlayer player) {
		this.cardModel = new CompCardModel("PLAYER " + player.getName());
		this.card = player.getRequiredComponent(SpecialZoneComponent.class).getSpecialZone().createCardOnBottom(cardModel);
	}
	
	public CardWithComponents getCard() {
		return card;
	}
	
	public CompCardModel getCardModel() {
		return cardModel;
	}
	
}
