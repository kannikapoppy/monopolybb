/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package src.client.ui.initgame;

import src.client.GameDetails;
import src.client.Server;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;
import javax.swing.SwingUtilities;
import src.client.GameDetails;
import src.client.Server;

/**
 *
 * @author blecherl
 */
public class RefreshWaitingGamesTask extends TimerTask{

    private GamesPanel gamesPanel;

    public RefreshWaitingGamesTask(GamesPanel gamesPanel) {
        this.gamesPanel = gamesPanel;
    }

    @Override
    public void run() {
        System.out.println("Running code on thread: " + Thread.currentThread().getName());
        List<String> waitingGamesNames = Server.getInstance().getWaitingGames();
        final List<GameDetails> waitingGamesWithStatus = new LinkedList<GameDetails>();
        for (String gameName : waitingGamesNames) {
            GameDetails gameDetails = Server.getInstance().getGameDetails(gameName);
            waitingGamesWithStatus.add(gameDetails);
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                System.out.println("Running code on thread: " + Thread.currentThread().getName());
                gamesPanel.setGames(waitingGamesWithStatus);
            }
        });
    }
}