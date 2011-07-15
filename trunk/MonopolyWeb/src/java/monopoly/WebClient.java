/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package monopoly;

import java.util.List;
import  src.client.*;

/**
 *
 * @author Benda
 */
public class WebClient
{

    private static final String GAME_IN_PROGRESS_ERROR_MSG = "A game is in progress and the server supports only one conccurent game";
    private static final String SERVER_CONNECTION_ERROR_MSG = "An error has occured while connecting to the server";

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
            serverMessage = SERVER_CONNECTION_ERROR_MSG;
            clientState = ClientState.Failed;
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
                serverMessage = SERVER_CONNECTION_ERROR_MSG;
                clientState = ClientState.Failed;
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
                if (numPlayers <= 0 || numPlayers > 6)
                {
                    serverMessage = "The number of players must be a number between 1-6";
                    return -2;
                }
            }
            catch (NumberFormatException ex)
            {
                serverMessage = "The number of players must be a number between 1-6";
                return -2;
            }

            int numHumans = 0;
            try
            {
                numHumans = Integer.parseInt(numHumansStr);
                if (numHumans < 0 || numHumans > 6)
                {
                    serverMessage = "The number of human players must be a number between 0-6";
                    return -2;
                }
            }
            catch (NumberFormatException ex)
            {
                serverMessage = "The number of human players must be a number between 0-6";
                return -2;
            }

            boolean automateRollDice = (autoDiceStr.compareTo("True") == 0);
            boolean success;

            try
            {
                 success = Server.getInstance().startNewGame(gameName, numHumans, numPlayers - numHumans, automateRollDice);
            }
            catch (Exception ex)
            {
                serverMessage = SERVER_CONNECTION_ERROR_MSG;
                clientState = ClientState.Failed;
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
            serverMessage = SERVER_CONNECTION_ERROR_MSG;
            clientState = ClientState.Failed;
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
            //            new GetEventsTask(gameName, this, playerName), 0, 1);
            //clientState = ClientState.WaitingStart;
            //usersLeftTimer = Server.getInstance().startPolling("Waiting For Players Timer",
            //            new WaitingForPlayersTask(gameName,waitingForPlayersLabel), 0, 1);
        }
        
        return 0;
    }
}
