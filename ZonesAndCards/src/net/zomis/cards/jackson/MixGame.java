package net.zomis.cards.jackson;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.phases.GamePhase;
import net.zomis.events.EventExecutor;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public abstract class MixGame extends CardGame<Player, CardModel> {

	@JsonProperty("zones")
	@JsonTypeInfo(use=Id.NONE)
	private final Set<CardZone<Card<?>>> zones = new HashSet<CardZone<Card<?>>>();
	
	@JsonProperty
	private GamePhase currentPhase;
	
	@Override
	@JsonIgnore
	public abstract GamePhase getActivePhase();
	
	@Override
	@JsonIgnore
	protected abstract void setActivePhase(GamePhase phase);
	@Override
	@JsonIgnore
	public abstract boolean isNextPhaseAllowed();
	
	@Override
	@JsonIgnore
	public abstract List<CardZone<?>> getPublicZones();
	
	@Override
	@JsonIgnore
	public abstract Player getCurrentPlayer();
	
	@JsonIgnore
	private EventExecutor events = new EventExecutor();

	@Override
	@JsonIgnore
	protected abstract EventExecutor getEvents();
	
}
