package logic.command;

import logic.Game;
import logic.GameTurn;
import logic.board.GameBoard;
import logic.math.Vector2;
import logic.tile.Tile;
import logic.tile.TileFlags;
import stream.ByteInputStream;
import stream.ByteOutputStream;

public class PlaceTileDrawnCommand implements ICommand {
    private Vector2 position;

    public PlaceTileDrawnCommand(Vector2 position) {
        this.position = position;
    }

    @Override
    public CommandId getId() {
        return CommandId.PLACE_TILE_DRAWN;
    }

    @Override
    public void encode(ByteOutputStream stream) {
        stream.writeInt(position.getX());
        stream.writeInt(position.getY());
    }

    @Override
    public void decode(ByteInputStream stream) {
        position = new Vector2(stream.readInt(), stream.readInt());
    }

    /**
     * Executes the command.
     * @param game The game context
     * @return True if the tile was placed, false otherwise
     */
    @Override
    public boolean execute(Game game) {
        GameTurn turn = game.getTurn();

        if (turn.hasPlacedTile()) {
            game.getListener().onCommandFailed("You can only place one tile per turn.");
            return false;
        }

        Tile tile = game.getTurn().getTileToDraw();
        GameBoard board = game.getBoard();

        if (board.getStartingTile() == null) {
            if (!tile.hasFlags(TileFlags.STARTING)) {
                game.getListener().onCommandFailed("Starting tile must be placed before another tile can be placed.");
                return false;
            }

            if (!position.equals(GameBoard.STARTING_TILE_POSITION)) {
                game.getListener().onCommandFailed("Starting tile must be at 0,0.");
                return false;
            }
        } else {
            if (tile.hasFlags(TileFlags.STARTING)) {
                game.getListener().onCommandFailed("Try to place two starting tile!");
                return false;
            }

            if (!tile.canBePlacedAt(position, board)) {
                game.getListener().onCommandFailed("Tile cannot be placed here.");
                return false;
            }
        }

        if (board.hasTileAt(position)) {
            game.getListener().onCommandFailed("Try to place a tile on another.");
            return false;
        }

        tile.setPosition(position);
        board.place(tile);
        turn.setTilePlaced();
        game.getListener().onTilePlaced(tile);

        return true;
    }
}
