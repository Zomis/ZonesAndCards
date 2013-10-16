package net.zomis.cards.classics;

import java.util.SortedMap;
import java.util.TreeMap;

import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;

public class ClassicGame extends CardGame {

	public static enum AceValue { LOW, HIGH; }
	
	private final SortedMap<Suite, SortedMap<Integer, ClassicCard>> cardModels = new TreeMap<Suite, SortedMap<Integer, ClassicCard>>();
	private final int	aceValue;
	private final int	minRank;
	private final int	maxRank;
	
	public SortedMap<Suite, SortedMap<Integer, ClassicCard>> getCardModels() {
		return new TreeMap<Suite, SortedMap<Integer, ClassicCard>>(cardModels);
	}
	public int getMaxRank() {
		return maxRank;
	}
	public int getMinRank() {
		return minRank;
	}
	public int getAceValue() {
		return this.aceValue;
	}
	
	public ClassicGame(AceValue aceValue) {
		this.aceValue = (aceValue == AceValue.LOW ? ClassicCard.RANK_ACE_LOW : ClassicCard.RANK_ACE_HIGH);
		this.minRank = Math.min(2, getAceValue());
		this.maxRank = Math.max(ClassicCard.RANK_KING, getAceValue());
		for (Suite suite : Suite.values()) {
			if (!suite.isWildcard()) {
				SortedMap<Integer, ClassicCard> values = new TreeMap<Integer, ClassicCard>();
				for (int rank = minRank; rank <= maxRank; rank++) {
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
	public CardPlayer findPlayerWithBoard(CardZone zone) {
		for (Player player : this.getPlayers()) {
			CardPlayer pl = (CardPlayer) player;
			if (pl.getBoard() == zone)
				return pl;
		}
		return null;
	}
	public CardPlayer findPlayerWithHand(CardZone zone) {
		for (Player player : this.getPlayers()) {
			CardPlayer pl = (CardPlayer) player;
			if (pl.getHand() == zone)
				return pl;
		}
		return null;
	}
	public CardPlayer findPlayerWithZone(CardZone zone) {
		CardPlayer pl = findPlayerWithHand(zone);
		if (pl == null) 
			pl = findPlayerWithBoard(zone);
		return pl;
	}
	
}
