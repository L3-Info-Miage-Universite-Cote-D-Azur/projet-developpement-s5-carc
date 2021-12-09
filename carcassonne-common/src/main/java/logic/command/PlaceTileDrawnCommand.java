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
    public static final int ERROR_STARTING_TILE_ATTEMPTED = -1;
    public static final int ERROR_STARTING_TILE_POSITION_INVALID = -2;
    public static final int ERROR_TILE_INVALID = -3;
    public static final int ERROR_TILE_POSITION_INVALID = -4;
    public static final int ERROR_TILE_POSITION_OCCUPIED = -5;

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
     * Checks whether the command is valid and can be executed.
     *
     * @return {@link #ERROR_SUCCESS} whether the command can be executed, otherwise an error code.
     */
    @Override
    public int canBeExecuted(Game game) {
        GameTurnPlaceTileState placeTileState = (GameTurnPlaceTileState) game.getState();
        Tile tile = placeTileState.getTileDrawn();
        GameBoard board = game.getBoard();

        if (board.getStartingTile() == null) {
            if (!tile.hasFlag(TileFlags.STARTING)) {
                return ERROR_STARTING_TILE_ATTEMPTED;
            }

            if (!position.equals(GameBoard.STARTING_TILE_POSITION)) {
                return ERROR_STARTING_TILE_POSITION_INVALID;
            }
        } else {
            if (tile.hasFlag(TileFlags.STARTING)) {
                return ERROR_TILE_INVALID;
            }

            if (!tile.canBePlacedAt(position)) {
                return ERROR_TILE_POSITION_INVALID;
            }
        }

        if (board.hasTileAt(position)) {
            return ERROR_TILE_POSITION_OCCUPIED;
        }

        return ERROR_SUCCESS;
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
