package net.zomis.cards.cwars2;

import java.util.Map.Entry;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.actions.ZoneMoveAction;
import net.zomis.cards.util.IResource;

public class CWars2PlayAction extends ZoneMoveAction {

	private final CWars2Card	model;
	private final CWars2Player player;
	
	public CWars2PlayAction(Card card, CWars2Card model) {
		super(card);
		this.model = model;
		CWars2Game game = (CWars2Game) card.getGame();
		player = game.getCurrentPlayer();
		this.setDestination(game.getDiscard());
	}
	
	public CWars2Player getPlayer() {
		return player;
	}
	
	@Override
	public boolean isAllowed() {
		CWars2Player player = (CWars2Player) getCard().getGame().getCurrentPlayer();
		if (!player.getHand().cardList().contains(getCard()))
			return setErrorMessage("Card is not in player's hand");
		if (player.getGame().getDiscarded() > 0)
			return setErrorMessage("Cards has been discarded");
		if (!model.isAllowed())
			return setErrorMessage("Model does not allow it");
		
		for (Entry<IResource, Integer> cost : model.getCosts().getValues()) {
			if (!player.getResources().hasResources(cost.getKey(), cost.getValue())) {
				return setErrorMessage("Not enough resources: " + cost.getKey() + ": " + cost.getValue() + " existing: " + player.getResources());
			}
		}
		return true;
	}

	@Override
	protected void onPerform() {
		CWars2Player player = (CWars2Player) getCard().getGame().getCurrentPlayer();
		CWars2Game game = player.getGame();
		for (Entry<IResource, Integer> cost : model.getCosts().getValues()) {
			player.getResources().changeResources(cost.getKey(), -cost.getValue());
		}
		for (Entry<IResource, Integer> effect : model.effects.getValues()) {
			player.getResources().changeResources(effect.getKey(), effect.getValue());
		}
		
		CWars2Player opp = (CWars2Player) player.getOpponents().get(0);
		for (Entry<IResource, Integer> effect : model.opponentEffects.getValues()) {
			opp.getResources().changeResources(effect.getKey(), effect.getValue());
		}
		if (model.damage > 0) {
			opp.getResources().changeResources(CWars2Game.WALL, -model.damage);
			int overflow = -opp.getResources().getResources(CWars2Game.WALL);
			if (overflow > 0) {
				opp.getResources().changeResources(CWars2Game.WALL, overflow);
				opp.getResources().changeResources(CWars2Game.CASTLE, -overflow);
			}
		}
		if (model.castleDamage > 0) {
			opp.getResources().changeResources(CWars2Game.CASTLE, -model.castleDamage);
		}
		
		model.perform(game);
		
		super.onPerform();
		game.discarded();
		if (!game.nextPhase()) 
			throw new AssertionError("Game.nextPhase was not successful.");
		player.fillHand();
		setOKMessage("All OK");
	}
	
}
