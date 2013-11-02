package net.zomis.cards.hearts;

import net.zomis.IndexIterator;
import net.zomis.IndexIteratorStatus;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.events.game.GameOverEvent;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.util.IResource;
import net.zomis.cards.util.ResourceType;
import net.zomis.events.Event;
import net.zomis.events.EventListener;

public class HeartsSuperGame extends HeartsGame implements EventListener {

	private HeartsGiveDirection currentGive;
	private static final IResource score = new ResourceType("Points", 0);
	
	public HeartsSuperGame(String[] names) {
		super(HeartsGiveDirection.LEFT);
		this.currentGive = this.giveDirection;
		
		if (names.length != 4)
			throw new IllegalArgumentException("Must play Hearts with 4 players");
		
		for (String str : names) {
			this.addPlayer(str);
		}
		this.getEvents().registerListener(this);
	}
	
	@Override
	protected void onStart() {
		this.newGame();
	}
	
	private void newGame() {
		this.giveDirection = this.currentGive;
		for (Player player : this.getPlayers()) {
			CardPlayer cp = (CardPlayer) player;
			cp.getHand().moveToBottomOf(null);
			cp.getBoard().moveToBottomOf(null);
			cp.getBoard().setGloballyKnown(false);
		}
		super.onStart();
		int index = (currentGive.ordinal() + 1) % HeartsGiveDirection.values().length;
		currentGive = HeartsGiveDirection.values()[index];
	}
	
	@Event(priority=1)
	public void onGameEnd(GameOverEvent event) {
//			CustomFacade.getLog().i("Hearts round is over, previous points " + Arrays.toString(this.getScores()));
		int distributedPoints = 0;
		for (IndexIteratorStatus<Player> player : new IndexIterator<Player>(this.getPlayers())) {
			int inc = this.calcRealPoints((CardPlayer) player.getValue());
			distributedPoints += inc;
			player.getValue().getResources().changeResources(score, inc);
		}
//			CustomFacade.getLog().i("Hearts round is over, current points " + Arrays.toString(this.getScores()));
		if (distributedPoints != 26 && distributedPoints != 26 * (this.getPlayers().size() - 1))
			throw new AssertionError("Unexpected distributed points: " + distributedPoints);
			
		for (Player player : getPlayers()) {
			if (player.getResources().getResources(score) >= 100) return;
		}
		event.setCancelled(true);
		this.newGame();
	}

	public int[] getScores() {
		int[] arr = new int[getPlayers().size()];
		for (int i = 0; i < arr.length; i++)
			arr[i] = getPlayers().get(i).getResources().getResources(score);
		return arr;
	}
	
	@Override
	public StackAction processStackAction() {
		CardPlayer curr = this.getCurrentPlayer();
		if (curr != null && curr.getHand().size() > HeartsGame.MAGIC_NUMBER)
			throw new AssertionError("Where the **** did all those cards come from? " + curr.getHand().size() + ": " + curr.getHand());
		StackAction sup = super.processStackAction();
//		CustomFacade.getLog().d(curr + " - " + sup.toString());
		return sup;
	}
	
}
