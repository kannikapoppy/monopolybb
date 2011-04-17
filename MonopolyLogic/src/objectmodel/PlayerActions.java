package objectmodel;

/**
 * The actions a player can raise in the logic
 * @author Benda & Eizenman
 *
 */
public abstract class PlayerActions
{
	protected Object actor;

	public PlayerActions()
	{
		this.actor = null;
	}
	
	public PlayerActions(Object actor)
	{
		this.actor = actor;
	}
	
	/**
	 * Create a money transfer to a second player
	 * @param otherPlayer - the player to pay to
	 * @param money - the amount of money
	 */
	public abstract void payToPlayer(Player otherPlayer, int money);
	/**
	 * Create a money transfer to all players
	 * @param money - the amount of money to pay each player
	 */
	public abstract void payToAllPlayers(int money);
	/**
	 * Create a money transfer from all players
	 * @param money - the amount of money to collect from each player
	 */
	public abstract void getMoneyFromAllPlayers(int money);
	/**
	 * Create a money transfer from a second player
	 * @param otherPlayer - the player to be charged
	 * @param money - the amount of money to charge
	 */
	public abstract void getMoneyFromPlayer(Player otherPlayer, int money);
	/**
	 * Create a money transfer from the bank
	 * @param money - the amount of money to collect from the bank
	 */
	public abstract void getMoneyFromBank(int money);
	/**
	 * Create a  money transfer to the bank
	 * @param money - the amount of money to transfer to the bank
	 */
	public abstract void payMoneyToBank(int money);
	/**
	 * Move to a specific cell
	 * @param cellName - the specific cell's name
	 * @param getRoadToll - whether to collect/pay toll on the way to that cell
	 */
	public abstract CellBase moveToCell(String cellName, boolean getRoadToll);
	/**
	 * Draw a chance card
	 */
	public abstract void drawChanceCard();
	/**
	 * Draw a community chest card
	 */
	public abstract void drawCommunityChestCard();
	/**
	 * Return a chance card to the deck (useful for 'Free Jail Pass' card)
	 * @param card - the card to be return
	 */
	public abstract void returnCardToDeck(BonusCard card);
}
