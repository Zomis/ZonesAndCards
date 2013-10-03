package test.net.zomis.cards.model;

import net.zomis.cards.simple.SimpleGame;
import net.zomis.custommap.CustomFacade;
import net.zomis.custommap.view.Log4jLog;
import net.zomis.custommap.view.swing.ZomisSwingLog4j;

import org.junit.Test;

public class CardTest {

	@Test
	public void fs() {
		ZomisSwingLog4j.addConsoleAppender(null);
		new CustomFacade(new Log4jLog("Cards"));
		new SimpleGame();
	}
	
}
