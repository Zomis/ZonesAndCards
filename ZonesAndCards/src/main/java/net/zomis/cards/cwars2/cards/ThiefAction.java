package net.zomis.cards.cwars2.cards;

import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Player;
import net.zomis.cards.cwars2.CWars2Res.Resources;
import net.zomis.cards.model.actions.PublicAction;

public class ThiefAction extends PublicAction {

	private CWars2Game	game;
	private int	steal;

	public ThiefAction(CWars2Game game, int steal) {
		this.game = game;
		this.steal = steal;
	}
	
	@Override
	public void onPerform() {
		for (Resources res : Resources.values()) {
			CWars2Player next = (CWars2Player) game.getCurrentPlayer().getOpponents().get(0);
			int oldValue = next.getResources().getResources(res);
			int take = Math.min(oldValue, steal);
			next.getResources().changeResources(res, -take);
			int newValue = next.getResources().getResources(res);
			game.getCurrentPlayer().getResources().changeResources(res, -(newValue - oldValue));
		}
	}
	
}
