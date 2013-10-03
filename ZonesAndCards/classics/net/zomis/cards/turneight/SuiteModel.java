package net.zomis.cards.turneight;

import net.zomis.cards.classics.Suite;
import net.zomis.cards.model.CardModel;

public class SuiteModel extends CardModel {

	private final Suite	suite;

	public SuiteModel(Suite suite) {
		super(suite.toString());
		this.suite = suite;
	}
	
	public Suite getSuite() {
		return suite;
	}

}
