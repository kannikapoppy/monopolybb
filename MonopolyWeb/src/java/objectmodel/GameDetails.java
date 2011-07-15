/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package objectmodel;

/**
 *
 * @author blecherl
 */
public class GameDetails {
    private String gameName;
    private int totalHumanPlayers;
    private int joinedHumanPlayers;
    private int computerizedPlayers;
    private String status;

    public GameDetails(String gameName, int totalHumanPlayers, int joinedHumanPlayers, int computerizedPlayers, String status) {
        this.gameName = gameName;
        this.totalHumanPlayers = totalHumanPlayers;
        this.joinedHumanPlayers = joinedHumanPlayers;
        this.computerizedPlayers = computerizedPlayers;
        this.status = status;
    }

    public String getGameName() {
        return gameName;
    }

    public int getComputerizedPlayers() {
        return computerizedPlayers;
    }

    public int getJoinedHumanPlayers() {
        return joinedHumanPlayers;
    }

    public String getStatus() {
        return status;
    }

    public int getTotalHumanPlayers() {
        return totalHumanPlayers;
    }

    public int getWaitingForPlayersNumber() {
        return totalHumanPlayers - joinedHumanPlayers;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GameDetails other = (GameDetails) obj;
        if ((this.gameName == null) ? (other.gameName != null) : !this.gameName.equals(other.gameName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.gameName != null ? this.gameName.hashCode() : 0);
        return hash;
    }
}