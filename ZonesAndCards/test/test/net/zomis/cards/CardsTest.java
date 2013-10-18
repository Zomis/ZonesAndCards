package test.net.zomis.cards;

import static org.junit.Assert.assertEquals;
import junit.framework.Assert;
import net.zomis.cards.model.CardZone;
import net.zomis.custommap.CustomFacade;
import net.zomis.custommap.view.Log4jLog;
import net.zomis.custommap.view.swing.ZomisSwingLog4j;

import org.junit.Before;
import org.junit.BeforeClass;

public abstract class CardsTest<E> {

	@BeforeClass
	public static void beforeClass() {
		if (!CustomFacade.isInitialized()) {
//			ZomisSwingLog4j.addConsoleAppender(null);
			ZomisSwingLog4j.addConsoleAppender(Log4jLog.DETAILED_LAYOUT);
			new CustomFacade(new Log4jLog("Cards"));
		}
	}

	protected void assertZoneSize(int expectedSize, CardZone zone) {
		assertEquals("Unexpected zone size " + zone + " containing " + zone.cardList(), expectedSize, zone.size());
	}
	
	protected E	game;
	
	@Before
	public final void before() {
		E local = newTestObject();
		this.onBefore();
		if (this.game == null)
			this.game = local;
		if (this.game == null)
			Assert.fail("Game not initialized");
	}
	
	protected void onBefore() {
	}

	public E getGame() {
		return game;
	}
	
	protected E newTestObject() {
		return null;
	}
	
}
