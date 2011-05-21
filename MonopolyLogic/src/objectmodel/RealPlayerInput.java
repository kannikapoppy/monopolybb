/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package objectmodel;

import Utils.WaitNotifyManager;
import main.StateManager;
import main.UserPrompt;

/**
 *
 * @author Benda & Eizenman
 */
public class RealPlayerInput extends PlayerInput
	{

		public RealPlayerInput(Object sender) {
			super(sender);
		}

		@Override
		public boolean buyCell(CellBase landCell, Player landedPlayer) {
			WaitNotifyManager waiter = new WaitNotifyManager();

                        StateManager.getStateManager().setCurrentStateToPlayerOfferBuying(this,
    						landedPlayer.getName() + " has just been offered to buy " + landCell.getName(),
    						landedPlayer, landCell, waiter);

                        waiter.doWait();

                        Object resAsObject = UserPrompt.GetObject();
                        if (resAsObject == null)
                            return false;
                        else
                            return (Boolean)resAsObject;
		}

		@Override
		public boolean buildHouse(City landCell, Player landedPlayer) {
			WaitNotifyManager waiter = new WaitNotifyManager();

                        StateManager.getStateManager().setCurrentStateToPlayerOfferBuilding(this,
                                landedPlayer.getName() + " is offered to buy " + landCell.getName(),
                                landedPlayer, landCell, waiter);

                        waiter.doWait();

                        Object resAsObject = UserPrompt.GetObject();
                        if (resAsObject == null)
                            return false;
                        else
                            return (Boolean)resAsObject;
		}
	}
