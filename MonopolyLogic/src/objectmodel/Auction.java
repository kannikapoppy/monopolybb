package objectmodel;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import main.GameStates;
import main.StateManager;

/**
 * Represent a single auction taking place in the game
 * Controls the flow of the auction
 * @author Benda & Eizenman
 *
 */
public class Auction
{
	/**
	 * The person selling items
	 */
	private Player seller;
	/**
	 * The dictionary of the player bids
	 */
	private Dictionary<Player, AuctionBid> auctionSuggestions;
	/**
	 * A list of the items being proposed in the auction
	 */
	private List<CellBase> sellingItems;
	
	/**
	 * Create the auction
	 * Set the seller & the players that will suggest a bid
	 * @param seller - the auction runner
	 * @param allPlayers - all the players in the game
	 */
	public Auction(Player seller, List<Player> allPlayers)
	{
		this.seller = seller;
		auctionSuggestions = new Hashtable<Player, AuctionBid>();
		// select only the players that are in the game and are not the seller
		for (Player player : allPlayers)
		{
			if (player != seller && player.isInGame())
			{
				auctionSuggestions.put(player, new AuctionBid());
			}
		}
	}
	
	/**
	 * Perform the auction
	 * 1. Get the items to propose
	 * 2. Get the bids (cash & items)
	 * 3. Select the favorite bid
	 * 4. Close the deal 
	 */
	public void doAuction()
	{
		// Get the items
		sellingItems = seller.getInputObject().getAuctionItem();
		
		if (sellingItems == null)
		{
			// none selected auction finishes...
			StateManager.getStateManager().setCurrentState(this, GameStates.Auction, "No items selected for the auction");
			return;
		}
		
		// get the bids
		Enumeration<Player> keys = auctionSuggestions.keys();
		while(keys.hasMoreElements())
		{
			Player biddingPlayer = keys.nextElement();
			AuctionBid suggestion;
			do
			{
				StateManager.getStateManager().setCurrentState(this, GameStates.PlayerBidding, biddingPlayer.getName() + " is placing a bid");
				suggestion = biddingPlayer.getInputObject().getAuctionSuggestion(sellingItems);
			} while (!validateSuggestion(biddingPlayer, suggestion.getCash()));
			
			auctionSuggestions.put(biddingPlayer, suggestion);
		}
		
		// get the decision
		Player buyer = seller.getInputObject().getAuctionDecision(auctionSuggestions);
		if (buyer != null)
		{
			// got a buyer!
			StateManager.getStateManager().setCurrentState(this, GameStates.Auction, seller.getName() + " has decided to sell the items to " + buyer.getName());
			seller.getPlayerActions().performAuctionExchange(buyer, sellingItems, auctionSuggestions.get(buyer));
		}
		else
		{
			// no buyer!
			StateManager.getStateManager().setCurrentState(this, GameStates.Auction, seller.getName() + " has decided not to sell the items");
		}
	}

	/**
	 * Makes sure the suggestion is in the range the player can afford
	 * @param biddingPlayer - the player who made the suggestion
	 * @param suggestion - the $$$ suggestion
	 * @return
	 */
	private boolean validateSuggestion(Player biddingPlayer, int suggestion) {
		if (suggestion < 0)
		{
			StateManager.getStateManager().setCurrentState(this, GameStates.PlayerBidding, biddingPlayer.getName() + " has placed a negative bid");
			return false;
		}
		
		if (biddingPlayer.getMoney() < 0)
		{
			StateManager.getStateManager().setCurrentState(this, GameStates.PlayerBidding, biddingPlayer.getName() + " has placed an invalid bid (ain't got the cash)");
			return false;
		}
		
		return true;
	}
}
