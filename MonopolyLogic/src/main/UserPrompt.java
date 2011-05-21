/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import Utils.WaitNotifyManager;
import objectmodel.Player;

/**
 *
 * @author Administrator
 */
public class UserPrompt {
    private static Object s_lastObjectPrompted = null;
    private static WaitNotifyManager s_eventHandler = null;
    private static boolean s_timeoutOccured = false;
    private static int s_eventID = -1;
    private static boolean s_notified = false;
    private static Player s_player = null;

    public static Object GetObject()
    {
        return s_lastObjectPrompted;
    }

    public static void Init(WaitNotifyManager eventHandler, int eventID, Player player)
    {
        s_eventHandler = eventHandler;
        s_lastObjectPrompted = null;
        s_eventID = eventID;
        s_notified = false;
        s_player = player;
    }

    @SuppressWarnings("NotifyWhileNotSynced")
    public static void Notify(int eventID, Object result)
    {
        if (eventID == s_eventID && !s_notified)
        {
            s_notified = true;
            s_eventID = -1;
            s_player = null;
            s_lastObjectPrompted = result;
            s_eventHandler.doNotify();
        }
    }

    public static void NotifyTimeout(int eventID)
    {
        if (eventID == s_eventID && !s_notified)
        {
            s_notified = true;
            s_eventID = -1;
            s_player.RequestToResign();
            s_player = null;
            s_lastObjectPrompted = null;
            s_eventHandler.doNotify();
        }
    }

    public static boolean Validate(Player player, int eventID)
    {
        return eventID == s_eventID && s_player == player;
    }
}
