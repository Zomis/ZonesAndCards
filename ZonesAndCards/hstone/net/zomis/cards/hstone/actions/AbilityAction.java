package net.zomis.cards.hstone.actions;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneRes;
import net.zomis.cards.model.StackAction;

public class AbilityAction extends StackAction {

	private final HStoneCard	source;

	public AbilityAction(HStoneCard source) {
		this.source = source;
	}
	
	@Override
	public boolean actionIsAllowed() {
		if (!source.getResources().hasResources(HStoneRes.ACTION_POINTS, 1))
			return setErrorMessage("No Action Points");
		if (!source.getPlayer().getResources().hasResources(HStoneRes.MANA_AVAILABLE, 2))
			return setErrorMessage("Not enough mana: " + source.getPlayer());
		return setOKMessage("All OK");
	}
	
	@Override
	protected void onPerform() {
		source.getGame().selectOrPerform(source.getModel().getEffect(), source);
	}
	
}
