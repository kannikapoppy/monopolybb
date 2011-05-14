package GameStateChangedEvent;

import objectmodel.City;
import objectmodel.Player;
import main.GameStates;

public class GameStateChangedToPlayerBuildingEvent extends GameStateChangedToPlayerActionEvent {
	City where;

	public GameStateChangedToPlayerBuildingEvent(Object source, GameStates previousState, GameStates newState,
			String message, Player player, City where)
	{
		super(source, previousState, newState, message, player);
		this.where = where;

	}

	public City getCity()
	{
		return where;
	}
}
