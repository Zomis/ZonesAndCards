package test.net.zomis.cards.mdjq;

import static org.junit.Assert.*;

import java.io.File;

import net.zomis.cards.mdjq.MDJQGame;
import net.zomis.cards.mdjq.MDJQRes.MColor;
import net.zomis.cards.mdjq.cards.MCCardsScanFiles;
import net.zomis.cards.mdjq.cards.MDJQJacksonCard;
import net.zomis.cards.mdjq.cards.MDJQJackCards;
import net.zomis.custommap.CustomFacade;

import org.junit.Test;


public class MDJQReadFiles extends MDJQTest {

	private final MCCardsScanFiles scanner = new MCCardsScanFiles(new File("C:/Games/MagicAssistantWorkspace/magiccards/MagicDB"));
	
	@Test
	public void test() {
//		MCCardsScanFiles scanner = new MCCardsScanFiles(new File("C:/Games/MagicAssistant/_test"));
		
		MDJQJacksonCard jq = new MDJQJacksonCard();
		jq.setCost("{4}{W}{W}{W}{U}{B}{R}{G}{G}");
		assertMana(jq.getCost(), MColor.COLORLESS, 4);
		assertMana(jq.getCost(), MColor.WHITE, 3);
		assertMana(jq.getCost(), MColor.BLUE,  1);
		assertMana(jq.getCost(), MColor.BLACK, 1);
		assertMana(jq.getCost(), MColor.RED,   1);
		assertMana(jq.getCost(), MColor.GREEN, 2);
		
		String str = "Bant[^<]*";
		str = "Angel of Mercy";
		MDJQJackCards ret = scanner.scanFor(str);
		scanLog(str);
		assertNotNull("Still haven't found what I'm looking for.", ret);
		assertNotNull(ret.getSelected());
		assertTrue(ret.getSelected().name.matches(str));
		CustomFacade.getLog().i("Selected: " + ret.getSelected());
		CustomFacade.getLog().i("Selected: " + ret.getSelected().createCode());
		scanLog("Liliana Vess");

	}
	private void scanLog(String string) {
		CustomFacade.getLog().i(string + " - " + scanner.scanFor(string).getSelected());
	}
	@Override
	protected void onBefore() {
		game = new MDJQGame();
	}
	
}
