package net.zomis.cards.wart;

import static org.junit.Assert.*;
import net.zomis.cards.CardsTest;
import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.CompGameFactory;
import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.BattlefieldComponent;
import net.zomis.cards.components.HealthComponent;
import net.zomis.cards.components.ManaComponent;
import net.zomis.cards.iface.Component;

import org.junit.Ignore;
import org.junit.Test;

public class WSComponentTest extends CardsTest<FirstCompGame> {

	@Test
	public void startWithOneMana() {
		ManaComponent mana = playerComp(ManaComponent.class);
		assertEquals(1, mana.getMana());
		assertEquals(1, mana.getMaxMana());
	}
	
	@Test
	public void increasingMana() {
		int mana = game.getCurrentPlayer().getRequiredComponent(ManaComponent.class).getMana();
		nextTurn();
		nextTurn();
		assertEquals(mana + 1, playerComp(ManaComponent.class).getMana());
		assertEquals("Incorrect MaxMana", playerComp(ManaComponent.class).getMana(), playerComp(ManaComponent.class).getMaxMana());
	}
	
	private <T extends Component> T playerComp(Class<T> class1) {
		return game.getCurrentPlayer().getRequiredComponent(class1);
	}

	@Test
	@Ignore
	public void attack() {
//		superPlayCard("Murloc Raider");
		CardWithComponents card = game.getCurrentPlayer().getRequiredComponent(BattlefieldComponent.class).getBattlefield().getTopCard();
		assertNotNull("Card did not enter the battlefield", card);
		nextTurn();
		nextTurn();

		CardWithComponents card2 = game.getCurrentPlayer().getRequiredComponent(BattlefieldComponent.class).getBattlefield().getTopCard();
		assertTrue(card == card2);
		assertNotNull("Card did not enter the battlefield", card);
		CompPlayer target = game.getCurrentPlayer().getNextPlayer();
		assertEquals(30, target.getRequiredComponent(HealthComponent.class).getHealth());
		use(card);
//		target(target.getPlayerCard());
		assertEquals(28, target.getRequiredComponent(HealthComponent.class).getHealth());
	}
	
	private void use(CardWithComponents card) {
		game.click(card);
	}

	private void nextTurn() {
		game.click(game.getActionZone().getTopCard());
	}

	@Override
	protected FirstCompGame newTestObject() {
		FirstCompGame gameObject = CompGameFactory.hearthstone();
		gameObject.startGame();
		return gameObject;
	}
	
}
