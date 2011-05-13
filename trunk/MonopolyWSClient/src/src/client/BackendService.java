/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package src.client;

import comm.GameDetailsResult;
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

/**
 *
 * @author blecherl
 */
public class BackendService {

    private MonopolyGamePortType monopolyGamePortType;

    public BackendService() {
        monopolyGamePortType = new MonopolyGame().getMonopolyGameHttpSoap11Endpoint();
    }

    public String getGameBoardXML() {
        return monopolyGamePortType.getGameBoardXML().getReturn().getValue();
    }

    public boolean startGame(String name, int humanPlayers, int computerPlayers, boolean useAutomaticDiceRollCheckBox) {
        MonopolyResult result = monopolyGamePortType.startGame(name, humanPlayers, computerPlayers, useAutomaticDiceRollCheckBox);
        return !hasError(result);
    } 

    public List<String> getWaitingGames() {
        GetWaitingGamesResponse result = monopolyGamePortType.getWaitingGames();
        return result != null ? result.getReturn() : Collections.EMPTY_LIST;
    }

    public int joinGame (String gameName, String playerName) {
        IDResult result = monopolyGamePortType.joinGame(gameName, playerName);
        if (hasError(result)) {
            return -1;
        } else {
            return result.getResult();
        }
    }

    private boolean hasError(MonopolyResult monopolyResult) {
        if (monopolyResult == null || monopolyResult.isError()) {
            JOptionPane.showMessageDialog(null, monopolyResult.getErrorMessage().getValue(), "Server Communication Error", JOptionPane.ERROR_MESSAGE);
            return true;
        } else {
            return false;
        }
    }

    public List<PlayerDetails> getPlayersDetails(String gameName) {
        List<PlayerDetails> players = new LinkedList<PlayerDetails>();
        PlayerDetailsResult result = monopolyGamePortType.getPlayersDetails(gameName);
        if (hasError(result)) {
            return Collections.EMPTY_LIST;
        } else {
            for (int index=0 ; index < result.getNames().size() ; index++) {
                players.add(new PlayerDetails(
                        result.getNames().get(index),
                        result.getIsHumans().get(index),
                        result.getIsActive().get(index),
                        result.getMoney().get(index)));
            }
            return players;
        }

    }

    public GameDetails getGameDetails (String gameName) {
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
}