package net.zomis.cards.cwars2.cards;

import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Player;
import net.zomis.cards.cwars2.CWars2Res;
import net.zomis.cards.model.actions.PublicAction;
import net.zomis.cards.resources.ResourceData;
import net.zomis.cards.resources.ResourceListener;
import net.zomis.cards.resources.ResourceMap;

public class MagicAttackMultiply extends PublicAction {
	private final boolean	opponent;
	private final double	multiplier;
	private final CWars2Game	game;

	MagicAttackMultiply() { this(null, false, 0); }
	public MagicAttackMultiply(CWars2Game game, boolean opponent, double multiplier) {
		this.opponent = opponent;
		this.multiplier = multiplier;
		this.game = game;
	}
	
	@Override
	public void onPerform() {
		CWars2Player player = opponent ? game.getCurrentPlayer().getNextPlayer() : game.getCurrentPlayer();
		ResourceListener old = player.getResources().getListener(CWars2Res.CASTLE);
		double previousMult = 1;
		if (old instanceof MagicAttackListener) {
			MagicAttackListener previous = (MagicAttackListener) old;
			if (previous.multiplier == this.multiplier) // Don't allow Magic Weapon twice
				return;
			// opponent setting does not matter here really, neither does game. We just want the ResourceListener-part.
			previousMult = previous.multiplier;
		}
		MagicAttackListener listener = new MagicAttackListener(player, multiplier * previousMult);
		player.getResources().setListener(CWars2Res.CASTLE, listener);
		player.getResources().setListener(CWars2Res.WALL, listener);
	}
	
	public static class MagicAttackListener implements ResourceListener {
		private final CWars2Player	player;
		private final double	multiplier;
		MagicAttackListener() { this.player = null; this.multiplier = 1; }
		public MagicAttackListener(CWars2Player player, double multiplier) {
			this.player = player;
			this.multiplier = multiplier;
		}
		@Override
		public boolean onResourceChange(ResourceMap map, ResourceData type, int newValue) {
			if (this.player == player.getGame().getCurrentPlayer()) {
				// Self-inflicted damage should be performed.
				return true;
			}
			map.setListener(CWars2Res.CASTLE, null);
			map.setListener(CWars2Res.WALL, null);
			int change = (int)(newValue * multiplier);
			map.changeResources(type.getResource(), change);
			return false;
		}
	}
}
