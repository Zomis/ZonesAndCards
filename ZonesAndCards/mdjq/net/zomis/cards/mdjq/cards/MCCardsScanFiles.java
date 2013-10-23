package net.zomis.cards.mdjq.cards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class MCCardsScanFiles {

	private File	dir;

	public MCCardsScanFiles(File directory) {
		this.dir = directory;
	}
	
	private static ObjectMapper mapper() {
		XmlMapper xmlMapper = new XmlMapper();
	    SimpleModule module = new SimpleModule("EnhancedDatesModule", new Version(0, 1, 0, "alpha", "net.zomis", "cards"));
	    // functionality includes add mix-in annotations???
//	    module.addSerializer(Units.class, new UnitsSerializer());
//	    module.addSerializer(String.class, new StringSerializerWithout0A());
//	    module.addSerializer(FieldContainerActions.class, new ActionsSerializer());

		xmlMapper.registerModule(module);
		return xmlMapper;
	}
	
	public MDJQJackCards scanFor(final String cardName) {
		String searchName = "<name>" + cardName + "</name>";
//		fileloop:
		for (File file : dir.listFiles()) {
			Scanner scan = null;
			try {
				if (!file.isDirectory()) {
					scan = new Scanner(file);
					scan.useDelimiter("\\Z");
					String str;
					
					str = scan.findWithinHorizon(searchName, (int) file.length());
					if (str != null) {
//						CustomFacade.getLog().i("Scanning " + file.getName() + ": " + str);
						scan.close();
						MDJQJackCards value = mapper().readValue(file, MDJQJackCards.class);
						value.select(cardName);
						return value;
//						continue fileloop;
					}
				}
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				if (scan != null)
					scan.close();
			}
		}
		return new MDJQJackCards();
	}
	
}
