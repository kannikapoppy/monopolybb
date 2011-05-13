/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package monopoly.results;

/**
 *
 * @author blecherl
 */
public class GameDetailsResult extends MonopolyResult{

    private String status;
    private int totalHumanPlayers;
    private int totalComputerPlayers;
    private int joinedHumanPlayers;
    private boolean isAutomaticDiceRoll;

    public GameDetailsResult(String status, int totalHumanPlayers, int totalComputerPlayers, int joinedHumanPlayers, boolean isAutomaticDiceRoll) {
        super();
        this.status = status;
        this.totalHumanPlayers = totalHumanPlayers;
        this.totalComputerPlayers = totalComputerPlayers;
        this.joinedHumanPlayers = joinedHumanPlayers;
        this.isAutomaticDiceRoll = isAutomaticDiceRoll;
    }

    public GameDetailsResult(String errorMessage) {
        super (errorMessage);
    }

    public GameDetailsResult(boolean hasError, String errorMessage) {
        super (hasError, errorMessage);
    }

    public int getJoinedHumanPlayers() {
        return joinedHumanPlayers;
    }

    public String getStatus() {
        return status;
    }

    public int getTotalComputerPlayers() {
        return totalComputerPlayers;
    }

    public int getTotalHumanPlayers() {
        return totalHumanPlayers;
    }

    public boolean isIsAutomaticDiceRoll() {
        return isAutomaticDiceRoll;
    }

    public static GameDetailsResult error(String message) {
        return new GameDetailsResult(message);
    }
}