import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import objectmodel.AutomaticPlayer;
import objectmodel.CellBase;
import objectmodel.City;
import objectmodel.Player;
import objectmodel.PlayerInput;

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
