package GameStateChangedEvent;

import objectmodel.Player;
import main.GameStates;
import main.MoneyTransactionDirection;

public class GameStateChangedToPlayerPaymentsEvent extends GameStateChangedToPlayerActionEvent {
	int amount;
        Player OtherParicipiant;
        MoneyTransactionDirection direction;

	public GameStateChangedToPlayerPaymentsEvent(Object source, GameStates previousState, GameStates newState,
			String message, Player player, int amount, Player OtherParicipiant,
                        MoneyTransactionDirection direction)
	{
		super(source, previousState, newState, message, player);
		this.amount = amount;
                this.OtherParicipiant = OtherParicipiant;
                this.direction = direction;
	}

	public int getAmount()
	{
		return amount;
	}

        public Player GetOtherParicipiant()
        {
                return OtherParicipiant;
        }

        public MoneyTransactionDirection GetMoneyTransactionDirection()
        {
                return direction;
        }
}
