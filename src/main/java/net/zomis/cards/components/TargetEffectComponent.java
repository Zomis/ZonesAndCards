package net.zomis.cards.components;

import net.zomis.cards.iface.CardEffectTargets;
import net.zomis.cards.iface.Component;
import net.zomis.cards.iface.TargetOptions;
import net.zomis.cards.model.StackAction;

public class TargetEffectComponent extends StackAction implements Component {

	private final CardEffectTargets	effect;
	private final TargetOptions options;

	public TargetEffectComponent(TargetOptions options, CardEffectTargets effect) {
		this.effect = effect;
		this.options = options;
	}

	public CardEffectTargets getEffect() {
		return effect;
	}
	
	public TargetOptions getOptions() {
		return options;
	}
	
	@Override
	public String toString() {
		return effect.toString();
	}
	
	@Override
	public boolean actionIsAllowed() {
		// TODO Auto-generated method stub
		return super.actionIsAllowed();
	}
	
	@Override
	protected void onPerform() {
		// TODO Auto-generated method stub
		super.onPerform();
	}
}
