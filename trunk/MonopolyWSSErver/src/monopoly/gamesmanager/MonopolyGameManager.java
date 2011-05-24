/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package monopoly.gamesmanager;

import GameStateChangedEvent.GameStateChangedEvent;
import GameStateChangedEvent.GameStateChangedToPlayerActionEvent;
import GameStateChangedEvent.GameStateChangedToPlayerBuildingEvent;
import GameStateChangedEvent.GameStateChangedToPlayerBuyingEvent;
import GameStateChangedEvent.GameStateChangedToPlayerDrewCardEvent;
import GameStateChangedEvent.GameStateChangedToPlayerGettingOutOfJailEvent;
import GameStateChangedEvent.GameStateChangedToPlayerMovingEvent;
import GameStateChangedEvent.GameStateChangedToPlayerPaymentsEvent;
import GameStateChangedEvent.GameStateChangedToPlayerRollingEvent;
import GameStateChangedEvent.GameStateChangedToPromptPlayerActionEvent;
import GameStateChangedEvent.GameStateChangedToPromptPlayerBuildingEvent;
import GameStateChangedEvent.GameStateChangedToPromptPlayerBuyingEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import main.MonopolyGameLogic;
import monopoly.Event;
import monopoly.results.EventArrayResult;
import objectmodel.Player;
import java.util.Timer;
import main.MoneyTransactionDirection;
import main.StateManager.GameStateChangedEventListener;
import main.UserPrompt;
import main.UserPromptTimerTask;
import monopoly.EventImpl;
import objectmodel.AutomaticPlayer;
import objectmodel.RealPlayerInput;
import objectmodel.ServerEvents;

/**
 *
 * @author blecherl
 */
public class MonopolyGameManager {

   GameDetails game = null;
   List<Event> events;

    public MonopolyGameManager() {
        game = null;
        events = new ArrayList<Event>();
    }

    public EventArrayResult TryGetEvents(int eventID)
    {
        if (eventID < 0 || eventID >= events.size())
            return null;

        if (eventID == events.size())
            return new EventArrayResult(new Event[0]);

        Event eventsToReturn[] = new Event[events.size() - eventID];

        for (int i=eventID;i<events.size();i++)
        {
            eventsToReturn[i-eventID] = events.get(i);
        }

        EventArrayResult result = new EventArrayResult(eventsToReturn);
        return result;
    }

    public GameDetails getGame()
    {
        return game;
    }

    public boolean addGame(String gameName, int humanPlayers, int computerizedPlayers, boolean useAutomaticDiceRoll) {
        if (isGameExists()) {
            return false;
        } else {
            events.clear();
            game = new GameDetails(gameName, humanPlayers, computerizedPlayers,
                    useAutomaticDiceRoll, events, this);
            return true;
        }
    }

    private void SignalGameEnded()
    {
        game = null;
    }

    private boolean isGameExists() {
        return game != null;
    }

    public boolean isGameStarted(String gameName) {
        return isGameExists() && game.GetName().compareTo(gameName) == 0 && game.isGameStarted();
    }

    public boolean isGameActive(String gameName) {
        return isGameExists() && game.GetName().compareTo(gameName) == 0 && game.isGameActive();
    }

    public int joinPlayer(String gameName, String playerName) {
        if (isGameExists() && game.GetName().compareTo(gameName) == 0) {
            return game.joinPlayer(playerName);
        } else {
            return -1;
        }
    }

    public List<String> getGamesNames() {
        ArrayList<String> res = new ArrayList<String>();
        if (isGameExists())
            res.add(game.GetName());
        return res;
    }
    
    public int getComputerizedPlayers(String gameName) {
        return isGameExists() && game.GetName().compareTo(gameName) == 0 ? game.computerizedPlayers : -1;
    }

    public int getHumanPlayers(String gameName) {
        return isGameExists() && game.GetName().compareTo(gameName) == 0 ? game.humanPlayers : -1;
    }

    public int getJoinedHumanPlayers(String gameName) {
        return isGameExists() && game.GetName().compareTo(gameName) == 0 ? game.getJoinedHumanPlayers() : -1;
    }

    public boolean isAutomaticDiceRoll(String gameName) {
        return isGameExists() && game.GetName().compareTo(gameName) == 0 ? game.useAutomaticDiceRoll : true;
    }

    public String[] getPlayersNames(String gameName) {
        if (!isGameExists() && game.GetName().compareTo(gameName) == 0) {
            return new String[0];
        }

        List<String> names = new LinkedList<String>();
        for (Player details : game.players) {
            names.add(details.getName());
        }
        return names.toArray(new String[names.size()]);
    }

    public boolean[] getPlayersHumanity(String gameName) {
        if (!isGameExists() && game.GetName().compareTo(gameName) == 0) {
            return new boolean[0];
        }
        boolean[] results = new boolean[game.players.size()];
        for (int index=0 ; index < game.players.size() ; index++) {
            results[index] = !(game.players.get(index) instanceof AutomaticPlayer);
        }
        return results;
    }

    public boolean[] getPlayersActive(String gameName) {
        if (!isGameExists() && game.GetName().compareTo(gameName) == 0) {
            return new boolean[0];
        }
        boolean[] results = new boolean[game.players.size()];
        for (int index=0 ; index < game.players.size() ; index++) {
            results[index] = game.players.get(index).isInGame();
        }
        return results;
    }

    public int[] getPlayersAmounts(String gameName) {
        if (!isGameExists() && game.GetName().compareTo(gameName) == 0) {
            return new int[0];
        }
        int[] results = new int[game.players.size()];
        for (int index=0 ; index < game.players.size() ; index++) {
            results[index] = game.players.get(index).getMoney();
        }
        return results;
    }

    public class GameDetails {
        private static final int DEFAULT_TIMEOUT = 90*1000;

        private String gameName;
        private int humanPlayers;
        private int computerizedPlayers;
        private boolean useAutomaticDiceRoll;
        private List<Player> players;
        private MonopolyGameLogic gameLogic;
        GameStateChangedEventListener listener = null;
        List<Event> events = null;
        MonopolyGameManager gameManager;

        public GameDetails(String gameName, int humanPlayers, int computerizedPlayers, boolean useAutomaticDiceRoll,
                List<Event> events, MonopolyGameManager gameManager) {
            this.gameName = gameName;
            this.humanPlayers = humanPlayers;
            this.computerizedPlayers = computerizedPlayers;
            this.useAutomaticDiceRoll = useAutomaticDiceRoll;
            this.events = events;
            this.players = new LinkedList<Player>();
            this.gameManager = gameManager;
        }

        public String GetName()
        {
            return gameName;
        }

        public Player GetPlayer(int playerID)
        {
            return players.get(playerID);
        }

        public boolean isGameStarted() {
            return this.gameName != null;
        }

        public boolean isGameActive() {
            return isGameStarted() && (this.humanPlayers + this.computerizedPlayers == this.players.size());
        }

        private boolean isNameAlreadyExists(String playerName) {
            if (!isGameExists())
                return true;

            for (Player player : game.players)
            {
                if (player.getName().compareTo(playerName) == 0)
                    return true;
            }

            return false;        
        }

        public int joinPlayer(String playerName) {
            if (isNameAlreadyExists(playerName))
                return -1;

            Player newPlayer = new Player();
            newPlayer.setName(playerName);
            newPlayer.setInputObject(new RealPlayerInput(newPlayer));
            this.players.add(newPlayer);

            int retValue = this.players.size() - 1;
            
            if (this.players.size() == this.humanPlayers)
            {
                if (!InnerStartGame())
                    return -1;
            }

            return retValue;
        }

        private boolean InnerStartGame() {
            //crate all machine players
            int currentIndex = 0;
            for (int i = 0; i < computerizedPlayers; i++) {
                AutomaticPlayer newMachinePlayer = new AutomaticPlayer();
                while (isNameAlreadyExists("Machine Player " + currentIndex))
                {
                    currentIndex++;
                }
                newMachinePlayer.setName("Machine Player " + currentIndex);
                currentIndex++;
                players.add(newMachinePlayer);
            }
            // all players have joined. we need to start the game
            gameLogic = new MonopolyGameLogic(useAutomaticDiceRoll);
            // Create the event Handler & register to the events
            listener = new GameStateChangedEventListener() {

                @Override
                public void gameStateChanged(GameStateChangedEvent evt) {
                    HandleState(evt);
                }
            };
            gameLogic.getStateManager().registerToGameChangedEvent(GameStateChangedEventListener.class, listener);
            if (!gameLogic.initGame(players)) {
                return false;
            }
            try {
                if (!gameLogic.startGame(false)) {
                    return false;
                }
            } catch (Exception ex) {
                return false;
            }

            return true;
        }

        /**
	 * Write the new state to the console
	 * @param newState - the new state
	 * @param message - the message triggered with the new state
	 */
	private void HandleState(GameStateChangedEvent evt)
	{
            switch (evt.getNewState())
		{
                    case Starting:
                        EventImpl startGameEvent = new EventImpl(gameName, events.size() + 1, 
                                ServerEvents.GameStart);
                        events.add(startGameEvent);
                        break;
                    case GameOver:
                        GameStateChangedToPlayerActionEvent finishEvent = 
					(GameStateChangedToPlayerActionEvent)evt;
                        EventImpl gameWinnerEvent = new EventImpl(gameName, events.size() + 1,
                                ServerEvents.GameWinner, finishEvent.getPlayer().getName(), finishEvent.getPlayer().getBoardPosition());
                        EventImpl gameOverEvent = new EventImpl(gameName, events.size() + 2, ServerEvents.GameOver);
                        events.add(gameWinnerEvent);
                        events.add(gameOverEvent);
                        gameLogic.getStateManager().unregisterFromGameChangedEvent(listener);
                        gameManager.SignalGameEnded();
                        break;
                    case PlayerLost:
                        GameStateChangedToPlayerActionEvent playerLostInnerEvent =
                                (GameStateChangedToPlayerActionEvent)evt;
                        EventImpl playerLostEvent = new EventImpl(gameName, events.size() + 1,
                                ServerEvents.PlayerLost, playerLostInnerEvent.getPlayer().getName(),
                                playerLostInnerEvent.getPlayer().getBoardPosition());
                        events.add(playerLostEvent);
                        break;
                    case PlayerResigned:
                        GameStateChangedToPlayerActionEvent playerResignedInnerEvent =
                                (GameStateChangedToPlayerActionEvent)evt;
                        EventImpl playerResignedEvent = new EventImpl(gameName, events.size() + 1,
                                ServerEvents.PlayerResigned, playerResignedInnerEvent.getPlayer().getName(),
                                playerResignedInnerEvent.getPlayer().getBoardPosition());
                        events.add(playerResignedEvent);
                        break;
                    case PromptPlayerForRollingDice:
                        GameStateChangedToPromptPlayerActionEvent waitingForRollEvent =
                                (GameStateChangedToPromptPlayerActionEvent)evt;
                        int rolleventid = events.size() + 1;
                        EventImpl promptDiceEvent = new EventImpl(gameName, rolleventid,
                                ServerEvents.PromptPlayerToRollDice, waitingForRollEvent.getPlayer().getName(),
                                waitingForRollEvent.getPlayer().getBoardPosition(), DEFAULT_TIMEOUT);
                        events.add(promptDiceEvent);
                        UserPrompt.Init(waitingForRollEvent.GetEventHandler(), rolleventid,
                                waitingForRollEvent.getPlayer());
                        Timer rollDiceTimeoutTimer = new Timer();
                        rollDiceTimeoutTimer.schedule(new UserPromptTimerTask(rolleventid), DEFAULT_TIMEOUT);
                        break;
                    case PlayerRolling:
                        GameStateChangedToPlayerRollingEvent rollingEvent =
                                (GameStateChangedToPlayerRollingEvent)evt;
                        EventImpl diceEvent = new EventImpl(gameName, events.size() + 1,
                                ServerEvents.DiceRoll, rollingEvent.getPlayer().getName(),
                                rollingEvent.getDiceThrowResult(), rollingEvent.getPlayer().getBoardPosition());
                        events.add(diceEvent);
                        break;
                    case PlayerMoving:
                        GameStateChangedToPlayerMovingEvent movingInnerEvent =
                                (GameStateChangedToPlayerMovingEvent)evt;
                        // move the player on the board
                        if (movingInnerEvent.getDestinationCell().getType().compareTo("Jail") == 0
                                && movingInnerEvent.getPlayer().isInJail())
                        {
                            EventImpl goToJailEvent = new EventImpl(gameName, events.size() + 1,
                                ServerEvents.GoToJail, movingInnerEvent.getPlayer().getName(),
                                movingInnerEvent.getPlayer().getBoardPosition());
                            events.add(goToJailEvent);
                        }

                        EventImpl movingEvent = new EventImpl(gameName, events.size() + 1,
                                ServerEvents.Move, movingInnerEvent.getPlayer().getName(),
                                movingInnerEvent.getOriginCell(),
                                movingInnerEvent.getDestinationCell());

                        events.add(movingEvent);
                        break;
                    case PlayerPassedStartSquare:
                        GameStateChangedToPlayerActionEvent playerPassedStartInnerEvent =
                                (GameStateChangedToPlayerActionEvent)evt;
                        EventImpl playerPassedStartEvent = new EventImpl(gameName, events.size() + 1,
                                ServerEvents.PassedStartSquare, playerPassedStartInnerEvent.getPlayer().getName(),
                                playerPassedStartInnerEvent.getPlayer().getBoardPosition(), DEFAULT_TIMEOUT);
                        events.add(playerPassedStartEvent);
                        break;
                    case PlayerLandedOnStartSquare:
                        GameStateChangedToPlayerActionEvent playerLandedOnStartInnerEvent =
                                (GameStateChangedToPlayerActionEvent)evt;
                        EventImpl playerLandedOnStartEvent = new EventImpl(gameName, events.size() + 1,
                                ServerEvents.LandedOnStartSquare, playerLandedOnStartInnerEvent.getPlayer().getName(),
                                playerLandedOnStartInnerEvent.getPlayer().getBoardPosition(), DEFAULT_TIMEOUT);
                        events.add(playerLandedOnStartEvent);
                        break;
                    case PlayerBuying:
                        GameStateChangedToPlayerBuyingEvent buyingEvent =
                                (GameStateChangedToPlayerBuyingEvent)evt;
                        EventImpl assetBoughtEvent = new EventImpl(gameName, events.size() + 1,
                                ServerEvents.AssetBought, buyingEvent.getPlayer().getName(), buyingEvent.getCell().getID());
                        events.add(assetBoughtEvent);
                        break;
                    case PromptPlayerForBuying:
                        GameStateChangedToPromptPlayerBuyingEvent promptBuyingInnerEvent =
                                (GameStateChangedToPromptPlayerBuyingEvent)evt;
                        int eventid = events.size() + 1;
                        EventImpl promptBuyingEvent = new EventImpl(gameName, eventid,
                                ServerEvents.PromptPlayerToBuyAsset, promptBuyingInnerEvent.getPlayer().getName(),
                                promptBuyingInnerEvent.getCell().getID(), DEFAULT_TIMEOUT);
                        events.add(promptBuyingEvent);
                        UserPrompt.Init(promptBuyingInnerEvent.GetEventHandler(), eventid,
                                promptBuyingInnerEvent.getPlayer());
                        Timer buyTimeoutTimer = new Timer();
                        buyTimeoutTimer.schedule(new UserPromptTimerTask(eventid), DEFAULT_TIMEOUT);
                        break;
                    case PlayerBuilding:
                        GameStateChangedToPlayerBuildingEvent buildingEvent =
                                (GameStateChangedToPlayerBuildingEvent)evt;
                        EventImpl houseBoughtEvent = new EventImpl(gameName, events.size() + 1,
                                ServerEvents.HouseBought, buildingEvent.getPlayer().getName(), buildingEvent.getCity().getID());
                        events.add(houseBoughtEvent);
                        break;
                    case PromptPlayerForBuilding:
                        GameStateChangedToPromptPlayerBuildingEvent promptBuildingInnerEvent =
                                (GameStateChangedToPromptPlayerBuildingEvent)evt;
                        int buildeventid = events.size() + 1;
                        EventImpl promptBuildingEvent = new EventImpl(gameName, buildeventid,
                                ServerEvents.PromptPlayerToBuyHouse, promptBuildingInnerEvent.getPlayer().getName(),
                                promptBuildingInnerEvent.getCity().getID(), DEFAULT_TIMEOUT);
                        events.add(promptBuildingEvent);
                        UserPrompt.Init(promptBuildingInnerEvent.GetEventHandler(), buildeventid,
                                promptBuildingInnerEvent.getPlayer());
                        Timer buildTimeoutTimer = new Timer();
                        buildTimeoutTimer.schedule(new UserPromptTimerTask(buildeventid), DEFAULT_TIMEOUT);
                        break;
                    case PlayerDrewCard:
                        GameStateChangedToPlayerDrewCardEvent drewCardEvent =
                                (GameStateChangedToPlayerDrewCardEvent)evt;
                        int eventType;
                        if (drewCardEvent.getDeck().getType().compareTo("Community Chest") == 0)
                            eventType = ServerEvents.WarrantCard;
                        else if (drewCardEvent.getCard().getType().compareTo("Jail Pass") == 0)
                            eventType = ServerEvents.GetOutOfJailCard;
                        else
                            eventType = ServerEvents.SurpriseCard;

                        EventImpl cardEvent = new EventImpl(gameName, events.size() + 1,
                                eventType, drewCardEvent.getPlayer().getName(),
                                drewCardEvent.getCard().getMessage(),
                                drewCardEvent.getPlayer().getBoardPosition());
                        events.add(cardEvent);
                        break;
                    case PlayerGettingOutOfJail:
                        GameStateChangedToPlayerGettingOutOfJailEvent outOfJailEvent =
                                (GameStateChangedToPlayerGettingOutOfJailEvent)evt;
                        EventImpl outOfJailCardEvent = new EventImpl(gameName, events.size() + 1,
                                ServerEvents.PlayerUsedGetOutOfJailCard, outOfJailEvent.getPlayer().getName(), outOfJailEvent.getPlayer().getBoardPosition());
                        events.add(outOfJailCardEvent);
                        break;
                    case PlayerPayment:
                            GameStateChangedToPlayerPaymentsEvent payEvent =
                                    (GameStateChangedToPlayerPaymentsEvent)evt;

                            boolean isPaymentToOrFromTreasury = payEvent.GetOtherParicipiant() == null;
                            boolean isPaymemtFromBank = payEvent.GetOtherParicipiant() == null && payEvent.GetMoneyTransactionDirection() == MoneyTransactionDirection.GettingPaid;
                            String paymentToPlayerName = "";
                            String paymentFromPlayerName = "";
                            if (payEvent.GetMoneyTransactionDirection() == MoneyTransactionDirection.GettingPaid)
                            {
                                paymentToPlayerName = payEvent.getPlayer().getName();
                                if (payEvent.GetOtherParicipiant() != null)
                                    paymentFromPlayerName = payEvent.GetOtherParicipiant().getName();
                            }
                            else
                            {
                                paymentFromPlayerName = payEvent.getPlayer().getName();
                                if (payEvent.GetOtherParicipiant() != null)
                                    paymentToPlayerName = payEvent.GetOtherParicipiant().getName();
                            }

                            int paymentAmount = payEvent.getAmount();

                            EventImpl paymentEvent = new EventImpl(gameName, events.size() + 1,
                                ServerEvents.Payment, paymentFromPlayerName,
                                isPaymentToOrFromTreasury, !isPaymemtFromBank,
                                paymentToPlayerName, paymentAmount, payEvent.getPlayer().getBoardPosition());
                            events.add(paymentEvent);
                            break;
		}
	}

        public int getJoinedHumanPlayers() {
            return this.players.size();
        }

        public String[] getPlayersNames() {
            List<String> names = new LinkedList<String>();
            for (Player details : players) {
                names.add(details.getName());
            }
            return names.toArray(new String[names.size()]);
        }

        public boolean[] getPlayersHumanity() {
            boolean[] results = new boolean[players.size()];
            for (int index=0 ; index < players.size() ; index++) {
                results[index] = !(players.get(index) instanceof AutomaticPlayer);
            }
            return results;
        }

        public boolean[] getPlayersActive() {
            boolean[] results = new boolean[players.size()];
            for (int index=0 ; index < players.size() ; index++) {
                results[index] = players.get(index).isInGame();
            }
            return results;
        }

        public int[] getPlayersAmounts() {
            int[] results = new int[players.size()];
            for (int index=0 ; index < players.size() ; index++) {
                results[index] = players.get(index).getMoney();
            }
            return results;
        }
    }
}
