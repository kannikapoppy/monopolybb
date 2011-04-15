package services;

import java.util.Dictionary;
import java.util.List;

import objectmodel.AuctionBid;
import objectmodel.CellBase;
import objectmodel.City;
import objectmodel.Player;
import objectmodel.PlayerInput;

/**
 * The input object that is used for the human players in the console game
 * @author Benda & Eizenman
 *
 */
public class UIPlayerInput extends PlayerInput
{
	/**
	 * A c'tor that gets a reference of the player
	 * @param sender - the player of which this class will be the input of
	 */
	public UIPlayerInput(Object sender) {
		super(sender);
	}

	/**
	 * Gets an single auction bid for a list of cells being proposed for sale
	 */
	@Override
	public AuctionBid getAuctionSuggestion(List<CellBase> auctionCell) {
		return new AuctionBid();
	}

	/**
	 * Asks the player if he wants to sell an asset
	 */
	@Override
	public boolean doPerformAuction()
	{
		return true;
	}

	/**
	 * Checks if the player wants to buy a cell
	 */
	@Override
	public boolean buyCell(CellBase landCell)
	{
		return true;
	}
	
	/**
	 * Checks if the player wants to build a house on the given cell
	 */
	@Override
	public boolean buildHouse(City landCell) {
		return true;
	}
	
	/**
	 * Gets an auction decision from the player who performed an auction
	 * The decision is made based on the given auction suggestions from all the players
	 */
	@Override
	public Player getAuctionDecision(
			Dictionary<Player, AuctionBid> auctionResult)
	{
		return null;
	}

	/**
	 * Gets the item(s) to be proposed in the auction
	 */
	@Override
	public List<CellBase> getAuctionItem()
	{
		return null;
	}	
}