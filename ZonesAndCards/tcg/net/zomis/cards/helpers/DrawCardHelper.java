package net.zomis.cards.helpers;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.DeckComponent;
import net.zomis.cards.components.HandComponent;
import net.zomis.cards.events2.DrawCardEvent;
import net.zomis.cards.iface.HasCompGame;
import net.zomis.cards.iface.HasComponents;
import net.zomis.custommap.view.ZomisLog;

public class DrawCardHelper {

	
	public static <T extends HasComponents & HasCompGame> void drawcard(T player) {
		player.compatibility(HandComponent.class).and(DeckComponent.class).required();
		
		HandComponent hand = player.getComponent(HandComponent.class);
		DeckComponent deck = player.getComponent(DeckComponent.class);
		
		ZomisLog.info("Draw a card");
		FirstCompGame game = player.getGame();
		DrawCardEvent event = new DrawCardEvent(player, deck.getDeck().getTopCard());
		
		game.executeEvent(event, new Runnable() {
			@Override
			public void run() {
				ZomisLog.info("Moving card, top is " + deck.getDeck().getTopCard());
				CardWithComponents card = deck.getDeck().getTopCard();
				card.zoneMoveOnBottom(hand.getHand());
			}
		});
	}

	public static void drawCards(CompPlayer player, int cards) {
		for (int i = 0; i < cards; i++) {
			drawcard(player);
		}
	}

}
