package net.zomis.cards.crgame;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.actions.NextTurnAction;
import net.zomis.cards.model.phases.PlayerPhase;
import net.zomis.cards.util.DeckBuilder;

public class CRCardGame extends CardGame<CRPlayer, CRCardModel> {

	private static final int START_CARDS	= 6;
	private static final int QUALITY_TARGET	= 100;
	private CRTargetParameters	targetParameters;

	public CRCardGame() {
		CRPlayerFactory playerFactory = new CRPlayerFactory();
		CRCards.createCards(this);
		this.addPlayer(playerFactory.newPlayerCodeReview(this));
		this.addPlayer(playerFactory.newPlayerPCG(this));
		for (CRPlayer player : getPlayers()) {
			this.addPhase(new PlayerPhase(player));
			this.addZone(player.getHand());
			this.addZone(player.getBattlefield());
			this.addZone(player.getDeck());
			DeckBuilder.createExact(player, player.getCards().getCount(this));
			player.getDeck().shuffle();
		}
		this.setActivePhase(getPhases().get(0));
		this.getCurrentPlayer().drawCards(START_CARDS - 1);
		this.getPlayers().get(1).drawCards(START_CARDS);
		this.setActionHandler(new CRHandler());
		getCurrentPlayer().newTurn();
		addAction(new CRCardModel("End Turn", CRCardType.SPELL, 0), () -> new NextTurnAction(this));
	}
	
	@Override
	public boolean isNextPhaseAllowed() {
		if (this.isTargetSelectionMode())
			return false;
		return super.isNextPhaseAllowed();
	}
	
	@Override
	public boolean nextPhase() {
		if (isGameOver())
			return false;
		
		boolean result = super.nextPhase();
		getCurrentPlayer().newTurn();
		for (CRPlayer player : getPlayers()) {
			int quality = player.getResources().getResources(CRRes.QUALITY);
			if (quality >= QUALITY_TARGET) {
				endGame();
				break;
			}
			else if (quality <= 0) {
				endGame();
				break;
			}
		}
		return result;
	}
	
	public List<CRCard> findAll(CRCard source, CRFilter filter) {
		List<CRCard> results = new ArrayList<CRCard>();
		for (CRPlayer player : getPlayers()) {
			for (CRCard card : player.getBattlefield()) {
				if (filter.test(source, card))
					results.add(card);
			}
		}
		return results;
	}

	public void setTargets(CRCard card, CRFilter targets, CREffect effect) {
		this.targetParameters = CRTargetParameters.create(card, targets, effect);
	}
	
	public void clearTargets() {
		this.targetParameters = null;
	}
	
	public CRTargetParameters getTargetParameters() {
		if (!isTargetSelectionMode())
			throw new NullPointerException("Not in target selection mode.");
		return targetParameters;
	}

	public boolean isTargetSelectionMode() {
		return targetParameters != null;
	}

}
