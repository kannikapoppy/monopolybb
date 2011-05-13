/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package monopoly;

/**
 *
 * @author blecherl
 */
public class EventImpl implements Event{

    public String getGameName() {
        return "GameName";
    }

    public int getEventID() {
        return 0;
    }

    public int getTimeoutCount() {
        return 1;
    }

    public int getEventType() {
        return 2;
    }

    public String getPlayerName() {
        return "PlayerName";
    }

    public String getEventMessage() {
        return "EventMessage";
    }

    public int getBoardSquareID() {
        return 3;
    }

    public int getFirstDiceResult() {
        return 4;
    }

    public int getSecondDiceResult() {
        return 5;
    }

    public boolean isPlayerMoved() {
        return false;
    }

    public int getNextBoardSquareID() {
        return 6;
    }

    public boolean isPaymentToOrFromTreasury() {
        return true;
    }

    public boolean isPaymemtFromUser() {
        return true;
    }

    public String getPaymentToPlayerName() {
        return "PaymentName";
    }

    public int getPaymentAmount() {
        return 7;
    }

}
