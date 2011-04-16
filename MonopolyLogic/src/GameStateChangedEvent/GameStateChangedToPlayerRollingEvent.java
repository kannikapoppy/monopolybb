package GameStateChangedEvent;

import objectmodel.Dice.DiceThrowResult;
import objectmodel.Player;
import main.GameStates;

public class GameStateChangedToPlayerRollingEvent extends GameStateChangedToPlayerActionEvent {
	DiceThrowResult diceThrow;
	
	public GameStateChangedToPlayerRollingEvent(Object source, GameStates previousState, GameStates newState, 
			String message, Player player, DiceThrowResult diceThrow)
	{
		super(source, previousState, newState, message, player);
		this.diceThrow = diceThrow;
		
	}
	
	public DiceThrowResult getDiceThrowResult()
	{
		return diceThrow;
	}
}
