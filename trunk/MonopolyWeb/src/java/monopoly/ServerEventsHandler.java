/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package monopoly;

import comm.Event;
import java.util.List;
import objectmodel.CellBase;
import objectmodel.ServerEvents;
import src.client.Server;

/**
 *
 * @author Benda & Eizenman
 */
public class ServerEventsHandler
{
    private static final String BUY_CELL_WINDOW_TITLE = "Buy Asset Option";
    private static final String BUY_CELL_QUESTION_PREFIX = "Would you like to buy ";
	
    private static final String BUILD_HOUSE_WINDOW_TITLE = "Build A House";
    private static final String BUILD_HOUSE_QUESTION_PREFIX = "Would you like to build a house at ";

    private String gameName;
    private int lastEventId = 0;
    private String playerName;
    private WebClient client;

    public ServerEventsHandler(String gameName, WebClient client, String playerName) {
        this.gameName = gameName;
        this.client = client;
        this.playerName = playerName;
    }

    public void getNewEvents() {
        if (gameName != null)
        {
            List<Event> events = null;
            try {
                events = Server.getInstance().getAllEvents(lastEventId);
            }
            catch (Exception ex)
            {
                client.setConnectionFailed();
                return;
            }
            if (events == null || events.isEmpty())
                return;
            
            for (Event event : events)
            {
                lastEventId = Math.max(lastEventId, event.getEventID());
                switch (event.getEventType())
                {
                    case ServerEvents.GameStart:
                        // game started
                        try {
                        client.initUI(Server.getInstance().getPlayersDetails(gameName));
                        }
                        catch (Exception ex) {
                            client.setConnectionFailed();
                            return;
                        }
                        break;
                    case ServerEvents.GameOver:
                        // game ended
                        client.addMessageToClient(Utils.GenerateGameOverMessage(client.getContext()));
                        break;
                    case ServerEvents.GameWinner:
                        // game winner
                        String winnerName = event.getPlayerName().getValue();
                        client.addMessageToClient(Utils.GenerateGameWinnerMessage(client.getContext(), winnerName));
                        break;
                    case ServerEvents.PlayerResigned:
                    case ServerEvents.PlayerLost:
                        // player lost
                        String playerName = event.getPlayerName().getValue();
                        int playerID = client.getPlayerIDByName(playerName);
                        int cellID = event.getBoardSquareID();
                        client.addMessageToClient(Utils.GeneratePlayerLostMessage(client.getContext(), playerID, playerName, event.getEventType() == ServerEvents.PlayerResigned));
                        //board.UpdatePlayerLost(playerName, cellID,
                        //        event.getEventType() == ServerEvents.PlayerResigned);
                        break;
                    case ServerEvents.PromptPlayerToRollDice:
                        // Prompt Player to Roll Dice
                        playerName = event.getPlayerName().getValue();
                        playerID = client.getPlayerIDByName(playerName);
                        client.addMessageToClient(Utils.GenerateSetPlayerTurnMessage(client.getContext(), playerID));
                        //board.SetPlayingUser(event.getPlayerName().getValue());

                        if (event.getPlayerName().getValue().compareTo(this.playerName) != 0)
                            return;

                        client.addMessageToClient(Utils.GenerateThrowDiceRequest(client.getContext(), false));
                        break;
                    case ServerEvents.DiceRoll:
                        // dice roll
                        playerName = event.getPlayerName().getValue();
                        playerID = client.getPlayerIDByName(playerName);
                        client.addMessageToClient(Utils.GenerateSetPlayerTurnMessage(client.getContext(), playerID));
                        // simulate the dice throw
                        client.addMessageToClient(Utils.GenerateSendDiceValueMessage(client.getContext(), event.getFirstDiceResult(), event.getSecondDiceResult()));
                        //board.SimulateDiceThrow(event.getFirstDiceResult(), event.getSecondDiceResult());
                        break;
                    case ServerEvents.Move:
                        // move player
                        // move the player on the board
                        playerName = event.getPlayerName().getValue();
                        playerID = client.getPlayerIDByName(playerName);
                        client.setPlayerJailStatus(playerName, false);
                        client.addMessageToClient(Utils.GenerateMovePlayerMessage(client.getContext(), playerID, event.getBoardSquareID(), event.getNextBoardSquareID()));
//                        board.MovePlayer(event.getPlayerName().getValue(),
//                                event.getBoardSquareID(),
//                                event.getNextBoardSquareID());
                        break;
                    case ServerEvents.PassedStartSquare:
                        // Passed Start Square
                        break;
                    case ServerEvents.LandedOnStartSquare:
                        // Landed on Start Square
                        break;
                    case ServerEvents.GoToJail:
                        // Go to Jail (only logically)
                        playerName = event.getPlayerName().getValue();
                        client.setPlayerJailStatus(playerName, true);
                        //board.setPlayerInJailLogicly(event.getPlayerName().getValue());
                        break;
                    case ServerEvents.PromptPlayerToBuyAsset:
                        // Prompt Player to Buy Asset
                        if (event.getPlayerName().getValue().compareTo(this.playerName) != 0)
                            return;

                        client.setPendingEventID(event.getEventID());
                        final CellBase landCell = client.getCellBase(event.getBoardSquareID());
                        client.addMessageToClient(Utils.GenerateBuyAssetRequest(client.getContext(), landCell));
                        break;
                    case ServerEvents.PromptPlayerToBuyHouse:
                        // Prompt Player to Buy House
                        if (event.getPlayerName().getValue().compareTo(this.playerName) != 0)
                            return;

                        client.setPendingEventID(event.getEventID());
                        final CellBase landCellToBuildHouse = client.getCellBase(event.getBoardSquareID());
                        client.addMessageToClient(Utils.GenerateBuildHouseRequest(client.getContext(), landCellToBuildHouse));
                        break;
                    case ServerEvents.AssetBought:
                        // Asset bought message
                        playerName = event.getPlayerName().getValue();
                        playerID = client.getPlayerIDByName(playerName);
                        client.setAssetOwner(event.getPlayerName().getValue(), event.getBoardSquareID());
                        client.addMessageToClient(Utils.GenerateAssetBoughtMessage(client.getContext(), playerID, event.getBoardSquareID()));
                        break;
                    case ServerEvents.HouseBought:
                        // House bought message
                        playerName = event.getPlayerName().getValue();
                        playerID = client.getPlayerIDByName(playerName);
                        client.SetHouseBuilt(event.getPlayerName().getValue(), event.getBoardSquareID());
                        client.addMessageToClient(Utils.GenerateHouseBuiltMessage(client.getContext(), playerID, event.getBoardSquareID()));
                        break;
                    case ServerEvents.SurpriseCard:
                        // Surprise Card
                        client.addMessageToClient(Utils.GenerateGeneralMessage(client.getContext(), "Chance Card", event.getEventMessage().getValue()));
                        break;
                    case ServerEvents.WarrantCard:
                        // Warrant Card
                        client.addMessageToClient(Utils.GenerateGeneralMessage(client.getContext(), "Community Chest Card", event.getEventMessage().getValue()));
                        break;
                    case ServerEvents.GetOutOfJailCard:
                        // Get Out Of Jail Card
                        client.addMessageToClient(Utils.GenerateGeneralMessage(client.getContext(), "Chance Card", event.getEventMessage().getValue()));
                        break;
                    case ServerEvents.Payment:
                        // Payment
                        int amountPaid = event.getPaymentAmount();

                        if (event.isPaymemtFromUser())
                        {
                            String from = event.getPlayerName().getValue();
                            if (event.isPaymentToOrFromTreasury())
                            {
                                client.TransferMoneyToBank(from, amountPaid);
                            }
                            else
                            {
                                String to = event.getPaymentToPlayerName().getValue();
                                client.TransferMoneyFromUserToUser(from, to, amountPaid);
                            }
                        }
                        else
                        {
                            String to = event.getPaymentToPlayerName().getValue();
                            client.TransferMoneyFromBank(to, amountPaid);
                        }
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
}
