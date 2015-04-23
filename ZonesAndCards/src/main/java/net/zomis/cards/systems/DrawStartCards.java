package net.zomis.cards.systems;

import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.helpers.DrawCardHelper;
import net.zomis.cards.iface.GameSystem;

public class DrawStartCards implements GameSystem {

	private final int cards;
	private final CompPlayer forPlayer;
	
	public DrawStartCards(int i) {
		this(null, i);
	}

	public DrawStartCards(CompPlayer compPlayer, int i) {
		this.forPlayer = compPlayer;
		this.cards = i;
	}

	@Override
	public void onStart(FirstCompGame game) {
		if (forPlayer == null) {
			for (CompPlayer player : game.getPlayers())
				DrawCardHelper.drawCards(player, cards);
		}
		else DrawCardHelper.drawCards(forPlayer, cards);
	}

}
