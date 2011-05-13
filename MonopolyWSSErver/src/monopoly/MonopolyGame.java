package monopoly;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import monopoly.gamesmanager.MonopolyGameManager;
import monopoly.results.EventArrayResult;
import monopoly.results.GameDetailsResult;
import monopoly.results.IDResult;
import monopoly.results.MonopolyResult;
import monopoly.results.PlayerDetailsResult;

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
        return "BoardSchema";
    }

    public String getGameBoardXML() {
        InputStream is = null;
        try {
                is = getClass().getClassLoader().getResourceAsStream("resources/monopoly_config.xml");
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
        //validate evendID against last eventID
        //validate playerID against games players and players state
        //validate game mode
        return new MonopolyResult("Dice1= " + dice1 + ", Dice2= " + dice2);
    }

    public MonopolyResult resign(int playerID) {
        //validate playerID against games players and players state
        return new MonopolyResult();
    }

    public MonopolyResult buy(int playerID, int eventID, boolean buy) {
        //validate evendID against last eventID
        //validate playerID against games players and players state
        //validate asset from eventID
        return new MonopolyResult(true, "No Buy!");
    }
}
