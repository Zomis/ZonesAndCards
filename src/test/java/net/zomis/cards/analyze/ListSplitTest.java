package net.zomis.cards.analyze;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import net.zomis.cards.analyze2.ListSplit;

import org.junit.Test;

public class ListSplitTest {

	@Test
	public void sameList() {
		List<String> a = Arrays.asList("a", "b", "c");
		ListSplit<String> split = ListSplit.split(a, a);
		assertNull(split);
	}
	
	@Test
	public void allCommon() {
		List<String> a = Arrays.asList("a", "b", "c");
		List<String> b = Arrays.asList("a", "b", "c");
		ListSplit<String> split = ListSplit.split(a, b);
		assertSame(a, split.getBoth());
		assertTrue(split.getOnlyA().isEmpty());
		assertTrue(split.getOnlyB().isEmpty());
		assertFalse(split.splitPerformed());
	}
	
	@Test
	public void noFieldsInCommon() {
		List<String> a = Arrays.asList("a", "b", "c");
		List<String> b = Arrays.asList("d", "e", "f");
		ListSplit<String> split = ListSplit.split(a, b);
		assertNull(split);
	}
	
	@Test
	public void split() {
		List<String> a = Arrays.asList("a", "b", "c");
		List<String> b = Arrays.asList("b", "c", "d");
		ListSplit<String> split = ListSplit.split(a, b);
		assertEquals(Arrays.asList("a"), split.getOnlyA());
		assertEquals(Arrays.asList("b", "c"), split.getBoth());
		assertEquals(Arrays.asList("d"), split.getOnlyB());
		assertTrue(split.splitPerformed());
	}
	
}
