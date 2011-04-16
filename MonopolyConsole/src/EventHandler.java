import GameStateChangedEvent.GameStateChangedEvent;
import main.GameStates;
import main.MonopolyGame;
import main.StateManager.GameStateChangedEventListener;

/**
 * 
 * @author Benda & Eizenman
 * Handles all events triggered from the logic
 * In this case registered only to the base event listener to log all the messages to the console
 * 
 */
public class EventHandler
{
	/**
	 * Registers to the events in the logic
	 * @param monopolyGame - the logic
	 */
	public void registerEvents(MonopolyGame monopolyGame)
	{
		monopolyGame.getStateManager().registerToGameChangedEvent(GameStateChangedEventListener.class, new GameStateChangedEventListener() {
			
			@Override
			public void gameStateChanged(GameStateChangedEvent evt) {
				writeNewState(evt.getNewState(), evt.getMessage());
			}
		});
	}
	
	/**
	 * Write the new state to the console
	 * @param newState - the new state
	 * @param message - the message triggered with the new state
	 */
	private void writeNewState(GameStates newState, String message)
	{
		System.out.println(newState + ": " + message);
	}
}
