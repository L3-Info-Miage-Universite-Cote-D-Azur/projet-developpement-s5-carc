package logic;

import logic.command.ICommand;
import logic.dragon.Dragon;
import logic.dragon.Fairy;
import logic.meeple.Meeple;
import logic.state.GameState;
import logic.tile.Tile;
import logic.tile.chunk.Chunk;

public interface IGameListener {
    //region Game state events

    /**
     * Called when a new turn is started.
     * @param turn The turn number.
     * @param tileDrawn The tile drawn.
     */
    void onTurnStarted(int turn, Tile tileDrawn);

    /**
     * Called when a turn is ended.
     * @param turn The turn number.
     */
    void onTurnEnded(int turn);

    /**
     * Called when the game is started.
     */
    void onGameStarted();

    /**
     * Called when the game is ended.
     */
    void onGameEnded();

    /**
     * Called when the game state has changed.
     */
    void onStateChanged(GameState state);

    //endregion

    //region Game action events

    /**
     * Called when a tile is placed on the board.
     * @param tile The tile that was placed.
     */
    void onTilePlaced(Tile tile);

    /**
     * Called when a tile is rotated.
     * @param tile The tile rotated.
     */
    void onTileRotated(Tile tile);

    /**
     * Called when a meeple is placed on a chunk.
     * @param chunk The chunk the meeple was placed on.
     * @param meeple The meeple that was placed.
     */
    void onMeeplePlaced(Chunk chunk, Meeple meeple);

    /**
     * Called when a meeple is removed from a chunk.
     *
     * @param chunk The chunk the meeple was removed from.
     * @param meeple The meeple that was removed.
     */
    void onMeepleRemoved(Chunk chunk, Meeple meeple);

    /**
     * Called when a fairy is spawned on the board.
     * @param fairy The fairy that was spawned.
     */
    void onFairySpawned(Fairy fairy);

    /**
     * Called when a fairy is dead (removed from the board).
     * @param fairy The fairy that was removed.
     */
    void onFairyDeath(Fairy fairy);

    /**
     * Called when a dragon is spawned on the board.
     * @param dragon The dragon that was spawned.
     */
    void onDragonSpawned(Dragon dragon);

    /**
     * Called when a dragon is dead (removed from the board).
     * @param dragon The dragon that was removed.
     */
    void onDragonDeath(Dragon dragon);

    /**
     * Called when the dragon has been moved.
     * @param dragon The dragon that was moved.
     */
    void onDragonMove(Dragon dragon);

    //endregion

    //region Game command events

    /**
     * Called when a command has been executed.
     * @param command The command that was executed.
     */
    void onCommandExecuted(ICommand command);

    /**
     * Called when a command has been failed.
     * @param command The command that was failed.
     * @param errorCode The error code.
     */
    void onCommandFailed(ICommand command, int errorCode);

    //endregion
}
