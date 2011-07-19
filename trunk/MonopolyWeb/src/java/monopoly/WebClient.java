/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package monopoly;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import objectmodel.Asset;
import objectmodel.CellBase;
import objectmodel.City;
import objectmodel.GameBoard;
import objectmodel.PlayerDetails;
import org.xml.sax.SAXException;
import  src.client.*;

public class WebClient
{

    public static final String GAME_IN_PROGRESS_ERROR_MSG = "A game is in progress and the server supports only one conccurent game";
    public static final String SERVER_CONNECTION_ERROR_MSG = "An error has occured while connecting to the server";

    public enum  ClientState
    {
        Init,
        Creating,
        Joining,
        WaitingStart,
        Running,
        Failed
    }

    private ClientState clientState;
    private String playerName;
    private String gameName;
    private String serverMessage;
    private String waitingText = "";
    private ServerEventsHandler eventsHandler;
    private List<PlayerDetails> players;
    private GameBoard gameBoard;
    private ServletContext context;
    private LinkedList<String> messages;
    private int pendingEventID;

    public int getPendingEventID() {
        return pendingEventID;
    }

    public void setPendingEventID(int pendingEventID) {
        this.pendingEventID = pendingEventID;
    }

    public void setContext(ServletContext context) {
        this.context = context;
    }

    public ServletContext getContext() {
        return context;
    }

    public String getWaitingText() {
        return waitingText;
    }

    public void setWaitingText(String waitingText) {
        this.waitingText = waitingText;
    }

    public String getPlayerName() {
        return playerName;
    }

    public ClientState getClientState() {
        return clientState;
    }

    public String getGameName() {
        return gameName;
    }

    public String getServerMessage() {
        return serverMessage;
    }

    public WebClient()
    {
        clientState = ClientState.Init;
        messages = new LinkedList<String>();
    }

    public void setConnectionFailed()
    {
        serverMessage = SERVER_CONNECTION_ERROR_MSG;
        clientState = ClientState.Failed;
    }

    public int initClient()
    {
        List<String> waitingGames;
        try
        {
            waitingGames = Server.getInstance().getWaitingGames();
        }
        catch (Exception ex)
        {
            setConnectionFailed();
            return -1;
        }

        if (waitingGames.isEmpty())
        {
            boolean areThereRunningGames;
            try
            {
                areThereRunningGames = !Server.getInstance().getActiveGames().isEmpty();
            }
            catch (Exception ex)
            {
                setConnectionFailed();
                return -1;
            }

            if (areThereRunningGames)
            {
                serverMessage = GAME_IN_PROGRESS_ERROR_MSG;
                clientState = ClientState.Failed;
                return -2;
            }

            clientState = ClientState.Creating;
        }
        else
        {
            gameName = waitingGames.get(0);
            clientState = ClientState.Joining;
        }
        return  0;
    }

    public int createGame(String gameName, String numPlayersStr, String numHumansStr, String autoDiceStr)
    {
            if (gameName == null || gameName.trim().isEmpty())
            {
                serverMessage = "Empty Name Is Not Valid. Please Provide A Name For The New Game";
                return -2;
             }

            int numPlayers = 0;
            try
            {
                numPlayers = Integer.parseInt(numPlayersStr);
                if (numPlayers < Utils.MIN_PLAYERS || numPlayers > Utils.MAX_PLAYERS)
                {
                    serverMessage = "The number of players must be a number between 2-6";
                    return -2;
                }
            }
            catch (NumberFormatException ex)
            {
                serverMessage = "The number of players must be a number between 2-6";
                return -2;
            }

            int numHumans = 0;
            try
            {
                numHumans = Integer.parseInt(numHumansStr);
                if (numHumans < Utils.MIN_HUMANS || numHumans > Utils.MAX_HUMANS || numHumans > numPlayers)
                {
                    serverMessage = "The number of human players must be a number between 1-6 and must be less than the number of players";
                    return -2;
                }
            }
            catch (NumberFormatException ex)
            {
                serverMessage = "The number of human players must be a number between 1-6 and must be less than the number of players";
                return -2;
            }

            boolean automateRollDice = (autoDiceStr.compareTo("1") == 0);
            boolean success;

            try
            {
                 success = Server.getInstance().startNewGame(gameName, numHumans, numPlayers - numHumans, automateRollDice);
            }
            catch (Exception ex)
            {
                setConnectionFailed();
                return -1;
            }

            if (!success)
            {
                // show error message and suggest to try again
                serverMessage = "Something went wrong. It's possible that a game has already been created. Try Again Later";
                clientState = ClientState.Failed;
                return  -1;
            }
            else
            {
                this.gameName = gameName;
                clientState = ClientState.Joining;
                // show message that updates and states how many people are missing
            }
            return 0;
    }

    public int joinGame(String playerName)
    {
        if (clientState != ClientState.Joining)
        {
            
            return  -1;
        }

        if (playerName == null || playerName.trim().isEmpty())
        {
            serverMessage = "Empty Name Is Not Valid. Please Provide User Name To Join The Game '" + gameName + "'";
            return -2;
        }

        int playerId;
        try
        {
            playerId = Server.getInstance().joinPlayer(gameName, playerName);
        }
        catch (Exception ex)
        {
            setConnectionFailed();
            return -1;
        }

        if (playerId == -1)
        {
            // error occured, show message to try again with a different name
            serverMessage = "Something went wrong. Try Again";
            return -2;
        }
        else
        {
            this.playerName = playerName;
            // timer to show how many players are missing
            Server.getInstance().setPlayerId(playerId);
            //getAllEventsTimer = Server.getInstance().startPolling("Events Timer",
            eventsHandler =  new ServerEventsHandler(gameName, this, playerName);
            clientState = ClientState.WaitingStart;
            //usersLeftTimer = Server.getInstance().startPolling("Waiting For Players Timer",
            //            new WaitingForPlayersTask(gameName, this), 0, 1);
        }
        
        return 0;
    }

    public void initUI(final List<PlayerDetails> players)
    {
        this.players = players;

        String boardXML = null;
        String boardSchema = null;

        try {
            boardXML = Server.getInstance().getGameBoardXML();
            boardSchema = Server.getInstance().getGameBoardSchema();
        } catch (Exception ex) {
            setConnectionFailed();
            return;
        }

        JAXBContext jContext;
        gameBoard = null;
        try
        {
            jContext = JAXBContext.newInstance("objectmodel", objectmodel.ObjectFactory.class.getClassLoader());
            Unmarshaller unmarshaller = jContext.createUnmarshaller() ;
            SchemaFactory schemaFactory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
            InputStream xsdIs = new ByteArrayInputStream(boardSchema.getBytes("UTF-8"));
            StreamSource sourceStrm = new StreamSource(xsdIs);
            Schema schema = null;
            try {
                schema = schemaFactory.newSchema(sourceStrm);
            } catch (SAXException ex) {
                Logger.getLogger(WebClient.class.getName()).log(Level.SEVERE, null, ex);
                setConnectionFailed();
            }
            if (schema != null)
            {
                unmarshaller.setSchema(schema);
                unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
            }
            InputStream xmlIs = new ByteArrayInputStream(boardXML.getBytes("UTF-8"));
            gameBoard = (GameBoard)unmarshaller.unmarshal(xmlIs);
        }
        catch (JAXBException ex)
        {
            Logger.getLogger(WebClient.class.getName()).log(Level.SEVERE, null, ex);
            setConnectionFailed();
        }
        catch (UnsupportedEncodingException e)
        {
            Logger.getLogger(WebClient.class.getName()).log(Level.SEVERE, null, e);
            setConnectionFailed();
        }

        addMessageToClient(Utils.GenerateBoardJSON(gameBoard, context));
        addMessageToClient(Utils.GeneratePlayersJSON(players, context));

        clientState = ClientState.Running;
    }

    public void addMessageToClient(String message)
    {
        messages.addLast(message);
    }

    public String getMessageToClient()
    {
        String message = "";

        if (messages.size() > 0)
        {
            message = messages.removeFirst();
        }

        return message;
    }

    public int getPlayerIDByName(String playerName)
    {
        for(int i=0; i < players.size(); i++)
        {
            if (players.get(i).getName().compareTo(playerName) ==0)
                return i+1;
        }
        return 0;
    }

    public int sendDiceRollToServer(String dice1Str, String dice2Str)
    {
        int dice1,dice2;
        try
        {
            dice1 = Integer.parseInt(dice1Str);
            dice2 = Integer.parseInt(dice2Str);
        }
        catch (Exception exp)
        {
            serverMessage = "Enter valid dice numbers (1-6)!";
            addMessageToClient(Utils.GenerateThrowDiceRequest(context, true));
            return -2;
        }
        if (dice1 > Utils.DICE_MAX || dice1 < Utils.DICE_MIN || dice2 > Utils.DICE_MAX || dice2 < Utils.DICE_MIN)
        {
            serverMessage = "Enter valid dice numbers (1-6)!";
            addMessageToClient(Utils.GenerateThrowDiceRequest(context, true));
            return -2;
        }

        try
        {
            Server.getInstance().setDiceRollResults(pendingEventID, dice1, dice2);
        }
        catch (Exception ex) {
            setConnectionFailed();
            return -1;
        }

        return 0;
    }

    public int sendBuyReplyToServer(String buyReplyStr)
    {
        boolean buyReply = (buyReplyStr.compareToIgnoreCase(Utils.POSITIVE_BUY_REPLY) == 0);
        
        try
        {
            Server.getInstance().buy(pendingEventID, buyReply);
        }
        catch (Exception ex)
        {
            setConnectionFailed();
            return -1;
        }
        return 0;
    }

    public PlayerDetails GetPlayerDetails(String playerName)
    {
        PlayerDetails retValue = null;
        for (PlayerDetails p : players)
        {
            if (p.getName().compareTo(playerName) == 0)
            {
                retValue = p;
                break;
            }
        }
        return retValue;
    }

    public void setPlayerJailStatus(String playerName, boolean inJail)
    {
        PlayerDetails player = GetPlayerDetails(playerName);
        player.setInJail(inJail);
    }

    public void setAssetOwner(String playerName, Integer boardSquareID)
    {
        PlayerDetails player = GetPlayerDetails(playerName);
        CellBase currentPosition = getCellBase(boardSquareID);
        if (currentPosition instanceof Asset)
        {
            ((Asset)currentPosition).setOwner(player);
        }
        else
        {
            ((City)currentPosition).setOwner(player);
        }
    }

    public CellBase getCellBase(int currentPositionID)
    {
        for (CellBase cell : gameBoard.getCellBase())
        {
            if(cell.getID() == currentPositionID)
                return cell;
        }

        return null;
    }

    public void SetHouseBuilt(String playerName, Integer boardSquareID)
    {
        PlayerDetails player = GetPlayerDetails(playerName);
        final City city = (City)getCellBase(boardSquareID);
        city.IncrementNumberOfHouses();
    }

    public void TransferMoneyFromUserToUser(String from, String to, int amountPaid)
    {
        PlayerDetails fromPlayer = GetPlayerDetails(from);
        PlayerDetails toPlayer = GetPlayerDetails(to);
        fromPlayer.SubtractAmount(amountPaid);
        toPlayer.AddAmount(amountPaid);

        addMessageToClient(Utils.GenerateUpdatePlayerBalanceMessage(context, getPlayerIDByName(from), fromPlayer.getAmount()));
        addMessageToClient(Utils.GenerateUpdatePlayerBalanceMessage(context, getPlayerIDByName(to), toPlayer.getAmount()));
    }

    public void TransferMoneyToBank(String from, int amountPaid)
    {
        PlayerDetails fromPlayer = GetPlayerDetails(from);
        fromPlayer.SubtractAmount(amountPaid);

        addMessageToClient(Utils.GenerateUpdatePlayerBalanceMessage(context, getPlayerIDByName(from), fromPlayer.getAmount()));
    }

    public void TransferMoneyFromBank(String to, int amountPaid)
    {
        PlayerDetails toPlayer = GetPlayerDetails(to);
        toPlayer.AddAmount(amountPaid);

        addMessageToClient(Utils.GenerateUpdatePlayerBalanceMessage(context, getPlayerIDByName(to), toPlayer.getAmount()));
    }

    public void updateWaitMessage()
    {
        if (gameName != null)
        {
            int missing;

            try {
                missing = Server.getInstance().getGameDetails(gameName).getWaitingForPlayersNumber();
            } catch (Exception ex) {
                setConnectionFailed();
                return;
            }

            setWaitingText("Waiting For " + missing + " Players To Join The Game '" + gameName + "'");
        }
    }

    public void updateNewEvents()
    {
        eventsHandler.getNewEvents();
    }

    void resignFromGame() {
        if(clientState == ClientState.Running)
        {
            try {
                Server.getInstance().Resign();
            } catch (Exception ex)
            {
                Logger.getLogger(WebClient.class.getName()).log(Level.SEVERE, null, ex);
            }

            serverMessage = "You have left the game";
            clientState = ClientState.Failed;
        }
    }
}
