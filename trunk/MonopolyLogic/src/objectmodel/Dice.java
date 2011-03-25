package objectmodel;

import java.util.Random;

/**
 * A random dice used for advancing in the game
 * @author Benda & Eizenman
 *
 */
public class Dice 
{
	private int lastRoll;
	private int currentRoll;
	private int dice1;
	private int dice2;
	private boolean isDouble;
	private Random random;
	
	public int getLastRoll() {
		return lastRoll;
	}

	public int getCurrentRoll() {
		return currentRoll;
	}

	public boolean isDouble() {
		return isDouble;
	}
	
	public Dice()
	{
		random = new Random();
	}
	
	public int roll()
	{
		lastRoll = currentRoll;
		dice1 = random.nextInt(6) + 1;
		dice2 = random.nextInt(6) + 1;
		currentRoll = dice1 + dice2;
		isDouble = (dice1 == dice2);
		
		return currentRoll;
	}
	
	@Override
	public String toString()
	{
		return dice1 + ":" + dice2;
	}
}
