package test.net.zomis.general;

import junit.framework.Assert;
import net.zomis.custommap.CustomFacade;
import net.zomis.events.Event;
import net.zomis.events.EventExecutor;
import net.zomis.events.EventListener;
import net.zomis.events.IEvent;

import org.junit.Test;

import test.net.zomis.cards.CardsUntypedTest;

public class EventExecutorTest extends CardsUntypedTest {

	private class SimpleEvent implements IEvent {
		public SimpleEvent(String initial) {
			this.bubu = initial;
		}
		public String bubu;
	}
	
	@Test
	public void sorted() {
		EventExecutor exec = new EventExecutor();
		exec.registerListener(new SecondListener());
		exec.registerListener(new FirstListener());
//		CustomFacade.getLog().i("Bindings: " + exec.getBindings());
//		CustomFacade.getLog().i("List: " + exec.getListenersFor(SimpleEvent.class));
		SimpleEvent event = exec.executeEvent(new SimpleEvent("12345"));
		Assert.assertEquals("DONE", event.bubu);
	}
	
	public static class FirstListener implements EventListener {
		@Event(priority=49)
		public void onEvent(SimpleEvent event) {
//			Assert.assertEquals("12345", event.bubu);
			event.bubu = "firstDone";
			CustomFacade.getLog().i("Called: " + this.toString());
		}
		@Override
		public int hashCode() {
			return 2;
		}
	}
	public static class SecondListener implements EventListener {
		@Event(priority=49)
		public void onEvent(SimpleEvent event) {
//			Assert.assertEquals("firstDone", event.bubu);
			event.bubu = "DONE";
			CustomFacade.getLog().i("Called: " + this.toString());
		}
		@Override
		public int hashCode() {
			return 3;
		}
	}
	
}
