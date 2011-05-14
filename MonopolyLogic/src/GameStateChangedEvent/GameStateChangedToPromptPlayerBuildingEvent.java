package GameStateChangedEvent;

import Utils.WaitNotifyManager;
import objectmodel.City;
import objectmodel.Player;
import main.GameStates;

public class GameStateChangedToPromptPlayerBuildingEvent extends GameStateChangedToPlayerBuildingEvent {
	WaitNotifyManager eventHandler;

	public GameStateChangedToPromptPlayerBuildingEvent(Object source, GameStates previousState, GameStates newState,
			String message, Player player, City where, WaitNotifyManager eventHandler)
	{
		super(source, previousState, newState, message, player, where);
		this.eventHandler = eventHandler;

	}

	public City getCity()
	{
		return where;
	}

        public WaitNotifyManager GetEventHandler()
        {
                return eventHandler;
        }
}
