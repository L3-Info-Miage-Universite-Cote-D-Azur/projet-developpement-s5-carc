package client.ai;

import logic.Game;
import logic.command.EndTurnCommand;
import logic.command.PlaceMeepleCommand;
import logic.command.PlaceTileDrawnCommand;
import logic.command.RotateTileDrawnCommand;
import logic.math.Vector2;
import logic.player.IPlayerListener;
import logic.player.Player;
import logic.state.GameState;
import logic.state.turn.GameTurnPlaceTileState;
import logic.tile.Tile;
import logic.tile.chunk.Chunk;

/**
 * The AI class is the base class for all AI players.
 */
public abstract class AI implements IPlayerListener {
    protected final Player player;

    public AI(Player player) {
        this.player = player;
    }

    /**
     * Called when the game waiting for the player to place a tile.
     */
    @Override
    public void onWaitingPlaceTile() {
        GameTurnPlaceTileState placeTileState = (GameTurnPlaceTileState) player.getGame().getState();
        Tile tileDrawn = placeTileState.getTileDrawn();
        Vector2 position = findPositionForTile(tileDrawn);

        player.getGame().getCommandExecutor().execute(new RotateTileDrawnCommand(tileDrawn.getRotation()));
        player.getGame().getCommandExecutor().execute(new PlaceTileDrawnCommand(position));
    }

    /**
     * Called when the game waiting for the player to plays the extra actions of the turn.
     */
    @Override
    public void onWaitingExtraAction() {
        runMeepleRoutine();
        player.getGame().getCommandExecutor().execute(new EndTurnCommand());
    }

    /**
     * Runs the routine for placing a meeple.
     * This routine calls the {@link #pickChunkToPlaceMeeple()} method to find the chunk to place the meeple and then places it.
     */
    private void runMeepleRoutine() {
        if (player.hasRemainingMeeples()) {
            Chunk chunk = pickChunkToPlaceMeeple();

            if (chunk != null) {
                player.getGame().getCommandExecutor().execute(new PlaceMeepleCommand(chunk.getParent(), chunk.getCurrentId()));
            }
        }
    }

    /**
     * Finds a position for the given tile.
     * @param tile The tile to find a position for.
     * @return The position where the tile can be placed.
     */
    protected abstract Vector2 findPositionForTile(Tile tile);

    /**
     * Picks a tile's chunk where the meeple can be placed.
     * Returns null if no chunk should be placed.
     * @return The chunk where the meeple can be placed.
     */
    protected abstract Chunk pickChunkToPlaceMeeple();
}
