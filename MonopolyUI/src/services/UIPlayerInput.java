package services;

import java.lang.reflect.InvocationTargetException;
import java.util.Dictionary;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

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
	private static final String BUY_CELL_WINDOW_TITLE = "Buy Asset Option";
	private static final String BUY_CELL_QUESTION_PREFIX = "Would you like to buy ";
	
	private static final String BUILD_HOUSE_WINDOW_TITLE = "Build A House";
	private static final String BUILD_HOUSE_QUESTION_PREFIX = "Would you like to build a house at ";
	
	
	/**
	 * A c'tor that gets a reference of the player
	 * @param sender - the player of which this class will be the input of
	 */
	public UIPlayerInput(Object sender) {
		super(sender);
	}

//	/**
//	 * Gets an single auction bid for a list of cells being proposed for sale
//	 */
//	@Override
//	public AuctionBid getAuctionSuggestion(List<CellBase> auctionCell) {
//		return new AuctionBid();
//	}

//	/**
//	 * Asks the player if he wants to sell an asset
//	 */
//	@Override
//	public boolean doPerformAuction()
//	{
//		return true;
//	}

	/**
	 * Checks if the player wants to buy a cell
	 */
	@Override
	public boolean buyCell(final CellBase landCell)
	{
		final boolean[] result = new boolean[1];
		
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() 
			    { 
					result[0] = JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, 
							BUY_CELL_QUESTION_PREFIX + landCell.getName() + " ?",
			                BUY_CELL_WINDOW_TITLE, JOptionPane.YES_NO_OPTION,
			                JOptionPane.QUESTION_MESSAGE);
			    }
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return result[0];
	}
	
	/**
	 * Checks if the player wants to build a house on the given cell
	 */
	@Override
	public boolean buildHouse(final City landCell) {
final boolean[] result = new boolean[1];
		
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() 
			    { 
					result[0] = JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, 
							BUILD_HOUSE_QUESTION_PREFIX + landCell.getName() + " ?",
			                BUILD_HOUSE_WINDOW_TITLE, JOptionPane.YES_NO_OPTION,
			                JOptionPane.QUESTION_MESSAGE);
			    }
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return result[0];
	}
	
//	/**
//	 * Gets an auction decision from the player who performed an auction
//	 * The decision is made based on the given auction suggestions from all the players
//	 */
//	@Override
//	public Player getAuctionDecision(
//			Dictionary<Player, AuctionBid> auctionResult)
//	{
//		return null;
//	}
//
//	/**
//	 * Gets the item(s) to be proposed in the auction
//	 */
//	@Override
//	public List<CellBase> getAuctionItem()
//	{
//		return null;
//	}	
}