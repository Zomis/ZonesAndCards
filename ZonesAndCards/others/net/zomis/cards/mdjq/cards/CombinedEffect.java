package net.zomis.cards.mdjq.cards;

import net.zomis.cards.mdjq.MDJQEvent;
import net.zomis.cards.mdjq.MDJQStackAction;

public class CombinedEffect extends MDJQStackAction {

	private TriggeredEffect[] effects;
	private MDJQEvent	event;

	public CombinedEffect(MDJQEvent event, TriggeredEffect[] effect) {
		super(ActionType.TRIGGERED); // TODO: event.getType() ?
		this.event = event;
		this.effects = effect;
	}
	
	@Override
	public boolean isAllowed() {
		for (TriggeredEffect effect : effects) {
			if (effect instanceof HasTargets) {
				HasTargets eff = (HasTargets) effect;
				if (!eff.getTargets().isAllChosen())
					return false;
			}
		}
		return true;
	}
	
	@Override
	public void perform() {
		for (TriggeredEffect trig : effects) {
			trig.apply(event);
		}
	}
	
}
