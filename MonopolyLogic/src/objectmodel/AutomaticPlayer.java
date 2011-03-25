package objectmodel;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

/**
 * 
 * All AI decisions are taken in this class
 * This class represents a "robot input"
 *
 */
public class AutomaticPlayer extends Player
{
	private static final int MAX_CASH_FOR_PERFORMING_AUCTION = 50;
	private static final int MIN_CASH_FOR_BUYING_CELL = 300;
	private static final int MIN_CASH_FOR_BUILDING_HOUSE = 200;
	private static final int AUCTION_SUGGESTION = 100;
	
	public AutomaticPlayer()
	{
		inputObject = new AutomaticInput(this);
	}
	
	public class AutomaticInput extends PlayerInput
	{

		public AutomaticInput(Object sender) {
			super(sender);
		}

		@Override
		public boolean doPerformAuction() {
			if (((Player) sender).getMoney() < MAX_CASH_FOR_PERFORMING_AUCTION)
			{
				return true;
			}
			return false;
		}

		@Override
		public boolean buyCell(CellBase landCell) {
			if (((Player) sender).getMoney() > MIN_CASH_FOR_BUYING_CELL)
			{
				return true;
			}
			return false;
		}
		
		@Override
		public boolean buildHouse(City landCell) {
			if (((Player) sender).getMoney() > MIN_CASH_FOR_BUILDING_HOUSE)
			{
				return true;
			}
			return false;
		}

		@Override
		public AuctionBid getAuctionSuggestion(List<CellBase> auctionCell) {
			AuctionBid bid = new AuctionBid();
			if (((Player) sender).getMoney() > AUCTION_SUGGESTION)
			{
				bid.setCash(AUCTION_SUGGESTION);
			}
			
			return bid;
		}

		@Override
		public Player getAuctionDecision(
				Dictionary<Player, AuctionBid> auctionResult) {
			Player highestBidder = null;
			
			Enumeration<Player> keys = auctionResult.keys(); 
			while (keys.hasMoreElements())
			{
				Player newBidder = keys.nextElement();
				if (highestBidder == null)
				{
					highestBidder = newBidder;
				}
				else
				{
					if (auctionResult.get(newBidder).getCash() > auctionResult.get(highestBidder).getCash())
					{
						highestBidder = newBidder;
					}
				}
			}
			
			return highestBidder;
		}

		@Override
		public List<CellBase> getAuctionItem() {
			Player me = (Player) sender;
			List<CellBase> sellingList = null;
			
			if (!me.getOwnedCells().getCell().isEmpty())
			{
				CellBase itemToSell = me.getOwnedCells().getCell().get(0);
				if (RealEstate.getRealEstate().doesOwnWholeGroup(itemToSell))
				{
					sellingList = RealEstate.getRealEstate().getWholeGroup(itemToSell);
				}
				else
				{
					sellingList = new ArrayList<CellBase>();
					sellingList.add(itemToSell);
				}
			}
			return sellingList;
		}
	}
}
