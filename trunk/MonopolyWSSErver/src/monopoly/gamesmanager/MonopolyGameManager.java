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
import objectmodel.PlayerColor;
import java.util.Arrays;
import java.util.Timer;
import main.MoneyTransactionDirection;
import main.StateManager.GameStateChangedEventListener;
import main.UserPrompt;
import main.UserPromptTimerTask;
import monopoly.EventImpl;
import objectmodel.AutomaticPlayer;

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

        Event eventsToReturn[] = new Event[events.size()];
        eventsToReturn = events.subList(eventID + 1 , events.size() - 1).toArray(eventsToReturn);

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
            game = new GameDetails(gameName, humanPlayers, computerizedPlayers,
                    useAutomaticDiceRoll, events);
            return true;
        }
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

    public int joinPlayer(String playerName, String gameName) {
        if (isGameExists() && game.GetName().compareTo(gameName) == 0) {
            return game.joinPlayer(playerName);
        } else {
            return -1;
        }
    }

    public List<String> getGamesNames() {
        ArrayList<String> res = new ArrayList<String>();
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
        private static final int DEFAULT_TIMEOUT = 30;

        private String gameName;
        private int humanPlayers;
        private int computerizedPlayers;
        private boolean useAutomaticDiceRoll;
        private List<Player> players;
        private MonopolyGameLogic gameLogic;
        GameStateChangedEventListener listener = null;
        List<Event> events = null;

        public GameDetails(String gameName, int humanPlayers, int computerizedPlayers, boolean useAutomaticDiceRoll,
                List<Event> events) {
            this.gameName = gameName;
            this.humanPlayers = humanPlayers;
            this.computerizedPlayers = computerizedPlayers;
            this.useAutomaticDiceRoll = useAutomaticDiceRoll;
            this.events = events;
            this.players = new LinkedList<Player>();

            if (humanPlayers == 0)
                InnerStartGame();
        }

        public String GetName()
        {
            return gameName;
        }

        public Player GetPlayer(int playerID)
        {
            return players.get(playerID-1);
        }

        public boolean isGameStarted() {
            return this.gameName != null;
        }

        public boolean isGameActive() {
            return isGameStarted() && (this.humanPlayers + this.computerizedPlayers == this.players.size());
        }

        private PlayerColor ChooseFreeColor(List<Player> existingPlayers)
        {
            ArrayList<PlayerColor> availableColors = new ArrayList<PlayerColor>(Arrays.asList(PlayerColor.values()));
            for (Player player : existingPlayers)
            {
                    availableColors.remove(player.getPlayerColor());
            }
            return availableColors.get(0);
        }

        public int joinPlayer(String playerName) {
            Player newPlayer = new Player();
            newPlayer.setName(playerName);
            newPlayer.setPlayerColor(ChooseFreeColor(players));
            this.players.add(newPlayer);
            
            if (this.players.size() == this.humanPlayers)
            {
                InnerStartGame();
            }

            return this.players.size();
        }

        private void InnerStartGame() {
            //crate all machine players
            for (int i = 0; i < computerizedPlayers; i++) {
                AutomaticPlayer newMachinePlayer = new AutomaticPlayer();
                newMachinePlayer.setName("Machine Player " + i);
                newMachinePlayer.setPlayerColor(ChooseFreeColor(players));
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
                // TODO: throw exception
                // throw new Exception(ERROR_INITIALIZING_MSG);
            }
            try {
                if (!gameLogic.startGame(false)) {
                    // TODO: throw exception
                    // throw ex;
                }
            } catch (Exception ex) {
                // TODO: throw exception
                // throw ex;
            }
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
                        EventImpl startGameEvent = new EventImpl(gameName, events.size(), 1);
                        events.add(startGameEvent);
                    case GameOver:
                        GameStateChangedToPlayerActionEvent finishEvent = 
					(GameStateChangedToPlayerActionEvent)evt;
                        EventImpl gameWinnerEvent = new EventImpl(gameName, events.size(), 
                                3, finishEvent.getPlayer().getName());
                        EventImpl gameOverEvent = new EventImpl(gameName, events.size(), 2);
                        events.add(gameWinnerEvent);
                        events.add(gameOverEvent);
                    case PlayerLost:
                        GameStateChangedToPlayerActionEvent playerLostInnerEvent =
                                (GameStateChangedToPlayerActionEvent)evt;
                        EventImpl playerLostEvent = new EventImpl(gameName, events.size(),
                                4, playerLostInnerEvent.getPlayer().getName());
                        events.add(playerLostEvent);
                        break;
                    case PromptPlayerForRollingDice:
                        GameStateChangedToPromptPlayerActionEvent waitingForRollEvent =
                                (GameStateChangedToPromptPlayerActionEvent)evt;
                        int rolleventid = events.size();
                        EventImpl promptDiceEvent = new EventImpl(gameName, rolleventid,
                                6, waitingForRollEvent.getPlayer().getName(), DEFAULT_TIMEOUT);
                        events.add(promptDiceEvent);
                        UserPrompt.Init(waitingForRollEvent.GetEventHandler(), rolleventid,
                                waitingForRollEvent.getPlayer());
                        Timer rollDiceTimeoutTimer = new Timer();
                        rollDiceTimeoutTimer.schedule(new UserPromptTimerTask(rolleventid), DEFAULT_TIMEOUT);
                        break;
                    case PlayerRolling:
                        GameStateChangedToPlayerRollingEvent rollingEvent =
                                (GameStateChangedToPlayerRollingEvent)evt;
                        EventImpl diceEvent = new EventImpl(gameName, events.size(),
                                7, rollingEvent.getPlayer().getName(),
                                rollingEvent.getDiceThrowResult());
                        events.add(diceEvent);
                        break;
                    case PlayerMoving:
                        GameStateChangedToPlayerMovingEvent movingInnerEvent =
                                (GameStateChangedToPlayerMovingEvent)evt;
                        // move the player on the board
                        EventImpl movingEvent = new EventImpl(gameName, events.size(),
                                8, movingInnerEvent.getPlayer().getName(),
                                movingInnerEvent.getOriginCell(),
                                movingInnerEvent.getDestinationCell());
                        
                        if (movingInnerEvent.getDestinationCell().getType().compareTo("Jail") == 0 
                                && movingInnerEvent.getPlayer().isInJail())
                        {
                            EventImpl goToJailEvent = new EventImpl(gameName, events.size(),
                                11, movingInnerEvent.getPlayer().getName());
                            events.add(goToJailEvent);
                        }

                        events.add(movingEvent);
                        break;
                    case PlayerPassedStartSquare:
                        GameStateChangedToPlayerActionEvent playerPassedStartInnerEvent =
                                (GameStateChangedToPlayerRollingEvent)evt;
                        EventImpl playerPassedStartEvent = new EventImpl(gameName, events.size(),
                                9, playerPassedStartInnerEvent.getPlayer().getName(), DEFAULT_TIMEOUT);
                        events.add(playerPassedStartEvent);
                        break;
                    case PlayerLandedOnStartSquare:
                        GameStateChangedToPlayerActionEvent playerLandedOnStartInnerEvent =
                                (GameStateChangedToPlayerRollingEvent)evt;
                        EventImpl playerLandedOnStartEvent = new EventImpl(gameName, events.size(),
                                10, playerLandedOnStartInnerEvent.getPlayer().getName(), DEFAULT_TIMEOUT);
                        events.add(playerLandedOnStartEvent);
                        break;
                    case PlayerBuying:
                        GameStateChangedToPlayerBuyingEvent buyingEvent =
                                (GameStateChangedToPlayerBuyingEvent)evt;
                        EventImpl assetBoughtEvent = new EventImpl(gameName, events.size(),
                                14, buyingEvent.getPlayer().getName(), buyingEvent.getCell());
                        events.add(assetBoughtEvent);
                        break;
                    case PromptPlayerForBuying:
                        GameStateChangedToPromptPlayerBuyingEvent promptBuyingInnerEvent =
                                (GameStateChangedToPromptPlayerBuyingEvent)evt;
                        int eventid = events.size();
                        EventImpl promptBuyingEvent = new EventImpl(gameName, eventid,
                                12, promptBuyingInnerEvent.getPlayer().getName(), 
                                promptBuyingInnerEvent.getCell(), DEFAULT_TIMEOUT);
                        events.add(promptBuyingEvent);
                        UserPrompt.Init(promptBuyingInnerEvent.GetEventHandler(), eventid,
                                promptBuyingInnerEvent.getPlayer());
                        Timer buyTimeoutTimer = new Timer();
                        buyTimeoutTimer.schedule(new UserPromptTimerTask(eventid), DEFAULT_TIMEOUT);
                        break;
                    case PlayerBuilding:
                        GameStateChangedToPlayerBuildingEvent buildingEvent =
                                (GameStateChangedToPlayerBuildingEvent)evt;
                        EventImpl houseBoughtEvent = new EventImpl(gameName, events.size(),
                                15, buildingEvent.getPlayer().getName(), buildingEvent.getCity());
                        events.add(houseBoughtEvent);
                    case PromptPlayerForBuilding:
                        GameStateChangedToPromptPlayerBuildingEvent promptBuildingInnerEvent =
                                (GameStateChangedToPromptPlayerBuildingEvent)evt;
                        int buildeventid = events.size();
                        EventImpl promptBuildingEvent = new EventImpl(gameName, buildeventid,
                                15, promptBuildingInnerEvent.getPlayer().getName(), 
                                promptBuildingInnerEvent.getCity(), DEFAULT_TIMEOUT);
                        events.add(promptBuildingEvent);
                        UserPrompt.Init(promptBuildingInnerEvent.GetEventHandler(), buildeventid,
                                promptBuildingInnerEvent.getPlayer());
                        Timer buildTimeoutTimer = new Timer();
                        buildTimeoutTimer.schedule(new UserPromptTimerTask(buildeventid), DEFAULT_TIMEOUT);
                    case PlayerDrewCard:
                        GameStateChangedToPlayerDrewCardEvent drewCardEvent =
                                (GameStateChangedToPlayerDrewCardEvent)evt;
                        int eventType;
                        if (drewCardEvent.getDeck().getType().compareTo("Community Chest") == 0)
                            eventType = 17;
                        else if (drewCardEvent.getCard().getType().compareTo("Jail Pass") == 0)
                            eventType = 18;
                        else
                            eventType = 16;

                        EventImpl cardEvent = new EventImpl(gameName, events.size(),
                                eventType, drewCardEvent.getPlayer().getName(),
                                drewCardEvent.getCard().getMessage());
                        events.add(cardEvent);
                        break;
                    case PlayerGettingOutOfJail:
                        GameStateChangedToPlayerGettingOutOfJailEvent outOfJailEvent =
                                (GameStateChangedToPlayerGettingOutOfJailEvent)evt;
                        EventImpl outOfJailCardEvent = new EventImpl(gameName, events.size(),
                                20, outOfJailEvent.getPlayer().getName());
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

                            EventImpl paymentEvent = new EventImpl(gameName, events.size(),
                                19, paymentFromPlayerName,
                                isPaymentToOrFromTreasury, !isPaymemtFromBank,
                                paymentToPlayerName, paymentAmount);
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
