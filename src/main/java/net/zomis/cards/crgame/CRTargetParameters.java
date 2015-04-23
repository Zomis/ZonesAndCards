package net.zomis.cards.crgame;

public class CRTargetParameters {

	private final CRCard	source;
	private final CRFilter	targets;
	private final CREffect	effect;

	public CRTargetParameters(CRCard card, CRFilter targets, CREffect effect) {
		this.source = card;
		this.targets = targets;
		this.effect = effect;
	}

	public static CRTargetParameters create(CRCard card, CRFilter targets, CREffect effect) {
		return new CRTargetParameters(card, targets, effect);
	}
	
	public CRCard getCard() {
		return source;
	}
	
	public CREffect getEffect() {
		return effect;
	}
	
	public CRFilter getTargets() {
		return targets;
	}

	public void apply(CRCard target) {
		effect.apply(source, target);
	}

	public boolean isValidTarget(CRCard target) {
		return targets.test(source, target);
	}

}
