package net.zomis.cards.iface;

import net.zomis.cards.cbased.FirstCompGame;

@FunctionalInterface
public interface GameSystem {
	void onStart(FirstCompGame game);
}
