package net.zomis.cards.wart;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.interfaces.HandPlayer;
import net.zomis.cards.interfaces.HasBattlefield;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.CardZoneLocation;
import net.zomis.cards.model.Player;
import net.zomis.cards.resources.ResourceData;
import net.zomis.cards.resources.ResourceListener;
import net.zomis.cards.resources.ResourceMap;
import net.zomis.cards.util.DeckBuilder;
import net.zomis.cards.util.DeckPlayer;
import net.zomis.cards.wart.factory.HSAbility;
import net.zomis.cards.wart.factory.HStoneCardModel;
import net.zomis.cards.wart.factory.HStoneChar;

public class HStonePlayer extends Player implements HandPlayer, DeckPlayer<HStoneCardModel>, ResourceListener,
	HasBattlefield {

	public static final int	MAX_CARDS_IN_HAND	= 10;
	public static final int	MAX_BATTLEFIELD_SIZE = 7;
	private final CardZone<HStoneCard> library;
	private final CardZone<HStoneCard> hand;
	private final CardZone<HStoneCard> battlefield;
	private List<HStoneCardModel> cards;
	private HStoneCard weapon;
	private HStoneCard heroPower;
	private final HStoneCard playerCard;
	private final CardZone<HStoneCard> specialZone;
	private final CardZone<HStoneCard> discard;
	private final CardZone<HStoneCard> secrets;
	
	public HStonePlayer(HStoneGame game, HStoneChar character) {
		this.setName(character.getName());
		this.hand        = new CardZone<HStoneCard>("Hand", this);
		this.library     = new CardZone<HStoneCard>("Deck", this);
		this.battlefield = new CardZone<HStoneCard>("Battlefield", this);
		this.discard	 = new CardZone<HStoneCard>("Discard", this);
		this.secrets	 = new CardZone<HStoneCard>("Secrets", this);
		
		this.hand.setKnown(this, true);
		this.battlefield.setGloballyKnown(true);
		this.discard.setGloballyKnown(true);
		
		this.cards = new ArrayList<HStoneCardModel>();
		this.specialZone = new CardZone<HStoneCard>("Special", this);
		this.specialZone.setGloballyKnown(true);
		
		this.heroPower = character.getCharClass().heroPowerCard(specialZone);
		this.playerCard = character.playerCard(specialZone);
		DeckBuilder.createExact(this, character.getDeck().getCount(game));
		this.getResources().setGlobalListener(this);
	}
	
	public HStoneCard getHeroPower() {
		return heroPower;
	}
	
	public HStoneCard getPlayerCard() {
		return playerCard;
	}
	
	@Override
	public CardZone<HStoneCard> getHand() {
		return hand;
	}
	
	public CardZone<HStoneCard> getLibrary() {
		return library;
	}
	
	@Override
	public CardZone<HStoneCard> getBattlefield() {
		return battlefield;
	}

	@Override
	public int getCardCount() {
		return cards.size();
	}

	public CardZone<HStoneCard> getDeck() {
		return this.library;
	}

	@Override
	public void addCard(HStoneCardModel card) {
		this.cards.add(card);
	}
	
	@Override
	public HStonePlayer getNextPlayer() {
		return (HStonePlayer) super.getNextPlayer();
	}

	public void drawCards(int cards) {
		for (int i = 0; i < cards; i++) {
			drawCard();
		}
	}

	public HStoneCard drawCard() {
		if (library.isEmpty()) {
			int times = getResources().getResources(HStoneRes.EMPTY_DRAWS);
//			FightModule.damage(getPlayerCard(), times);
			getResources().changeResources(HStoneRes.HEALTH, -times);
			getResources().changeResources(HStoneRes.EMPTY_DRAWS, 1);
			return null;
		}
		if (hand.size() >= MAX_CARDS_IN_HAND) {
			library.getTopCard().zoneMoveOnBottom(getDiscard());
			return null;
		}
		HStoneCard card = library.getTopCard();
		card.zoneMoveOnBottom(hand);
		return card;
	}

	public int getHealth() {
		return getResources().getResources(HStoneRes.HEALTH);
	}
	
	@Override
	public ResourceMap getResources() {
		return getPlayerCard().getResources();
	}

	public void onStart() {
		getResources().set(HStoneRes.HEALTH, 30);
		for (HStoneCardModel card : this.cards) {
			library.createCardOnTop(card);
		}
		library.shuffle();
	}
	
	@Override
	public HStoneGame getGame() {
		return (HStoneGame) super.getGame();
	}

	@Override
	public boolean onResourceChange(ResourceMap map, ResourceData type, int newValue) {
		if (type.getResource() == HStoneRes.HEALTH) {
			this.getGame().onPlayerHealthChange(this, newValue);
		}
		return true;
	}

	public boolean hasTauntMinions() {
		for (HStoneCard battleCard : getBattlefield()) {
			if (battleCard.hasAbility(HSAbility.TAUNT))
				return true;
		}
		return false;
	}

	public HStoneCard getWeapon() {
		return this.weapon;
	}

	public CardZone<HStoneCard> getSpecialZone() {
		return this.specialZone;
	}

	public int getArmor() {
		return getResources().getResources(HStoneRes.ARMOR);
	}

	public void setHeroPower(HStoneCardModel cardModel) {
		HStoneCard card = this.specialZone.createCardOnBottom(cardModel);
		this.heroPower.moveAndReplaceWith(CardZoneLocation.nowhere(), card);
	}

	public CardZone<HStoneCard> getDiscard() {
		return this.discard;
	}
	
	public CardZone<HStoneCard> getSecrets() {
		return secrets;
	}

	public void equip(HStoneCard card) {
		if (!card.getModel().isWeapon())
			throw new IllegalArgumentException("Card is not a weapon: " + card);
		if (this.weapon != null)
			this.weapon.zoneMoveOnBottom(getDiscard());
		this.weapon = card;
		card.zoneMoveOnBottom(this.specialZone);
	}

	public void destroyWeapon() {
		if (this.weapon != null)
			this.weapon.zoneMoveOnBottom(null);
		this.weapon = null;
	}
	
}
