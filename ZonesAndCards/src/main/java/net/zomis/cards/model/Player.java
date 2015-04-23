package net.zomis.cards.model;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.interfaces.HasResources;
import net.zomis.cards.resources.ResourceMap;

public class Player implements HasResources {

	CardGame<? extends Player, ?> game;
	
	private String name;
	private final ResourceMap resources = new ResourceMap(true);
	
	public CardGame<?, ?> getGame() {
		if (game == null)
			throw new IllegalStateException("Player was not added to game correctly.");
		return game;
	}
	
	@Override
	public ResourceMap getResources() {
		return resources;
	}
	public <E extends Player> List<E> getOpponents() {
		@SuppressWarnings("unchecked")
		List<E> players = (List<E>) game.getPlayers();
		int index = players.indexOf(this);
		List<E> before = players.subList(0, index);
		List<E> after = players.subList(index + 1, players.size());
		
		List<E> result = new ArrayList<E>(after.size() + before.size());
		result.addAll(after);
		result.addAll(before);
		return result;
	}
	
	public Player getNextPlayer() {
		return getOpponents().get(0);
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

	public int getIndex() {
		return getGame().getPlayers().indexOf(this);
//		List<?> players = new ArrayList<Object>(this.getGame().getPlayers());
//		for (int i = 0; i < players.size(); i++) {
//			if (players.get(i) == this)
//				return i;
//		}
//		return -1;
	}
}
