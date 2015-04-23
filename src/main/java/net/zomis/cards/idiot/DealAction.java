package net.zomis.cards.idiot;

import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.model.StackAction;

public class DealAction extends StackAction {

	private final IdiotGame	game;

	public DealAction(IdiotGame game) {
		this.game = game;
	}
	
	@Override
	public boolean actionIsAllowed() {
		return !game.getDeck().isEmpty();
	}
	
	@Override
	protected void onPerform() {
		for (ClassicCardZone zone : game.getIdiotZones()) {
			if (!game.getDeck().isEmpty()) {
				game.getDeck().getTopCard().zoneMoveOnBottom(zone);
			}
		}
	}
	
}
