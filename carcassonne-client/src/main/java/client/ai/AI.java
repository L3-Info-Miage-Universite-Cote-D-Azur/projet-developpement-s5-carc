package client.ai;

import logic.Game;
import logic.command.*;
import logic.dragon.Dragon;
import logic.player.IPlayerListener;
import logic.player.Player;
import logic.state.turn.GameTurnPlaceMeepleState;
import logic.state.turn.GameTurnPlaceTileState;
import logic.tile.Direction;
import logic.tile.Tile;
import logic.tile.TileFlags;
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
        TilePosition position = findPositionForTile(tileDrawn);

        player.getGame().getCommandExecutor().execute(new RotateTileDrawnCommand(position.getRotation()));
        player.getGame().getCommandExecutor().execute(new PlaceTileDrawnCommand(position.getPosition()));
    }

    /**
     * Called when the game waiting for the player to place a meeple.
     */
    @Override
    public void onWaitingMeeplePlacement() {
        GameTurnPlaceMeepleState placeMeepleState = (GameTurnPlaceMeepleState) player.getGame().getState();
        Tile tileDrawn = getGame().getBoard().getTileAt(placeMeepleState.getTileDrawnPosition());

        if (tileDrawn.hasFlag(TileFlags.PRINCESS)) {
            Chunk chunkToRemoveMeeple = findChunkToRemoveMeeple(tileDrawn);

            if (chunkToRemoveMeeple != null) {
                player.getGame().getCommandExecutor().execute(new RemoveMeepleCommand(chunkToRemoveMeeple.getParent().getPosition(), chunkToRemoveMeeple.getCurrentId()));
                return;
            }
        }

        Chunk chunk = findChunkToPlaceMeeple(getGame().getBoard().getTileAt(placeMeepleState.getTileDrawnPosition()));

        if (chunk != null) {
            player.getGame().getCommandExecutor().execute(new PlaceMeepleCommand(chunk.getParent().getPosition(), chunk.getCurrentId()));
        } else {
            player.getGame().getCommandExecutor().execute(new SkipMeeplePlacementCommand());
        }
    }

    /**
     * Called when the game waiting for the player to move the dragon.
     */
    @Override
    public void onWaitingDragonMove() {
        Direction direction = findDirectionForDragon(player.getGame().getBoard().getDragon());

        if (direction == null) {
            throw new IllegalStateException("No direction found for dragon.");
        }

        player.getGame().getCommandExecutor().execute(new MoveDragonCommand(direction));
    }

    /**
     * Gets the game's instance.
     * @return The game's instance.
     */
    public Game getGame() {
        return player.getGame();
    }

    /**
     * Finds a position for the given tile.
     *
     * @param tile The tile to find a position for.
     * @return The position where the tile can be placed.
     */
    protected abstract TilePosition findPositionForTile(Tile tile);

    /**
     * Finds a tile's chunk where the meeple can be placed.
     * Returns null if no chunk should be placed.
     *
     * @return The chunk where the meeple can be placed.
     */
    protected abstract Chunk findChunkToPlaceMeeple(Tile tileDrawn);

    /**
     * Finds a tile's chunk where the meeple can be removed.
     * Returns null if no chunk should be removed.
     *
     * @return The chunk where the meeple can be placed.
     */
    protected abstract Chunk findChunkToRemoveMeeple(Tile tileDrawn);

    /**
     * Finds a direction to move the dragon.
     *
     * @param dragon The dragon to find a direction for.
     * @return The direction where the dragon can be moved.
     */
    protected abstract Direction findDirectionForDragon(Dragon dragon);
}
