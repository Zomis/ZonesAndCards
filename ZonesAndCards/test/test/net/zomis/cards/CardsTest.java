package test.net.zomis.cards;

import net.zomis.custommap.CustomFacade;
import net.zomis.custommap.view.Log4jLog;
import net.zomis.custommap.view.swing.ZomisSwingLog4j;

import org.junit.Before;
import org.junit.BeforeClass;

public abstract class CardsTest<E> {

	@BeforeClass
	public static void beforeClass() {
		if (!CustomFacade.isInitialized()) {
			ZomisSwingLog4j.addConsoleAppender(null);
			new CustomFacade(new Log4jLog("Cards"));
		}
	}

	private E	game;
	
	@Before
	public void before() {
		this.game = newTestObject();
	}
	
	public E getGame() {
		return game;
	}
	
	protected abstract E newTestObject();
	
}
