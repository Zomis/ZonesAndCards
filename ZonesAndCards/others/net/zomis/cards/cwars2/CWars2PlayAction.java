package net.zomis.cards.cwars2;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.actions.ZoneMoveAction;

public class CWars2PlayAction extends ZoneMoveAction {

	private final CWars2Player player;
	private final CWars2Card	model;
	
	public CWars2PlayAction(Card<CWars2Card> card) {
		super(card);
		this.model = card.getModel();
		CWars2Game game = (CWars2Game) card.getGame();
		player = game.getCurrentPlayer();
		this.setDestination(player.getDiscard());
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
		return setOKMessage("Action is Allowed");
	}

	@Override
	protected void onPerform() {
		CWars2Player player = (CWars2Player) getCard().getGame().getCurrentPlayer();
		CWars2Game game = player.getGame();
		player.getResources().change(model.getCosts(), -1);
		player.getResources().change(model.effects, 1);
		
		CWars2Player opp = (CWars2Player) player.getOpponents().get(0);
		opp.getResources().change(model.opponentEffects, 1);
		
//		if (model.damage > 0) {
//			opp.getResources().changeResources(CWars2Res.WALL, -model.damage);
//		}
		
		int overflow = -opp.getResources().getResources(CWars2Res.WALL);
		if (overflow > 0) {
			opp.getResources().changeResources(CWars2Res.WALL, overflow); // TODO: This should NOT be animated. And it should not be handled with a pre & post event
			opp.getResources().changeResources(CWars2Res.CASTLE, -overflow);
		}
		
//		if (model.castleDamage > 0) {
//			opp.getResources().changeResources(CWars2Res.CASTLE, -model.castleDamage);
//		}
		
		player.getResources().clamp();
		opp.getResources().clamp();
		
		model.perform(game);
		
		super.onPerform();
		game.discarded();
		if (!game.nextPhase()) 
			throw new AssertionError("Game.nextPhase was not successful.");
		player.fillHand();
		setOKMessage("All OK");
	}

	@Override
	public String toString() {
		return this.player.getName() + " plays " + this.getCard().getModel().getName();
	}
	
}
