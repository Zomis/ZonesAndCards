package net.zomis.cards.cwars2.cards;

import java.util.Map.Entry;

import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Res.Producers;
import net.zomis.cards.cwars2.CWars2Res.Resources;
import net.zomis.cards.model.actions.PublicAction;
import net.zomis.cards.resources.IResource;
import net.zomis.cards.resources.ResourceData;
import net.zomis.cards.resources.ResourceMap;
import net.zomis.cards.resources.ResourceStrategy;
import net.zomis.cards.resources.common.FixedResourceStrategy;

public class AllResourcesFocusOn extends PublicAction implements ResourceStrategy {

	private Resources resource;
	private CWars2Game	game;

	AllResourcesFocusOn() {}
	public AllResourcesFocusOn(CWars2Game game, Resources resource) {
		this.game = game;
		this.resource = resource;
	}
	
	@Override
	public void onPerform() {
		ResourceMap res = this.game.getCurrentPlayer().getResources();
		for (Producers prod : Producers.values()) {
			res.setResourceStrategy(prod, prod == resource.getProducer() ? this : new FixedResourceStrategy(0));
		}
	}

	@Override
	public int getResourceAmount(ResourceData type, ResourceMap map) {
		int total = 0;
		for (Entry<IResource, ResourceData> ee : map.getData().entrySet()) {
			if (ee.getKey() instanceof Producers) {
				total += ee.getValue().getRealValueOrDefault();
			}
		}
		return total;
	}
	
}
