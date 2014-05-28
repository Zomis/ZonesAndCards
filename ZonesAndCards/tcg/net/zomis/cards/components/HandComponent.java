package net.zomis.cards.components;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.CompCardModel;
import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.events2.GameStartedEvent;
import net.zomis.cards.model.CardZone;

public class HandComponent implements PlayerComponent {

	private final CardZone<CardWithComponents<CompCardModel>> hand;
	
	public HandComponent(CompPlayer player) {
		this.hand = new CardZone<>("Hand", player);
		this.hand.setKnown(player, true);
		player.getGame().addZone(hand);
//		player.getGame().registerHandler(GameStartedEvent.class, this::onGameStart);
	}
	
	private void onGameStart(GameStartedEvent event) {
		FirstCompGame game = (FirstCompGame) event.getGame();
		game.addZone(hand);
	}
	
	public CardZone<CardWithComponents<CompCardModel>> getHand() {
		return hand;
	}
	
}
