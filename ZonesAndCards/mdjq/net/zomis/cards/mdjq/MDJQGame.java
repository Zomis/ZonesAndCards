package net.zomis.cards.mdjq;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.cards.events.card.ZoneChangeEvent;
import net.zomis.cards.events.game.PhaseChangeEvent;
import net.zomis.cards.mdjq.MDJQRes.MColor;
import net.zomis.cards.mdjq.MDJQZone.ZoneType;
import net.zomis.cards.mdjq.actions.MDJQTargetAction;
import net.zomis.cards.mdjq.cards.MCBlack;
import net.zomis.cards.mdjq.cards.MCWhite;
import net.zomis.cards.mdjq.events.MDJQEvent;
import net.zomis.cards.mdjq.events.MDJQPhaseChangeEvent;
import net.zomis.cards.mdjq.events.MDJQZoneChangeEvent;
import net.zomis.cards.mdjq.phases.MDJQCleanupPhase;
import net.zomis.cards.mdjq.phases.MDJQDrawPhase;
import net.zomis.cards.mdjq.phases.MDJQMainPhase;
import net.zomis.cards.mdjq.phases.MDJQUntapPhase;
import net.zomis.cards.mdjq.phases.MDJQUpkeepPhase;
import net.zomis.cards.mdjq.scorers.CardNameScorer;
import net.zomis.cards.mdjq.scorers.IsColorScorer;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.GamePhase;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.InvalidStackAction;
import net.zomis.custommap.CustomFacade;
import net.zomis.events.IEvent;

public class MDJQGame extends CardGame<MDJQPlayer, MDJQCardModel> {
	
	
	private MDJQZone battlefield;
	private MDJQZone exile;
	
	public MDJQGame() {
		this.setActionHandler(new MDJQHandler());
		this.battlefield = new MDJQZone("Battlefield", ZoneType.BATTLEFIELD, null);
		this.battlefield.setGloballyKnown(true);
		this.exile = new MDJQZone("Exile", ZoneType.EXILE, null);
		this.addZone(battlefield);
		this.addZone(exile);
		
		MDJQPhase first = null;
		new MCWhite().addCards(this);
		new MCBlack().addCards(this);
		for (int i = 0; i < 2; i++) {
			
			MDJQPlayer player = new MDJQPlayer("Player" + i);
			this.addZone(player.getGraveyard());
			this.addZone(player.getHand());
			this.addZone(player.getLibrary());
			
			List<MDJQPhase> phases = new LinkedList<MDJQPhase>();
			phases.add(new MDJQUntapPhase(player));
			phases.add(new MDJQUpkeepPhase(player));
			phases.add(new MDJQDrawPhase(player));
			MDJQMainPhase mainPhase = new MDJQMainPhase(player);
			phases.add(mainPhase);
			if (first == null)
				first = mainPhase;
			
//			phases.add(new MDJQCombatBeginningPhase(player));
//			phases.add(new MDJQCombatDeclareAttackersPhase(player));
//			phases.add(new MDJQCombatDeclareBlockersPhase(player));
//			phases.add(new MDJQCombatDamagePhase(player));
//			phases.add(new MDJQCombatEndPhase(player));
			
			phases.add(new MDJQMainPhase(player));
			phases.add(new MDJQCleanupPhase(player));
			
			for (MDJQPhase phase : phases)
				this.addPhase(phase);
			this.addPlayer(player);
			
		}
		setActivePhase(first);
	}
	
	
	@Override
	public void addAndProcessStackAction(StackAction action) {
		this.addStackAction(action);
	}
	@Override
	public StackAction processStackAction() {
		if (!this.isStarted())
			throw new IllegalStateException("Game is not started. Did you forget to call startGame() ?");
		
		if (getActivePhase() instanceof MDJQSelectTargetsPhase) {
			// Mostly because of calling onEnd on the phase and going back to the phase before
			super.nextPhase();
		}
		for (StackAction act : this.getStack()) {
			if (act instanceof MDJQTargetAction) {
				MDJQTargetAction action = (MDJQTargetAction) act;
				if (!action.getTargets().isAllChosen()) {
					CustomFacade.getLog().i("Need to specify targets for " + action);
					this.setActivePhase(new MDJQSelectTargetsPhase(action.getTargetChooser()));
					return new InvalidStackAction("Need to specify targets.");
				}
			}
		}

		return super.processStackAction();
//		MDJQStackAction action = (MDJQStackAction) this.getStack().pollFirst();
//		if (action == null) 
//			action = new MDJQStackAction(null);
//		
//		action.onPerform(); // Once it is on stack it should be performed. If it fizzles, then it fizzles inside perform().
//		executeEvent(new AfterActionEvent(this, action));
//		return action;
	}
	@Override
	protected <T extends IEvent> T executeEvent(T evnt) {
//		CustomFacade.getLog().d("Execute event: " + ev);
		IEvent ev = evnt;
		if (ev instanceof ZoneChangeEvent) {
			ZoneChangeEvent eev = (ZoneChangeEvent) ev;
			ev = new MDJQZoneChangeEvent((MDJQPermanent) eev.getCard(), (MDJQZone) eev.getFromCardZone(), (MDJQZone) eev.getToCardZone());
		}
		if (ev instanceof PhaseChangeEvent) {
			ev = new MDJQPhaseChangeEvent((PhaseChangeEvent) ev);
		}
		if (!(ev instanceof MDJQEvent)) {
			super.executeEvent(ev);
			return null;
		}
		MDJQEvent event = (MDJQEvent) ev;
		this.getBattlefield().trigger(event);
		for (Player pl : this.getPlayers()) {
			MDJQPlayer player = (MDJQPlayer) pl;
			player.getHand().trigger(event);
			player.getGraveyard().trigger(event);
		}
		
		super.executeEvent(event);
		return evnt;
	}
	
	public LinkedList<MDJQStackAction> getStackZone() {
		LinkedList<MDJQStackAction> act = new LinkedList<MDJQStackAction>();
		for (StackAction aa : super.getStack())
			act.add((MDJQStackAction) aa);
		return act;
	}
	
	public MDJQZone getBattlefield() {
		return battlefield;
	}
	public MDJQZone getExile() {
		return exile;
	}
	@Override
	public MDJQPlayer getCurrentPlayer() {
		return (MDJQPlayer) super.getCurrentPlayer();
	}
	
	@Override
	public void addStackAction(StackAction a) {
//		CustomFacade.getLog().d("Add to Stack: " + a);
		if (!(a instanceof MDJQStackAction)) {
			CustomFacade.getLog().w("Action is not an MDJQ Action: " + a);
			super.addStackAction(a);
		}
		else {
			MDJQStackAction action = (MDJQStackAction) a;
			action.onAddToStack();
			if (!action.isUseStack()) {
				action.onPerform();
			}
			else super.addStackAction(action);
		}
	}
	
	@Override
	public boolean nextPhase() {
		if (this.isEmptyStack())
			return super.nextPhase();
		else return false;
	}

	public boolean isEmptyStack() {
		return this.getStack().isEmpty();
	}

	@Override
	public void onStart() {
		for (Player pl : this.getPlayers())
			this.createDecks((MDJQPlayer) pl);
	}

	private void createDecks(MDJQPlayer player) {
		ScoreConfigFactory<MDJQPlayer, MDJQCardModel> factory = new ScoreConfigFactory<MDJQPlayer, MDJQCardModel>();
		int i = this.getPlayers().indexOf(player);
		if (i == 0) {
			factory.withScorer(new IsColorScorer(MColor.WHITE), 1000);
			factory.withScorer(new CardNameScorer("Plains"), 10);
		}
		else {
			factory.withScorer(new IsColorScorer(MColor.BLACK), 1000);
			factory.withScorer(new CardNameScorer("Swamp"), 10);
		}
		
//		factory.withScorer(new RandomScorer<MDJQPlayer, MDJQCardModel>());
//		factory.withScorer(new IsOfTypeScorer(MDJQRes.CardType.CREATURE));
		MDJQDeckBuilder db = new MDJQDeckBuilder(factory);
		db.createDeck(player, 12, new Random());
		
		player.getDeck().shuffle();
		
		for (int c = 0; c < 7; c++) {
			player.drawCard();
		}
	}
	
	@Override
	public MDJQPhase getActivePhase() {
		return (MDJQPhase) super.getActivePhase();
	}
	
	@Override
	protected void setActivePhase(GamePhase phase) {
		// There is another method in this package that calls this
		super.setActivePhase(phase);
	}
	
	public List<MDJQObject> getObjects() {
		List<MDJQObject> objects = new LinkedList<MDJQObject>();
		for (MDJQPlayer pl : this.getPlayers()) {
			objects.add(pl);
		}
		for (CardZone<?> z : this.getPublicZones()) {
			MDJQZone zone = (MDJQZone) z;
			for (MDJQPermanent card : zone) {
				objects.add(card);
			}
		}
		
		return objects;
	}
	
/*
 * 
 * App on tablet is quite good designed (mostly)
 * Select creature to show activated abilities options
 * When targeting, click on the cards.
 * Blocking: Drag from one card to another
 * 
 * In MDJQ, use TargetSelectPhase 
 * 
 * 
 * 1. active player triggers ETB
 * 2. non-active    triggers ETB
 * Players can choose in which order ETB-triggers are put on the stack.
 * 
 * When gamestate is changed, everything is done again.
 * 
 * Invisible state data:
 * - Unit attacks/turn (for some units)
 * - Played land this turn?
 * - When did it enter the battlefield (for some effects and the stack and stuff)
 * 
 * Missing phases:
 * - Sideboard
 * - Decide who should decide who should start
 * - Combat
 * 
 * Code design currently does not support:
 * - Options when playing something from hand (kickers, X, cycling...)
 * 
 */
}
