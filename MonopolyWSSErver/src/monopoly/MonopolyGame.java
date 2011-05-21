package monopoly;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import main.UserPrompt;
import monopoly.gamesmanager.MonopolyGameManager;
import monopoly.results.EventArrayResult;
import monopoly.results.GameDetailsResult;
import monopoly.results.IDResult;
import monopoly.results.MonopolyResult;
import monopoly.results.PlayerDetailsResult;
import objectmodel.AutomaticPlayer;
import objectmodel.DiceThrowResult;
import objectmodel.Player;

/**
 *
 * @author blecherl
 */
public class MonopolyGame {

    private MonopolyGameManager gameManager;

    public MonopolyGame() {
        gameManager = new MonopolyGameManager();
    }

    public String getGameBoardSchema() {
        InputStream is = null;
        try {
                is = getClass().getClassLoader().getResourceAsStream("configuration/MonopolyBoard.xsd");
                return new Scanner(is).useDelimiter("\\Z").next();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    public String getGameBoardXML() {
        InputStream is = null;
        try {
                is = getClass().getClassLoader().getResourceAsStream("configuration/MonopolyBoard.xml");
                return new Scanner(is).useDelimiter("\\Z").next();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    public MonopolyResult startGame(String gameName, int humanPlayers, int computerizedPlayers, boolean useAutomaticDiceRoll) {
        if (gameManager.isGameStarted(gameName)) {
            return MonopolyResult.error("A game '" + gameName + "' has already been started");
        } else if (humanPlayers  == 0) {
            return MonopolyResult.error("A game must have at least one human player");
        } else if (humanPlayers + computerizedPlayers < 2 || humanPlayers + computerizedPlayers > 6) {
            return MonopolyResult.error("A game must have between 2 and 6 players");
        } else {
            gameManager.addGame(gameName, humanPlayers, computerizedPlayers, useAutomaticDiceRoll);
            return new MonopolyResult();
        }
    }

    public GameDetailsResult getGameDetails(String gameName) {
        if (!gameManager.isGameStarted(gameName)) {
            return GameDetailsResult.error("Game '" + gameName + "' has not been started");
        } else {
            return new GameDetailsResult(
                    (gameManager.isGameActive(gameName) ? "ACTIVE" : "WAITING"),
                    gameManager.getHumanPlayers(gameName), gameManager.getComputerizedPlayers(gameName),
                    gameManager.getJoinedHumanPlayers(gameName), gameManager.isAutomaticDiceRoll(gameName));
        }
    }

    public String[] getWaitingGames() {
        List<String> results = new LinkedList<String>();
        for (String gameName : gameManager.getGamesNames()) {
            if (gameManager.isGameStarted(gameName) && !gameManager.isGameActive(gameName)) {
                results.add(gameName);
            }
        }
        return results.toArray(new String[results.size()]);
    }

    public String[] getActiveGames() {
        List<String> results = new LinkedList<String>();
        for (String gameName : gameManager.getGamesNames()) {
            if (gameManager.isGameStarted(gameName) && gameManager.isGameActive(gameName)) {
                results.add(gameName);
            }
        }
        return results.toArray(new String[results.size()]);
    }

    public IDResult joinGame(String gameName, String playerName) {
        if (gameManager.isGameStarted(gameName)
                && !gameManager.isGameActive(gameName)) {
            return new IDResult(gameManager.joinPlayer(gameName, playerName));
        } else {
            return IDResult.error("Game '" + gameName + "' is not active");
        }
    }

    public PlayerDetailsResult getPlayersDetails(String gameName) {
        if (!gameManager.isGameStarted(gameName)) {
            return PlayerDetailsResult.error("Game '" + gameName + "' has not been started");
        } else {
            return new PlayerDetailsResult(
                    gameManager.getPlayersNames(gameName),
                    gameManager.getPlayersHumanity(gameName),
                    gameManager.getPlayersActive(gameName),
                    gameManager.getPlayersAmounts(gameName));
        }
    }

    public EventArrayResult getAllEvents(int eventID) {
        //validate evendID against last eventID

        EventArrayResult events = gameManager.TryGetEvents(eventID);
        if (events == null)
        {
            EventArrayResult.error("eventID '" + eventID + "' is invalid");
        }

        return events;
    }

    public MonopolyResult setDiceRollResults(int playerID, int eventID, int dice1, int dice2) {
        if (gameManager.getGame() != null)
        {
            Player player = gameManager.getGame().GetPlayer(playerID);

            if (player instanceof AutomaticPlayer)
                return new MonopolyResult(true, "Cannot set dice role results for machine player!");

            if (dice1 < 1 || dice1 > 6 || dice2 < 1 || dice2 > 6)
                return new MonopolyResult(true, "dice result can only be between 1 and 6");

            if (player != null)
            {
                if (UserPrompt.Validate(player,eventID))
                {
                    UserPrompt.Notify(eventID, new DiceThrowResult(dice1, dice2));
                    return new MonopolyResult(false, "Dice1= " + dice1 + ", Dice2= " + dice2);
                }
            }
        }
        return new MonopolyResult(true, "Error!");
    }

    public MonopolyResult resign(int playerID) {
        //validate playerID against games players and players state
        if (gameManager.getGame() == null)
        {
            return new MonopolyResult(true, "game does not exists");
        }

        Player player = gameManager.getGame().GetPlayer(playerID);
        if (player == null)
        {
            return new MonopolyResult(true, "user does not exists");
        }
        if (player instanceof AutomaticPlayer)
        {
            return new MonopolyResult(true, "machine player cannot resign");
        }

        player.RequestToResign();
        return new MonopolyResult(false, "All Good");
    }

    public MonopolyResult buy(int playerID, int eventID, boolean buy) {
        if (gameManager.getGame() != null)
        {
            Player player = gameManager.getGame().GetPlayer(playerID);
            
            if (player != null)
            {
                if (UserPrompt.Validate(player,eventID))
                {
                    UserPrompt.Notify(eventID, buy);
                    return new MonopolyResult(false, "All Good");
                }
            }
        }
        return new MonopolyResult(true, "No Buy!");
    }
}
