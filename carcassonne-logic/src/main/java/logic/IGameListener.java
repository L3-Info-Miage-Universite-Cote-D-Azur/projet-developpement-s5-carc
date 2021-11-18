package logic;

import logic.player.Player;
import logic.tile.ChunkId;
import logic.tile.Tile;

/**
 * Interface for the game listener. Allowing the game to notify some events to listener.
 */
public interface IGameListener {
    /**
     * Called when the turn is started.
     * @param id The turn id.
     */
    void onTurnStarted(int id);

    /**
     * Called when the turn is over.
     * @param id The turn id.
     */
    void onTurnEnded(int id);

    /**
     * Called when a tile is placed.
     * @param tile The tile placed.
     */
    void onTilePlaced(Tile tile);

    /**
     * Called when a meeple is placed on a tile.
     * @param player The player who placed the meeple.
     * @param tile The tile on which the meeple was placed.
     * @param chunkId The chunk id on which the meeple was placed.
     */
    void onMeeplePlaced(Player player, Tile tile, ChunkId chunkId);

    void onMeepleRemoved(Player player, Tile tile, ChunkId chunkId);

    /**
     * Called when the game is started.
     */
    void onStart();

    /**
     * Called when the game is over.
     */
    void onEnd();
}
