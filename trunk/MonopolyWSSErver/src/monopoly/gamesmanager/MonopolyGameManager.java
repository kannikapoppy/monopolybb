/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package monopoly.gamesmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import monopoly.Event;
import monopoly.results.EventArrayResult;
import monopoly.results.MonopolyResult;

/**
 *
 * @author blecherl
 */
public class MonopolyGameManager {

   Map<String, GameDetails> games;
   List<Event> events;

    public MonopolyGameManager() {
        games = new HashMap<String, GameDetails>();
        events = new ArrayList<Event>();
    }

    public EventArrayResult TryGetEvents(int eventID)
    {
        if (eventID < 0 || eventID >= events.size())
            return null;

        Event eventsToReturn[] = new Event[events.size()];
        eventsToReturn = events.subList(eventID + 1 , events.size() - 1).toArray(eventsToReturn);

        EventArrayResult result = new EventArrayResult(eventsToReturn);
        return result;
    }

    public boolean addGame(String gameName, int humanPlayers, int computerizedPlayers, boolean useAutomaticDiceRoll) {
        if (isGameExists(gameName)) {
            return false;
        } else {
            games.put(gameName, new GameDetails(gameName, humanPlayers, computerizedPlayers, useAutomaticDiceRoll));
            return true;
        }
    }

    private boolean isGameExists(String gameName) {
        return games.containsKey(gameName);
    }

    public boolean isGameStarted(String gameName) {
        return isGameExists(gameName) && games.get(gameName).isGameStarted();
    }

    public boolean isGameActive(String gameName) {
        return isGameExists(gameName) && games.get(gameName).isGameActive();
    }

    public int joinPlayer(String gameName, String playerName) {
        if (isGameExists(gameName)) {
            return games.get(gameName).joinPlayer(playerName);
        } else {
            return -1;
        }
    }

    public Set<String> getGamesNames() {
        return games.keySet();
    }
    
    public int getComputerizedPlayers(String gameName) {
        return isGameExists(gameName) ? games.get(gameName).computerizedPlayers : -1;
    }

    public int getHumanPlayers(String gameName) {
        return isGameExists(gameName) ? games.get(gameName).humanPlayers : -1;
    }

    public int getJoinedHumanPlayers(String gameName) {
        return isGameExists(gameName) ? games.get(gameName).getJoinedHumanPlayers() : -1;
    }

    public boolean isAutomaticDiceRoll(String gameName) {
        return isGameExists(gameName) ? games.get(gameName).useAutomaticDiceRoll : true;
    }

    public String[] getPlayersNames(String gameName) {
        if (!isGameExists(gameName)) {
            return new String[0];
        }

        List<String> names = new LinkedList<String>();
        for (PlayerDetails details : games.get(gameName).players) {
            names.add(details.name);
        }
        return names.toArray(new String[names.size()]);
    }

    public boolean[] getPlayersHumanity(String gameName) {
        if (!isGameExists(gameName)) {
            return new boolean[0];
        }
        boolean[] results = new boolean[games.get(gameName).players.size()];
        for (int index=0 ; index < games.get(gameName).players.size() ; index++) {
            results[index] = games.get(gameName).players.get(index).isHuman;
        }
        return results;
    }

    public boolean[] getPlayersActive(String gameName) {
        if (!isGameExists(gameName)) {
            return new boolean[0];
        }
        boolean[] results = new boolean[games.get(gameName).players.size()];
        for (int index=0 ; index < games.get(gameName).players.size() ; index++) {
            results[index] = games.get(gameName).players.get(index).isActive;
        }
        return results;
    }

    public int[] getPlayersAmounts(String gameName) {
        if (!isGameExists(gameName)) {
            return new int[0];
        }
        int[] results = new int[games.get(gameName).players.size()];
        for (int index=0 ; index < games.get(gameName).players.size() ; index++) {
            results[index] = games.get(gameName).players.get(index).amount;
        }
        return results;
    }


    class GameDetails {
        private String gameName;
        private int humanPlayers;
        private int computerizedPlayers;
        private boolean useAutomaticDiceRoll;
        private List<PlayerDetails> players;

        public GameDetails(String gameName, int humanPlayers, int computerizedPlayers, boolean useAutomaticDiceRoll) {
            this.gameName = gameName;
            this.humanPlayers = humanPlayers;
            this.computerizedPlayers = computerizedPlayers;
            this.useAutomaticDiceRoll = useAutomaticDiceRoll;
            this.players = new LinkedList<PlayerDetails>();
        }

        public boolean isGameStarted() {
            return this.gameName != null;
        }

        public boolean isGameActive() {
            return isGameStarted() && (this.humanPlayers == this.players.size());
        }

        public int joinPlayer(String playerName) {
            this.players.add(new PlayerDetails(playerName, true));
            return this.players.size();
        }

        public int getJoinedHumanPlayers() {
            return this.players.size();
        }

        public String[] getPlayersNames() {
            List<String> names = new LinkedList<String>();
            for (PlayerDetails details : players) {
                names.add(details.name);
            }
            return names.toArray(new String[names.size()]);
        }

        public boolean[] getPlayersHumanity() {
            boolean[] results = new boolean[players.size()];
            for (int index=0 ; index < players.size() ; index++) {
                results[index] = players.get(index).isHuman;
            }
            return results;
        }

        public boolean[] getPlayersActive() {
            boolean[] results = new boolean[players.size()];
            for (int index=0 ; index < players.size() ; index++) {
                results[index] = players.get(index).isActive;
            }
            return results;
        }

        public int[] getPlayersAmounts() {
            int[] results = new int[players.size()];
            for (int index=0 ; index < players.size() ; index++) {
                results[index] = players.get(index).amount;
            }
            return results;
        }
    }
    
    class PlayerDetails {
        private String name;
        private boolean isHuman;
        private boolean isActive;
        private int amount;

        public PlayerDetails(String name, boolean isHuman) {
            this.name = name;
            this.isHuman = isHuman;
            this.isActive = true;
            this.amount = 1500;
        }
    }
}
