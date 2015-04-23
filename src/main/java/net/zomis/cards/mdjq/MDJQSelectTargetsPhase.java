package net.zomis.cards.mdjq;

import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.GamePhase;
import net.zomis.custommap.CustomFacade;


public class MDJQSelectTargetsPhase extends MDJQPhase {

	private GamePhase	nextPhase;

	public MDJQSelectTargetsPhase(MDJQPlayer pl) {
		super(pl, "Select-Targets");
		if (pl == null)
			throw new IllegalArgumentException("A player must be specified to determine who is responsible for selecting targets");
		
		this.nextPhase = pl.getGame().getActivePhase();
	}
	
	@Override
	public void onEnd(CardGame<?, ?> game) {
		CustomFacade.getLog().i("Ending temporary phase: " + this);
		MDJQGame g = (MDJQGame) game;
		g.setActivePhase(nextPhase);
	}

}
