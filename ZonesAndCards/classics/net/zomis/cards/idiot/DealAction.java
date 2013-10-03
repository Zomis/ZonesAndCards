package net.zomis.cards.idiot;

import java.util.LinkedList;

import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.model.Card;
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
			LinkedList<Card> list = game.getDeck().cardList();
			if (!list.isEmpty())
				list.getLast().zoneMove(zone, game.getCurrentPlayer());
		}
	}
	
}
