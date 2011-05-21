/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package monopoly;

import objectmodel.CellBase;
import objectmodel.DiceThrowResult;

/**
 *
 * @author blecherl
 */
public class EventImpl implements Event{

    private String m_gameName;
    private int m_eventID;
    private int m_timeoutCount = 0;
    private int m_eventType;
    private String m_playerName;
    private String m_eventMessage;
    private int m_boardSquareId;
    private int m_firstDiceResult;
    private int m_secondDiceResult;
    private boolean m_isPlayerMoved;
    private int m_nextBoardSquareId;
    private boolean m_isPaymentToOrFromTreasury;
    private boolean m_isPaymemtFromUser;
    private String m_PaymentToPlayerName;
    private int m_paymentAmount;

    public EventImpl(String gameName, int eventId, int eventType)
    {
        m_gameName = gameName;
        m_eventID = eventId;
        m_eventType = eventType;
    }

    public EventImpl(String gameName, int eventId, int eventType, String playerName,
            int boardPosition)
    {
        this(gameName, eventId, eventType);
        m_playerName = playerName;
        m_boardSquareId = boardPosition;
    }

    public EventImpl(String gameName, int eventId, int eventType, String playerName,
            int boardPosition, int timeoutCount)
    {
        this(gameName, eventId, eventType, playerName, boardPosition);
        m_timeoutCount = timeoutCount;
    }

    public EventImpl(String gameName, int eventId, int eventType, String playerName,
            DiceThrowResult diceThrow, int boardPosition)
    {
        this(gameName, eventId, eventType, playerName, boardPosition);
        m_firstDiceResult = diceThrow.getFirstDice();
        m_secondDiceResult = diceThrow.getSecondDice();
    }

    public EventImpl(String gameName, int eventId, int eventType, String playerName,
            CellBase originCell, CellBase destinationCell)
    {
        this(gameName, eventId, eventType, playerName, originCell.getID());
        m_nextBoardSquareId = destinationCell.getID();
        m_isPlayerMoved = true;
    }

    public EventImpl(String gameName, int eventId, int eventType, String playerName,
            String message, int boardPosition)
    {
        this(gameName, eventId, eventType, playerName, boardPosition);
        m_eventMessage = message;
    }
    
    public EventImpl(String gameName, int eventId, int eventType, String playerName,
            boolean isPaymentToOrFromTreasury, boolean isPaymemtFromUser,
            String paymentToPlayerName, int paymentAmount, int boardPosition)
    {
        this(gameName, eventId, eventType, playerName, boardPosition);
        m_isPaymentToOrFromTreasury = isPaymentToOrFromTreasury;
        m_isPaymemtFromUser = isPaymemtFromUser;;
        m_PaymentToPlayerName = paymentToPlayerName;
        m_paymentAmount = paymentAmount;
    }

    public String getGameName() {
        return m_gameName;
    }

    public int getEventID() {
        return m_eventID;
    }

    public int getTimeoutCount() {
        return m_timeoutCount;
    }

    public int getEventType() {
        return m_eventType;
    }

    public String getPlayerName() {
        return m_playerName;
    }

    public String getEventMessage() {
        return m_eventMessage;
    }

    public int getBoardSquareID() {
        return m_boardSquareId;
    }

    public int getFirstDiceResult() {
        return m_firstDiceResult;
    }

    public int getSecondDiceResult() {
        return m_secondDiceResult;
    }

    public boolean isPlayerMoved() {
        return m_isPlayerMoved;
    }

    public int getNextBoardSquareID() {
        return m_nextBoardSquareId;
    }

    public boolean isPaymentToOrFromTreasury() {
        return m_isPaymentToOrFromTreasury;
    }

    public boolean isPaymemtFromUser() {
        return m_isPaymemtFromUser;
    }

    public String getPaymentToPlayerName() {
        return m_PaymentToPlayerName;
    }

    public int getPaymentAmount() {
        return m_paymentAmount;
    }

}
