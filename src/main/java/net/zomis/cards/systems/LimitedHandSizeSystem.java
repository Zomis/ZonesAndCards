package net.zomis.cards.systems;

import java.util.function.Consumer;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.DeckComponent;
import net.zomis.cards.components.HandComponent;
import net.zomis.cards.events2.DrawCardEvent;
import net.zomis.cards.iface.GameSystem;
import net.zomis.cards.iface.HasComponents;
import net.zomis.events.EventExecutorGWT;

public class LimitedHandSizeSystem implements GameSystem {

	private final Consumer<CardWithComponents> onFullHand;
	private final int maxSize;

	public LimitedHandSizeSystem(int size, Consumer<CardWithComponents> onFullHand) {
		this.maxSize = size;
		this.onFullHand = onFullHand;
	}
	
	@Override
	public void onStart(FirstCompGame game) {
		game.registerHandler(DrawCardEvent.class, this::onDrawCard, EventExecutorGWT.PRE);
	}

	private void onDrawCard(DrawCardEvent event) {
		if (event.isCancelled())
			return;
		
		HasComponents player = event.getPlayer();
		DeckComponent deck = player.getRequiredComponent(DeckComponent.class);
		HandComponent hand = player.getRequiredComponent(HandComponent.class);

		if (hand.getHand().size() >= maxSize) {
			onFullHand.accept(deck.getDeck().getTopCard());
			event.setCancelled(true);
		}
	}
}
