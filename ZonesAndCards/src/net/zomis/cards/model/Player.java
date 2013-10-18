package net.zomis.cards.model;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.model.ai.CardAI;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class Player implements Comparable<Player> {

	@JsonBackReference
	CardGame game;
	
	private String name;
	private CardAI ai;
	
	public CardGame getGame() {
		if (game == null)
			throw new IllegalStateException("Player was not added to game correctly.");
		return game;
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
		if (this.ai == null)
			throw new NullPointerException("Player does not have an AI specified: " + this);
		return this.ai;
	}
	
	public void setAI(CardAI ai) {
		this.ai = ai;
	}
	
}
