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
     * Called when the player must do extra action turn (e.g. place/remove a meeple).
     * Used to notify the player that it must play.
     */
    void onWaitingExtraAction();
}
