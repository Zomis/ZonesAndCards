package net.zomis.cards.systems;

import net.zomis.cards.cbased.CompCardModel;
import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.DeckSourceComponent;
import net.zomis.cards.components.ZoneComponent;
import net.zomis.custommap.view.ZomisLog;

public class DeckFromEachCardSystem implements GameSystem {

	private final int count;
	private final Class<? extends ZoneComponent> toComponent;

	public DeckFromEachCardSystem(int count, Class<? extends ZoneComponent> toComponent) {
		this.count = count;
		this.toComponent = toComponent;
	}
	
	@Override
	public void onStart(FirstCompGame game) {
		ZomisLog.info(this.toString());
		for (CompPlayer pl : game.getPlayers()) {
			if (toComponent != null) {
				pl.compatibility(this.toComponent).required();
				ZomisLog.info("Creating deck for " + pl + " to " + toComponent);
			
				ZoneComponent component = pl.getComponent(toComponent);
				for (CompCardModel card : game.getCards().values()) {
					for (int i = 0; i < count; i++)
						component.getZone().createCardOnBottom(card);
				}
			}
			else {
				if (pl.compatibility(DeckSourceComponent.class).failsThenWarn())
					return;
				ZomisLog.info("Creating deck for " + pl);
			
				DeckSourceComponent component = pl.getComponent(DeckSourceComponent.class);
				for (CompCardModel card : game.getCards().values()) {
					for (int i = 0; i < count; i++)
						component.addCard(card);
				}
			}
		}
	}

}
