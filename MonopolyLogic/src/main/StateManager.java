package main;

import java.util.EventListener;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.event.EventListenerList;

import objectmodel.BonusCard;
import objectmodel.CardsDeck;
import objectmodel.CellBase;
import objectmodel.City;
import objectmodel.DiceThrowResult;
import objectmodel.Player;

import GameStateChangedEvent.GameStateChangedEvent;
import GameStateChangedEvent.GameStateChangedToPlayerActionEvent;
import GameStateChangedEvent.GameStateChangedToPlayerBuildingEvent;
import GameStateChangedEvent.GameStateChangedToPlayerBuyingEvent;
import GameStateChangedEvent.GameStateChangedToPlayerDrewCardEvent;
import GameStateChangedEvent.GameStateChangedToPlayerGettingOutOfJailEvent;
import GameStateChangedEvent.GameStateChangedToPlayerLandedEvent;
import GameStateChangedEvent.GameStateChangedToPlayerMovingEvent;
import GameStateChangedEvent.GameStateChangedToPlayerPaymentsEvent;
import GameStateChangedEvent.GameStateChangedToPlayerRollingEvent;
import GameStateChangedEvent.GameStateChangedToPromptPlayerActionEvent;
import GameStateChangedEvent.GameStateChangedToPromptPlayerBuildingEvent;
import GameStateChangedEvent.GameStateChangedToPromptPlayerBuyingEvent;
import Utils.WaitNotifyManager;

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
	 * @param message - a message regarding the new state
	 */
	private void innerSetCurrentState(GameStateChangedEvent newEvent)
	{
		// Change the state and raise the event
		currentState = newEvent.getNewState();
		TriggerEvent(newEvent);
	}
	
	/**
	 * Sets the current game state
	 * Triggers an event for the new state 
	 * @param source - the object who triggered the game state change
	 * @param newState - the new state
	 * @param message - a message regarding the new state
	 */
	public void setCurrentStateToInitializing(Object source, String message)
	{
		GameStateChangedEvent newEvent = new GameStateChangedEvent(this, currentState, GameStates.Initializing, message);
		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}
	
	/**
	 * Sets the current game state
	 * Triggers an event for the new state 
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 */
	public void setCurrentStateToInitialized(Object source, String message)
	{
		GameStateChangedEvent newEvent = new GameStateChangedEvent(this, currentState, GameStates.Initialized, message);
		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}
	
	/**
	 * Sets the current game state
	 * Triggers an event for the new state 
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 */
	public void setCurrentStateToUninitialized(Object source, String message)
	{
		GameStateChangedEvent newEvent = new GameStateChangedEvent(this, currentState, GameStates.Uninitialized, message);
		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}
	
	/**
	 * Sets the current game state
	 * Triggers an event for the new state 
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 */
	public void setCurrentStateToStarting(Object source, String message)
	{
		GameStateChangedEvent newEvent = new GameStateChangedEvent(this, currentState, GameStates.Starting, message);
		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}
	
	/**
	 * Sets the current game state
	 * Triggers an event for the new state 
	 * @param source - the object who triggered the game state change
	 * @param newState - the new state
	 * @param message - a message regarding the new state
	 */
	public void setCurrentStateToError(Object source, String message)
	{
                GameStateChangedEvent newEvent = new GameStateChangedEvent(this, currentState, GameStates.Initializing, message);
		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}
	
	/**
	 * Sets the current game state
	 * Triggers an event for the new state 
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param winner - winner of the game
	 */
	public void setCurrentStateToGameOver(Object source, String message, Player winner)
	{
		GameStateChangedToPlayerActionEvent newEvent = new GameStateChangedToPlayerActionEvent(this, currentState, 
				GameStates.GameOver, message, winner);
		
		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}
	
	/**
	 * Sets the current game state
	 * Triggers an event for the new state 
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 * @param diceThrow - dice throw results
	 */
	public void setCurrentStateToPlayerRolling(Object source, String message, Player player, DiceThrowResult diceThrow)
	{
		GameStateChangedToPlayerRollingEvent newEvent = new GameStateChangedToPlayerRollingEvent(this, currentState, 
				GameStates.PlayerRolling, message, player, diceThrow);
		
		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}
	
	/**
	 * Sets the current game state
	 * Triggers an event for the new state 
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 * @param numberOfSteps - number of steps to move. 
	 */
	public void setCurrentStateToPlayerMoving(Object source, String message, Player player, 
			CellBase origin, CellBase destination)
	{
		GameStateChangedToPlayerMovingEvent newEvent = new GameStateChangedToPlayerMovingEvent(this, currentState, 
				GameStates.PlayerMoving, message, player, origin, destination);
		
		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}
	
	/**
	 * Sets the current game state
	 * Triggers an event for the new state 
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 * @param numberOfSteps - number of steps to move. 
	 */
	public void setCurrentStateToPlayerGettingOutOfJail(Object source, String message, 
			Player player, GettingOutOfJailReason reason)
	{
		GameStateChangedToPlayerGettingOutOfJailEvent newEvent =
                        new GameStateChangedToPlayerGettingOutOfJailEvent(this, currentState,
				GameStates.PlayerGettingOutOfJail, message, player, reason);
		
		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}
	
	
	
	/**
	 * Sets the current game state
	 * Triggers an event for the new state 
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 * @param landingCell - cell at which user landed
	 */
	public void setCurrentStateToPlayerLanded(Object source, String message, Player player, CellBase landingCell)
	{
		GameStateChangedToPlayerLandedEvent newEvent = new GameStateChangedToPlayerLandedEvent(this, currentState, 
				GameStates.PlayerLanded, message, player, landingCell);
		
		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}
	
	/**
	 * Sets the current game state
	 * Triggers an event for the new state 
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 * @param boughtCell - cell which was bought
	 */
	public void setCurrentStateToPlayerBuying(Object source, String message, Player player, 
			CellBase boughtCell)
	{
		GameStateChangedToPlayerBuyingEvent newEvent = new GameStateChangedToPlayerBuyingEvent(this, currentState, 
				GameStates.PlayerBuying, message, player, boughtCell);
		
		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}

        /**
	 * Sets the current game state
	 * Triggers an event for the new state
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 * @param boughtCell - cell which was bought
	 */
	public void setCurrentStateToPlayerOfferBuying(Object source, String message, Player player,
			CellBase offeredCell, WaitNotifyManager eventHandler)
	{
		GameStateChangedToPromptPlayerBuyingEvent newEvent = new GameStateChangedToPromptPlayerBuyingEvent(this,
                        currentState, GameStates.PromptPlayerForBuying, message, player, offeredCell, eventHandler);

		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}
	
	/**
	 * Sets the current game state
	 * Triggers an event for the new state 
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 * @param amount - amount to pay
         * @param otherPlayer - player getting the money
	 */
	public void setCurrentStateToPlayerPayingToAnotherPlayer(Object source, String message, Player player,
			int amount, Player otherPlayer)
	{
		GameStateChangedToPlayerPaymentsEvent newEvent =
                        new GameStateChangedToPlayerPaymentsEvent(this, currentState,
				GameStates.PlayerPayment, message, player, amount, otherPlayer,
                                MoneyTransactionDirection.Paying);
		
		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}

        /**
	 * Sets the current game state
	 * Triggers an event for the new state
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 * @param amount - amount to pay
	 */
	public void setCurrentStateToPlayerPayingToBank(Object source, String message, Player player,
			int amount)
	{
		GameStateChangedToPlayerPaymentsEvent newEvent =
                        new GameStateChangedToPlayerPaymentsEvent(this, currentState,
				GameStates.PlayerPayment, message, player, amount, null,
                                MoneyTransactionDirection.Paying);

		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}

        /**
	 * Sets the current game state
	 * Triggers an event for the new state
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 * @param amount - amount to pay
	 */
	public void setCurrentStateToPlayerGettingPaidByTheBank(Object source, String message, Player player,
			int amount)
	{
		GameStateChangedToPlayerPaymentsEvent newEvent =
                        new GameStateChangedToPlayerPaymentsEvent(this, currentState,
				GameStates.PlayerPayment, message, player, amount, null,
                                MoneyTransactionDirection.GettingPaid);

		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}
	
	/**
	 * Sets the current game state
	 * Triggers an event for the new state 
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 * @param where - city at which building
	 */
	public void setCurrentStateToPlayerBuilding(Object source, String message, Player player, 
			City where)
	{
		GameStateChangedToPlayerBuildingEvent newEvent = new GameStateChangedToPlayerBuildingEvent(this, currentState, 
				GameStates.PlayerBuilding, message, player, where);
		
		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}

        /**
	 * Sets the current game state
	 * Triggers an event for the new state
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 * @param where - city at which building
	 */
	public void setCurrentStateToPlayerOfferBuilding(Object source, String message, Player player,
			City where, WaitNotifyManager eventHandler)
	{
		GameStateChangedToPromptPlayerBuildingEvent newEvent = new GameStateChangedToPromptPlayerBuildingEvent(this,
                        currentState, GameStates.PromptPlayerForBuilding, message, player, where, eventHandler);

		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}

        /**
	 * Sets the current game state
	 * Triggers an event for the new state
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 * @param where - city at which building
	 */
	public void setCurrentStateToPromptPlayerRollDice(Object source, String message, Player player,
			WaitNotifyManager eventHandler)
	{
		GameStateChangedToPromptPlayerActionEvent newEvent = new GameStateChangedToPromptPlayerActionEvent(this,
                        currentState, GameStates.PromptPlayerForRollingDice, message, player, eventHandler);

		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}
	
	/**
	 * Sets the current game state
	 * Triggers an event for the new state 
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 * @param msg - card text
	 * @param type - card type
	 */
	public void setCurrentStateToPlayerDrewCard(Object source, String message, Player player, 
			BonusCard card, CardsDeck fromDeck)
	{
		GameStateChangedToPlayerDrewCardEvent newEvent = new GameStateChangedToPlayerDrewCardEvent(this, currentState, 
				GameStates.PlayerDrewCard, message, player, card, fromDeck);
		
		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}
	
	/**
	 * Sets the current game state
	 * Triggers an event for the new state 
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 */
	public void setCurrentStateToPlayerBroke(Object source, String message, Player player)
	{
		GameStateChangedToPlayerActionEvent newEvent = new GameStateChangedToPlayerActionEvent(this, currentState, 
				GameStates.PlayerBroke, message, player);
		
		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}
	
	/**
	 * Sets the current game state
	 * Triggers an event for the new state 
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 */
	public void setCurrentStateToPlayerLost(Object source, String message, Player player)
	{
		GameStateChangedToPlayerActionEvent newEvent = new GameStateChangedToPlayerActionEvent(this, currentState, 
				GameStates.PlayerLost, message, player);
		
		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}

        /**
	 * Sets the current game state
	 * Triggers an event for the new state
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 */
	public void setCurrentStateToPlayerResigned(Object source, String message, Player player)
	{
		GameStateChangedToPlayerActionEvent newEvent = new GameStateChangedToPlayerActionEvent(this, currentState,
				GameStates.PlayerResigned, message, player);

		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}
	
	/**
	 * Sets the current game state
	 * Triggers an event for the new state 
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 */
	public void setCurrentStateToPlayerSwitching(Object source, String message, Player player)
	{
		GameStateChangedToPlayerActionEvent newEvent = new GameStateChangedToPlayerActionEvent(this, currentState, 
				GameStates.PlayerSwitching, message, player);
		
		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}

        /**
	 * Sets the current game state
	 * Triggers an event for the new state
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 */
	public void setCurrentStateToPlayerPassedStartSquare(Object source, String message, Player player)
	{
		GameStateChangedToPlayerActionEvent newEvent = new GameStateChangedToPlayerActionEvent(this, currentState,
				GameStates.PlayerPassedStartSquare, message, player);

		// Change the state and raise the event
		innerSetCurrentState(newEvent);
	}

        /**
	 * Sets the current game state
	 * Triggers an event for the new state
	 * @param source - the object who triggered the game state change
	 * @param message - a message regarding the new state
	 * @param player - player who made the action
	 */
	public void setCurrentStateToPlayerLandedOnStartSquare(Object source, String message, Player player)
	{
		GameStateChangedToPlayerActionEvent newEvent = new GameStateChangedToPlayerActionEvent(this, currentState,
				GameStates.PlayerLandedOnStartSquare, message, player);

		// Change the state and raise the event
		innerSetCurrentState(newEvent);
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
                        case PromptPlayerForRollingDice:
			case PlayerMoving:
			case PlayerGettingOutOfJail:
			case PlayerLanded:
			case PlayerBuying:
                        case PromptPlayerForBuying:
			case PlayerBidding:
			case PlayerPayment:
			case PlayerBuilding:
                        case PromptPlayerForBuilding:
			case PlayerDrewCard:
			case Auction:
			case PlayerBroke:
			case PlayerLost:
                        case PlayerResigned:
                        case PlayerPassedStartSquare:
                        case PlayerLandedOnStartSquare:
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
