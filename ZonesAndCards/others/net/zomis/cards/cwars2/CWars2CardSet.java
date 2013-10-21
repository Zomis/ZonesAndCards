package net.zomis.cards.cwars2;

import static net.zomis.cards.cwars2.CWars2Res.Producers.*;
import static net.zomis.cards.cwars2.CWars2Res.Resources.*;

import java.util.Map.Entry;

import net.zomis.cards.cwars2.CWars2Res.Producers;
import net.zomis.cards.cwars2.CWars2Res.Resources;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.util.FixedResourceStrategy;
import net.zomis.cards.util.IResource;
import net.zomis.cards.util.ResourceListener;
import net.zomis.cards.util.ResourceMap;
import net.zomis.cards.util.ResourceStrategy;

public class CWars2CardSet {
	
	private CWars2Game	game;

	public CWars2CardSet() {}
	
	public void addCards(CWars2Game game) {
		this.game = game;
		addWeaponCards();
		addCrystalCards();
		addBrickCards();
		addUnlockableCards();
		addBuyableCards();
	}
	
	private void addBuyableCards() {
		new CWars2CardFactory("Protect Resources").setResourceCost(WEAPONS, 8).addAction(new ProtectResourcesAction(game)).addTo(game);
		
		CWars2CardFactory reward = new CWars2CardFactory("Reward Workers");
		CWars2CardFactory remove = new CWars2CardFactory("Remove Resources").setResourceCost(CRYSTALS, 17);
		for (IResource res : Resources.values()) {
			reward.setResourceCost(res, 1);
			remove.setOppEffect(res, -8);
		}
		remove.addTo(game);
		reward.addAction(new MultiplyNextResourceIncome(game, 2, true));
		reward.addTo(game);
		
		new CWars2CardFactory("Hail Storm").setResourceCost(CRYSTALS, 14).setDamage(18).addTo(game);
		new CWars2CardFactory("Giant Snowball").setResourceCost(BRICKS, 13).setDamage(16).addTo(game);
		new CWars2CardFactory("Ram Attack").setResourceCost(BRICKS, 4).setResourceCost(WEAPONS, 4).setDamage(12).addTo(game);
		
	}

	private void addUnlockableCards() {
		for (Producers val : Producers.values()) {
			IResource resource = val.getResource();
			new CWars2CardFactory("Sacrifice " + val).setResourceCost(val, 1).setResourceCost(resource, 6).setOppEffect(val, -1)
				.addAction(new RequiresTwo(game, val, 2)).addTo(game);
		}
		
		new CWars2CardFactory("Dragon").setResourceCost(BRICKS, 20).setResourceCost(CRYSTALS, 20).setDamage(38).addTo(game);
		new CWars2CardFactory("Trojan Horse").setResourceCost(WEAPONS, 20).setResourceCost(BRICKS, 15).setCastleDamage(30).addTo(game);
		new CWars2CardFactory("Comet Strike").setResourceCost(CRYSTALS, 10).setResourceCost(BRICKS, 20).setDamage(30).addTo(game);
		
		CWars2CardFactory curse = new CWars2CardFactory("Curse");
		for (Resources res : Resources.values()) {
			curse.setResourceCost(res, 15);
			curse.setOppEffect(res, -1);
			curse.setOppEffect(res.getProducer(), -1);
		}
		curse.setOppEffect(CWars2Res.WALL, -1);
		curse.setOppEffect(CWars2Res.CASTLE, -1);
		curse.addTo(game);
	}

	private void addBrickCards() {
		new CWars2CardFactory("Builder").setResourceCost(BRICKS, 8).setMyEffect(BUILDERS, 1).addTo(game);
		new CWars2CardFactory("Tower").setResourceCost(BRICKS, 10).setMyEffect(CWars2Res.CASTLE, 10).addTo(game);
		new CWars2CardFactory("Tavern").setResourceCost(BRICKS, 12).setMyEffect(CWars2Res.CASTLE, 15).addTo(game);
		new CWars2CardFactory("House").setResourceCost(BRICKS, 5).setMyEffect(CWars2Res.CASTLE, 5).addTo(game);
		new CWars2CardFactory("Babylon").setResourceCost(BRICKS, 25).setMyEffect(CWars2Res.CASTLE, 30).addTo(game);
		new CWars2CardFactory("Wall").setResourceCost(BRICKS, 4).setMyEffect(CWars2Res.WALL, 6).addTo(game);
		new CWars2CardFactory("School").setResourceCost(BRICKS, 30).setMyEffect(BUILDERS, 1)
			.setMyEffect(RECRUITS, 1).setMyEffect(WIZARDS, 1).addTo(game);
		
		new CWars2CardFactory("Fence").setResourceCost(BRICKS, 5).setMyEffect(CWars2Res.WALL, 9).addTo(game);
		new CWars2CardFactory("Catapult").setResourceCost(BRICKS, 10).setDamage(12).addTo(game);
		new CWars2CardFactory("Battering Ram").setResourceCost(BRICKS, 7).setDamage(9).addTo(game);
		new CWars2CardFactory("Wain").setResourceCost(BRICKS, 10).setOppEffect(CWars2Res.CASTLE, -6).setMyEffect(CWars2Res.CASTLE, 6).addTo(game);
		new CWars2CardFactory("All Bricks").setResourceCost(BRICKS, 1).addAction(new AllResourcesFocusOn(game, BRICKS)).addTo(game);
		new CWars2CardFactory("Large Wall").setResourceCost(BRICKS, 14).setMyEffect(CWars2Res.WALL, 20).addTo(game);
		new CWars2CardFactory("Reverse").setResourceCost(BRICKS, 3).setResourceCost(CWars2Res.WALL, 4).setMyEffect(CWars2Res.CASTLE, 8).addTo(game);
	}

	private void addCrystalCards() {
		new CWars2CardFactory("Mage").setResourceCost(CRYSTALS, 8).setMyEffect(WIZARDS, 1).addTo(game);
		new CWars2CardFactory("Lightning").setResourceCost(CRYSTALS, 20).setDamage(22).addTo(game);
		new CWars2CardFactory("Quake").setResourceCost(CRYSTALS, 24).setDamage(27).addTo(game);
		new CWars2CardFactory("Pixies").setResourceCost(CRYSTALS, 18).setMyEffect(CWars2Res.CASTLE, 22).addTo(game);
		new CWars2CardFactory("Magic Wall").setResourceCost(CRYSTALS, 14).setMyEffect(CWars2Res.WALL, 20).addTo(game);
		new CWars2CardFactory("Magic Defense").setResourceCost(CRYSTALS, 10).addAction(new MagicAttackMultiply(game, false, 0)).addTo(game);
		new CWars2CardFactory("Magic Weapons").setResourceCost(CRYSTALS, 15).addAction(new MagicAttackMultiply(game, true, 2)).addTo(game);
		
		for (IResource res : Resources.values()) {
			new CWars2CardFactory("Add " + res).setResourceCost(CRYSTALS, 5).setMyEffect(res, 8).addTo(game);
			new CWars2CardFactory("Remove " + res).setResourceCost(CRYSTALS, 5).setOppEffect(res, -8).addTo(game);
		}
		
		new CWars2CardFactory("All Crystals").setResourceCost(CRYSTALS, 1).addAction(new AllResourcesFocusOn(game, CRYSTALS)).addTo(game);
	}

	private void addWeaponCards() {
		new CWars2CardFactory("Archer").setResourceCost(WEAPONS, 1).setDamage(2).addTo(game);
		new CWars2CardFactory("Fire Archer").setResourceCost(WEAPONS, 3).setDamage(5).addTo(game);
		new CWars2CardFactory("Bomb").setResourceCost(WEAPONS, 14).setDamage(18).addTo(game);
		new CWars2CardFactory("Cannon").setResourceCost(WEAPONS, 16).setDamage(20).addTo(game);
//		new CWars2CardFactory("Spy").setResourceCost(weapons, 8).addTo(cards);
		new CWars2CardFactory("Platoon").setResourceCost(WEAPONS, 7).setDamage(9).addTo(game);
		new CWars2CardFactory("Ambush").setResourceCost(WEAPONS, 20).setCastleDamage(15).addTo(game);

		new CWars2CardFactory("Knight").setResourceCost(WEAPONS, 10).setDamage(10).addTo(game);
		new CWars2CardFactory("Thief").setResourceCost(WEAPONS, 17).addAction(new ThiefAction(game, 8)).addTo(game);
		new CWars2CardFactory("Recruit").setResourceCost(WEAPONS, 8).setMyEffect(RECRUITS, 1).addTo(game);
		new CWars2CardFactory("Guards").setResourceCost(WEAPONS, 7).setMyEffect(CWars2Res.WALL, 12).addTo(game);
		new CWars2CardFactory("Roadblock").setResourceCost(WEAPONS, 8).addAction(new MultiplyNextResourceIncome(game, 0, false)).addTo(game);
		new CWars2CardFactory("All Weapons").setResourceCost(WEAPONS, 1).addAction(new AllResourcesFocusOn(game, WEAPONS)).addTo(game);
//		new CWars2CardFactory("Sabotage").setResourceCost(weapons, 13).addTo(cards);
	}
	
	public static class AllResourcesFocusOn extends StackAction implements ResourceStrategy {

		private Resources resource;
		private CWars2Game	game;

		AllResourcesFocusOn() {}
		public AllResourcesFocusOn(CWars2Game game, Resources resource) {
			this.game = game;
			this.resource = resource;
		}
		
		@Override
		protected void onPerform() {
			ResourceMap res = this.game.getCurrentPlayer().getResources();
			for (Producers prod : Producers.values()) {
				res.setResourceStrategy(prod, prod == resource.getProducer() ? this : new FixedResourceStrategy(0));
			}
		}

		@Override
		public int getResourceAmount(IResource type, ResourceMap map) {
			int total = 0;
			for (Entry<IResource, Integer> ee : map.getValues()) {
				if (ee.getKey() instanceof Producers) {
					total += ee.getValue();
				}
			}
			return total;
		}
		
	}
	public static class ProtectResourcesAction extends StackAction {

		private final CWars2Game game;

		public ProtectResourcesAction(CWars2Game game) {
			this.game = game;
		}
		@Override
		protected void onPerform() {
			final CWars2Player protectedPlayer = game.getCurrentPlayer();
			final ResourceListener listener = new ResListener(protectedPlayer);
			for (Resources type : Resources.values())
				game.getCurrentPlayer().getResources().setListener(type, listener);
		}
		
		public static class ResListener implements ResourceListener {
			private final CWars2Player	protectedPlayer;
			ResListener() {this(null); }
			public ResListener(CWars2Player protectedPlayer) {
				this.protectedPlayer = protectedPlayer;
			}
			@Override
			public boolean onResourceChange(ResourceMap map, IResource type, int value) {
				if (value > 0) return true;
				if (protectedPlayer.getGame().getCurrentPlayer() == protectedPlayer) return true;
				return false;
			}
		}
		
	}
	public static class MagicAttackMultiply extends StackAction implements ResourceListener {
		private final boolean	opponent;
		private final double	multiplier;
		private final CWars2Game	game;

		MagicAttackMultiply() { this(null, false, 0); }
		public MagicAttackMultiply(CWars2Game game, boolean opponent, double multiplier) {
			this.opponent = opponent;
			this.multiplier = multiplier;
			this.game = game;
		}
		
		@Override
		protected void onPerform() {
			CWars2Player player = opponent ? game.getCurrentPlayer().getNextPlayer() : game.getCurrentPlayer();
			ResourceListener old = player.getResources().getListener(CWars2Res.CASTLE);
			if (old instanceof MagicAttackMultiply) {
				MagicAttackMultiply previous = (MagicAttackMultiply) old;
				if (previous == this) // Don't allow Magic Weapon twice
					return;
				// opponent setting does not matter here really, neither does game. We just want the ResourceListener-part.
				MagicAttackMultiply listener = new MagicAttackMultiply(null, false, multiplier * previous.multiplier);
				player.getResources().setListener(CWars2Res.CASTLE, listener);
				player.getResources().setListener(CWars2Res.WALL, listener);
			}
			else {
				player.getResources().setListener(CWars2Res.CASTLE, this);
				player.getResources().setListener(CWars2Res.WALL, this);
			}
		}
		
		@Override
		public boolean onResourceChange(ResourceMap map, IResource type, int value) {
			map.setListener(CWars2Res.CASTLE, null);
			map.setListener(CWars2Res.WALL, null);
			int change = (int)(value * multiplier);
			map.changeResources(type, change);
			return false;
		}
	}
	
	public static class MultiplyNextResourceIncome extends StackAction implements ResourceStrategy {
		
		private CWars2Game game;
		private int	multiplier;
		private boolean	currentPlayer;

		MultiplyNextResourceIncome() {}
		public MultiplyNextResourceIncome(CWars2Game game, int multiplier, boolean currentPlayer) {
			this.game = game;
			this.multiplier = multiplier;
			this.currentPlayer = currentPlayer;
		}

		@Override
		protected void onPerform() {
			ResourceMap res = currentPlayer ? game.getCurrentPlayer().getResources() : ((CWars2Player) game.getCurrentPlayer().getOpponents().get(0)).getResources();
			for (Producers prod : Producers.values()) {
				res.setResourceStrategy(prod, this);
			}
		}

		@Override
		public int getResourceAmount(IResource type, ResourceMap map) {
			int value = new ResourceMap(map).getResources(type);
			return value * multiplier;
		}
	}
	
	private static class ThiefAction extends StackAction {

		private CWars2Game	game;
		private int	steal;

		public ThiefAction(CWars2Game game, int steal) {
			this.game = game;
			this.steal = steal;
		}
		
		@Override
		protected void onPerform() {
			for (Resources res : Resources.values()) {
				CWars2Player next = (CWars2Player) game.getCurrentPlayer().getOpponents().get(0);
				int oldValue = next.getResources().getResources(res);
				int take = Math.min(oldValue, steal);
				next.getResources().changeResources(res, -take);
				int newValue = next.getResources().getResources(res);
				game.getCurrentPlayer().getResources().changeResources(res, -(newValue - oldValue));
			}
		}
		
	}

}
