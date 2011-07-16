/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package monopoly;

import java.util.TimerTask;
import src.client.Server;
import src.services.Utils;

/**
 *
 * @author Benda & Eizenman
 */
public class WaitingForPlayersTask extends TimerTask
{
    private WebClient client;
    private String gameName;

    public WaitingForPlayersTask(String gameName, WebClient client) {
        this.gameName = gameName;
    }

    @Override
    public void run() {
        if (gameName != null) {
            int missing;
            
            try {
                missing = Server.getInstance().getGameDetails(gameName).getWaitingForPlayersNumber();
            } catch (Exception ex) {
                client.setConnectionFailed();
                return;
            }

            client.setWaitingText("Waiting For " + missing + " Players To Join The Game '" + gameName + "'");
        }
    }
}
