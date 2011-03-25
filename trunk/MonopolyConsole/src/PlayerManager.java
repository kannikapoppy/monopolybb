import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import objectmodel.AuctionBid;
import objectmodel.AutomaticPlayer;
import objectmodel.CellBase;
import objectmodel.City;
import objectmodel.Player;
import objectmodel.PlayerInput;
import objectmodel.RealEstate;

/**
 * An assisting class used to create all the players in the game
 * Also defines the 'Human' input for the human players
 * @author Benda & Eizenman
 *
 */
public class PlayerManager
{
	/**
	 * The scanner used to get input from the player
	 */
	private Scanner scanner = new Scanner(System.in);
	
	/**
	 * Creates the players that will play in the game
	 * @return The list of players
	 */
	public List<Player> CreatePlayers()
	{
		boolean validInput = false;
		int numPlayers, numHumanPlayers;
		
		while(!validInput)
		{
			try
			{
				// Get the number of players (human, robots)
				System.out.println("Enter the total amount of players: ");
				numPlayers = Integer.parseInt(scanner.nextLine());
				
				System.out.println("Enter the amount of human players: ");
				numHumanPlayers = Integer.parseInt(scanner.nextLine());
				
				// validate the input & create the players if all good
				if (validatePlayersNumber(numPlayers, numHumanPlayers))
				{
					return generatePlayers(numHumanPlayers, numPlayers - numHumanPlayers);
				}
			}
			catch (Exception exp)
			{
				System.out.println("Bad input: " + exp.getMessage());
			}
		}
		
		return null;
	}
	
	/**
	 * Validate the number of players is between 2 & 6
	 * @param numPlayers - The total number of players 
	 * @param numHumanPlayers - The number of human players
	 * @return Whether the player numbers are correct or not
	 */
	private boolean validatePlayersNumber(int numPlayers, int numHumanPlayers)
	{
		if (numPlayers < 2 || numPlayers > 6)
		{
			System.out.println("The number of players should be 2-6!");
			return false;
		}
		if (numHumanPlayers > numPlayers || numHumanPlayers < 0)
		{
			System.out.println("The number of human players should be between 0 to the number of players!");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Create the players according to the numbers indicated
	 * Gives each robot player a name automatically & asks for a name for the human players
	 * @param numHumanPlayers - the number of human players to be created
	 * @param numAutoPlayers - the number of robots to be created
	 * @return - The list of the generated players
	 */
	private List<Player> generatePlayers(int numHumanPlayers, int numAutoPlayers)
	{
		List<Player> playerList = new ArrayList<Player>();
		
		// create the human players w/ names
		for (int i = 0; i < numHumanPlayers; i++)
		{
			Player humanPlayer = new Player();
			System.out.println("Enter human player #" + (i+1) + "'s name: ");
			humanPlayer.setName(scanner.nextLine());
			humanPlayer.setInputObject(new ConsoleInput(humanPlayer));
			playerList.add(humanPlayer);
		}
		
		// create the robots with a name like "Computer#"
		for (int i = 0; i < numAutoPlayers; i++)
		{
			Player autoPlayer = new AutomaticPlayer();
			autoPlayer.setName("Computer" + (i + 1));
			playerList.add(autoPlayer);
		}
		
		return playerList;
	}
	
	/**
	 * The input object that is used for the human players in the console game
	 * @author Benda & Eizenman
	 *
	 */
	public class ConsoleInput extends PlayerInput
	{
		/**
		 * A c'tor that gets a reference of the player
		 * @param sender - the player of which this class will be the input of
		 */
		public ConsoleInput(Object sender) {
			super(sender);
		}

		/**
		 * Gets an single auction bid for a list of cells being proposed for sale
		 */
		@Override
		public AuctionBid getAuctionSuggestion(List<CellBase> auctionCell) {
			AuctionBid bid = new AuctionBid();
			Player me = (Player) sender;
			System.out.println(me.getName() + " place your offer for buying: ");
			
			for (CellBase cell : auctionCell)
			{
				System.out.println("****" + cell.getName() + "****");
			}
			
			System.out.println("\tCash offer: ");
			int cashOffer = 0;
			while ((cashOffer = ReadIntFromConsole()) < 0)
			{
				System.out.println("\tBad input, try again:");
			}
			bid.setCash(cashOffer);
			
			System.out.println("\tAsset offer: ");
			bid.setCells(getAuctionItem());
			
			return bid;
		}

		/**
		 * Asks the player if he wants to sell an asset
		 */
		@Override
		public boolean doPerformAuction()
		{
			System.out.println("Would you like to sell an asset? (Y/N):");
			return GetYesNoAnswer();
		}

		/**
		 * Checks if the player wants to buy a cell
		 */
		@Override
		public boolean buyCell(CellBase landCell)
		{
			String balance = "Your balance is: " + ((Player) sender).getMoney();
			System.out.println(balance);
			String buyMessage = "Would you like to buy " + landCell.getName() + " for " + landCell.getPrice()+ "? (Y/N):";
			System.out.println(buyMessage);
			return GetYesNoAnswer();
		}
		
		/**
		 * Checks if the player wants to build a house on the given cell
		 */
		@Override
		public boolean buildHouse(City landCell) {
			String buyMessage = "Would you like to build a house in " + landCell.getName() + " for " + landCell.getSingleHousePrice() + "? (Y/N):";
			System.out.println(buyMessage);
			return GetYesNoAnswer();
		}
		
		/**
		 * Gets an auction decision from the player who performed an auction
		 * The decision is made based on the given auction suggestions from all the players
		 */
		@Override
		public Player getAuctionDecision(
				Dictionary<Player, AuctionBid> auctionResult)
		{
			System.out.println("Auction finished, time to select your decision:");
			List<Player> players = new ArrayList<Player>();
			int counter = 1;
			
			System.out.println("0. Cancel Auction");
			Enumeration<Player> keys = auctionResult.keys();
			while (keys.hasMoreElements())
			{
				Player suggestingPlayer = keys.nextElement();
				AuctionBid bid = auctionResult.get(suggestingPlayer);
				System.out.println(counter + ". " + suggestingPlayer.getName() + " suggsting:");
				System.out.println("\tCash: " + bid.getCash());
				if (bid.getCells() != null)
				{
					for (CellBase cell : bid.getCells())
					{
						System.out.println("\tAsset: " + cell.getName());
					}
				}
				
				players.add(suggestingPlayer);
				counter ++;
			}
			
			int selection = 0;
			while ((selection = ReadIntFromConsole()) < 0 || selection > players.size())
			{
				System.out.println("Invalid input, try again:");
			}
			if (selection == 0)
			{
				return null;
			}
			
			return players.get(selection - 1);
		}

		/**
		 * Gets the item(s) to be proposed in the auction
		 */
		@Override
		public List<CellBase> getAuctionItem()
		{
			Player me = (Player) sender;
			List<CellBase> sellingList = null;
			
			if (!me.getOwnedCells().getCell().isEmpty())
			{
				int counter = 1;
				System.out.println(me.getName() + " select item to sell:");
				System.out.println("\t0. None");
				for (CellBase itemToSell : me.getOwnedCells().getCell())
				{
					System.out.println("\t" + counter + ". " + itemToSell.getName());
					counter ++;
				}
				
				int selection = 0;
				while ((selection = ReadIntFromConsole()) < 0 || selection > me.getOwnedCells().getCell().size())
				{
					System.out.println("Invalid input, try again:");
				}
				
				if (selection != 0)
				{
					CellBase itemToSell = me.getOwnedCells().getCell().get(selection - 1);
					if (RealEstate.getRealEstate().doesOwnWholeGroup(itemToSell))
					{
						System.out.println("You own the whole group. Sell group? (Y/N)");
						if (GetYesNoAnswer())
						{
							sellingList = RealEstate.getRealEstate().getWholeGroup(itemToSell);
						}
					}
					else
					{
						sellingList = new ArrayList<CellBase>();
						sellingList.add(itemToSell);
					}
				}
			}
			return sellingList;
		}
		
		/**
		 * Reads an integer from the console
		 * @return the integer read from the console
		 */
		private int ReadIntFromConsole()
		{
			boolean validInt = false;
			int newInt = 0;
			
			while (!validInt)
			{
				try
				{
					newInt = Integer.parseInt(scanner.nextLine());
					validInt = true;
				}
				catch (Exception exp)
				{
					System.out.println("Please enter a valid number");
				}
			}
			
			return newInt;
		}
		
		/**
		 * Gets a Yes/No answer
		 * @return a boolean translation for (Y/N)
		 */
		private boolean GetYesNoAnswer()
		{
			String input;
			while (!(input = scanner.nextLine()).equalsIgnoreCase("Y") && !input.equalsIgnoreCase("N"))
			{
				System.out.println("Invalid input. Try again (Y/N): ");
			}
			
			return (input.equalsIgnoreCase("Y"));
		}		
	}
	
	
}
