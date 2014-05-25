package net.zomis.cards.wart;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.wart.sets.HStoneOption;

public class OptionAction extends StackAction {

	private HStoneOption option;
	private final Card<?> card;

	public OptionAction(Card<?> card, HStoneOption option) {
		this.option = option;
		this.card = card;
	}
	
	@Override
	public boolean actionIsAllowed() {
		HStoneGame game = (HStoneGame) card.getGame();
		return option.getEffect().hasAnyAvailableTargets(game.getTemporaryFor());
	}
	
	@Override
	protected void onPerform() {
		HStoneGame game = (HStoneGame) card.getGame();
		game.selectOrPerform(option.getEffect(), game.getTemporaryFor());
		game.getTemporaryZone().moveToBottomOf(null);
	}

}
