package client.listener;

import client.logger.Logger;
import client.logger.LoggerCategory;
import logic.Game;
import logic.IGameListener;
import logic.state.GameState;
import logic.tile.Tile;
import logic.tile.chunk.Chunk;

/**
 * GameLogger is a class that implements IGameListener and logs all the events
 */
public class GameLogger implements IGameListener {
    private final Game game;

    public GameLogger(Game game) {
        this.game = game;
    }

    /**
     * Called when the game is started.
     */
    @Override
    public void onGameStarted() {
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "--- GAME STARTED ---");
    }

    /**
     * Called when the game is over.
     */
    @Override
    public void onGameOver() {
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "--- GAME OVER ---");
    }

    /**
     * Called when the turn is started.
     */
    @Override
    public void onTurnStarted(int id, Tile tileDrawn) {
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "--- TURN %d STARTED ---", id);
    }

    /**
     * Called when the turn is ended.
     */
    @Override
    public void onTurnEnded(int id) {
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "--- TURN %d ENDED ---", id);
    }

    /**
     * Invoked when the game state changes.
     *
     * @param state The new game state.
     */
    @Override
    public void onStateChanged(GameState state) {
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "Game state changed to %s", state.getType());
    }

    /**
     * Invoked when a tile is placed.
     *
     * @param tile The tile placed.
     */
    @Override
    public void onTilePlaced(Tile tile) {
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "Place tile %s at (%d,%d)", tile.getConfig().model, tile.getPosition().getX(), tile.getPosition().getY());
    }

    /**
     * Invoked when a meeple is placed.
     *
     * @param chunk The chunk on which the meeple was placed.
     */
    @Override
    public void onMeeplePlaced(Chunk chunk) {
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "Place meeple of player %d at tile (%d,%d), chunk %s", game.getTurnExecutor().getId(), chunk.getParent().getPosition().getX(), chunk.getParent().getPosition().getY(), chunk.getCurrentId());
    }

    /**
     * Invoked when a fairy is placed.
     *
     * @param chunk The chunk on which the fairy was placed.
     */
    @Override
    public void onFairyPlaced(Chunk chunk) {
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "Place fairy at tile (%d,%d), chunk %s", chunk.getParent().getPosition().getX(), chunk.getParent().getPosition().getY(), chunk.getCurrentId());
    }

    /**
     * Invoked when a meeple is removed.
     *
     * @param chunk The chunk on which the meeple was removed.
     */
    @Override
    public void onMeepleRemoved(Chunk chunk) {
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "Meeple of player %d at tile (%d,%d), chunk %s is removed", game.getTurnExecutor().getId(), chunk.getParent().getPosition().getX(), chunk.getParent().getPosition().getY(), chunk.getCurrentId());
    }
}
