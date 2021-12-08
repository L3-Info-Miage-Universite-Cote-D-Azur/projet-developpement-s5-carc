package client.listener;

import client.Client;
import client.logger.Logger;
import client.logger.LoggerCategory;
import logic.Game;
import logic.IGameListener;
import logic.command.ICommand;
import logic.dragon.Dragon;
import logic.dragon.Fairy;
import logic.meeple.Meeple;
import logic.state.GameState;
import logic.tile.Tile;
import logic.tile.chunk.Chunk;
import network.message.game.GameCommandRequestMessage;

public class ClientGameListener implements IGameListener {
    private final Client client;
    private final Game game;

    public ClientGameListener(Client client, Game game) {
        this.client = client;
        this.game = game;
    }

    /**
     * Called when a new turn is started.
     *
     * @param turn      The turn number.
     * @param tileDrawn The tile drawn.
     */
    @Override
    public void onTurnStarted(int turn, Tile tileDrawn) {
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "--- TURN %d STARTED ---", turn);
    }

    /**
     * Called when a turn is ended.
     *
     * @param turn The turn number.
     */
    @Override
    public void onTurnEnded(int turn) {
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "--- TURN %d ENDED ---", turn);
    }

    /**
     * Called when the game is started.
     */
    @Override
    public void onGameStarted() {
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "--- GAME STARTED ---");
    }

    /**
     * Called when the game is ended.
     */
    @Override
    public void onGameEnded() {
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "--- GAME OVER ---");
    }

    /**
     * Called when the game state has changed.
     *
     * @param state
     */
    @Override
    public void onStateChanged(GameState state) {
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "Game state changed to %s", state.getType());
    }

    /**
     * Called when a tile is placed on the board.
     *
     * @param tile The tile that was placed.
     */
    @Override
    public void onTilePlaced(Tile tile) {
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "Place tile %s at (%d,%d)", tile.getConfig().model, tile.getPosition().getX(), tile.getPosition().getY());
    }

    /**
     * Called when a tile is rotated.
     *
     * @param tile The tile rotated.
     */
    @Override
    public void onTileRotated(Tile tile) {
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "Rotate tile %s to ", tile.getConfig().model, tile.getRotation());
    }

    /**
     * Called when a meeple is placed on a chunk.
     *
     * @param chunk  The chunk the meeple was placed on.
     * @param meeple The meeple that was placed.
     */
    @Override
    public void onMeeplePlaced(Chunk chunk, Meeple meeple) {
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "Place meeple at tile (%d,%d), chunk %s", chunk.getParent().getPosition().getX(), chunk.getParent().getPosition().getY(), chunk.getCurrentId());
    }

    /**
     * Called when a meeple is removed from a chunk.
     *
     * @param chunk  The chunk the meeple was removed from.
     * @param meeple The meeple that was removed.
     */
    @Override
    public void onMeepleRemoved(Chunk chunk, Meeple meeple) {
        Logger.info(LoggerCategory.GAME, "Meeple of player %d has been removed from tile (%d,%d), chunk %s", meeple.getOwner().getId(), chunk.getParent().getPosition().getX(), chunk.getParent().getPosition().getY(), chunk.getCurrentId());
    }

    /**
     * Called when a fairy is spawned on the board.
     *
     * @param fairy The fairy that was spawned.
     */
    @Override
    public void onFairySpawned(Fairy fairy) {
        Chunk chunk = fairy.getChunk();
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "Place fairy at tile (%d,%d), chunk %s", chunk.getParent().getPosition().getX(), chunk.getParent().getPosition().getY(), chunk.getCurrentId());
    }

    /**
     * Called when a fairy is dead (removed from the board).
     *
     * @param fairy The fairy that was removed.
     */
    @Override
    public void onFairyDeath(Fairy fairy) {
        Logger.info(LoggerCategory.GAME, "Fairy removed from the board");
    }

    /**
     * Called when a dragon is spawned on the board.
     *
     * @param dragon The dragon that was spawned.
     */
    @Override
    public void onDragonSpawned(Dragon dragon) {
        Logger.info(LoggerCategory.GAME, "Dragon spawned on the board at %s", dragon.getPosition());
    }

    /**
     * Called when a dragon is dead (removed from the board).
     *
     * @param dragon The dragon that was removed.
     */
    @Override
    public void onDragonDeath(Dragon dragon) {
        Logger.info(LoggerCategory.GAME, "Dragon removed from the board");
    }

    /**
     * Called when the dragon has been moved.
     *
     * @param dragon The dragon that was moved.
     */
    @Override
    public void onDragonMove(Dragon dragon) {
        Logger.player(LoggerCategory.GAME, game.getTurnExecutor(), "Dragon moved to %s", dragon.getPosition());
    }

    /**
     * Called when a command has been executed.
     *
     * @param command The command that was executed.
     */
    @Override
    public void onCommandExecuted(ICommand command) {
        client.getServerConnection().send(new GameCommandRequestMessage(command));
    }

    /**
     * Called when a command has been failed.
     *
     * @param command   The command that was failed.
     * @param errorCode The error code.
     */
    @Override
    public void onCommandFailed(ICommand command, int errorCode) {
        Logger.warn(LoggerCategory.GAME, "Game: command failed: %s, error %s", command.getClass().getName(), errorCode);
    }
}
