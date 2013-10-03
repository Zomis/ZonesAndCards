package net.zomis.cards.simple;

import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;

public class SimplePlayer extends Player {


	private int resources;
	private int resourcesPerTurn;
	private int	life;
	CardZone hand;
	CardZone library;
	
	// getHand, getLibrary, get... put references to Zones inside class
	
	public SimplePlayer() {
		this.resources = 8;
		this.resourcesPerTurn = 2;
		this.life = 20;
	}
	
	public int getResources() {
		return resources;
	}
	public int getResourcesPerTurn() {
		return resourcesPerTurn;
	}
	public void setResourcesPerTurn(int resourcesPerTurn) {
		this.resourcesPerTurn = resourcesPerTurn;
	}
	public void setResources(int resources) {
		this.resources = resources;
	}
	public void changeLife(int value) {
		this.life += value;
	}
	
	public int getLife() {
		return life;
	}
	public void changeResources(int i) {
		this.resources += i;
	}
	
	@Override
	public String toString() {
		return String.format("{%s: %d life, %d res, %d res/turn}", this.getName(), this.getLife(), this.getResources(), this.getResourcesPerTurn());
	}

	public CardZone getHand() {
		return this.hand;
	}

	public CardZone getLibrary() {
		return this.library;
	}
	
}
