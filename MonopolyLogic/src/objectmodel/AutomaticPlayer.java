package objectmodel;

/**
 * 
 * All AI decisions are taken in this class
 * This class represents a "robot input"
 *
 */
public class AutomaticPlayer extends Player
{
	private static final int MIN_CASH_FOR_BUYING_CELL = 300;
	private static final int MIN_CASH_FOR_BUILDING_HOUSE = 200;
	
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
	}
}
