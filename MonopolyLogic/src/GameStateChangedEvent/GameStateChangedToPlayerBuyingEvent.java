package GameStateChangedEvent;

import objectmodel.CellBase;
import objectmodel.Player;
import main.GameStates;

public class GameStateChangedToPlayerBuyingEvent extends GameStateChangedToPlayerActionEvent {
	CellBase cellBought;

	public GameStateChangedToPlayerBuyingEvent(Object source, GameStates previousState, GameStates newState,
			String message, Player player, CellBase cellBought)
	{
		super(source, previousState, newState, message, player);
		this.cellBought = cellBought;

	}

	public CellBase getCell()
	{
		return cellBought;
	}
}
