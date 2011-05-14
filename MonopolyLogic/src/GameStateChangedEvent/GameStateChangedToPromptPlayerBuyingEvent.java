package GameStateChangedEvent;

import Utils.WaitNotifyManager;
import objectmodel.CellBase;
import objectmodel.Player;
import main.GameStates;

public class GameStateChangedToPromptPlayerBuyingEvent extends GameStateChangedToPlayerBuyingEvent {
	WaitNotifyManager eventHandler;

	public GameStateChangedToPromptPlayerBuyingEvent(Object source, GameStates previousState, GameStates newState,
			String message, Player player, CellBase cellBought, WaitNotifyManager eventHandler)
	{
		super(source, previousState, newState, message, player, cellBought);
		this.eventHandler = eventHandler;

	}

	public CellBase getCell()
	{
		return cellBought;
	}

        public WaitNotifyManager GetEventHandler()
        {
                return eventHandler;
        }
}
