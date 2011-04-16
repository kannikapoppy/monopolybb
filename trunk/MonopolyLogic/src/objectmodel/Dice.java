package objectmodel;

import java.util.Random;

/**
 * A random dice used for advancing in the game
 * @author Benda & Eizenman
 *
 */
public class Dice 
{
	public class DiceThrowResult
	{
		private int dice1;
		private int dice2;
		
		public DiceThrowResult(int dice1, int dice2)
		{
			this.dice1 = dice1;
			this.dice2 = dice2;
		}
		
		public int sumOfDice()
		{
			return dice1 + dice2;
		}
		
		public boolean isDouble()
		{
			return dice1 == dice2;
		}
		
		public int getFirstDice()
		{
			return dice1;
		}
		
		public int getSecondDice()
		{
			return dice2;
		}
		
		@Override
		public String toString()
		{
			return dice1 + ":" + dice2;
		}
	}
	
	private DiceThrowResult currentRoll;
	private Random random;

	public DiceThrowResult getCurrentRoll() {
		return currentRoll;
	}
	
	public Dice()
	{
		random = new Random();
	}
	
	public DiceThrowResult roll()
	{
		int dice1 = random.nextInt(6) + 1;
		int dice2 = random.nextInt(6) + 1;
		currentRoll = new DiceThrowResult(dice1, dice2);
		
		return currentRoll;
	}
	
	@Override
	public String toString()
	{
		return currentRoll.toString();
	}
}
