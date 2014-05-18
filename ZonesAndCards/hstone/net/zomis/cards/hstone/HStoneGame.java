package net.zomis.cards.hstone;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.hstone.actions.AttackAction;
import net.zomis.cards.hstone.ench.HStoneEnchForward;
import net.zomis.cards.hstone.ench.HStoneEnchFromModel;
import net.zomis.cards.hstone.ench.HStoneEnchantment;
import net.zomis.cards.hstone.events.HStoneCardPlayedEvent;
import net.zomis.cards.hstone.events.HStoneTurnEndEvent;
import net.zomis.cards.hstone.events.HStoneTurnStartEvent;
import net.zomis.cards.hstone.factory.CardType;
import net.zomis.cards.hstone.factory.HStoneCardModel;
import net.zomis.cards.hstone.factory.HStoneChar;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.model.ActionProvider;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.NextTurnAction;
import net.zomis.cards.model.phases.GamePhase;
import net.zomis.events.IEvent;

public class HStoneGame extends CardGame<HStonePlayer, HStoneCardModel> {
	private HStoneEffect targets;
	private HStoneCard targetsFor;
	
	private final List<HStoneEnchantment> enchantments;
	
	private int turnNumber;
	
	public void setTargetFilter(HStoneEffect targets, HStoneCard forWhat) {
		this.targets = targets;
		this.targetsFor = forWhat;
	}
	
	public HStoneCard getTargetsFor() {
		return targetsFor;
	}
	
	public boolean isTargetSelectionMode() {
		return targets != null;
	}
	
	public HStoneGame(HStoneChar playerA, HStoneChar playerB) {
		enchantments = new ArrayList<HStoneEnchantment>();
		addEnchantment(new HStoneEnchFromModel());
		
		new HStoneCards().addCards(this);
		
		HStonePlayer pl1 = new HStonePlayer(this, playerA);
		HStonePlayer pl2 = new HStonePlayer(this, playerB);
		addPlayer(pl1);
		addPlayer(pl2);
		addPhase(new HStonePhase(pl1));
		addPhase(new HStonePhase(pl2));
		
		for (Player pl : this.getPlayers()) {
			HStonePlayer player = (HStonePlayer) pl;
			addZone(player.getDeck());
			addZone(player.getHand());
			addZone(player.getBattlefield());
			addZone(player.getSpecialZone());
			addZone(player.getDiscard());
		}
		setActionHandler(new HStoneHandler());
		addAction(new HStoneCardModel("End Turn", 0, CardType.POWER), new ActionProvider() {
			@Override
			public StackAction get() {
				return new NextTurnAction(HStoneGame.this);
			}
		});
	}
	
	public void addEnchantment(HStoneEnchantment enchantment) {
		enchantments.add(enchantment);
	}

	public HStonePlayer getFirstPlayer() {
		return (HStonePlayer) this.getPlayers().get(0);
	}
	
	@Override
	protected void onStart() {
		for (Player player : this.getPlayers()) {
			HStonePlayer pl = (HStonePlayer) player;
			pl.onStart();
		}
		
		getFirstPlayer().drawCards(3);
		getFirstPlayer().getNextPlayer().drawCards(4);
		
		GamePhase phase = new GamePhase() { // Empty phase for exchanging some starting cards
			public void onEnd(CardGame<?, ?> game) {
				setActivePhaseDirectly(getPhases().get(0));
				HStoneCardModel coin = getCardModel("The Coin");
				if (coin == null)
					throw new NullPointerException("The Coin not found");
				getFirstPlayer().getNextPlayer().getHand().createCardOnBottom(coin);
			}
		};
		this.setActivePhase(phase);
	}
	
	@Override
	public HStonePlayer getCurrentPlayer() {
		return (HStonePlayer) super.getCurrentPlayer();
	}
	
	public void onPlayerHealthChange(HStonePlayer player, int newHealth) {
		if (newHealth <= 0)
			this.endGame();
	}

	public boolean addAndProcessFight(HStoneCard source, HStoneCard target) {
		AttackAction attackAction = new AttackAction(this, source, target);
		this.addAndProcessStackAction(attackAction);
		return attackAction.actionIsPerformed();
	}

	public boolean isTargetAllowed(HStoneCard card) {
		if (this.targets == null)
			return false;
		return this.targets.shouldKeep(this.targetsFor, card);
	}

	public void selectOrPerform(HStoneEffect effect, HStoneCard card) {
		if (effect == null)
			throw new IllegalArgumentException("Effect is null. Card is " + card);
		setTargetFilter(effect.needsTarget() ? effect : null, card);
		if (effect != null && !isTargetSelectionMode()) {
			effect.performEffect(card, null);
		}
	}

	@Override
	public boolean click(Card<?> card) {
		boolean result = super.click(card);
		if (result)
			this.cleanup();
		return result;
	}
	
	public void cleanup() {
		this.cleanup(new ArrayList<IEvent>());
	}
	
	public void cleanup(List<IEvent> events) {
		if (isGameOver())
			return;
		
		while (stackSize() > 0)
			this.processStackAction();
		
		for (HStonePlayer player : this.getPlayers()) {
			if (player.getHealth() <= 0) {
				endGame();
			}
			
			for (HStoneCard card : player.getBattlefield()) {
				events.addAll(card.cleanup());
			}
			for (HStoneCard card : player.getSpecialZone()) {
				events.addAll(card.cleanup());
			}
		}
		for (IEvent evt : events)
			this.executeEvent(evt);
		
		if (!events.isEmpty()) // if one or more events was processed, cleanup again with no events to be scheduled
			cleanup();
	}

	public List<HStoneCard> findAll(HStoneCard searcher, HSFilter filter) {
		List<HStoneCard> results = new ArrayList<HStoneCard>();
		for (HStonePlayer player : getPlayers()) {
			if (filter.shouldKeep(searcher, player.getPlayerCard()))
				results.add(player.getPlayerCard());
			
			for (HStoneCard card : player.getBattlefield()) {
				if (filter.shouldKeep(searcher, card))
					results.add(card);
			}
		}
		
		return results;
	}

	void increaseTurnCounter() {
		this.turnNumber++;
	}

	public int getTurnNumber() {
		return turnNumber;
	}

	void executeTurnStartEvent() {
		executeEvent(new HStoneTurnStartEvent(getCurrentPlayer()));
	}

	void executeTurnEndEvent() {
		executeEvent(new HStoneTurnEndEvent(getCurrentPlayer()));
	}

	public HStoneEffect getTargetsForEffect() {
		return this.targets;
	}
	
	public int getResources(HStoneCard card, HStoneRes resource) {
		Integer result = null;
		for (HStoneEnchantment ench : this.enchantments) {
			if (!ench.isActive())
				continue;
			if (!ench.appliesTo(card))
				continue;
			result = ench.getResource(card, resource, result);
		}
		if (result == null)
			throw new NullPointerException("No enchantment affected " + resource + " for " + card);
		return result;
	}

	public void callEvent(HStoneCardPlayedEvent hStoneCardPlayedEvent) {
		this.executeEvent(hStoneCardPlayedEvent);
	}

	public List<HStoneEnchantment> getEnchantments() {
		return new ArrayList<HStoneEnchantment>(enchantments);
	}

	public void addEnchantmentAfter(HStoneEnchForward newEnchantment, HStoneEnchantment addAfter) {
		int index = this.enchantments.indexOf(addAfter);
		this.enchantments.add(index, newEnchantment);
	}

	public HStonePlayer getOpponent() {
		return getCurrentPlayer().getNextPlayer();
	}
	
}
