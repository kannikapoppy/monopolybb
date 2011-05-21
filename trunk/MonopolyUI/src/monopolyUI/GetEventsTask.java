/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package src.monopolyUI;

import comm.Event;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import monopolyUI.Board;
import objectmodel.CellBase;
import objectmodel.GameBoard;
import objectmodel.ServerEvents;
import src.client.GameDetails;
import src.client.PlayerDetails;
import src.client.Server;

/**
 *
 * @author Benda & Eizenman
 */
public class GetEventsTask extends TimerTask
{
    private static final String BUY_CELL_WINDOW_TITLE = "Buy Asset Option";
    private static final String BUY_CELL_QUESTION_PREFIX = "Would you like to buy ";
	
    private static final String BUILD_HOUSE_WINDOW_TITLE = "Build A House";
    private static final String BUILD_HOUSE_QUESTION_PREFIX = "Would you like to build a house at ";

    private String gameName;
    private int lastEventId = 0;
    private Board board;
    private String playerName;

    public GetEventsTask(String gameName, Board board, String playerName) {
        this.gameName = gameName;
        this.board = board;
        this.playerName = playerName;
    }

    @Override
    public void run() {
        if (gameName != null) {
            List<Event> events = Server.getInstance().getAllEvents(lastEventId);
            if (events == null || events.isEmpty())
                return;
            
            for (Event event : events)
            {
                lastEventId = Math.max(lastEventId, event.getEventID());
                switch (event.getEventType())
                {
                    case ServerEvents.GameStart:
                        // game started
                        board.initUI(Server.getInstance().getPlayersDetails(gameName));
                        break;
                    case ServerEvents.GameOver:
                        // game ended
                        break;
                    case ServerEvents.GameWinner:
                        // game winner
                        try {
                            final String playerName = event.getPlayerName().getValue();
                            SwingUtilities.invokeAndWait(new Runnable() {
                                public void run()
                                    {
                                        JOptionPane.showMessageDialog(null,
                                            playerName + " won the game !!!",
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
                    case ServerEvents.PlayerResigned:
                    case ServerEvents.PlayerLost:
                        // player lost
                        String playerName = event.getPlayerName().getValue();
                        int cellID = event.getBoardSquareID();
                        board.UpdatePlayerLost(playerName, cellID);
                        break;
                    case ServerEvents.PromptPlayerToRollDice:
                        // Prompt Player to Roll Dice
                        if (event.getPlayerName().getValue().compareTo(this.playerName) != 0)
                            return;

                        board.SetPlayingUser(event.getPlayerName().getValue());
                        break;
                    case ServerEvents.DiceRoll:
                        // dice roll
                        board.SetPlayingUser(event.getPlayerName().getValue());
                        try { Thread.sleep(200); } catch (InterruptedException e) { }
                        // simulate the dice throw
                        board.SimulateDiceThrow(event.getFirstDiceResult(), event.getSecondDiceResult());
                        try { Thread.sleep(500); } catch (InterruptedException e) { }
                        break;
                    case ServerEvents.Move:
                        // move player
                        try { Thread.sleep(300); } catch (InterruptedException e) { }
                        // move the player on the board
                        board.MovePlayer(event.getPlayerName().getValue(),
                                event.getBoardSquareID(),
                                event.getNextBoardSquareID());

                        try { Thread.sleep(400); } catch (InterruptedException e) { }
                        break;
                    case ServerEvents.PassedStartSquare:
                        // Passed Start Square
                        break;
                    case ServerEvents.LandedOnStartSquare:
                        // Landed on Start Square
                        break;
                    case ServerEvents.GoToJail:
                        // Go to Jail
                        board.setPlayerInJailLogicly(event.getPlayerName().getValue());
                        break;
                    case ServerEvents.PromptPlayerToBuyAsset:
                        // Prompt Player to Buy Asset
                        if (event.getPlayerName().getValue().compareTo(this.playerName) != 0)
                            return;
                        
                        final boolean[] result = new boolean[1];
                        final CellBase landCell = board.GetCellBase(event.getBoardSquareID());
                        try {
                            SwingUtilities.invokeAndWait(new Runnable() {
				public void run()
                                {
                                    result[0] = JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,
                                        BUY_CELL_QUESTION_PREFIX + landCell.getName() + " ?",
                                        BUY_CELL_WINDOW_TITLE, JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE);
                                }
                                });
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Server.getInstance().buy(event.getEventID(), result[0]);
                        break;
                    case ServerEvents.PromptPlayerToBuyHouse:
                        // Prompt Player to Buy House
                        if (event.getPlayerName().getValue().compareTo(this.playerName) != 0)
                            return;

                        final boolean[] buyHouseResult = new boolean[1];
                        final CellBase landCellToBuildHouse = board.GetCellBase(event.getBoardSquareID());
			try {
                            SwingUtilities.invokeAndWait(new Runnable() {
				public void run()
                                {
                                    buyHouseResult[0] = JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,
                                        BUILD_HOUSE_QUESTION_PREFIX + landCellToBuildHouse.getName() + " ?",
			                BUILD_HOUSE_WINDOW_TITLE, JOptionPane.YES_NO_OPTION,
			                JOptionPane.QUESTION_MESSAGE);
                                }
                            });
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Server.getInstance().buy(event.getEventID(), buyHouseResult[0]);
                        break;
                    case ServerEvents.AssetBought:
                        // Asset bought message
                        board.SetAssetOwner(event.getPlayerName().getValue(), event.getBoardSquareID());
                        try { Thread.sleep(400); } catch (InterruptedException e) { }
                        break;
                    case ServerEvents.HouseBought:
                        // House bought message
                        board.SetHouseBuilt(event.getPlayerName().getValue(), event.getBoardSquareID());
                        try { Thread.sleep(400); } catch (InterruptedException e) { }
                        break;
                    case ServerEvents.SurpriseCard:
                        // Surprise Card
                        DisplayCard(event.getEventMessage().getValue(), "Chance");
                        try { Thread.sleep(400); } catch (InterruptedException e) { }
                        break;
                    case ServerEvents.WarrantCard:
                        // Warrant Card
                        DisplayCard(event.getEventMessage().getValue(), "Community Chest");
                        try { Thread.sleep(400); } catch (InterruptedException e) { }
                        break;
                    case ServerEvents.GetOutOfJailCard:
                        // Get Out Of Jail Card
                        DisplayCard(event.getEventMessage().getValue(), "Chance");
                        try { Thread.sleep(400); } catch (InterruptedException e) { }
                        break;
                    case ServerEvents.Payment:
                        // Payment
                        int amountPaid = event.getPaymentAmount();

                        if (event.isPaymemtFromUser())
                        {
                            String from = event.getPlayerName().getValue();
                            if (event.isPaymentToOrFromTreasury())
                            {
                                board.TransferMoneyToBank(from, amountPaid);
                            }
                            else
                            {
                                String to = event.getPaymentToPlayerName().getValue();
                                board.TransferMoneyFromUserToUser(from, to, amountPaid);
                            }
                        }
                        else
                        {
                            String to = event.getPaymentToPlayerName().getValue();
                            board.TransferMoneyFromBank(to, amountPaid);
                        }
                        try { Thread.sleep(400); } catch (InterruptedException e) { }
                        break;
                    case ServerEvents.PlayerUsedGetOutOfJailCard:
                        // Player used Get out of jail card
                        break;
                    default:
                        break;
                }
            }
        }
    }

    void DisplayCard(final String msg, final String deck)
    {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run()
                {
                    JOptionPane.showMessageDialog(null, msg, deck, JOptionPane.OK_OPTION);
                }
            });
	} catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	} catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	}
    }
}
