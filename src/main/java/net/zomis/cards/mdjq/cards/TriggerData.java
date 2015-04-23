package net.zomis.cards.mdjq.cards;

import net.zomis.cards.mdjq.MDJQPermanent;
import net.zomis.cards.mdjq.MDJQPlayer;

public class TriggerData {

	private final MDJQPermanent	permanent;
	private final MDJQPlayer	player;

	public TriggerData(MDJQPermanent permanent) {
		this.permanent = permanent;
		this.player = permanent.getController();
	}
	
	public MDJQPermanent getPermanent() {
		return permanent;
	}
	public MDJQPlayer getPlayer() {
		return player;
	}
}
