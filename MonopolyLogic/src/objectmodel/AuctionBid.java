package objectmodel;

import java.util.List;

/**
 * Represents a bid from a single player for a single auction
 * Contains the $$$ and assets the player proposes for the exchange
 * @author Benda & Eizenman
 *
 */
public class AuctionBid
{
	private int cash;
	private List<CellBase> cells;
	
	public int getCash() {
		return cash;
	}
	public void setCash(int cash) {
		this.cash = cash;
	}
	public List<CellBase> getCells() {
		return cells;
	}
	public void setCells(List<CellBase> cells) {
		this.cells = cells;
	}
}