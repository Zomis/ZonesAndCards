package net.zomis.cards.hstone;

import net.zomis.cards.hstone.sets.HStoneOption;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.StackAction;

public class OptionAction extends StackAction {

	private HStoneOption option;
	private final Card<?> card;

	public OptionAction(Card<?> card, HStoneOption option) {
		this.option = option;
		this.card = card;
	}
	
	@Override
	public boolean actionIsAllowed() {
		return true;
	}
	
	@Override
	protected void onPerform() {
		HStoneGame game = (HStoneGame) card.getGame();
		game.selectOrPerform(option.getEffect(), game.getTemporaryFor());
	}

}
