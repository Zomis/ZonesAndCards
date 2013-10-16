package net.zomis.cards.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class Player implements Comparable<Player> {

	@JsonBackReference
	CardGame game;
	
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
	
	private String name;
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
	
}
