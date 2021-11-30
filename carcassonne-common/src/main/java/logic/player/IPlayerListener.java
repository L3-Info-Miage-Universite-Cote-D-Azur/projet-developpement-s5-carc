package logic.player;

/**
 * Interface for a listener of a player.
 */
public interface IPlayerListener {
    /**
     * Called when the player must place the tile drawn.
     * Used to notify the player that it must place the tile drawn.
     */
    void onWaitingPlaceTile();

    /**
     * Called when the game waits for the player to place a meeple.
     * Used to notify the player that it must play.
     */
    void onWaitingMeeplePlacement();

    /**
     * Called when the player has to move the dragon.
     * Used to notify the player that it must place a meeple.
     */
    void onWaitingDragonMove();
}
