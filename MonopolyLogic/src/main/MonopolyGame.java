package main;
import javax.xml.bind.*;
import main.StateManager.GameStartEventListener;
import main.StateManager.GameStateChangedEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import objectmodel.*; 

/**
 * The main logic of the monopoly game
 * @author Benda & Eizenman
 *
 */
public class MonopolyGame
{
	/**
	 * The path for the game board config file
	 */
	private static final String BOARD_PATH = "bin\\configuration\\MonopolyBoard.xml";
	/**
	 * The path for the community chest card deck config file
	 */
	private static final String COMMUNITY_CHEST_DECK_PATH = "bin\\configuration\\CommunityChestDeck.xml";
	/**
	 * The path for the chance card deck config file
	 */
	private static final String CHANCE_DECK_PATH = "bin\\configuration\\ChanceDeck.xml";
	/**
	 * The game board representation
	 */
	private GameBoard gameBoard;
	/**
	 * The chance card deck
	 */
	private CardsDeck chanceDeck;
	/**
	 * The community chest card deck
	 */
	private CardsDeck communityChestDeck;
	/**
	 * The game dice
	 */
	private Dice gameDice;
	/**
	 * The players in the game
	 */
	private List<Player> playerList;
	/**
	 * The index of the current player
	 */
	private int currentPlayerIndex;
	/**
	 * A boolean indicator if the game is in play
	 */
	private boolean isPlaying;
	/**
	 * The thread used to run the logic
	 */
	private Thread logicThread;
	
	/**
	 * Creates a new monopoly game logic
	 */
	public MonopolyGame()
	{
		currentPlayerIndex = 0;
		gameDice = new Dice();
		registerEvents();
		getStateManager().setCurrentState(this, GameStates.Uninitialized, "Starting a fresh Monopoly Game");
	}
	
	/**
	 * Initiates the game objects
	 * @param playerList - the players that will play in this game
	 * @return - whether the initialization has succeeded or not
	 */
	public boolean initGame(List<Player> playerList)
	{
		getStateManager().setCurrentState(this, GameStates.Initializing, "Initialzing a new game");
		boolean initializedSuccessfully = true;
		String failMessage = "";
		this.playerList = playerList;
		
		// Check if the number of players is valid
		if (playerList == null || playerList.size() < 2)
		{
			initializedSuccessfully = false;
			failMessage = "The player list is too short";
		}
		
		// Set the players action objects for the logic methods a player can trigger
		for (Player player : playerList)
		{
			player.setPlayerActions(new GeneralActions(player));
		}
		
		// Load the game board & card decks
		try
		{			
			JAXBContext jContext = JAXBContext.newInstance("objectmodel");
			Unmarshaller unmarshaller = jContext.createUnmarshaller() ;
			gameBoard = (GameBoard)unmarshaller.unmarshal(new FileInputStream(BOARD_PATH)); 
			chanceDeck = (CardsDeck)unmarshaller.unmarshal(new FileInputStream(CHANCE_DECK_PATH));
			chanceDeck.shuffleDeck();
			communityChestDeck = (CardsDeck)unmarshaller.unmarshal(new FileInputStream(COMMUNITY_CHEST_DECK_PATH));
			communityChestDeck.shuffleDeck();
			RealEstate.getRealEstate().init(gameBoard);
		} 
		catch (JAXBException e)
		{
			initializedSuccessfully = false;
			failMessage = "Couldn't load the game board & cards since the XML file does not suit the schema: " + e.getMessage();
		}
		catch (FileNotFoundException e)
		{
			initializedSuccessfully = false;
			failMessage = "Couldn't load the game board & cards since a XML file could not be found: " + e.getMessage();
		}
		
		// check if all went right so far...
		if(initializedSuccessfully)
		{
			getStateManager().setCurrentState(this, GameStates.Initialized, "The game was initialized successfully");
			isPlaying = true;
		}
		else
		{
			getStateManager().setCurrentState(this, GameStates.Error, failMessage);
		}
		
		return initializedSuccessfully;
	}
	
	/**
	 * Starts the game logic in a different thread
	 * @param blocking - whether this method should block the main thread or not
	 * @return whether the game has started successfully. When run in blocking mode don't expect for an immediate answer!
	 * @throws InterruptedException
	 */
	public boolean startGame(boolean blocking) throws InterruptedException
	{
		// makes sure the game object are initialized
		if (gameBoard == null || chanceDeck == null || communityChestDeck == null)
		{
			getStateManager().setCurrentState(this, GameStates.Error, "Game started with null objects");
			return false;
		}
		// makes sure the players are all set
		if (playerList == null || playerList.size() < 2 || playerList.size() > 6)
		{
			getStateManager().setCurrentState(this, GameStates.Error, "Can only start a game with 2-6 players!");
			return false;
		}
		
		// creates and runs the logic thread
		logicThread = new Thread(new Runnable() {
			
			@Override
			public void run()
			{
				getStateManager().setCurrentState(this, GameStates.Starting, "Game Starting");
				gameLoop();
			}
		});
		logicThread.start();
		
		// blocking the main thread if needed
		if (blocking)
		{
			while (isPlaying)
			{
				Thread.sleep(1000);
			}
		}
		
		return true;
	}
	
	/**
	 * 
	 * @return Gets the states manager
	 */
	public StateManager getStateManager()
	{
		return StateManager.getStateManager();
	}
	
	/**
	 * switches the player to next one
	 */
	private void switchPlayer()
	{
		switchPlayer((currentPlayerIndex + 1) % playerList.size());
	}
	
	/**
	 * switches turns to a specific player
	 * @param playerIndex the specific player
	 */
	private void switchPlayer(int playerIndex)
	{
		Player nextPlayer = playerList.get(playerIndex);
		// if the player isn't in game move to the next one
		if (!nextPlayer.isInGame())
		{
			switchPlayer((playerIndex + 1) % playerList.size());
		}
		else
		{
			currentPlayerIndex = playerIndex;
			String cellName = gameBoard.getCellBase().get(nextPlayer.getBoardPosition()).getName();
			String switchMessage = "It's now " + nextPlayer.getName() + "'s turn. Current Position: " + cellName + ". Balance: " + nextPlayer.getMoney();
			getStateManager().setCurrentState(this, GameStates.PlayerSwitching, switchMessage);
		}
	}

	/**
	 * The main game loop!
	 */
	private void gameLoop()
	{
		// as long as we don't have a winner - go through the loop
		Player winner;
		do
		{
			// get the current player & position
			Player currentPlayer = playerList.get(currentPlayerIndex);
			CellBase currentCell = gameBoard.getCellBase().get(currentPlayer.getBoardPosition());
			
			// check if the player should roll the dice
			if(currentCell.shouldRollTheDice(currentPlayer))
			{
				// roll the dice & check if the player should move
				int moveCells = gameDice.roll();
				getStateManager().setCurrentState(this, GameStates.PlayerRolling, currentPlayer.getName() + " rolled a " + gameDice);
				if (currentCell.shouldMove(currentPlayer, gameDice))
				{
					// Move!
					getStateManager().setCurrentState(this, GameStates.PlayerMoving, currentPlayer.getName() + " is moving " + gameDice.getCurrentRoll() + " steps");
					movePlayer(currentPlayer, moveCells);
				}
				else
				{
					// probably in jail, didn't hit a double & doesn't have an out-of-jail card
					getStateManager().setCurrentState(this, GameStates.PlayerMoving, currentPlayer.getName() + " is staying in place");
				}
			}
			else
			{
				// probably in parking and can't move
				getStateManager().setCurrentState(this, GameStates.PlayerRolling, currentPlayer.getName() + " doesn't get to roll the dice");
			}
			
			// finished the turn, next player!
			switchPlayer();
		} while ((winner = getWinner()) == null);
		
		// Game over!
		// release the main thread if needed
		isPlaying = false;
		getStateManager().setCurrentState(this, GameStates.GameOver, winner.getName() + " has won the game");
	}

	/**
	 * Moves a player to a specific cell name
	 * The player will move 36 steps if the cell name could not be found
	 * @param movingPlayer - the player to be moved
	 * @param cellName - the cell name we want to reach
	 * @param getRoadToll - whether the player should get/pay road toll while moving
	 */
	private void movePlayer(Player movingPlayer, String cellName, boolean getRoadToll)
	{
		int stepsCount = 1;
		int currentPlayerPosition = (movingPlayer.getBoardPosition() + stepsCount) % gameBoard.getCellBase().size();
		CellBase currentCell = gameBoard.getCellBase().get(currentPlayerPosition);
		while (!currentCell.getName().equals(cellName) && stepsCount <= 36)
		{
			stepsCount ++;
			currentPlayerPosition = (movingPlayer.getBoardPosition() + stepsCount) % gameBoard.getCellBase().size();
			currentCell = gameBoard.getCellBase().get(currentPlayerPosition);
		}
		
		movePlayer(movingPlayer, stepsCount, getRoadToll);
	}
	
	/**
	 * Moves a player the steps count on the board w/ road toll
	 * @param movingPlayer - the player to be moved
	 * @param stepsCount - the number of cells to advance
	 */
	private void movePlayer(Player movingPlayer, int stepsCount)
	{
		movePlayer(movingPlayer, stepsCount, true);
	}
	
	/**
	 * Moves a player the steps count on the board
	 * @param movingPlayer - the player to be moved
	 * @param stepsCount - the number of cells to advance
	 * @param getRoadToll - whether the player should get/pay road toll
	 */
	private void movePlayer(Player movingPlayer, int stepsCount, boolean getRoadToll)
	{
		int currentPlayerPosition = movingPlayer.getBoardPosition();
		// pay/get pass toll for all the cells on the way
		while (stepsCount > 0)
		{
			currentPlayerPosition ++;
			currentPlayerPosition = currentPlayerPosition % gameBoard.getCellBase().size();
			if (stepsCount > 1)
			{
				if (getRoadToll)
				{
					CellBase passedCell = gameBoard.getCellBase().get(currentPlayerPosition);
					playerPassedThroughCell(movingPlayer, passedCell);
				}
			}
			else
			{
				// Reached the goal
				movingPlayer.setBoardPosition(currentPlayerPosition);
				playerLandedOnCell(movingPlayer, gameBoard.getCellBase().get(currentPlayerPosition));
			}
			stepsCount --;
		}
	}
	
	/**
	 * Called when the player has passed through a cell when the road toll is enabled
	 * @param passedPlayer - the passing player
	 * @param passedCell - the cell passed by
	 */
	private void playerPassedThroughCell(Player passedPlayer, CellBase passedCell)
	{
		// Pay/get the toll according to the toll's sign
		// negative = get paid
		// positive = pay!
		Integer passToll = passedCell.getPassToll();
		if (passToll != null)
		{
			if (passToll < 0)
			{
				performMoneyTransaction(passedPlayer, -passToll, true);
				String message = String.format("%s just received %d dollars for passing through %s", passedPlayer.getName(), -passToll, passedCell.getName());
				getStateManager().setCurrentState(this, GameStates.PlayerGotPaid, message);
			}
			else
			{
				performMoneyTransaction(passedPlayer, passToll, false);
				String message = String.format("%s just lost %d dollars for passing through %s", passedPlayer.getName(), passToll, passedCell.getName());
				getStateManager().setCurrentState(this, GameStates.PlayerPaying, message);
			}
		}
	}
	
	/**
	 * Perform a player landed on cell action
	 * @param landedPlayer - the player that landed on the cell
	 * @param landedCell - the cell on which the player has landed
	 */
	private void playerLandedOnCell(Player landedPlayer, CellBase landedCell)
	{
		getStateManager().setCurrentState(this, GameStates.PlayerLanded, landedPlayer.getName() + " has just landed on " + landedCell.getName());
		if(landedPlayer.getOwnedCells().getCell().size() > 0 && landedPlayer.getInputObject().doPerformAuction())
		{
			performAuction(landedPlayer);
		}
		
		landedCell.performPlayerLand(landedPlayer);
	}
	
	/**
	 * Perform an asset transaction between two players  
	 * @param sourcePlayer - the player with source assets
	 * @param destPlayer - the player with destination assets 
	 * @param sourceAssets - the assets that move from source player to destination player
	 * @param destAssets - the assets that move from destination player to source player
	 */
	private void performAssetTransaction(Player sourcePlayer, Player destPlayer, List<CellBase> sourceAssets, List<CellBase> destAssets)
	{
		// used for safety (so that the lists won't be modified as they are being iterated through)
		List<CellBase> assistingList = new ArrayList<CellBase>();
		
		if (sourceAssets != null)
		{
			for (CellBase cell : sourceAssets)
			{
				String message = cell.getName() + " is moving from " + sourcePlayer.getName() + " to " + destPlayer.getName();
				getStateManager().setCurrentState(this, GameStates.PlayerPaying, message);
				assistingList.add(cell);
			}
			
			for (CellBase cell : assistingList)
			{
				sourcePlayer.getOwnedCells().getCell().remove(cell);
				destPlayer.getOwnedCells().getCell().add(cell);
				cell.setOwner(destPlayer);
			}
		}
		
		if (destAssets != null)
		{
			assistingList.clear();
			
			for (CellBase cell : destAssets)
			{
				String message = cell.getName() + " is moving from " + destPlayer.getName() + " to " + sourcePlayer.getName();
				getStateManager().setCurrentState(this, GameStates.PlayerPaying, message);
				assistingList.add(cell);
			}
			
			for (CellBase cell : assistingList)
			{
				destPlayer.getOwnedCells().getCell().remove(cell);
				sourcePlayer.getOwnedCells().getCell().add(cell);
				cell.setOwner(sourcePlayer);
			}
		}
	}
	
	/**
	 * Perform a money transaction between two players or between a player and the bank (to player should be null)
	 * @param fromPlayer - the player paying
	 * @param toPlayer - the player being paid (the bank if null)
	 * @param amount - the amount of money transfered
	 */
	private void performMoneyTransaction(Player fromPlayer, Player toPlayer, int amount)
	{
		if (toPlayer == null)
		{
			performMoneyTransaction(fromPlayer, amount, false);
			return;
		}

		int debtMoney = fromPlayer.subtractMoney(amount);
		if (debtMoney == 0)
		{
			toPlayer.addMoney(amount);
		}
		else
		{
			toPlayer.addMoney(amount - debtMoney);
			handleMoneyDebt(fromPlayer, toPlayer, debtMoney);
		}
	}
	
	/**
	 * Perform a money transaction between a player and the bank
	 * @param relevantPlayer - the player involved in the transaction
	 * @param amount - the amount of money involved
	 * @param toPlayer - whether the money should move from the player or to the player
	 */
	private void performMoneyTransaction(Player relevantPlayer, int amount, boolean toPlayer)
	{
		if (toPlayer)
		{
			relevantPlayer.addMoney(amount);
		}
		else
		{
			int debtMoney = relevantPlayer.subtractMoney(amount);
			if (debtMoney > 0)
			{
				handleMoneyDebt(relevantPlayer, null, debtMoney);
			}
		}
	}
	
	/**
	 * Handles money debt between two players (or between a player and the bank if toDebtPlayer is null)
	 * Performs an auction as long as it is possible and makes sure the player in debt returns the money
	 * In case the player in debt does not have enough money to pay - all his assets move to the second player
	 * He then gets out of the game.
	 * @param inDebtPlayer - the player in debt
	 * @param toDebtPlayer - the player to whom the inDebtPlayer owes the money (== the bank if null)
	 * @param debtMoney - the amount of money that still has to be paid
	 */
	private void handleMoneyDebt(Player inDebtPlayer, Player toDebtPlayer, int debtMoney)
	{
		// Checks if the destination is the bank
		String toDebtPlayerName = "Bank";
		if (toDebtPlayer != null)
		{
			toDebtPlayerName = toDebtPlayer.getName();
		}
		
		// Triggers the player broke event
		String message = inDebtPlayer.getName() + " owes " + debtMoney + " to " + toDebtPlayerName;
		getStateManager().setCurrentState(this, GameStates.PlayerBroke, message);
		boolean playerHasAssets = (inDebtPlayer.getOwnedCells().getCell().size() > 0); 
		
		// Perform an auction if possible
		while (debtMoney > 0 && playerHasAssets && inDebtPlayer.canPerformAuction() && inDebtPlayer.getInputObject().doPerformAuction())
		{
			performAuction(inDebtPlayer);
			
			if (inDebtPlayer.getMoney() > 0)
			{
				int returnedCash = Math.min(debtMoney, inDebtPlayer.getMoney());
				debtMoney -= returnedCash;
				message = inDebtPlayer.getName() + " is returning " + returnedCash + " to " + toDebtPlayerName;
				getStateManager().setCurrentState(this, GameStates.PlayerPaying, message);
				performMoneyTransaction(inDebtPlayer, toDebtPlayer, returnedCash);
			}
		}
		
		// Player is still in debt and cannot sell assets
		if (debtMoney > 0)
		{
			dropPlayer(inDebtPlayer, toDebtPlayer);
			getStateManager().setCurrentState(this, GameStates.PlayerLost, inDebtPlayer.getName() + " has lost!");
		}
	}
	
	/**
	 * Call when the player should go out of the game
	 * Takes 'droppedPlayer' out of the game and moves his assets to 'passToPlayer' (the bank when null)
	 * @param droppedPlayer - the player to be dropped
	 * @param passToPlayer - the player to whom the assets will be transfered to (the bank when null)
	 */
	private void dropPlayer(Player droppedPlayer, Player passToPlayer)
	{
		// return cards to the deck
		for (BonusCard card : droppedPlayer.getOwnedCards().getCard())
		{
			chanceDeck.returnCard(card);
		}
		droppedPlayer.getOwnedCards().getCard().clear();
		droppedPlayer.setMoney(0);
		droppedPlayer.setIsInGame(false);
		
		// move cells to the other player/bank
		if (passToPlayer != null)
		{
			performAssetTransaction(droppedPlayer, passToPlayer, droppedPlayer.getOwnedCells().getCell(), null);
		}
		else
		{
			for (CellBase cell : droppedPlayer.getOwnedCells().getCell())
			{
				cell.returnToBank();
			}
			droppedPlayer.getOwnedCells().getCell().clear();
		}
	}
	
	/**
	 * Initiate an auction by 'auction player'
	 * @param auctionPlayer - the player that wants to perform the auction
	 */
	private void performAuction(Player auctionPlayer)
	{
		getStateManager().setCurrentState(this, GameStates.Auction, auctionPlayer.getName() + " is performing an auction");
		
		if (auctionPlayer.getOwnedCells().getCell().size() > 0 && auctionPlayer.canPerformAuction())
		{
			auctionPlayer.raiseAuctionPerformed();
			Auction auction = new Auction(auctionPlayer, playerList);
			auction.doAuction();
		}
		else
		{
			getStateManager().setCurrentState(this, GameStates.Auction, auctionPlayer.getName() + " has no items to sell!");
		}
	}
	
	/**
	 * Checks if there's a winner in the game (the last man standing)
	 * @return The winner winner chicken dinner
	 */
	private Player getWinner()
	{
		Player winner = null;
		for (Player player : playerList)
		{
			if (player.isInGame())
			{
				if (winner == null)
				{
					winner = player;
				}
				else
				{
					return null;
				}
			}
		}
		
		return winner;
	}
	
	/**
	 * Registers for the in-game events (@deprecated)
	 */
	private void registerEvents()
	{
		getStateManager().registerToGameChangedEvent(GameStartEventListener.class, new GameStartEventListener() {
			
			@Override
			public void gameStateChanged(GameStateChangedEvent evt)
			{
				switchPlayer(0);
			}
		});
	}

	private class GeneralActions extends PlayerActions
	{
		public GeneralActions(Object actor) {
			super(actor);
		}

		@Override
		public void payToPlayer(Player otherPlayer, int money) {
			performMoneyTransaction((Player) actor, otherPlayer, money);
		}

		@Override
		public void payToAllPlayers(int money) {
			for (Player otherPlayer : playerList)
			{
				if (otherPlayer.isInGame() && otherPlayer != (Player) actor)
				{
					performMoneyTransaction((Player) actor, otherPlayer, money);
				}
			}
		}

		@Override
		public void getMoneyFromAllPlayers(int money) {
			for (Player otherPlayer : playerList)
			{
				if (otherPlayer.isInGame() && otherPlayer != (Player) actor)
				{
					performMoneyTransaction(otherPlayer, (Player) actor, money);
				}
			}
		}

		@Override
		public void getMoneyFromPlayer(Player otherPlayer, int money) {
			performMoneyTransaction(otherPlayer, (Player) actor, money);
			
		}

		@Override
		public void getMoneyFromBank(int money) {
			performMoneyTransaction((Player) actor, money, true);
		}

		@Override
		public void payMoneyToBank(int money) {
			performMoneyTransaction((Player) actor, money, false);
		}

		@Override
		public void moveToCell(String cellName, boolean getRoadToll)
		{
			movePlayer((Player) actor, cellName, getRoadToll);
		}

		@Override
		public void drawChanceCard() {
			BonusCard card = chanceDeck.drawCard();
			getStateManager().setCurrentState(this, GameStates.PlayerDrewCard, card.getMessage());
			
			if (card.getType().equals("MoveTo"))
			{
				moveToCell(card.getMoveTo(), card.isGetGoCash());
				chanceDeck.returnCard(card);
			}
			else if(card.getType().equals("Jail Pass"))
			{
				((Player) actor).getOwnedCards().getCard().add(card);
			}
			else if(card.getType().equals("Money"))
			{
				if (card.isBank())
				{
					getMoneyFromBank(card.getAmount());
				}
				else
				{
					getMoneyFromAllPlayers(card.getAmount());
				}
				chanceDeck.returnCard(card);
			}
		}

		@Override
		public void drawCommunityChestCard() {
			BonusCard card = communityChestDeck.drawCard();
			getStateManager().setCurrentState(this, GameStates.PlayerDrewCard, card.getMessage());
			
			if (card.getType().equals("MoveTo"))
			{
				if (card.getMoveTo().equals("Jail (Free Pass)"))
				{
					((Player) actor).setInJail(true);
				}
				moveToCell(card.getMoveTo(), card.isGetGoCash());
			}
			else if(card.getType().equals("Money"))
			{
				if (card.isBank())
				{
					payMoneyToBank(card.getAmount());
				}
				else
				{
					payToAllPlayers(card.getAmount());
				}
			}
			
			communityChestDeck.returnCard(card);
		}

		@Override
		public void returnCardToDeck(BonusCard card) {
			chanceDeck.returnCard(card);
		}

		@Override
		public void performAuctionExchange(Player buyer,
				List<CellBase> sellingItems, AuctionBid auctionSuggestions)
		{
			if (auctionSuggestions.getCash() > 0)
			{
				String message = buyer.getName() + " is paying " + auctionSuggestions.getCash() + " to " + ((Player) actor).getName();
				getStateManager().setCurrentState(this, GameStates.PlayerPaying, message);
				buyer.getPlayerActions().payToPlayer((Player) actor, auctionSuggestions.getCash());
			}
			
			performAssetTransaction((Player) actor, buyer, sellingItems, auctionSuggestions.getCells());
		}
	}
}
