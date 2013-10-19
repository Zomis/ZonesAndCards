package test.net.zomis.cards;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import junit.framework.Assert;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.util.IResource;
import net.zomis.cards.util.ResourceMap;
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

	protected void assertResourceMapNoStrategies(ResourceMap resources) {
		for (Entry<IResource, Integer> ee : resources.getMapData().entrySet()) {
			assertEquals("A strategy is messing with the values for " + ee.getKey() + " in resource map " + resources,
				(int)ee.getValue(), resources.getResources(ee.getKey()));
		}
	}

	protected void assertResourceMapNoListeners(ResourceMap resources) {
		for (Entry<IResource, Integer> ee : resources.getMapData().entrySet()) {
			assertNull("Resource Map " + resources + " has listener for " + ee.getKey(), resources.getListener(ee.getKey()));
		}
	}
	protected void assertResourceDifference(ResourceMap oldValues, ResourceMap newValues, IResource res, int change) {
		int actualChange = newValues.getResources(res) - oldValues.getResources(res);
		assertEquals("Unexpected resource difference: " + oldValues + " vs. " + newValues + " when comparing " + res + " (expected change of " + change + " but was " + actualChange + ")", 
			oldValues.getResources(res) + change, 
			newValues.getResources(res)
		);
	}
	protected void assertResourcesEqual(ResourceMap map1, ResourceMap map2) {
		assertResourcesEqual(map1, map2, new IResource[]{});
	}
	protected void assertResourcesEqual(ResourceMap map1, ResourceMap map2, IResource[] ignore) {
		List<IResource> ignoreList = Arrays.asList(ignore);
		assertEquals("Resource Size not same: " + map1 + " vs. " + map2, map1.getMapData().size(), map2.getMapData().size());
		for (Entry<IResource, Integer> ee : map1.getMapData().entrySet()) {
			if (ignoreList.contains(ee.getKey()))
				continue;
			
			assertResourceDifference(map1, map2, ee.getKey(), 0);
//			assertEquals("Resource not same: " + map1 + " vs. " + map2 + " when comparing " + ee.getKey(), ee.getValue(), map2.getMapData().get(ee.getKey())); // this ignores resource strategies.
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
