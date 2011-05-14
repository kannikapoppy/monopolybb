/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.util.TimerTask;

/**
 *
 * @author Benda & Eizenman
 */
public class UserPromptTimerTask extends TimerTask
{
    private int eventID;

    public UserPromptTimerTask(int eventID)
    {
        this.eventID = eventID;
    }

    /**
    * When the timer executes, this code is run.
    */
    public void run()
    {
        UserPrompt.NotifyTimeout(eventID);
    }
}
