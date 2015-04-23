package net.zomis.cards.classics;

import net.zomis.cards.hearts.HeartsGame.GetZoneInterface;
import net.zomis.cards.interfaces.HandPlayer;
import net.zomis.cards.model.Player;

public class CardPlayer extends Player implements HandPlayer {

	public static class GetHand implements GetZoneInterface<CardPlayer> {
		@SuppressWarnings("unchecked")
		@Override
		public ClassicCardZone getZone(CardPlayer object) {
			return object.getHand();
		}
	}
	
	public static class GetBoard implements GetZoneInterface<CardPlayer> {
		@SuppressWarnings("unchecked")
		@Override
		public ClassicCardZone getZone(CardPlayer object) {
			return object.getBoard();
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
