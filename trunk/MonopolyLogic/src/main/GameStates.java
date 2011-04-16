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
	PlayerMoving,
	PlayerGettingOutOfJail,
	PlayerLanded,
	PlayerBuying,
	PlayerBidding,
	PlayerPaying,
	PlayerGotPaid,
	PlayerBuilding,
	PlayerDrewCard,
	PlayerSwitching,
	PlayerBroke,
	PlayerLost,
	Auction,
	GameOver,
	Error
}
