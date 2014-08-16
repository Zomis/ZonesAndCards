package net.zomis.cards.cbased;

import java.util.HashMap;
import java.util.Map;

import net.zomis.cards.iface.Component;
import net.zomis.cards.iface.HasCompGame;
import net.zomis.cards.iface.HasComponents;
import net.zomis.cards.model.Player;

public class CompPlayer extends Player implements HasComponents, HasCompGame {

	private final Map<Class<? extends Component>, Component> components = new HashMap<>();

	public CompPlayer() {
	}

	public void addComponent(Component component) {
		this.components.put(component.getClass(), component);
	}
	
	@Override
	public FirstCompGame getGame() {
		return (FirstCompGame) super.getGame();
	}

	@Override
	public Map<Class<? extends Component>, Component> getComponents() {
		return this.components;
	}

	@Override
	public CompPlayer getNextPlayer() {
		return (CompPlayer) super.getNextPlayer();
	}
	
	@Override
	public String toString() {
		return super.toString() + components.values();
	}
	
	@Override
	public CompPlayer setName(String name) {
		return (CompPlayer) super.setName(name);
	}
	
}
