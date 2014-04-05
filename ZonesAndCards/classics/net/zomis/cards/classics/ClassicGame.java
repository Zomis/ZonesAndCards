package net.zomis.cards.classics;

import java.util.SortedMap;
import java.util.TreeMap;

import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;

public class ClassicGame extends CardGame<CardPlayer, ClassicCard> {

	private final SortedMap<Suite, SortedMap<Integer, ClassicCard>> cardModels = new TreeMap<Suite, SortedMap<Integer, ClassicCard>>();
	private final AceValue aceConfig;
	
	public SortedMap<Suite, SortedMap<Integer, ClassicCard>> getCardModels() {
		return new TreeMap<Suite, SortedMap<Integer, ClassicCard>>(cardModels);
	}
	public int getAceValue() {
		return this.getAceConfig().getAceValue();
	}
	public AceValue getAceConfig() {
		return this.aceConfig;
	}
	
	public ClassicGame(AceValue aceValue) {
		this.aceConfig = aceValue;
		for (Suite suite : Suite.values()) {
			if (!suite.isWildcard()) {
				SortedMap<Integer, ClassicCard> values = new TreeMap<Integer, ClassicCard>();
				for (int rank : getAceConfig().getRanks()) {
					ClassicCard card = new ClassicCard(suite, rank);
					addCard(card);
					values.put(card.getRank(), card);
				}
				cardModels.put(suite, values);
			}
		}
		
		SortedMap<Integer, ClassicCard> wildcardMap = new TreeMap<Integer, ClassicCard>();
		ClassicCard card = new ClassicCard(Suite.EXTRA, ClassicCard.RANK_WILDCARD);
		wildcardMap.put(card.getRank(), card);
		cardModels.put(card.getSuite(), wildcardMap);
		this.addCard(card);
	}
	@Override
	public CardPlayer getCurrentPlayer() {
		return (CardPlayer) super.getCurrentPlayer();
	}
	public CardPlayer findPlayerWithBoard(ClassicCardZone zone) {
		for (Player player : this.getPlayers()) {
			CardPlayer pl = (CardPlayer) player;
			if (pl.getBoard() == zone)
				return pl;
		}
		return null;
	}
	public CardPlayer findPlayerWithHand(ClassicCardZone zone) {
		for (Player player : this.getPlayers()) {
			CardPlayer pl = (CardPlayer) player;
			if (pl.getHand() == zone)
				return pl;
		}
		return null;
	}
	public CardPlayer findPlayerWithZone(ClassicCardZone zone) {
		CardPlayer pl = findPlayerWithHand(zone);
		if (pl == null) 
			pl = findPlayerWithBoard(zone);
		return pl;
	}
	
}
