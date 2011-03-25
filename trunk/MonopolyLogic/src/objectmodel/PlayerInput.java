package objectmodel;

import java.util.Dictionary;
import java.util.List;

/**
 * The class containing the input methods required from a player in the game
 * @author Benda & Eizenman
 *
 */
public abstract class PlayerInput
{
	protected Object sender;
	
	/**
	 * General c'tor (needed for the XML loading)
	 */
	public PlayerInput()
	{
		this.sender = null;
	}
	
	/**
	 * A c'tor that gets the player
	 * @param sender
	 */
	public PlayerInput(Object sender)
	{
		this.sender = sender;
	}
	
	/**
	 * Get a bid from the user for the given list of items being proposed in the current auction
	 * @param auctionCell - the cells being proposed in the auction
	 * @return the bid for the player
	 */
	public abstract AuctionBid getAuctionSuggestion(List<CellBase> auctionCell);
	/**
	 * Get the player's opinion on performing an auction
	 * @return whether to perform an auction or not
	 */
	public abstract boolean doPerformAuction();
	/**
	 * Get the player's decision on whether he wants to buy a cell
	 * @param landCell - the cell on which the player landed and can now buy
	 * @return whether the player wants to buy the cell
	 */
	public abstract boolean buyCell(CellBase landCell);
	/**
	 * Gets the player's decision on whether he wants to build a house in a city
	 * @param landCell - the city in which the player can now build a house
	 * @return The player's decision whether he wants to build a house
	 */
	public abstract boolean buildHouse(City landCell);
	/**
	 * Gets the auction decision from the auction manager after all the players have made their bids
	 * @param auctionResult - A dictionary containing the players' bids
	 * @return The player selected to make the deal with (null if none selected)
	 */
	public abstract Player getAuctionDecision(Dictionary<Player, AuctionBid> auctionResult);
	/**
	 * Gets the player's decision on what group of cells he wants to sell in the auction 
	 * @return The items that will be offered in the auction (null if none)
	 */
	public abstract List<CellBase> getAuctionItem();
}
