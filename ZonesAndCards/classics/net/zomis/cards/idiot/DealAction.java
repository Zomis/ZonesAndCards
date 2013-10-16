package net.zomis.cards.idiot;

import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.model.StackAction;

public class DealAction extends StackAction {

	private final IdiotGame	game;

	public DealAction(IdiotGame game) {
		this.game = game;
	}
	
	@Override
	public boolean isAllowed() {
		return !game.getDeck().cardList().isEmpty();
	}
	
	@Override
	protected void perform() {
		for (ClassicCardZone zone : game.getIdiotZones()) {
			if (!game.getDeck().isEmpty()) {
				game.getDeck().getTopCard().zoneMoveOnBottom(zone);
			}
		}
	}
	
}
