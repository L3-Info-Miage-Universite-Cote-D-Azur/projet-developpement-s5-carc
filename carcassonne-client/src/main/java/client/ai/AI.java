package client.ai;

import logic.Game;
import logic.command.EndTurnCommand;
import logic.command.PlaceTileDrawnCommand;
import logic.math.Vector2;
import logic.player.IPlayerListener;
import logic.player.Player;
import logic.state.GameState;
import logic.state.turn.GameTurnPlaceTileState;
import logic.tile.Tile;

/**
 * The AI class is the base class for all AI players.
 */
public abstract class AI implements IPlayerListener {
    protected final Player player;

    public AI(Player player) {
        this.player = player;
    }

    @Override
    public void onWaitingPlaceTile() {
        GameTurnPlaceTileState placeTileState = (GameTurnPlaceTileState) player.getGame().getState();
        Tile tileDrawn = placeTileState.getTileDrawn();
        Vector2 position = findPositionForTile(tileDrawn);

        player.getGame().getCommandExecutor().execute(new PlaceTileDrawnCommand(position));
    }

    @Override
    public void onWaitingExtraAction() {
        placeMeepleIfNeeded();
        player.getGame().getCommandExecutor().execute(new EndTurnCommand());
    }

    /**
     * Finds a position for the given tile.
     * @param tile The tile to find a position for.
     * @return The position where the tile can be placed.
     */
    protected abstract Vector2 findPositionForTile(Tile tile);

    /**
     * Places a meeple on the board if needed.
     */
    protected abstract void placeMeepleIfNeeded();
}
