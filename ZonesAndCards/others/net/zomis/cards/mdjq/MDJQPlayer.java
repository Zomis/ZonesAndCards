package net.zomis.cards.mdjq;

import net.zomis.cards.mdjq.MDJQZone.ZoneType;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.util.DeckPlayer;
import net.zomis.cards.util.ResourceMap;

public class MDJQPlayer extends Player implements MDJQObject, DeckPlayer<MDJQCardModel> {

	private final MDJQZone library;
	private final MDJQZone hand;
	private final MDJQZone graveyard;
	private final ResourceMap manaPool;
	private int	life;
	private int	landsPlayed;
//	private final MDJQZone battlefield;
	
	public ResourceMap getManaPool() {
		return manaPool;
	}
	
	public MDJQPlayer(String name) {
		this.graveyard = new MDJQZone("Graveyard-" + name, ZoneType.GRAVEYARD, this);
		this.graveyard.setGloballyKnown(true);
		this.hand = new MDJQZone("Hand-" + name, ZoneType.HAND, this);
		this.hand.setGloballyKnown(true);
		this.hand.setKnown(this, true);
		this.library = new MDJQZone("Library-" + name, ZoneType.LIBRARY, this);
		this.manaPool = new ResourceMap();
		this.setName(name);
	}
	
	public int getLife() {
		return life;
	}
	
	public MDJQZone getLibrary() {
		return library;
	}
	public MDJQZone getHand() {
		return hand;
	}
	public MDJQZone getGraveyard() {
		return graveyard;
	}
	@Override
	public MDJQGame getGame() {
		return (MDJQGame) super.getGame();
	}
	/**
	 * 
	 * @param life
	 */
	public void changeLife(int life) {
		if (life > 0)
			this.gainLife(life);
		else this.damage(-life);
	}
	public void gainLife(int life) {
		this.life += life;
		// TODO: PlayerGainLifeEvent(MDJQObject: this, life)
	}

	public void damage(int damage) {
		this.life -= damage;
		// TODO: PlayerDamageEvent(MDJQObject: this, damage)
	}

	@Override
	public int getCardCount() {
		return this.library.cardList().size();
	}

	@Override
	public CardZone getDeck() {
		return this.library;
	}

	@Override
	public void addCard(MDJQCardModel card) {
		this.library.cardList().add(card.createCard(library));
	}
	@Override
	public String toString() {
		return this.getName();
	}

	public boolean isAllowLandPlay() {
		return this.landsPlayed == 0;
	}
	
	public void newTurn() {
		this.landsPlayed = 0;
	}

	public void landPlayed() {
		this.landsPlayed++;
	}

	public boolean isActivePlayer() {
		return this.getGame().getCurrentPlayer() == this;
	}

	public boolean hasPriority() {
		return this.isActivePlayer(); // TODO: Separate active player from player who has priority
	}
	
}
