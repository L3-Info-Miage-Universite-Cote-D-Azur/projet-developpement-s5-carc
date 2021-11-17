package ai;

import logic.command.EndTurnCommand;
import logic.command.PlaceTileDrawnCommand;
import logic.math.Vector2;
import logic.player.IPlayerListener;
import logic.player.Player;
import logic.tile.Tile;

/**
 * The AI class is the base class for all AI players.
 */
public abstract class AI implements IPlayerListener {
    protected final Player player;

    public AI(Player player) {
        this.player = player;
    }

    /**
     * This method is called when it's the turn of the AI player.
     */
    @Override
    public void play() {
        Tile tileToDraw = player.getGame().getTurn().getTileToDraw();
        Vector2 positionFound = findPositionForTile(tileToDraw);

        new PlaceTileDrawnCommand(positionFound).execute(player.getGame());

        placeMeepleIfNeeded();

        new EndTurnCommand().execute(player.getGame());
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
