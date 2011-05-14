package objectmodel;

import java.util.Random;

/**
 * A random dice used for advancing in the game
 * @author Benda & Eizenman
 *
 */
public class Dice 
{
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
