import java.io.IOException;
import main.*;

public class MainConsole {

	/**
	 * The main starting point
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		// Create the players manager
		PlayerManager playerManager = new PlayerManager();
		// Create the logic
		MonopolyGame monopolyGame = new MonopolyGame();
		// Create the event Handler & register to the events 
		EventHandler eventHandler = new EventHandler();
		eventHandler.registerEvents(monopolyGame);
		
		// Initiate the logic
		if (monopolyGame.initGame(playerManager.CreatePlayers()))
		{
			try
			{
				// All good - start the game!
				monopolyGame.startGame(true);
			}
			catch (InterruptedException e)
			{
				// Something went wrong during the game
				System.out.println("The game has ended unexpectedly! Error message: " + e.getMessage());
				e.printStackTrace();
			}
		}
		else
		{
			// Couldn't initialize the game
			System.out.println("Failed to initialize the game!");
		}
		
		System.out.println("Press any key to exit...");
		System.in.read();
	}
}
