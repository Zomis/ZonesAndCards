package net.zomis.cards.cwars2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.zomis.ArrayIterator;
import net.zomis.IndexIterator;
import net.zomis.IndexIteratorStatus;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.cards.model.AIHandler;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.phases.GamePhase;
import net.zomis.cards.util.ResourceType;


public class CWars2Game extends CardGame {

	private static final int	NUM_PLAYERS	= 2;
	private int discarded = 0;
	private final int discardsPerTurn = 3;
	
	private List<CWars2Card> cards;
	private ResourceType bricks;
	private ResourceType weapons;
	private ResourceType crystals;
	private ResourceType builders;
	private ResourceType recruits;
	private ResourceType wizards;
	private ResourceType castle;
	private ResourceType wall;
	private ResourceType[] restypes;
	private ResourceType[] producers;
	private CardZone	discard;
	private boolean discardMode;
	
	public CWars2Game() {
		this.bricks = new ResourceType("Bricks");
		this.builders = new ResourceType("Builders");
		this.weapons = new ResourceType("Weapons");
		this.recruits = new ResourceType("Recruits");
		this.crystals = new ResourceType("Crystals");
		this.wizards = new ResourceType("Mage");
		this.castle = new ResourceType("Castle");
		this.wall = new ResourceType("Wall");
		this.restypes = new ResourceType[]{ bricks, weapons, crystals };
		this.producers = new ResourceType[]{ builders, recruits, wizards };
		this.cards = new ArrayList<>();
		addWeaponCards();
		addCrystalCards();
		addBrickCards();
		addUnlockableCards();
		addBuyableCards();
		
		for (CWars2Card card : this.cards) {
			this.addCard(card);
		}
		
		for (int i = 0; i < NUM_PLAYERS; i++) {
			CWars2Player player = new CWars2Player("Player" + i);
			this.addPlayer(player);
			this.addPhase(new CWars2Phase(player));
			
			for (ResourceType res : restypes) {
				player.getResources().set(res, 8);
			}
			for (ResourceType res : producers) {
				player.getResources().set(res, 2);
			}
			player.getResources().set(castle, 25);
			player.getResources().set(wall, 15);
			addZone(player.getDeck());
			addZone(player.getHand());
			
			player.getDeck().setGloballyKnown(true);
			
			DeckBuilder deckBuilder = new DeckBuilder(new ScoreConfigFactory<CWars2Player, CWars2Card>());
			deckBuilder.createDeck(player, 15);
			player.saveDeck();
			player.fillHand();
		}
		discard = new CardZone("DiscardPile");
		discard.setGloballyKnown(true);
		discard.add(new CardModel("NULL").createCard());
		addZone(discard);
	}

	private void addBuyableCards() {
//		new CWars2CardFactory("Protect Resources").setResourceCost(weapons, 8).addTo(cards);
//		
//		CWars2CardFactory reward = new CWars2CardFactory("Reward Workers");
//		CWars2CardFactory remove = new CWars2CardFactory("Remove Resources").setResourceCost(crystals, 17);
//		for (ResourceType res : restypes) {
//			reward.setResourceCost(res, 1);
//			remove.setResourceEffect(res, -8);
//		}
//		reward.addTo(cards);
//		remove.addTo(cards);
		
		new CWars2CardFactory("Hail Storm").setResourceCost(crystals, 14).setDamage(18).addTo(cards);
		new CWars2CardFactory("Giant Snowball").setResourceCost(bricks, 13).setDamage(16).addTo(cards);
		new CWars2CardFactory("Ram Attack").setResourceCost(bricks, 4).setResourceCost(weapons, 4).setDamage(12).addTo(cards);
		
	}

	private void addUnlockableCards() {
		for (IndexIteratorStatus<ResourceType> res : new IndexIterator<ResourceType>(new ArrayIterator<ResourceType>(this.producers))) {
			int index = res.getIndex();
			ResourceType val = res.getValue();
			ResourceType resource = this.restypes[index];
			new CWars2CardFactory("Sacrifice " + val.getName()).setResourceCost(val, 1).setResourceCost(resource, 6)
				.setOppEffect(val, -1)
				.addAction(new RequiresTwo(this, val, 2)).addTo(cards);
		}
		
		new CWars2CardFactory("Dragon").setResourceCost(bricks, 20).setResourceCost(crystals, 20).setDamage(38).addTo(cards);
		new CWars2CardFactory("Trojan Horse").setResourceCost(weapons, 20).setResourceCost(bricks, 15).setCastleDamage(30).addTo(cards);
		new CWars2CardFactory("Comet Strike").setResourceCost(crystals, 10).setResourceCost(bricks, 20).setDamage(30).addTo(cards);
		
		CWars2CardFactory curse = new CWars2CardFactory("Curse");
		for (int i = 0; i < this.restypes.length; i++) {
			curse.setResourceCost(this.restypes[i], 15);
			curse.setOppEffect(this.restypes[i], -1);
			curse.setOppEffect(this.producers[i], -1);
		}
		curse.setOppEffect(wall, -1);
		curse.setOppEffect(castle, -1);
		curse.addTo(cards);
	}

	private void addBrickCards() {
		new CWars2CardFactory("Builder").setResourceCost(bricks, 8).setMyEffect(builders, 1).addTo(cards);
		new CWars2CardFactory("Tower").setResourceCost(bricks, 10).setMyEffect(castle, 10).addTo(cards);
		new CWars2CardFactory("Tavern").setResourceCost(bricks, 12).setMyEffect(castle, 15).addTo(cards);
		new CWars2CardFactory("House").setResourceCost(bricks, 5).setMyEffect(castle, 5).addTo(cards);
		new CWars2CardFactory("Babylon").setResourceCost(bricks, 25).setMyEffect(castle, 30).addTo(cards);
		new CWars2CardFactory("Wall").setResourceCost(bricks, 4).setMyEffect(wall, 6).addTo(cards);
		new CWars2CardFactory("School").setResourceCost(bricks, 30).setMyEffect(builders, 1)
			.setMyEffect(recruits, 1).setMyEffect(wizards, 1).addTo(cards);
		
		new CWars2CardFactory("Fence").setResourceCost(bricks, 5).setMyEffect(wall, 9).addTo(cards);
		new CWars2CardFactory("Catapult").setResourceCost(bricks, 10).setDamage(12).addTo(cards);
		new CWars2CardFactory("Battering Ram").setResourceCost(bricks, 7).setDamage(9).addTo(cards);
		new CWars2CardFactory("Wain").setResourceCost(bricks, 1).setDamage(10).addTo(cards);
//		new CWars2CardFactory("All Bricks").setResourceCost(bricks, 1).addTo(cards);
		new CWars2CardFactory("Large Wall").setResourceCost(bricks, 14).setMyEffect(wall, 20).addTo(cards);
		new CWars2CardFactory("Reverse").setResourceCost(bricks, 3).setResourceCost(wall, 4).setMyEffect(castle, 8).addTo(cards);
	}

	private void addCrystalCards() {
		new CWars2CardFactory("Mage").setResourceCost(crystals, 8).setMyEffect(wizards, 1).addTo(cards);
		new CWars2CardFactory("Lightning").setResourceCost(crystals, 20).setDamage(22).addTo(cards);
		new CWars2CardFactory("Quake").setResourceCost(crystals, 24).setDamage(27).addTo(cards);
		new CWars2CardFactory("Pixies").setResourceCost(crystals, 18).setMyEffect(castle, 22).addTo(cards);
		new CWars2CardFactory("Magic Wall").setResourceCost(crystals, 14).setMyEffect(wall, 20).addTo(cards);
//		new CWars2CardFactory("Magic Defense").setResourceCost(crystals, 10).addTo(cards);
//		new CWars2CardFactory("Magic Weapons").setResourceCost(crystals, 15).addTo(cards);
		
		for (ResourceType res : this.restypes) {
			new CWars2CardFactory("Add " + res.getName()).setResourceCost(crystals, 5).setMyEffect(res, 8).addTo(cards);
			new CWars2CardFactory("Remove " + res.getName()).setResourceCost(crystals, 5).setOppEffect(res, -8).addTo(cards);
		}
		
//		new CWars2CardFactory("All Crystals").setResourceCost(crystals, 1).addTo(cards);
	}

	private void addWeaponCards() {
		new CWars2CardFactory("Archer").setResourceCost(weapons, 1).setDamage(2).addTo(cards);
		new CWars2CardFactory("Fire Archer").setResourceCost(weapons, 3).setDamage(5).addTo(cards);
		new CWars2CardFactory("Bomb").setResourceCost(weapons, 14).setDamage(18).addTo(cards);
		new CWars2CardFactory("Cannon").setResourceCost(weapons, 16).setDamage(20).addTo(cards);
//		new CWars2CardFactory("Spy").setResourceCost(weapons, 8).addTo(cards);
		new CWars2CardFactory("Platoon").setResourceCost(weapons, 7).setDamage(9).addTo(cards);
		new CWars2CardFactory("Ambush").setResourceCost(weapons, 20).setCastleDamage(15).addTo(cards);

		new CWars2CardFactory("Knight").setResourceCost(weapons, 10).setDamage(10).addTo(cards);
//		new CWars2CardFactory("Thief").setResourceCost(weapons, 17).addTo(cards);
		new CWars2CardFactory("Recruit").setResourceCost(weapons, 8).setMyEffect(recruits, 1).addTo(cards);
		new CWars2CardFactory("Guards").setResourceCost(weapons, 7).setMyEffect(wall, 12).addTo(cards);
//		new CWars2CardFactory("Roadblock").setResourceCost(weapons, 8).addTo(cards);
//		new CWars2CardFactory("All Weapons").setResourceCost(weapons, 1).addTo(cards);
//		new CWars2CardFactory("Sabotage").setResourceCost(weapons, 13).addTo(cards);
	}
	
	@Override
	public AIHandler getAIHandler() {
		return new CWars2Handler();
	}
	public List<CWars2Card> getCards() {
		return Collections.unmodifiableList(cards);
	}
	public ResourceType[] getRestypes() {
		return restypes;
	}
	public ResourceType[] getProducers() {
		return producers;
	}
	@Override
	public void setActivePhase(GamePhase phase) {
		this.discarded = 0;
		this.discardMode = false;
		this.getCurrentPlayer().fillHand();
		super.setActivePhase(phase);
	}
	
	public int getDiscarded() {
		return discarded;
	}
	public CardZone getDiscard() {
		return discard;
	}

	public void discarded() {
		this.discarded++;
	}

	public ResourceType getResWall() {
		return this.wall;
	}
	public ResourceType getResCastle() {
		return castle;
	}
	@Override
	public CWars2Player getCurrentPlayer() {
		return (CWars2Player) super.getCurrentPlayer();
	}
	
	public void toggleDiscardMode() {
		this.discardMode = !this.discardMode;
	}
	public boolean isDiscardMode() {
		return discardMode;
	}
	
	@Override
	public boolean isNextPhaseAllowed() {
		return this.discarded > 0; // discarded is also increased directly before nextphase is called when you play
	}

	public int getDiscardsPerTurn() {
		return this.discardsPerTurn;
	}
}
