package net.zomis.cards.classics;

import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.HandPlayer;
import net.zomis.cards.model.CardZone.GetZoneInterface;
import net.zomis.cards.model.Player;

public class CardPlayer extends Player implements HandPlayer {

	public static class GetHand implements GetZoneInterface<Player> {
		@Override
		public CardZone getZone(Player object) {
			return ((CardPlayer)object).getHand();
		}
	}
	
	public static class GetBoard implements GetZoneInterface<Player> {
		@Override
		public CardZone getZone(Player object) {
			return ((CardPlayer)object).getBoard();
		}
	}

	private ClassicCardZone hand;
	private ClassicCardZone board;
	
	@Override
	public ClassicCardZone getHand() {
		return hand;
	}
	public void setHand(ClassicCardZone hand) {
		if (this.hand != null)
			throw new IllegalStateException("Hand is already set for player " + this);
		
		this.hand = hand;
	}
	
	public ClassicCardZone getBoard() {
		return board;
	}
	public void setBoard(ClassicCardZone board) {
		if (this.board != null)
			throw new IllegalStateException("Board is already set for player " + this);
		
		this.board = board;
	}
	
	@Override
	public ClassicGame getGame() {
		return (ClassicGame) super.getGame();
	}
	
}
