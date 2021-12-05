package logic;

import logic.player.Player;
import logic.state.GameState;
import logic.tile.Tile;
import logic.tile.chunk.ChunkId;

/**
 * Interface for the game listener. Allowing the game to notify some events to listener.
 */
public interface IGameListener {
    /**
     * Called when the game is started.
     */
    void onGameStarted();

    /**
     * Called when the game is over.
     */
    void onGameOver();

    /**
     * Called when the turn is started.
     */
    void onTurnStarted(int id, Tile tileDrawn);

    /**
     * Called when the turn is ended.
     */
    void onTurnEnded(int id);

    /**
     * Called when the game state changes.
     *
     * @param state The new game state.
     */
    void onStateChanged(GameState state);

    /**
     * Called when a tile is placed.
     *
     * @param tile The tile placed.
     */
    void onTilePlaced(Tile tile);

    /**
     * Called when a meeple is placed on a tile.
     *
     * @param player  The player who placed the meeple.
     * @param tile    The tile on which the meeple was placed.
     * @param chunkId The chunk id on which the meeple was placed.
     */
    void onMeeplePlaced(Player player, Tile tile, ChunkId chunkId);

    /**
     * Called when a meeple is removed from a tile.
     *
     * @param player  The player who removed the meeple.
     * @param tile    The tile from which the meeple was removed.
     * @param chunkId The chunk id from which the meeple was removed.
     */
    void onMeepleRemoved(Player player, Tile tile, ChunkId chunkId);
}
