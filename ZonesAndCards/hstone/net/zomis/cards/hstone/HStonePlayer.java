package net.zomis.cards.hstone;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.hstone.actions.BattlefieldAction;
import net.zomis.cards.hstone.factory.HSAbility;
import net.zomis.cards.hstone.factory.HStoneCardModel;
import net.zomis.cards.hstone.factory.HStoneChar;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.HandPlayer;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.resources.ResourceData;
import net.zomis.cards.resources.ResourceListener;
import net.zomis.cards.resources.ResourceMap;
import net.zomis.cards.util.DeckBuilder;
import net.zomis.cards.util.DeckPlayer;

public class HStonePlayer extends Player implements HandPlayer, DeckPlayer<HStoneCardModel>, HStoneTarget, ResourceListener {

	private static final int	MAX_CARDS_IN_HAND	= 10;
	private final CardZone<HStoneCard> library;
	private final CardZone<HStoneCard> hand;
	private final CardZone<HStoneCard> battlefield;
	private List<HStoneCardModel> cards;
	private HStoneCard weapon;
	private boolean frozen;
	
	public HStonePlayer(HStoneGame game, HStoneChar character) {
		this.setName(character.getName());
		this.hand = new CardZone<HStoneCard>(getName() + "-Hand", this);
		this.library = new CardZone<HStoneCard>(getName() + "-Deck", this);
		this.battlefield = new CardZone<HStoneCard>(getName() + "-Battlefield", this);
		this.cards = new ArrayList<HStoneCardModel>();
		DeckBuilder.createExact(this, character.getDeck().getCount(game));
		this.getResources().setGlobalListener(this);
	}
	
	@Override
	public CardZone<HStoneCard> getHand() {
		return hand;
	}
	
	public CardZone<HStoneCard> getLibrary() {
		return library;
	}
	
	public CardZone<HStoneCard> getBattlefield() {
		return battlefield;
	}

	@Override
	public int getCardCount() {
		return cards.size();
	}

	@Override
	public CardZone<HStoneCard> getDeck() {
		return this.library;
	}

	@Override
	public void addCard(HStoneCardModel card) {
		this.cards.add(card);
	}
	
	@Override
	public void clearCards() {
		this.cards = new ArrayList<HStoneCardModel>();
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

	public void drawCard() {
		if (hand.size() >= MAX_CARDS_IN_HAND) {
			return;
		}
		if (library.isEmpty()) {
			int times = getResources().getResources(HStoneRes.EMPTY_DRAWS);
			getResources().changeResources(HStoneRes.HEALTH, -times);
			getResources().changeResources(HStoneRes.EMPTY_DRAWS, 1);
			return;
		}
		library.getTopCard().zoneMoveOnBottom(hand);
	}

	public int getHealth() {
		return getResources().getResources(HStoneRes.HEALTH);
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

	@Override
	public StackAction clickAction() {
		return new BattlefieldAction(this);
	}

	@Override
	public boolean isAttackPossible() {
		return getResources().hasResources(HStoneRes.ATTACK, 1) && getResources().hasResources(HStoneRes.ACTION_POINTS, 1);
	}

	public boolean hasTauntMinions() {
		for (HStoneCard battleCard : getBattlefield().cardList()) {
			if (battleCard.hasAbility(HSAbility.TAUNT))
				return true;
		}
		return false;
	}

	@Override
	public boolean isFrozen() {
		return frozen;
	}

	@Override
	public void addAbility(HSAbility frozen) {
		if (frozen == HSAbility.FROZEN)
			this.frozen = true;
	}

	@Override
	public void damage(int damage) {
		FightModule.damage(this, damage);
	}

	public void removeWeapon() {
		if (weapon != null)
			weapon.zoneMoveOnBottom(null);
	}

	@Override
	public void heal(int healing) {
		FightModule.heal(this, healing);
	}

	@Override
	public HStonePlayer getPlayer() {
		return this;
	}
	
}
