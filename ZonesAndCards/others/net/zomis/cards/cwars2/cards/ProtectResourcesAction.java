package net.zomis.cards.cwars2.cards;

import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Player;
import net.zomis.cards.cwars2.CWars2Res.Resources;
import net.zomis.cards.model.actions.PublicAction;
import net.zomis.cards.resources.ResourceData;
import net.zomis.cards.resources.ResourceListener;
import net.zomis.cards.resources.ResourceMap;

public class ProtectResourcesAction extends PublicAction {

	private final CWars2Game game;

	public ProtectResourcesAction(CWars2Game game) {
		this.game = game;
	}
	@Override
	public void onPerform() {
		final CWars2Player protectedPlayer = game.getCurrentPlayer();
		final ResourceListener listener = new ResListener(protectedPlayer);
		for (Resources type : Resources.values())
			game.getCurrentPlayer().getResources().setListener(type, listener);
	}
	
	public static class ResListener implements ResourceListener {
		private final CWars2Player	protectedPlayer;
		ResListener() {this(null); }
		public ResListener(CWars2Player protectedPlayer) {
			this.protectedPlayer = protectedPlayer;
		}
		@Override
		public boolean onResourceChange(ResourceMap map, ResourceData type, int newValue) {
			if (newValue > 0) return true;
			if (protectedPlayer.getGame().getCurrentPlayer() == protectedPlayer) return true;
			return false;
		}
	}
	
}
