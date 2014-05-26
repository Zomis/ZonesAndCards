package net.zomis.cards.systems;

import net.zomis.cards.cbased.FirstCompGame;

@FunctionalInterface
public interface GameSystem {
	void onStart(FirstCompGame game);
}
