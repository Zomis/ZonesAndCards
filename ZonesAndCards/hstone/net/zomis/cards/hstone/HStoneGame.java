package net.zomis.cards.hstone;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.hstone.actions.AttackAction;
import net.zomis.cards.hstone.factory.Battlecry;
import net.zomis.cards.hstone.factory.HStoneCardFactory;
import net.zomis.cards.hstone.factory.HStoneCardModel;
import net.zomis.cards.hstone.factory.HStoneChar;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.hstone.factory.HStoneRarity;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.phases.GamePhase;

public class HStoneGame extends CardGame<HStonePlayer, HStoneCardModel> {

	private HSFilter targets;
	
	@Deprecated
	private HStoneCard targetsFor;
	
	public void setTargetFilter(HSFilter targets, HStoneCard forWhat) {
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
		}
		setActionHandler(new HStoneHandler());
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
				HStoneCardModel ring = HStoneCardFactory.spell(0, HStoneRarity.COMMON, "The Coin").effect(Battlecry.tempMana(1)).card();
				addCard(ring);
				getFirstPlayer().getNextPlayer().getHand().createCardOnBottom(ring);
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
		System.out.println("After fight: " + stackSize());
//		while (this.stackSize() > 0)
//			this.processStackAction();
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

	public void cleanup() {
		for (HStonePlayer player : this.getPlayers()) {
			if (player.getHealth() <= 0) {
				endGame();
			}
			
			for (HStoneCard card : player.getBattlefield()) {
				card.cleanup();
			}
			for (HStoneCard card : player.getSpecialZone()) {
				card.cleanup();
			}
		}
	}

	public HStoneCardModel getCardModel(String minion) {
		for (CardModel model : getAvailableCards()) {
			if (model.getName().equals(minion))
				return (HStoneCardModel) model;
		}
		return null;
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

	
}
