package main;

import java.util.EventListener;
import java.util.EventObject;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.event.EventListenerList;

/**
 * Manages the transitions of states in the game
 * @author Benda & Eizenman
 *
 */
public class StateManager
{
	/**
	 * The single instance of the states manager
	 */
	private static StateManager stateManager = null;
	/**
	 * a lock object for thread safety
	 */
	private static Lock lockObject = new ReentrantLock();
	
	/**
	 * Gets the single instance of the state manager
	 * @return
	 */
	public static StateManager getStateManager()
	{
		if (stateManager == null)
		{
			if (lockObject.tryLock())
			{
				if(stateManager == null)
				{
					stateManager = new StateManager();
				}
				lockObject.unlock();
			}
		}
		
		return stateManager;
	}
	
	/**
	 * General c'tor
	 */
	private StateManager()
	{
		currentState = GameStates.Uninitialized;
	}
	
	/**
	 * The current state of the game
	 */
	private GameStates currentState;
	/**
	 * A list of the event listeners of <ALL TYPES>
	 */
	private EventListenerList listenerList = new EventListenerList();
	
	/**
	 * Gets the current state
	 * @return
	 */
	public GameStates getCurrentState()
	{
		return currentState;
	}
	
	/**
	 * Sets the current game state
	 * Triggers an event for the new state 
	 * @param source - the object who triggered the game state change
	 * @param newState - the new state
	 * @param message - a message regarding the new state
	 */
	public void setCurrentState(Object source, GameStates newState, String message)
	{
		GameStateChangedEvent newEvent = new GameStateChangedEvent(this);
		newEvent.setPreviousState(currentState);
		newEvent.setNewState(newState);
		newEvent.setMessage(message);		
		// Change the state and raise the event
		currentState = newState;
		TriggerEvent(newEvent);
	}
	
	/**
	 * Register for a general event with a specific type of event listener
	 * @param listenerClass - the event listener type
	 * @param evtListener - the event listener
	 */
	public void registerToGameChangedEvent(Class listenerClass, GameStateChangedEventListener evtListener)
	{
		listenerList.add(listenerClass, evtListener);
	}
	
	/**
	 * Unregister the event listener
	 * @param evtListener
	 */
	public void unregisterFromGameChangedEvent(GameStateChangedEventListener evtListener)
	{
		listenerList.remove(GameStateChangedEventListener.class, evtListener);
	}
	
	/**
	 * Triggers the event raised
	 * Calls only the relevant listeners according to the new state
	 * @param newEvent - the new event to be raised
	 */
	private void TriggerEvent(GameStateChangedEvent newEvent)
	{
		GameStateChangedEventListener[] releventListeners = null;
		
		// The game state to listener types map
		switch(newEvent.getNewState())
		{
			case Error:
			case GameOver:
				releventListeners = listenerList.getListeners(GameOverEventListener.class);
				break;
			case Initializing:
			case Initialized:
			case Uninitialized:
				releventListeners = listenerList.getListeners(GameInitializationEventListener.class);
				break;
			case Starting:
				releventListeners = listenerList.getListeners(GameStartEventListener.class);
				break;
			case PlayerSelling:
			case PlayerRolling:
			case PlayerMoving:
			case PlayerLanded:
			case PlayerBuying:
			case PlayerBidding:
			case PlayerPaying:
			case PlayerGotPaid:
			case PlayerBuilding:
			case PlayerDrewCard:
			case Auction:
			case PlayerBroke:
			case PlayerLost:
				releventListeners = listenerList.getListeners(PlayerActionEventListener.class);
				break;
			case PlayerSwitching:
				releventListeners = listenerList.getListeners(TurnSwitchEventListener.class);
				break;
		}
		
		// notify the general event
		for(GameStateChangedEventListener listener : listenerList.getListeners(GameStateChangedEventListener.class))
		{ 
			listener.gameStateChanged(newEvent);
		}
		
		// notify the specific event
		for(GameStateChangedEventListener listener : releventListeners)
		{ 
			listener.gameStateChanged(newEvent);
		}
	}
	
	public class GameStateChangedEvent extends EventObject
	{
		public GameStates getNewState() {
			return newState;
		}

		public void setNewState(GameStates newState) {
			this.newState = newState;
		}

		public GameStates getPreviousState() {
			return previousState;
		}

		public void setPreviousState(GameStates previousState) {
			this.previousState = previousState;
		}
		
		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		private GameStates newState;
		private GameStates previousState;
		private String message;

		public GameStateChangedEvent(Object source)
		{
			super(source);
		}
	}
	
	public interface GameStateChangedEventListener extends EventListener
	{
		public void gameStateChanged(GameStateChangedEvent evt);
	}
	
	public interface GameInitializationEventListener extends GameStateChangedEventListener
	{
		public void gameStateChanged(GameStateChangedEvent evt);
	}
	
	public interface GameStartEventListener extends GameStateChangedEventListener
	{
		public void gameStateChanged(GameStateChangedEvent evt);
	}
	
	public interface TurnSwitchEventListener extends GameStateChangedEventListener
	{
		public void gameStateChanged(GameStateChangedEvent evt);
	}
	
	public interface PlayerActionEventListener extends GameStateChangedEventListener
	{
		public void gameStateChanged(GameStateChangedEvent evt);
	}
	
	public interface GameOverEventListener extends GameStateChangedEventListener
	{
		public void gameStateChanged(GameStateChangedEvent evt);
	}
	
	public enum StateTiming
	{
		BeforeChange,
		OnChange,
		AfterChange
	}
}
