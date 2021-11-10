package logic.command;

import logger.Logger;
import logic.Game;
import logic.board.GameBoard;
import logic.math.Vector2;
import logic.tile.Tile;
import logic.tile.TileFlags;

public class PlaceTileCommand implements ICommand {
    private final Tile tile;
    private final Vector2 position;

    public PlaceTileCommand(Tile tile, Vector2 position) {
        this.tile = tile;
        this.position = position;
    }

    @Override
    public boolean execute(Game game) {
        if (tile.getPosition() != null) {
            return false;
        }

        GameBoard board = game.getBoard();

        if (board.getStartingTile() == null) {
            if (!tile.hasFlags(TileFlags.STARTING)) {
                Logger.warning("Starting tile must be placed before another tile can be placed.");
                return false;
            }

            if (!position.equals(GameBoard.STARTING_TILE_POSITION)) {
                Logger.warning("Starting tile must be at 0,0.");
                return false;
            }
        } else {
            if (tile.hasFlags(TileFlags.STARTING)) {
                Logger.warning("Try to place two starting tile!");
                return false;
            }

            if (!tile.canBePlacedAt(position, board)) {
                Logger.warning("Tile cannot be placed here.");
                return false;
            }
        }

        if (board.hasTileAt(position)) {
            Logger.warning("Try to place a tile on another.");
            return false;
        }

        Logger.info(String.format("Place %s at %s", tile, position));

        tile.setPosition(position);
        board.place(tile);

        return true;
    }
}
