package GameStateChangedEvent;

import Utils.WaitNotifyManager;
import objectmodel.Player;
import main.GameStates;

public class GameStateChangedToPromptPlayerActionEvent extends GameStateChangedToPlayerActionEvent {
	WaitNotifyManager eventHandler;
	
	public GameStateChangedToPromptPlayerActionEvent(Object source, GameStates previousState, GameStates newState, 
			String message, Player player, WaitNotifyManager eventHandler)
	{
		super(source, previousState, newState, message, player);
		this.eventHandler = eventHandler;
		
	}
	
	public Player getPlayer()
	{
		return player;
	}

        public WaitNotifyManager GetEventHandler()
        {
                return eventHandler;
        }
}
