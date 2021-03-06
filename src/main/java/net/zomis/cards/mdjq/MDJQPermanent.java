package net.zomis.cards.mdjq;

import net.zomis.cards.mdjq.MDJQRes.CardType;
import net.zomis.cards.mdjq.actions.PlayLandAction;
import net.zomis.cards.mdjq.actions.PlaySpellAction;
import net.zomis.cards.mdjq.phases.MDJQMainPhase;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.InvalidStackAction;
import net.zomis.cards.resources.ResourceMap;
import net.zomis.cards.resources.ResourceType;

public class MDJQPermanent extends Card<MDJQCardModel> implements MDJQObject {

	protected MDJQPermanent(MDJQCardModel model, MDJQZone initialZone, MDJQPlayer owner) {
		super(model);
		this.currentZone = initialZone;
		this.owner = owner;
		this.controller = owner;
	}

	// private MDJQPermanent attachedTo;
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
	private ResourceMap values; // TODO: MDJQPermanent ResourceMap: tapped, counters, current damage/remaining toughness, static abilities (flying etc.)
	// TODO: ActivatedAbilities ResourceMap can include mana costs (X also), tapping costs
	// TODO: Triggered-/Static -Effects can affect ResourceMaps: life (for players), P/T for creatures (perhaps don't even need to use strategies so much?) 
	private MDJQPlayer controller;
	private final MDJQPlayer owner;
	private boolean	tapped;
	private boolean summoningSickness = true;
	
	public MDJQPlayer getController() {
		return controller;
	}
	
	@Override
	public MDJQPlayer getOwner() {
		return owner;
	}
	
	public boolean hasSummoningSickness() {
		return summoningSickness;
	}
	public int getValue(ResourceType type) {
		return values.getResources(type);
	}
	public ResourceMap getValues() {
		return values;
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
		return "{Card: " + getController() + "-" + super.toString() + "}";
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
		this.summoningSickness = false;
		this.tapped = false;
	}
	
}
