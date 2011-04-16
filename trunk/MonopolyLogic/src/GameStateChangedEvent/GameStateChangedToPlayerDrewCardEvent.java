package GameStateChangedEvent;

import objectmodel.BonusCard;
import objectmodel.Player;
import main.GameStates;

public class GameStateChangedToPlayerDrewCardEvent extends GameStateChangedToPlayerActionEvent {
	BonusCard card;
	
	public GameStateChangedToPlayerDrewCardEvent(Object source, GameStates previousState, GameStates newState, 
			String message, Player player, BonusCard card)
	{
		super(source, previousState, newState, message, player);
		this.card = card;
	}
	
	public BonusCard getCard()
	{
		return card;
	}
}