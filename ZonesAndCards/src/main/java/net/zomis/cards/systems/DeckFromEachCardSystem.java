package net.zomis.cards.systems;

import java.util.List;

import net.zomis.cards.cbased.CompCardModel;
import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.DeckSourceComponent;
import net.zomis.cards.components.ZoneComponent;
import net.zomis.cards.iface.GameSystem;
import net.zomis.custommap.view.ZomisLog;
import net.zomis.utils.ZomisList;

public class DeckFromEachCardSystem implements GameSystem {

	private final int count;
	private final Class<? extends ZoneComponent> toComponent;

	public DeckFromEachCardSystem(int count, Class<? extends ZoneComponent> toComponent) {
		this.count = count;
		this.toComponent = toComponent;
	}
	
	@Override
	public void onStart(FirstCompGame game) {
		ZomisLog.info("Create deck from each card. " + this.count + " to " + toComponent);
		List<CompCardModel> cards = ZomisList.getAll(game.getCards().values(), c -> !game.getActionZone().containsModel(c));
		for (CompPlayer pl : game.getPlayers()) {
			if (toComponent != null) {
				pl.compatibility(this.toComponent).required();
				ZomisLog.info("Creating deck for " + pl + " to " + toComponent);
			
				ZoneComponent component = pl.getComponent(toComponent);
				for (CompCardModel card : cards) {
					for (int i = 0; i < count; i++)
						component.getZone().createCardOnBottom(card);
				}
			}
			else {
				if (pl.compatibility(DeckSourceComponent.class).failsThenWarn())
					return;
				ZomisLog.info("Creating deck for " + pl);
			
				DeckSourceComponent component = pl.getComponent(DeckSourceComponent.class);
				for (CompCardModel card : cards) {
					for (int i = 0; i < count; i++)
						component.addCard(card);
				}
			}
		}
	}

}
