package net.zomis.cards.hstone;

import net.zomis.cards.hstone.actions.AttackAction;
import net.zomis.cards.hstone.factory.Battlecry;
import net.zomis.cards.hstone.factory.HStoneCardFactory;
import net.zomis.cards.hstone.factory.HStoneChar;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.hstone.factory.HStoneRarity;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.phases.GamePhase;
import net.zomis.utils.ZomisList.FilterInterface;

public class HStoneGame extends CardGame {

	private FilterInterface<HStoneTarget> targets;
	private HStoneTarget targetsFor;
	
	public void setTargetFilter(FilterInterface<HStoneTarget> targets, HStoneTarget forWhat) {
		this.targets = targets;
		this.targetsFor = forWhat;
	}
	
	public HStoneTarget getTargetsFor() {
		return targetsFor;
	}
	
	public boolean isTargetSelectionMode() {
		return targets != null;
	}
	
	public HStoneGame(HStoneChar playerA, HStoneChar playerB) {
		HStoneCards.neutralSmall(this);
		
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
			public void onEnd(CardGame game) {
				setActivePhaseDirectly(getPhases().get(0));
				CardModel ring = HStoneCardFactory.spell(0, HStoneRarity.COMMON, "The Coin").effect(Battlecry.tempMana(1)).card();
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

	public boolean addAndProcessFight(HStoneTarget source, HStoneTarget target) {
		AttackAction attackAction = new AttackAction(this, source, target);
		this.addAndProcessStackAction(attackAction);
		System.out.println("After fight: " + stackSize());
//		while (this.stackSize() > 0)
//			this.processStackAction();
		return attackAction.actionIsPerformed();
	}

	public boolean isTargetAllowed(HStoneTarget card) {
		if (this.targets == null)
			return false;
		return this.targets.shouldKeep(card);
	}

	public void selectOrPerform(HStoneEffect effect, HStoneCard card) {
		setTargetFilter(effect.needsTarget() ? effect : null, card);
		if (effect != null && !isTargetSelectionMode()) {
			effect.performEffect(card, null);
		}
	}

	public void cleanup() {
		for (Player pl : this.getPlayers()) {
			HStonePlayer player = (HStonePlayer) pl;
			if (player.getHealth() <= 0) {
				endGame();
			}
			
			for (Card card : player.getBattlefield().cardList()) {
				HStoneCard hsc = (HStoneCard) card;
				hsc.cleanup();
			}
		}
	}
	
}
