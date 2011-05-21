/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package src.monopolyUI;

import comm.Event;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.TimerTask;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import monopolyUI.Board;
import objectmodel.CellBase;
import objectmodel.ServerEvents;
import src.client.Server;

/**
 *
 * @author Benda & Eizenman
 */
public class WaitingForPlayersTask extends TimerTask
{
    private JLabel waitingForPlayersLabel;
    private String gameName;

    public WaitingForPlayersTask(String gameName, JLabel waitingForPlayersLabel) {
        this.gameName = gameName;
        this.waitingForPlayersLabel = waitingForPlayersLabel;
    }

    @Override
    public void run() {
        if (gameName != null) {
            int missing = Server.getInstance().getGameDetails(gameName).getWaitingForPlayersNumber();
            waitingForPlayersLabel.setText("Waiting For " + missing + " Players To Join The Game '" + gameName + "'");
        }
    }
}
