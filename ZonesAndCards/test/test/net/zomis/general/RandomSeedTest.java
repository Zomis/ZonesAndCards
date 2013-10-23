package test.net.zomis.general;

import static org.junit.Assert.*;

import java.util.Random;

import net.zomis.ZomisUtils;

import org.junit.Test;

public class RandomSeedTest {

	@Test
	public void random() {
		Random random = new Random();
		long seed = ZomisUtils.getSeed(random);
		int value = random.nextInt();
		
		random = new Random(seed);
		assertEquals(value, random.nextInt());
	}
	
}
