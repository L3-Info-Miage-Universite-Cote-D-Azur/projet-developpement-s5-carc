package logic.command;

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
                game.getListener().logWarning("Starting tile must be placed before another tile can be placed.");
                return false;
            }

            if (!position.equals(GameBoard.STARTING_TILE_POSITION)) {
                game.getListener().logWarning("Starting tile must be at 0,0.");
                return false;
            }
        } else {
            if (tile.hasFlags(TileFlags.STARTING)) {
                game.getListener().logWarning("Try to place two starting tile!");
                return false;
            }

            if (!tile.canBePlacedAt(position, board)) {
                game.getListener().logWarning("Tile cannot be placed here.");
                return false;
            }
        }

        if (board.hasTileAt(position)) {
            game.getListener().logWarning("Try to place a tile on another.");
            return false;
        }

        tile.setPosition(position);
        board.place(tile);
        game.getListener().onTilePlaced(tile);

        return true;
    }
}
