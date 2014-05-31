package net.zomis.cards.cwars2;

import net.zomis.cards.model.actions.PublicAction;
import net.zomis.cards.resources.IResource;

public class CWars2CardFactory {
	
	private final CWars2Card card;

	public CWars2CardFactory(String name) {
		this.card = new CWars2Card(name);
	}
	public CWars2CardFactory setDamage(int i) {
		this.setOppEffect(CWars2Res.WALL, -i);
		return this;
	}
	public CWars2CardFactory setMyEffect(IResource type, int i) {
		card.effects.set(type, i);
		return this;
	}
	public CWars2CardFactory setOppEffect(IResource type, int i) {
		card.opponentEffects.set(type, i);
		return this;
	}
	public CWars2CardFactory setCastleDamage(int i) {
		this.setOppEffect(CWars2Res.CASTLE, -i);
		return this;
	}

	public void addTo(CWars2Game game) {
		game.addCard(card);
	}
	public CWars2CardFactory setResourceCost(IResource type, int i) {
		this.card.costs.set(type, i);
		return this;
	}
	public CWars2CardFactory addAction(PublicAction action) {
		this.card.addAction(action);
		return this;
	}
	
}
