package net.zomis.cards.cwars2;

import java.util.Collection;

import net.zomis.cards.count.CardCount;
import net.zomis.cards.cwars2.CWars2Res.Producers;
import net.zomis.cards.cwars2.CWars2Res.Resources;
import net.zomis.cards.cwars2.ais.CWars2Decks;
import net.zomis.cards.cwars2.cards.BricksCards;
import net.zomis.cards.cwars2.cards.CrystalCards;
import net.zomis.cards.cwars2.cards.MultiplayerSpecificCards;
import net.zomis.cards.cwars2.cards.SinglePlayerCards;
import net.zomis.cards.cwars2.cards.UnlockableGlobal;
import net.zomis.cards.cwars2.cards.WeaponCards;
import net.zomis.cards.model.Player;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.util.DeckBuilder;
import net.zomis.cards.util.DeckList;

public class CWars2Setup {

	private CWars2Game game;
	
	public CWars2Setup() {
		game = new CWars2Game();
	}
	public CWars2Setup setRandomSeed(long seed) {
		game.setRandomSeed(seed);
		return this;
	}
	
	public static CWars2Setup newSingleplayerGame() {
		CWars2Setup setup = new CWars2Setup().addDefaultResources().addSingleplayerCards();
		setup.game.init();
		for (Player pl : setup.game.getPlayers()) {
			for (Resources res : Resources.values()) {
				pl.getResources().set(res, 5);
			}
			pl.getResources().set(CWars2Res.CASTLE, 24);
			pl.getResources().set(CWars2Res.WALL, 10);
		}
			
		return setup;
	}
	public static CWars2Setup newMultiplayerGame() {
		CWars2Setup setup = new CWars2Setup().addDefaultResources().addMultiplayerCards();
		setup.game.init();
		return setup;
	}
	
	public CWars2Setup addMultiplayerCards() {
		new CrystalCards().addCards(game);
		new WeaponCards().addCards(game);
		new BricksCards().addCards(game);
		
		new MultiplayerSpecificCards().addCards(game);
		new UnlockableGlobal().addCards(game);
		return this;
	}
	private CWars2Setup addSingleplayerCards() {
		new CrystalCards().addCards(game);
		new WeaponCards().addCards(game);
		new BricksCards().addCards(game);
		
		new SinglePlayerCards().addCards(game);
		new UnlockableGlobal().addCards(game);
		return this;
	}
	public CWars2Setup withCardSet(CardSet<CWars2Game> set) {
		set.addCards(game);
		return this;
	}
	public CWars2Setup withCard(CWars2CardFactory card) {
		card.addTo(game);
		return this;
	}
	public CWars2Setup addDefaultResources() {
		for (Producers prod : CWars2Res.Producers.values()) {
			game.addResource(prod);
			game.addResource(prod.getResource());
		}
		return this;
	}
	private void setDeck(int player, Collection<CardCount<CWars2Card>> cards) {
		CWars2Player pl = (CWars2Player) this.game.getPlayers().get(player);
		DeckBuilder.createExact(pl, cards);
	}
	public void setDecks(Collection<CardCount<CWars2Card>> cardsPlayer1, Collection<CardCount<CWars2Card>> cardsPlayer2) {
		this.setDeck(0, cardsPlayer1);
		this.setDeck(1, cardsPlayer2);
	}
	
	public CWars2Game build() {
		return this.game;
	}
	
	public static CWars2Game defaultGame(Long seed) {
		CWars2Setup setup = CWars2Setup.newMultiplayerGame();
		if (seed != null)
			setup.setRandomSeed(seed);
		setup.setDecks(CWars2Decks.zomisMultiplayerDeck(), CWars2Decks.zomisMultiplayerDeck());
		setup.game.init();
		return setup.build();
	}
	public CWars2Setup setDecks(DeckList deckA, DeckList deckB) {
		this.setDecks(deckA.getCount(game), deckB.getCount(game));
		return this;
	}
	
	public CWars2Game start() {
		game.startGame();
		return game;
	}
}
