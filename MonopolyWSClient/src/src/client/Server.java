package src.client;

import comm.Event;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author blecherl
 */
public class Server {

    private static Server instance;

    private BackendService backendService;
    private int playerID = -1;

    static {
        instance = new Server();
    }

    private Server() {
        backendService = new BackendService();
    }

    public static Server getInstance() {
        return instance;
    }

    public void setPlayerId(int playerId)
    {
        this.playerID = playerId;
    }

    public Timer startPolling (String timerName, TimerTask task, int delay, int period) {
        Timer timer = new Timer (timerName, true);
        timer.scheduleAtFixedRate(task, delay*1000, period*1000);
        return timer;
    }

    public List<String> getWaitingGames() {
        return backendService.getWaitingGames();
    }

    public String getGameBoardXML() {
        return backendService.getGameBoardXML();
    }

    public List<String> getActiveGames() {
        return backendService.getActiveGames();
    }

    public List<Event> getAllEvents(int lastEventID) {
        return backendService.getAllEvents(lastEventID);
    }

    public boolean startNewGame(String name, int humanPlayers, int computerPlayers, boolean useAutomaticDiceRollCheckBox) {
        return backendService.startGame(name, humanPlayers, computerPlayers, useAutomaticDiceRollCheckBox);
    }

    public List<PlayerDetails> getPlayersDetails(String gameName) {
        return backendService.getPlayersDetails(gameName);
    }

    public int joinPlayer(String gameName, String playerName) {
        return backendService.joinGame(gameName, playerName);
    }

    public boolean setDiceRollResults(int eventId, int firstDice, int secondDice) {
        return backendService.setDiceRollResults(this.playerID, eventId, firstDice, secondDice);
    }

    public boolean buy(int eventId, boolean buy) {
        return backendService.buy(this.playerID, eventId, buy);
    }

    public GameDetails getGameDetails(String gameName) {
        return backendService.getGameDetails(gameName);
    }

    public boolean Resign()
    {
        return backendService.Resign(this.playerID);
    }
}