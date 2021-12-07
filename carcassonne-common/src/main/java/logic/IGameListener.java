package logic;

import logic.state.GameState;
import logic.tile.Tile;
import logic.tile.chunk.Chunk;

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
     * @param chunk The chunk on which the meeple was placed.
     */
    void onMeeplePlaced(Chunk chunk);

    /**
     * Called when a fairy is placed on a chunk
     *
     * @param chunk The chunk on which the fairy was placed.
     */
    void onFairyPlaced(Chunk chunk);

    /**
     * Called when a meeple is removed from a tile.
     *
     * @param chunk The chunk on which the meeple was removed.
     */
    void onMeepleRemoved(Chunk chunk);
}
