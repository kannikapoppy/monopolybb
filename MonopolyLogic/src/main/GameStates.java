package main;

/**
 * The different states the monopoly game can be in
 * @author Benda & Eizenman
 *
 */
public enum GameStates
{
	Uninitialized,
	Initializing,
	Initialized,
	Starting,
	PlayerSelling,
	PlayerRolling,
        PromptPlayerForRollingDice,
	PlayerMoving,
	PlayerGettingOutOfJail,
	PlayerLanded,
	PlayerBuying,
        PromptPlayerForBuying,
	PlayerBidding,
	PlayerPayment,
	PlayerBuilding,
        PromptPlayerForBuilding,
	PlayerDrewCard,
	PlayerSwitching,
        PlayerPassedStartSquare,
        PlayerLandedOnStartSquare,
	PlayerBroke,
	PlayerLost,
	Auction,
	GameOver,
	Error
}
