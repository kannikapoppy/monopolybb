package monopoly;

/**
 * User: Liron Blecher
 * Date: 3/28/11
 */

/*
 Event Types:
1.	Game Start – game started
2.	Game Over – game ended
3.	Game Winner – the winner of the game (play name will be in the event)
4.	Player Resigned – player resigned from game: the client needs to update all the player’s assets as free
5.	Player Lost - player lost game: the client needs to update all the player’s assets as free
6.	Prompt Player to Roll Dice – prompt user to enter dice roll results (with timer)
7.	Dice Roll (might be generated automatically or after user enters dice roll as well)
8.	Move – move a player to a given square board
9.	Passed Start Square – just a notification message, a payment message will be sent afterward
10.	Landed on Start Square – just a notification message, a payment message will be sent afterward
11.	Go to Jail – just a notification message, move message will be sent afterwards
12.	Prompt Player to Buy Asset – prompt user to buy asset (with timer)
13.	Prompt Player to Buy House – prompt user to buy a house (with timer)
14.	Asset bought message – just a notification message
15.	House bought message – just a notification message
16.	Surprise Card – just a notification message, one or more payment messages will be sent afterward
17.	Warrant Card – just a notification message, one or more payment messages will be sent afterward
18.	Get Out Of Jail Card - just a notification message
19.	Payment – indicates that an amount of money should be transferred from/to the player.
20.     Player used Jail Card - just a notification message

*/
public interface Event {

    //generic event details
    public String getGameName();
    public int getEventID();
    public int getTimeoutCount();

    //event details
    //(after a card, the relevant events will be sent)
    //for example, a move message or one or more payment events)
    public int getEventType();
    public String getPlayerName();
    public String getEventMessage(); //can be used for all types of events
    public int getBoardSquareID(); //current player game board square ID

    //dice
    public int getFirstDiceResult();
    public int getSecondDiceResult();

    //move
    public boolean isPlayerMoved();
    public int getNextBoardSquareID();
    
    //payment
    public boolean isPaymentToOrFromTreasury();
    public boolean isPaymemtFromUser();
    public String getPaymentToPlayerName();
    public int getPaymentAmount();
}