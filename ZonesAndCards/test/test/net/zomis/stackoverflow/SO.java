package test.net.zomis.stackoverflow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class SO
{   
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
	public static class SaveMe2 extends SaveMe {
	}
	
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
	public static class SaveMe {
		public String name;
	}
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
	public static class StoreMe {
		public List<SaveMe> saves = new ArrayList<SO.SaveMe>();
		public Set<SaveMe> otherSaves = new HashSet<SO.SaveMe>();
	}
	
	@Test
	public void sda() {
		StoreMe data = new StoreMe();
		for (int i = 0; i < 3; i++) {
			SaveMe save = new SaveMe2();
			save.name = "Save#" + i;
			data.saves.add(save);
			data.otherSaves.add(save);
		}
		String str = "";
		try {
			str = mapper().writeValueAsString(data);
		}
		catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		System.out.println(data);
		System.out.println(str);
		
		StoreMe newMe = null;
		try {
			newMe = mapper().readValue(str, StoreMe.class);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.out.println(newMe);
	}
	
	
	private ObjectMapper mapper() {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	    mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
	    return mapper;
	}


	@Test
	@Ignore
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