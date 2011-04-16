package GameStateChangedEvent;

import objectmodel.Player;
import main.GameStates;

public class GameStateChangedToPlayerActionEvent extends GameStateChangedEvent {
	Player player;
	
	public GameStateChangedToPlayerActionEvent(Object source, GameStates previousState, GameStates newState, 
			String message, Player player)
	{
		super(source, previousState, newState, message);
		this.player = player;
		
	}
	
	public Player getPlayer()
	{
		return player;
	}
}
