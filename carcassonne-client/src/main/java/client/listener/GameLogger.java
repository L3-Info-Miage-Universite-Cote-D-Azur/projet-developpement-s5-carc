package client.listener;

import client.logger.Logger;
import client.logger.LoggerCategory;
import logic.IGameListener;
import logic.player.Player;
import logic.state.GameState;
import logic.tile.ChunkId;
import logic.tile.Tile;

/**
 * GameLogger is a class that implements IGameListener and logs all the events
 */
public class GameLogger implements IGameListener {
    /**
     * Called when the game is started.
     */
    @Override
    public void onGameStarted() {
        Logger.info(LoggerCategory.GAME, "--- GAME STARTED ---");
    }

    /**
     * Called when the game is over.
     */
    @Override
    public void onGameOver() {
        Logger.info(LoggerCategory.GAME, "--- GAME OVER ---");
    }

    /**
     * Called when the turn is started.
     */
    @Override
    public void onTurnStarted(int id) {
        Logger.info(LoggerCategory.GAME, "--- TURN %d STARTED ---", id);
    }

    /**
     * Called when the turn is ended.
     */
    @Override
    public void onTurnEnded(int id) {
        Logger.info(LoggerCategory.GAME, "--- TURN %d ENDED ---", id);
    }

    /**
     * Invoked when the game state changes.
     * @param state The new game state.
     */
    @Override
    public void onStateChanged(GameState state) {
        Logger.info(LoggerCategory.GAME, "Game state changed to %s", state.getType());
    }

    /**
     * Invoked when a tile is placed.
     * @param tile The tile placed.
     */
    @Override
    public void onTilePlaced(Tile tile) {
        Logger.info(LoggerCategory.GAME, "Place tile %s at (%d,%d)", tile.getConfig().model, tile.getPosition().getX(), tile.getPosition().getY());
    }

    /**
     * Invoked when a meeple is placed.
     * @param player The player who placed the meeple.
     * @param tile The tile on which the meeple was placed.
     * @param chunkId The chunk id on which the meeple was placed.
     */
    @Override
    public void onMeeplePlaced(Player player, Tile tile, ChunkId chunkId) {
        Logger.info(LoggerCategory.GAME, "Place meeple of player %d at tile (%d,%d), chunk %s", player.getId(), tile.getPosition().getX(), tile.getPosition().getY(), chunkId);
    }

    /**
     * Invoked when a meeple is removed.
     * @param player The player who removed the meeple.
     * @param tile The tile on which the meeple was removed.
     * @param chunkId The chunk id on which the meeple was removed.
     */
    @Override
    public void onMeepleRemoved(Player player, Tile tile, ChunkId chunkId) {
        Logger.info(LoggerCategory.GAME, "Meeple of player %d at tile (%d,%d), chunk %s is removed", player.getId(), tile.getPosition().getX(), tile.getPosition().getY(), chunkId);
    }
}
