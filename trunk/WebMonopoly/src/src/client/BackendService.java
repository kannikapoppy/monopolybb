/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package src.client;

import objectmodel.PlayerDetails;
import objectmodel.GameDetails;
import comm.Event;
import comm.EventArrayResult;
import comm.GameDetailsResult;
import comm.GetActiveGamesResponse;
import comm.GetWaitingGamesResponse;
import comm.IDResult;
import comm.MonopolyGame;
import comm.MonopolyGamePortType;
import comm.MonopolyResult;
import comm.PlayerDetailsResult;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import objectmodel.PlayerColor;

/**
 *
 * @author blecherl
 */
public class BackendService {

    private MonopolyGamePortType monopolyGamePortType;

    public BackendService() {
        monopolyGamePortType = new MonopolyGame().getMonopolyGameHttpSoap11Endpoint();
    }

    public String getGameBoardXML() throws Exception {
        return monopolyGamePortType.getGameBoardXML().getReturn().getValue();
    }

    public String getGameBoardSchema() throws Exception {
        return monopolyGamePortType.getGameBoardSchema().getReturn().getValue();
    }

    public boolean startGame(String name, int humanPlayers, int computerPlayers, boolean useAutomaticDiceRollCheckBox) throws Exception {
        MonopolyResult result = monopolyGamePortType.startGame(name, humanPlayers, computerPlayers, useAutomaticDiceRollCheckBox);
        return !hasError(result);
    } 

    public List<String> getWaitingGames() throws Exception {
        GetWaitingGamesResponse result = monopolyGamePortType.getWaitingGames();
        return result != null ? result.getReturn() : Collections.EMPTY_LIST;
    }

    public List<String> getActiveGames() throws Exception {
        GetActiveGamesResponse result = monopolyGamePortType.getActiveGames();
        return result != null ? result.getReturn() : Collections.EMPTY_LIST;
    }

    public List<Event> getAllEvents(int lastEventID) throws Exception {
        EventArrayResult result = monopolyGamePortType.getAllEvents(lastEventID);
        return result != null ? result.getResults() : Collections.EMPTY_LIST;
    }

    public int joinGame (String gameName, String playerName) throws Exception {
        IDResult result = monopolyGamePortType.joinGame(gameName, playerName);
        if (hasError(result)) {
            return -1;
        } else {
            return result.getResult();
        }
    }

    public boolean setDiceRollResults (int playerId, int eventId, int firstDice, int secondDice) throws Exception {
        MonopolyResult result = monopolyGamePortType.setDiceRollResults(playerId, eventId,
                firstDice, secondDice);
        return !hasError(result);
    }

    public boolean buy (int playerId, int eventId, boolean buy) throws Exception {
        MonopolyResult result = monopolyGamePortType.buy(playerId, eventId, buy);
        return !hasError(result);
    }

    private boolean hasError(MonopolyResult monopolyResult) {
        if (monopolyResult == null || monopolyResult.isError()) {
            JOptionPane.showMessageDialog(null, monopolyResult.getErrorMessage().getValue(), "Server Communication Error", JOptionPane.ERROR_MESSAGE);
            return true;
        } else {
            return false;
        }
    }

    public List<PlayerDetails> getPlayersDetails(String gameName) throws Exception {
        List<PlayerDetails> players = new LinkedList<PlayerDetails>();
        PlayerDetailsResult result = monopolyGamePortType.getPlayersDetails(gameName);
        if (hasError(result)) {
            return Collections.EMPTY_LIST;
        } else {
            PlayerColor[] colors = PlayerColor.values();
            for (int index=0 ; index < result.getNames().size() ; index++) {
                players.add(new PlayerDetails(
                        result.getNames().get(index),
                        result.getIsHumans().get(index),
                        result.getIsActive().get(index),
                        result.getMoney().get(index),
                        (PlayerColor)colors[index]));
            }
            return players;
        }

    }

    public GameDetails getGameDetails (String gameName) throws Exception {
        GameDetailsResult result = monopolyGamePortType.getGameDetails(gameName);
        if (hasError(result)) {
            return null;
        } else {
            return new GameDetails(
                    gameName,
                    result.getTotalHumanPlayers(),
                    result.getJoinedHumanPlayers(),
                    result.getTotalComputerPlayers(),
                    result.getStatus().getValue());
        }
    }

    public boolean Resign(int playerID) throws Exception
    {
        MonopolyResult result = monopolyGamePortType.resign(playerID);
        return !hasError(result);
    }
}