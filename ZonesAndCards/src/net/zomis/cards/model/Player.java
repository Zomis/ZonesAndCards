package net.zomis.cards.model;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.model.ai.CardAI;
import net.zomis.cards.util.ResourceMap;

public class Player implements Comparable<Player> {

	CardGame game;
	
	private String name;
	private CardAI ai;
	private final ResourceMap resources = new ResourceMap(true);
	
	public CardGame getGame() {
		if (game == null)
			throw new IllegalStateException("Player was not added to game correctly.");
		return game;
	}
	
	public ResourceMap getResources() {
		return resources;
	}
	public List<Player> getOpponents() {
		List<Player> players = game.getPlayers();
		int index = players.indexOf(this);
		List<Player> before = players.subList(0, index);
		List<Player> after = players.subList(index + 1, players.size());
		
		List<Player> result = new ArrayList<Player>(after.size() + before.size());
		result.addAll(after);
		result.addAll(before);
		return result;
	}
	
	public String getName() {
		return name;
	}
	public Player setName(String name) {
		this.name = name;
		return this;
	}
	
	@Override
	public String toString() {
		return "Player-" + this.getName();
	}

	@Override
	public int compareTo(Player o) {
		return this.name.compareTo(o.name);
	}

	public CardAI getAI() {
		return this.ai;
	}
	
	public void setAI(CardAI ai) {
		this.ai = ai;
	}
	
}
