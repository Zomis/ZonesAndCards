package net.zomis.cards.mdjq;

import net.zomis.cards.mdjq.MDJQRes.CardType;
import net.zomis.cards.mdjq.cards.PlayLandAction;
import net.zomis.cards.mdjq.cards.PlaySpellAction;
import net.zomis.cards.mdjq.phases.MDJQMainPhase;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.InvalidStackAction;
import net.zomis.cards.util.ResourceMap;

public class MDJQPermanent extends Card implements MDJQObject {

	protected MDJQPermanent(MDJQCardModel model, CardZone initialZone, MDJQPlayer owner) {
		super(model);
		this.currentZone = initialZone;
		this.owner = owner;
		this.controller = owner;
	}

//	private MDJQPermanent attachedTo;
	// private Abilities activatedAbilities; // Abilities on cards can change dynamically
	// private Abilities staticAbilities; // Not sure if this is needed.
	
/*
 * int getPower()
 * powerStrategy?
 * getStaticEffects(ResourceType.power).affect(this, Res.power)
 * game.addStaticEffect(this)
 * 
 * */	
//	private List<MDJQPermanent> attachments;
	private ResourceMap counters;
	private MDJQPlayer controller;
	private final MDJQPlayer owner;
	private boolean	tapped;
	
	public MDJQPlayer getController() {
		return controller;
	}
	public MDJQPlayer getOwner() {
		return owner;
	}
	
	public ResourceMap getCounters() {
		return counters;
	}
	
	@Override
	public MDJQCardModel getModel() {
		return (MDJQCardModel) super.getModel();
	}
	
	@Override
	public MDJQZone getCurrentZone() {
		return (MDJQZone) super.getCurrentZone();
	}
	
	@Override
	public MDJQGame getGame() {
		return (MDJQGame) super.getGame();
	}
	@Override
	public String toString() {
		return getController() + "-" + super.toString();
	}
	private static final StackAction invalid = new InvalidStackAction();
	public StackAction playFromHandAction() {
		if (this.getModel().isType(CardType.LAND)) {
			if (getController().isAllowLandPlay() && this.getGame().getActivePhase() instanceof MDJQMainPhase && this.getGame().isEmptyStack())
				return new PlayLandAction(this);
		}
		else {
			return new PlaySpellAction(this);
		}
		
		return invalid;
	}
	public boolean isTapped() {
		return this.tapped;
	}
	public void tap() {
		if (this.tapped)
			throw new IllegalStateException("Card is already tapped.");
		this.tapped = true;
	}
	public void untap() {
		this.tapped = false;
	}
	
}
