package net.zomis.cards.cwars2;

import java.util.Map.Entry;

import net.zomis.cards.cwars2.CWars2Res.Producers;
import net.zomis.cards.cwars2.CWars2Res.Resources;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.actions.ZoneMoveAction;
import net.zomis.cards.util.IResource;
import net.zomis.cards.util.ResourceMap;

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
	public boolean actionIsAllowed() {
		CWars2Player player = (CWars2Player) getCard().getGame().getCurrentPlayer();
		if (!player.getHand().cardList().contains(getCard()))
			return setErrorMessage("Card is not in player's hand");
		if (player.getGame().getDiscarded() > 0)
			return setErrorMessage("Cards has been discarded");
		if (!model.checkAllowed())
			return setErrorMessage("Model does not allow it");
		
		if (!player.getResources().hasResources(model.getCosts())) {
			return setErrorMessage("Not enough resources: " + model.getCosts() + " existing: " + player.getResources());
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
			opp.getResources().changeResources(CWars2Res.WALL, -model.damage);
			int overflow = -opp.getResources().getResources(CWars2Res.WALL);
			if (overflow > 0) {
				opp.getResources().changeResources(CWars2Res.WALL, overflow);
				opp.getResources().changeResources(CWars2Res.CASTLE, -overflow);
			}
		}
		if (model.castleDamage > 0) {
			opp.getResources().changeResources(CWars2Res.CASTLE, -model.castleDamage);
		}
		
		this.clamp(player.getResources());
		this.clamp(opp.getResources());
		
		model.perform(game);
		
		super.onPerform();
		game.discarded();
		if (!game.nextPhase()) 
			throw new AssertionError("Game.nextPhase was not successful.");
		player.fillHand();
		setOKMessage("All OK");
	}

	private void clamp(ResourceMap resources) {
		ResourceMap res2 = new ResourceMap(resources);
		for (Entry<IResource, Integer> ee : res2.getValues()) {
			if (ee.getKey() instanceof Resources) {
				if (ee.getValue() <= Resources.MIN)
					resources.set(ee.getKey(), Resources.MIN);
			}
			if (ee.getKey() instanceof Producers) {
				if (ee.getValue() <= Producers.MIN)
					resources.set(ee.getKey(), Producers.MIN);
			}
		}
	}
	
}
