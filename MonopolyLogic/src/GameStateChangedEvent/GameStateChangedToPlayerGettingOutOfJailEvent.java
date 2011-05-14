package GameStateChangedEvent;

import objectmodel.Player;
import main.GameStates;
import main.GettingOutOfJailReason;

public class GameStateChangedToPlayerGettingOutOfJailEvent extends GameStateChangedToPlayerActionEvent
{
        GettingOutOfJailReason reason;

	public GameStateChangedToPlayerGettingOutOfJailEvent(Object source, GameStates previousState, GameStates newState,
			String message, Player player, GettingOutOfJailReason reason)
	{
		super(source, previousState, newState, message, player);
		this.reason = reason;

	}

	public GettingOutOfJailReason getReason()
	{
		return reason;
	}
}
