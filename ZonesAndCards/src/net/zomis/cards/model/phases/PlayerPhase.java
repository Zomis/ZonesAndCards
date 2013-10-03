package net.zomis.cards.model.phases;

import net.zomis.cards.model.Player;


public class PlayerPhase extends GamePhase implements IPlayerPhase {

	private final Player player;

	public PlayerPhase(Player pl) {
		this.player = pl;
	}
	
	@Override
	public Player getPlayer() {
		return player;
	}
	
	@Override
	public String toString() {
		return "Phase-" + player;
	}

}
