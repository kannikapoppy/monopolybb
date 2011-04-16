package GameStateChangedEvent;

import objectmodel.CellBase;
import objectmodel.Player;
import main.GameStates;

public class GameStateChangedToPlayerLandedEvent extends GameStateChangedToPlayerActionEvent {
	CellBase landingCell;
	
	public GameStateChangedToPlayerLandedEvent(Object source, GameStates previousState, GameStates newState, 
			String message, Player player, CellBase landingCell)
	{
		super(source, previousState, newState, message, player);
		this.landingCell = landingCell;
		
	}
	
	public CellBase getLandingCell()
	{
		return landingCell;
	}
}
