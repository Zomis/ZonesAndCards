package net.zomis.cards.systems;

import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.events.card.CardPlayedEvent;
import net.zomis.cards.iface.GameSystem;

/**
 * <p>Functionality for removing a card once it has been used. (Only cards that are owned by a player is removed)</p>
 * <p>Listens for {@link CardPlayedEvent}</p>
 */
public class ConsumeCardSystem implements GameSystem {

	@Override
	public void onStart(FirstCompGame game) {
		game.registerHandler(CardPlayedEvent.class, this::cardPlayed, 500);
	}
	
	public void cardPlayed(CardPlayedEvent event) {
		if (event.getCard().getOwner() != null)
			event.getCard().zoneMoveOnBottom(null);
	}

}
