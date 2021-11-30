package logic.command;

import logic.Game;
import logic.board.GameBoard;
import logic.math.Vector2;
import logic.state.GameStateType;
import logic.state.turn.GameTurnPlaceTileState;
import logic.tile.Tile;
import logic.tile.TileFlags;
import stream.ByteInputStream;
import stream.ByteOutputStream;
import stream.ByteStreamHelper;

/**
 * Command to place the tile drawn during the turn to the board.
 */
public class PlaceTileDrawnCommand implements ICommand {
    private Vector2 position;

    public PlaceTileDrawnCommand() {
    }

    public PlaceTileDrawnCommand(Vector2 position) {
        this.position = position;
    }

    /**
     * Gets the command type.
     *
     * @return the command type
     */
    @Override
    public CommandType getType() {
        return CommandType.PLACE_TILE_DRAWN;
    }

    /**
     * Encodes the command attributes to the output stream.
     *
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {
        ByteStreamHelper.encodeVector(stream, position);
    }

    /**
     * Decodes the command attributes from the input stream.
     *
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {
        position = ByteStreamHelper.decodeVector(stream);
    }

    /**
     * Checks if the command is valid and can be executed.
     *
     * @return true if the command is valid
     */
    @Override
    public boolean canBeExecuted(Game game) {
        GameTurnPlaceTileState placeTileState = (GameTurnPlaceTileState) game.getState();
        Tile tile = placeTileState.getTileDrawn();
        GameBoard board = game.getBoard();

        if (board.getStartingTile() == null) {
            if (!tile.hasFlag(TileFlags.STARTING)) {
                game.getCommandExecutor().getListener().onCommandFailed(this, "Starting tile must be placed before another tile can be placed.");
                return false;
            }

            if (!position.equals(GameBoard.STARTING_TILE_POSITION)) {
                game.getCommandExecutor().getListener().onCommandFailed(this, "Starting tile must be at (0,0).");
                return false;
            }
        } else {
            if (tile.hasFlag(TileFlags.STARTING)) {
                game.getCommandExecutor().getListener().onCommandFailed(this, "Try to place two starting tile!");
                return false;
            }

            if (!tile.canBePlacedAt(position)) {
                game.getCommandExecutor().getListener().onCommandFailed(this, "Tile cannot be placed here.");
                return false;
            }
        }

        if (board.hasTileAt(position)) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "Try to place a tile on another.");
            return false;
        }

        return true;
    }

    /**
     * Gets the game state required to execute the command.
     *
     * @return the game state
     */
    @Override
    public GameStateType getRequiredState() {
        return GameStateType.TURN_PLACE_TILE;
    }

    /**
     * Executes the command.
     *
     * @param game The game context
     * @return True if the tile was placed, false otherwise
     */
    @Override
    public void execute(Game game) {
        GameTurnPlaceTileState placeTileState = (GameTurnPlaceTileState) game.getState();
        Tile tile = placeTileState.getTileDrawn();

        tile.setPosition(position);
        game.getBoard().place(tile);
        game.getListener().onTilePlaced(tile);

        placeTileState.complete();
    }
}
