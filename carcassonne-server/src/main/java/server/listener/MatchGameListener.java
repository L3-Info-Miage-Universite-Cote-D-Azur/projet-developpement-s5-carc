package server.listener;

import logic.IGameListener;
import logic.command.ICommand;
import logic.dragon.Dragon;
import logic.dragon.Fairy;
import logic.meeple.Meeple;
import logic.player.Player;
import logic.state.GameState;
import logic.tile.Tile;
import logic.tile.chunk.Chunk;
import server.logger.Logger;
import server.matchmaking.Match;

public class MatchGameListener implements IGameListener {
    private final Match match;

    public MatchGameListener(Match match) {
        this.match = match;
    }

    /**
     * Called when a new turn is started.
     *
     * @param turn      The turn number.
     * @param tileDrawn The tile drawn.
     */
    @Override
    public void onTurnStarted(int turn, Tile tileDrawn) {
        Logger.info("Match %d: Turn %d started", match.getId(), turn);
        match.onTurnStarted(tileDrawn);
    }

    /**
     * Called when a turn is ended.
     *
     * @param turn The turn number.
     */
    @Override
    public void onTurnEnded(int turn) {
        Logger.info("Match %d: Turn %d ended", match.getId(), turn);
    }

    /**
     * Called when the game is started.
     */
    @Override
    public void onGameStarted() {
        Logger.info("Match %d: Game started", match.getId());
        match.onGameStarted();
    }

    /**
     * Called when the game is ended.
     */
    @Override
    public void onGameEnded() {
        Logger.info("Match %d: Game ended", match.getId());
        match.onGameEnded();
    }

    /**
     * Called when the game state has changed.
     *
     * @param state
     */
    @Override
    public void onStateChanged(GameState state) {
        Logger.debug("Match %d: State changed to %s", match.getId(), state.getType());
    }

    /**
     * Called when a tile is placed on the board.
     *
     * @param tile The tile that was placed.
     */
    @Override
    public void onTilePlaced(Tile tile) {
        Logger.info("Match %d: Tile %s placed", match.getId(), tile);
    }

    /**
     * Called when a tile is rotated.
     *
     * @param tile The tile rotated.
     */
    @Override
    public void onTileRotated(Tile tile) {
        Logger.info("Match %d: Tile %s rotated", match.getId(), tile);
    }

    /**
     * Called when a meeple is placed on a chunk.
     *
     * @param chunk  The chunk the meeple was placed on.
     * @param meeple The meeple that was placed.
     */
    @Override
    public void onMeeplePlaced(Chunk chunk, Meeple meeple) {
        Logger.info("Match %d: Meeple of player %s placed on chunk %s", match.getId(), meeple.getOwner().getId(), chunk.getCurrentId());
    }

    /**
     * Called when a meeple is removed from a chunk.
     *
     * @param chunk  The chunk the meeple was removed from.
     * @param meeple The meeple that was removed.
     */
    @Override
    public void onMeepleRemoved(Chunk chunk, Meeple meeple) {
        Logger.info("Match %d: Meeple of player %s removed from chunk %s", match.getId(), meeple.getOwner().getId(), chunk.getCurrentId());
    }

    /**
     * Called when a fairy is spawned on the board.
     *
     * @param fairy The fairy that was spawned.
     */
    @Override
    public void onFairySpawned(Fairy fairy) {
        Logger.info("Match %d: Fairy spawned at %s, chunk %s", match.getId(), fairy.getTilePosition(), fairy.getChunk().getCurrentId());
    }

    /**
     * Called when a fairy is dead (removed from the board).
     *
     * @param fairy The fairy that was removed.
     */
    @Override
    public void onFairyDeath(Fairy fairy) {
        Logger.info("Match %d: Fairy removed", match.getId());
    }

    /**
     * Called when a dragon is spawned on the board.
     *
     * @param dragon The dragon that was spawned.
     */
    @Override
    public void onDragonSpawned(Dragon dragon) {
        Logger.info("Match %d: Dragon spawned at %s", match.getId(), dragon.getPosition());
    }

    /**
     * Called when a dragon is dead (removed from the board).
     *
     * @param dragon The dragon that was removed.
     */
    @Override
    public void onDragonDeath(Dragon dragon) {
        Logger.info("Match %d: Dragon removed", match.getId());
    }

    /**
     * Called when the dragon has been moved.
     *
     * @param dragon The dragon that was moved.
     */
    @Override
    public void onDragonMove(Dragon dragon) {
        Logger.info("Match %d: Dragon moved to %s", match.getId(), dragon.getPosition());
    }

    /**
     * Called when a player has been earned points.
     *
     * @param player The player that earned the points.
     * @param score  The score that was earned.
     */
    @Override
    public void onScoreEarned(Player player, int score) {
        Logger.info("Match %d: Player %s earned %d points", match.getId(), player.getId(), score);
    }

    /**
     * Called when a command has been executed.
     *
     * @param command The command that was executed.
     */
    @Override
    public void onCommandExecuted(ICommand command) {
        Logger.debug("Match %d: Command %s executed", match.getId(), command);
        match.onCommandExecuted(command);
    }

    /**
     * Called when a command has been failed.
     *
     * @param command   The command that was failed.
     * @param errorCode The error code.
     */
    @Override
    public void onCommandFailed(ICommand command, int errorCode) {
        Logger.warn("Match %d: Command %s failed with error code %d", match.getId(), command, errorCode);
    }
}
