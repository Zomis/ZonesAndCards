package test.net.zomis.stackoverflow;

import java.util.Random;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class SO
{   
	@Test
	public void sda() {
	}


	ObjectMapper mapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		return mapper;
	}


	//	@Test
	//	@Ignore
	public void esg() {
		for (int i = 0; i < 3; i++)
			test(i * 42 + 14);
	}

	public void test(int seed) {
		Random random = new Random(seed);
		int a = random.nextInt(100);
		int b = random.nextInt(100);

		System.out.println();
		out("a", a);
		out("b", b);
		out("megamoda", (a % b) % (a + 1));
		out("megamodb", (b % a) % (b + 1));
		out("mod1", a % b);
		out("mod2", b % a);
		out("modSum ", a % b + b % a);

		out("div1", a / b * b);
		out("div2", b / a * a);

	}

	void out(String string, int i) {
		System.out.println(string + ": " + i);
	}
}