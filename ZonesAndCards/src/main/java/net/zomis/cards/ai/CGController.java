package net.zomis.cards.ai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.InvalidStackAction;

public class CGController {
	
	@FunctionalInterface
	public interface AI {
		Card<?> decideMove(Player player);
	}
	
	private final CardGame<?, ?> game;
	private final Map<Player, AI> ais = new HashMap<>();
	
	public CGController(CardGame<?, ?> game) {
		this.game = game;
	}
	
	public void autoplay() {
		while (!game.isGameOver() && this.playIfPossible()) {
			
		}
	}
	
	public void setAI(Player player, AI ai) {
		if (ai == null)
			throw new IllegalArgumentException("AI cannot be null for player " + player);
		if (!game.getPlayers().contains(player))
			throw new IllegalArgumentException("Player " + player + " does not exist in game " + game);
		ais.put(player, ai);
	}
	
	public void setAI(int player, AI ai) {
		List<? extends Player> players = game.getPlayers();
		int size = players.size();
		if (player > size || player < 0)
			throw new IllegalArgumentException("Player index not in range 0-" + size + ": " + player);
		setAI(players.get(player), ai);
	}
	
	/**
	 * Make a move in the game with the specified player and AI.
	 * 
	 * @param player The player to make a move for
	 * @param ai The AI to make a move with
	 * @return The StackAction that was chosen, or an InvalidStackAction if the AI returned null 
	 * @throws IllegalArgumentException if the AI or player is null
	 */
	private StackAction play(Player player, AI ai) {
		if (player == null)
			throw new IllegalArgumentException("Player cannot be null: with AI " + ai);
		if (ai == null)
			throw new IllegalArgumentException("AI cannot be null: for player " + player);
		
		Card<?> action = ai.decideMove(player);
		if (action == null)
			return new InvalidStackAction("AI returned null card");
		
		return game.clickPerform(action);
	}
	
	public boolean playIfPossible() {
		if (game.getCurrentPlayer() == null)
			return this.playAll();
		
		if (hasAIforCurrentPlayer())
			return play().actionIsPerformed();
		return false;
	}
	
	/**
	 * Try to make a move for all stored AIs that can make a move.
	 * 
	 * @return True if at least one AI made a move.
	 */
	public boolean playAll() {
		boolean performedSomething = false;
		for (Entry<Player, AI> ee : this.ais.entrySet()) {
			StackAction performed = play(ee.getKey(), ee.getValue());
			if (performed.actionIsPerformed())
				performedSomething = true; 
		}
		return performedSomething;
	}
	
	public StackAction play() {
		Player player = game.getCurrentPlayer();
		AI ai = ais.get(player);
		return play(player, ai);
	}
	
	
	public CardGame<?, ?> getGame() {
		return game;
	}

	public CGController setAIs(AI... ais) {
		for (int i = 0; i < ais.length; i++) {
			setAI(i, ais[i]);
		}
		return this;
	}

	public StackAction play(Player pl) {
		return play(pl, ais.get(pl));
	}

	public static void finishWithAIs(CardGame<?, ?> game, AI... ais) {
		new CGController(game).setAIs(ais).autoplay();
	}

	public AI getAI(Player player) {
		return ais.get(player);
	}

	public StackAction playWithAI(AI ai) {
		return play(game.getCurrentPlayer(), ai);
	}

	public boolean hasAIforCurrentPlayer() {
		Player current = game.getCurrentPlayer();
		if (current == null)
			return !this.ais.isEmpty();
		return ais.get(current) != null;
	}

}
