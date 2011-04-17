package services;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import GameStateChangedEvent.GameStateChangedEvent;
import GameStateChangedEvent.GameStateChangedToPlayerActionEvent;
import GameStateChangedEvent.GameStateChangedToPlayerBuildingEvent;
import GameStateChangedEvent.GameStateChangedToPlayerBuyingEvent;
import GameStateChangedEvent.GameStateChangedToPlayerDrewCardEvent;
import GameStateChangedEvent.GameStateChangedToPlayerMovingEvent;
import GameStateChangedEvent.GameStateChangedToPlayerPaymentsEvent;
import GameStateChangedEvent.GameStateChangedToPlayerRollingEvent;
import main.GameStates;
import main.MonopolyGame;
import main.StateManager.GameStateChangedEventListener;
import monopolyUI.Board;

/**
 * 
 * @author Benda & Eizenman
 * Handles all events triggered from the logic
 * In this case registered only to the base event listener to log all the messages to the console
 * 
 */
public class EventHandler
{
	private Board board = null;
	
	public EventHandler(Board board)
	{
		this.board = board;
	}
	
	/**
	 * Registers to the events in the logic
	 * @param monopolyGame - the logic
	 */
	public void registerEvents(MonopolyGame monopolyGame)
	{
		monopolyGame.getStateManager().registerToGameChangedEvent(GameStateChangedEventListener.class, new GameStateChangedEventListener() {
			
			@Override
			public void gameStateChanged(GameStateChangedEvent evt) {
				HandleState(evt);
			}
		});
	}
	
	/**
	 * Write the new state to the console
	 * @param newState - the new state
	 * @param message - the message triggered with the new state
	 */
	private void HandleState(GameStateChangedEvent evt)
	{
		try { Thread.sleep(50); } catch (InterruptedException e) { }
		switch (evt.getNewState())
		{
			case PlayerSwitching:
				try { Thread.sleep(1500); } catch (InterruptedException e) { }
				break;
			case PlayerMoving:
				GameStateChangedToPlayerMovingEvent movingEvent = 
					(GameStateChangedToPlayerMovingEvent)evt;
				board.MovePlayer(movingEvent.getPlayer(), movingEvent.getOriginCell(),
						movingEvent.getDestinationCell());
				break;
			case PlayerRolling:
				GameStateChangedToPlayerRollingEvent rollingEvent = 
					(GameStateChangedToPlayerRollingEvent)evt;
				board.SimulateDiceThrow(rollingEvent.getDiceThrowResult());
				break;
			case WaitingForPlayerToRoll:
				board.EnableDiceThrow();
				break;
			case PlayerBuying:
				GameStateChangedToPlayerBuyingEvent buyingEvent = 
					(GameStateChangedToPlayerBuyingEvent)evt;
				board.SetCellOwner(buyingEvent.getBoughtCell(), buyingEvent.getPlayer());
				board.UpdateBalance(buyingEvent.getPlayer());
				break;
			case PlayerBuilding:
				GameStateChangedToPlayerBuildingEvent buildingEvent = 
					(GameStateChangedToPlayerBuildingEvent)evt;
				board.BuildHouse(buildingEvent.getCity());
				break;
			case PlayerPaying:
				GameStateChangedToPlayerPaymentsEvent payEvent = 
					(GameStateChangedToPlayerPaymentsEvent)evt;
				board.UpdateBalance(payEvent.getPlayer());
				break;
			case PlayerGotPaid:
				GameStateChangedToPlayerPaymentsEvent gotPaidEvent = 
					(GameStateChangedToPlayerPaymentsEvent)evt;
				board.UpdateBalance(gotPaidEvent.getPlayer());
				break;
			case PlayerGettingOutOfJail:
				GameStateChangedToPlayerActionEvent outOfJailEvent = 
					(GameStateChangedToPlayerActionEvent)evt;
				board.GetPlayerOutOfJail(outOfJailEvent.getPlayer());
				break;
			case PlayerDrewCard:
				final GameStateChangedToPlayerDrewCardEvent drewCardEvent = 
					(GameStateChangedToPlayerDrewCardEvent)evt;
				
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() 
					    { 
							JOptionPane.showMessageDialog(null, 
									drewCardEvent.getCard().getMessage(),
									drewCardEvent.getDeck().getType(), JOptionPane.OK_OPTION);
					    }
					});
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				break;
			case PlayerLost:
				GameStateChangedToPlayerActionEvent playerLostEvent = 
					(GameStateChangedToPlayerActionEvent)evt;
				board.UpdatePlayerLost(playerLostEvent.getPlayer());
				break;
			case GameOver:
				final GameStateChangedToPlayerActionEvent finishEvent = 
					(GameStateChangedToPlayerActionEvent)evt;
				
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() 
					    { 
							JOptionPane.showMessageDialog(null, 
									finishEvent.getPlayer().getName() + " won the game !!!",
									"VICTORY !!!", JOptionPane.OK_OPTION);
					    }
					});
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				break;
		}
	}
}
