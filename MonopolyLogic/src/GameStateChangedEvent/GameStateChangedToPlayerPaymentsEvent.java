package GameStateChangedEvent;

import objectmodel.Player;
import main.GameStates;

public class GameStateChangedToPlayerPaymentsEvent extends GameStateChangedToPlayerActionEvent {
	int amount;
	
	public GameStateChangedToPlayerPaymentsEvent(Object source, GameStates previousState, GameStates newState, 
			String message, Player player, int amount)
	{
		super(source, previousState, newState, message, player);
		this.amount = amount;
		
	}
	
	public int getAmount()
	{
		return amount;
	}
}
