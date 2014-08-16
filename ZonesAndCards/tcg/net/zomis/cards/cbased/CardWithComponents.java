package net.zomis.cards.cbased;

import java.util.HashMap;
import java.util.Map;

import net.zomis.cards.iface.Component;
import net.zomis.cards.iface.HasComponents;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;

public class CardWithComponents extends Card<CompCardModel> implements HasComponents {

	private final Map<Class<? extends Component>, Component> components = new HashMap<>();
	
	public CardWithComponents(CompCardModel model, CardZone<?> initialZone) {
		super(model);
		this.currentZone = initialZone;
	}

	@Override
	public Map<Class<? extends Component>, Component> getComponents() {
		return components;
	}
	
	@Override
	public FirstCompGame getGame() {
		return (FirstCompGame) super.getGame();
	}

	@Override
	public CompPlayer getOwner() {
		return (CompPlayer) super.getOwner();
	}

}
