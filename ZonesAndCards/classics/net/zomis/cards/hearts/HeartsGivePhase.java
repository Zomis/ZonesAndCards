package net.zomis.cards.hearts;

import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.GamePhase;

public class HeartsGivePhase extends GamePhase {

	private final HeartsGiveDirection	giveDirection;

	public HeartsGivePhase(HeartsGiveDirection giveDirection) {
		this.giveDirection = giveDirection;
	}
	
	@Override
	public void onStart(CardGame<?, ?> game) {
		HeartsGame gm = (HeartsGame) game;
		for (CardPlayer pl : gm.getPlayers()) {
			CardPlayer player = (CardPlayer) pl;
			gm.sort(player.getHand());
		}
	}
	
	@Override
	public void onEnd(CardGame<?, ?> game) {
		HeartsGame gm = (HeartsGame) game;
		if (this.giveDirection.isGive()) {
			for (CardPlayer player : gm.getPlayers()) {
				// Check if all players have placed 3 cards on their board.
				CardPlayer pl =  (CardPlayer) player;
				if (pl.getBoard().size() != HeartsGiveAction.GIVE_COUNT) {
					return;
				}
			}
		}
		
		for (CardPlayer player : gm.getPlayers()) {
			// Move the cards in the direction
			CardPlayer pl = (CardPlayer) player;
			CardPlayer dest = this.giveDirection.getGiveToPlayer(pl);
			pl.getBoard().moveToBottomOf(dest.getHand());
		}
		
		// Find 2 of clubs, set active phase to that player.
		gm.scanForStartingCard();
	}

}
