package GameStateChangedEvent;

import objectmodel.BonusCard;
import objectmodel.CardsDeck;
import objectmodel.Player;
import main.GameStates;

public class GameStateChangedToPlayerDrewCardEvent extends GameStateChangedToPlayerActionEvent {
	BonusCard card;
	CardsDeck fromDeck;
	
	public GameStateChangedToPlayerDrewCardEvent(Object source, GameStates previousState, GameStates newState, 
			String message, Player player, BonusCard card, CardsDeck fromDeck)
	{
		super(source, previousState, newState, message, player);
		this.card = card;
		this.fromDeck = fromDeck;
	}
	
	public BonusCard getCard()
	{
		return card;
	}
	
	public CardsDeck getDeck()
	{
		return fromDeck;
	}
}