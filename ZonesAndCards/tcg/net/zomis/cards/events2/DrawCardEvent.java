package net.zomis.cards.events2;

import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.events.CardEvent;
import net.zomis.cards.model.Card;
import net.zomis.events.IEvent;

public class DrawCardEvent extends CardEvent implements IEvent {

	private CompPlayer	player;

	public DrawCardEvent(CompPlayer player, Card<?> card) {
		super(card);
		this.player = player;
	}
	
	public CompPlayer getPlayer() {
		return player;
	}
}
