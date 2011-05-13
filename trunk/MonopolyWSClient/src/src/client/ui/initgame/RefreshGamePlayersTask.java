/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package src.client.ui.initgame;

import java.util.List;
import java.util.TimerTask;
import javax.swing.SwingUtilities;
import src.client.PlayerDetails;
import src.client.Server;

/**
 *
 * @author blecherl
 */
public class RefreshGamePlayersTask extends TimerTask{

    private GamesPanel gamePanel;
    private String gameName;

    public RefreshGamePlayersTask(GamesPanel gamePanel, String gameName) {
        this.gamePanel = gamePanel;
        this.gameName = gameName;
    }

    @Override
    public void run() {
        if (gameName != null) {
            final List<PlayerDetails> players = Server.getInstance().getPlayersDetails(gameName);
            System.out.println("Running code on thread: " + Thread.currentThread().getName());
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    System.out.println("Running code on thread: " + Thread.currentThread().getName());
                    gamePanel.setGamePlayers(players);
                }
            });
        }
    }
}
