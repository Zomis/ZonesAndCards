package net.zomis.cards.systems;

import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.DeckComponent;
import net.zomis.cards.components.DeckSourceComponent;
import net.zomis.cards.components.HealthComponent;
import net.zomis.cards.events2.DrawCardEvent;
import net.zomis.cards.iface.GameSystem;
import net.zomis.cards.resources.IResource;
import net.zomis.cards.resources.ResourceData;
import net.zomis.custommap.view.ZomisLog;
import net.zomis.events.EventExecutorGWT;

/**
 * <p>Functionality for taking damage when out of cards.</p>
 * <p>Requires: {@link DeckComponent} and {@link DeckSourceComponent}</p>
 * <p>Listens for {@link DrawCardEvent}</p>
 */
public class DamageIncreasingWhenOutOfCardsSystem implements GameSystem {

	private enum ConsecutiveDamage implements IResource {
		DAMAGE_TAKEN;

		@Override
		public ResourceData createData(IResource resource) {
			return ResourceData.forResource(resource);
		}
	}

	@Override
	public void onStart(FirstCompGame game) {
		game.registerHandler(DrawCardEvent.class, this::onDrawCard, EventExecutorGWT.PRE);
	}
	
	private void onDrawCard(DrawCardEvent event) {
		if (event.isCancelled())
			return;
		
		ZomisLog.info("DamageIncreasingWhenOutOfCardsSystem - DrawCard Event triggered");
		if (event.getPlayer().compatibility(DeckComponent.class).and(DeckSourceComponent.class).failsThenWarn()) {
			return;
		}
		
		DeckComponent comp = event.getPlayer().getComponent(DeckComponent.class);
		if (!comp.getDeck().isEmpty()) {
			return;
		}
		
		CompPlayer player = (CompPlayer) event.getPlayer();
		player.getResources().changeResources(ConsecutiveDamage.DAMAGE_TAKEN, 1);
		int value = player.getResources().get(ConsecutiveDamage.DAMAGE_TAKEN);
		ZomisLog.info("Out of cards: " + player + ", taking damage: " + value);
		
		player.getRequiredComponent(HealthComponent.class).damage(value);
		event.setCancelled(true);
	}

}
