package net.zomis.cards.mdjq;

import java.util.LinkedList;
import java.util.List;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.cards.events.ZoneChangeEvent;
import net.zomis.cards.mdjq.MDJQRes.MColor;
import net.zomis.cards.mdjq.MDJQZone.ZoneType;
import net.zomis.cards.mdjq.cards.MCBlack;
import net.zomis.cards.mdjq.cards.MCWhite;
import net.zomis.cards.mdjq.phases.MDJQCleanupPhase;
import net.zomis.cards.mdjq.phases.MDJQDrawPhase;
import net.zomis.cards.mdjq.phases.MDJQMainPhase;
import net.zomis.cards.mdjq.phases.MDJQUntapPhase;
import net.zomis.cards.mdjq.phases.MDJQUpkeepPhase;
import net.zomis.cards.mdjq.scorers.CardNameScorer;
import net.zomis.cards.mdjq.scorers.IsColorScorer;
import net.zomis.cards.model.AIHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.InvalidStackAction;
import net.zomis.cards.swing.AfterActionEvent;
import net.zomis.custommap.CustomFacade;
import net.zomis.events.IEvent;

public class MDJQGame extends CardGame {
	
	
	private MDJQZone battlefield;
	private MDJQZone exile;
	
	public MDJQGame() {
		this.setRandomSeed(22);
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
			
			ScoreConfigFactory<MDJQPlayer, MDJQCardModel> factory = new ScoreConfigFactory<MDJQPlayer, MDJQCardModel>();
			
			if (i == 0) {
				factory.withScorer(new IsColorScorer(MColor.WHITE), 1000);
				factory.withScorer(new CardNameScorer("Plains"), 10);
			}
			else {
				factory.withScorer(new IsColorScorer(MColor.BLACK), 1000);
				factory.withScorer(new CardNameScorer("Swamp"), 10);
			}
			
//			factory.withScorer(new RandomScorer<MDJQPlayer, MDJQCardModel>());
//			factory.withScorer(new IsOfTypeScorer(MDJQRes.CardType.CREATURE));
			MDJQDeckBuilder db = new MDJQDeckBuilder(factory);
			db.createDeck(player, 12);
			
			player.getDeck().shuffle();
			
			for (int c = 0; c < 7; c++) {
				Card card = player.getLibrary().cardList().peekFirst();
				card.zoneMoveOnBottom(player.getHand());
			}
			
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
		
		// TODO: Make sure that all stack actions has targets (scan from bottom to top on the stack). If they do, THEN call super.processStack();
		
		MDJQStackAction action = (MDJQStackAction) this.getStack().pollFirst();
		if (action == null) 
			action = new MDJQStackAction(null);
		
		action.perform(); // Once it is on stack it should be performed. If it fizzles, then it fizzles inside perform().
		executeEvent(new AfterActionEvent(this, action));
		return action;
	}
	@Override
	public void executeEvent(IEvent ev) {
		if (ev instanceof ZoneChangeEvent) {
			ZoneChangeEvent eev = (ZoneChangeEvent) ev;
			ev = new MDJQZoneChangeEvent(eev.getCard(), eev.getFromCardZone(), eev.getToCardZone());
		}
		MDJQEvent event = (MDJQEvent) ev;
		
//		TODO: this.getBattlefield().onEvent(event);
		
		super.executeEvent(event);
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
	public AIHandler getAIHandler() {
		return new MDJQHandler();
	}
	
	@Override
	public void addStackAction(StackAction a) {
		CustomFacade.getLog().d("MDJQ Action: " + a);
		
		if (a instanceof InvalidStackAction)
			return;
		
		if (!(a instanceof MDJQStackAction))
			throw new IllegalArgumentException("Action is not an MDJQ Action: " + a);
		
		MDJQStackAction action = (MDJQStackAction) a;
		action.onAddToStack();
		
		if (!action.isUseStack()) {
			action.perform();
		}
		else super.addStackAction(action);
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

	public void addCard(MDJQCardModel card) {
		super.addCard(card);
	}

	@Override
	public void onStart() {
	}
	
/*
 * 
 * App on tablet is quite good designed (mostly)
 * Select creature to show activated abilities options
 * When targeting, click on the cards.
 * 
 * In MDJQ, use TargetSelectPhase 
 * 
 * 
 * 1. active player triggar ETB
 * 2. non-active    triggar ETB
 * Spelare f�r v�lja vilken ordning ETB-effekter l�ggs p� stacken
 * 
 * Gamestate �ndras, allt g�rs om
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
