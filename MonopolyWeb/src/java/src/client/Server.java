package src.client;

import objectmodel.PlayerDetails;
import objectmodel.GameDetails;
import comm.Event;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public List<String> getWaitingGames() throws Exception {
        return backendService.getWaitingGames();
    }

    public String getGameBoardXML() throws Exception {
        String xml = backendService.getGameBoardXML();
        Matcher cleanerMatcher = (Pattern.compile("^([\\W]+)<")).matcher( xml.trim() );
        xml = cleanerMatcher.replaceFirst("<");
        return xml;
    }

    public String getGameBoardSchema() throws Exception {
        String schema = backendService.getGameBoardSchema();
        Matcher cleanerMatcher = (Pattern.compile("^([\\W]+)<")).matcher( schema.trim() );
        schema = cleanerMatcher.replaceFirst("<");
        return schema;
    }

    public List<String> getActiveGames() throws Exception {
        return backendService.getActiveGames();
    }

    public List<Event> getAllEvents(int lastEventID) throws Exception {
        return backendService.getAllEvents(lastEventID);
    }

    public boolean startNewGame(String name, int humanPlayers, int computerPlayers, boolean useAutomaticDiceRollCheckBox) throws Exception {
        return backendService.startGame(name, humanPlayers, computerPlayers, useAutomaticDiceRollCheckBox);
    }

    public List<PlayerDetails> getPlayersDetails(String gameName) throws Exception {
        return backendService.getPlayersDetails(gameName);
    }

    public int joinPlayer(String gameName, String playerName) throws Exception {
        return backendService.joinGame(gameName, playerName);
    }

    public boolean setDiceRollResults(int eventId, int firstDice, int secondDice) throws Exception {
        return backendService.setDiceRollResults(this.playerID, eventId, firstDice, secondDice);
    }

    public boolean buy(int eventId, boolean buy) throws Exception {
        return backendService.buy(this.playerID, eventId, buy);
    }

    public GameDetails getGameDetails(String gameName) throws Exception {
        return backendService.getGameDetails(gameName);
    }

    public boolean Resign() throws Exception
    {
        return backendService.Resign(this.playerID);
    }
}