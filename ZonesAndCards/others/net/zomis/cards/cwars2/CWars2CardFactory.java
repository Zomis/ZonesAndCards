package net.zomis.cards.cwars2;

import java.util.List;

import net.zomis.cards.model.StackAction;
import net.zomis.cards.util.ResourceType;

public class CWars2CardFactory {
	
	private CWars2Card	card;

	public CWars2CardFactory(String name) {
		this.card = new CWars2Card(name);
	}
	public CWars2CardFactory setDamage(int i) {
		card.damage = i;
		return this;
	}
	public CWars2CardFactory setMyEffect(ResourceType type, int i) {
		card.effects.set(type, i);
		return this;
	}
	public CWars2CardFactory setOppEffect(ResourceType type, int i) {
		card.opponentEffects.set(type, i);
		return this;
	}
	public CWars2CardFactory setCastleDamage(int i) {
		card.castleDamage = i;
		return this;
	}

	public CWars2Card getCard() {
		return card;
	}

	public void addTo(List<CWars2Card> cards) {
		cards.add(card);
	}
	public CWars2CardFactory setResourceCost(ResourceType type, int i) {
		this.card.costs.set(type, i);
		return this;
	}
	public CWars2CardFactory addAction(StackAction action) {
		this.card.addAction(action);
		return this;
	}
	
}
