package services;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Scanner;

import objectmodel.AuctionBid;
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
	 * Creates the players that will play in the game
	 * @return The list of players
	 */
	public List<Player> CreatePlayers()
	{
		return generatePlayers(1, 3);
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
			humanPlayer.setName("Liron");
			humanPlayer.setInputObject(new UIPlayerInput(humanPlayer));
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
	
	
	
	
}

