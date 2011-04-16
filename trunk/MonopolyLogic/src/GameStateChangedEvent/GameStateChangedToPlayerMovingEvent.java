package GameStateChangedEvent;

import objectmodel.CellBase;
import objectmodel.Player;
import main.GameStates;

public class GameStateChangedToPlayerMovingEvent extends GameStateChangedToPlayerActionEvent {
	CellBase originCell;
	CellBase destinationCell;
	
	public GameStateChangedToPlayerMovingEvent(Object source, GameStates previousState, GameStates newState, 
			String message, Player player, CellBase originCell, CellBase destinationCell)
	{
		super(source, previousState, newState, message, player);
		this.destinationCell = destinationCell;
		this.originCell = originCell;
	}
	
	public CellBase getDestinationCell()
	{
		return destinationCell;
	}
	
	public CellBase getOriginCell()
	{
		return originCell;
	}
}
