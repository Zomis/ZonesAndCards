package net.zomis.cards.model;


public class GamePhase {
	
	private final Player player;

	@Deprecated
	public GamePhase() {
		this(null);
	}
	
	public GamePhase(Player pl) {
		this.player = pl;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	@Override
	public String toString() {
		if (player == null)
			return "Phase-null";
		return "Phase-" + player.getName();
	}
	
	public void onStart(CardGame<?, ?> game) {
		
	}
	public void onEnd(CardGame<?, ?> game) {
		
	}
	
}
