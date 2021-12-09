package logic.command;

import logic.Game;
import logic.math.Vector2;
import logic.meeple.Meeple;
import logic.player.Player;
import logic.state.GameStateType;
import logic.state.turn.GameTurnPlaceMeepleState;
import logic.tile.Tile;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkId;
import stream.ByteInputStream;
import stream.ByteOutputStream;
import stream.ByteStreamHelper;

/**
 * Command to place a meeple on a tile.
 */
public class PlaceMeepleCommand implements ICommand {
    public static final int ERROR_TILE_NOT_FOUND = -1;
    public static final int ERROR_TILE_NOT_VALID = -2;
    public static final int ERROR_CHUNK_NOT_VALID = -3;

    private Vector2 tilePosition;
    private ChunkId chunkId;

    public PlaceMeepleCommand() {
    }

    public PlaceMeepleCommand(Vector2 tilePosition, ChunkId chunkId) {
        this.tilePosition = tilePosition;
        this.chunkId = chunkId;
    }

    /**
     * Gets the command type.
     *
     * @return the command type
     */
    @Override
    public CommandType getType() {
        return CommandType.PLACE_MEEPLE;
    }

    /**
     * Encodes the command attributes to the output stream.
     *
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {
        ByteStreamHelper.encodeVector(stream, tilePosition);
        stream.writeInt(chunkId.ordinal());
    }

    /**
     * Decodes the command attributes from the input stream.
     *
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {
        tilePosition = ByteStreamHelper.decodeVector(stream);
        chunkId = ChunkId.values()[stream.readInt()];
    }

    /**
     * Checks whether the command is valid and can be executed.
     *
     * @return {@link #ERROR_SUCCESS} whether the command can be executed, otherwise an error code.
     */
    @Override
    public int canBeExecuted(Game game) {
        GameTurnPlaceMeepleState placeMeepleState = (GameTurnPlaceMeepleState) game.getState();

        Tile tile = game.getBoard().getTileAt(tilePosition);

        if (tile == null) {
            return ERROR_TILE_NOT_FOUND;
        }

        Tile tileDrawn = game.getBoard().getTileAt(placeMeepleState.getTileDrawnPosition());

        if (tile != tileDrawn && !tileDrawn.hasPortal()) {
            return ERROR_TILE_NOT_VALID;
        }

        Chunk chunk = tile.getChunk(chunkId);

        if (chunk.getArea().hasMeeple()) {
            return ERROR_CHUNK_NOT_VALID;
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
        return GameStateType.TURN_PLACE_MEEPLE;
    }

    /**
     * Executes the command.
     *
     * @param game the game context
     */
    @Override
    public void execute(Game game) {
        GameTurnPlaceMeepleState placeMeepleState = (GameTurnPlaceMeepleState) game.getState();
        Player player = game.getTurnExecutor();
        Tile tile = game.getBoard().getTileAt(tilePosition);
        Chunk chunk = tile.getChunk(chunkId);

        chunk.setMeeple(new Meeple(player));
        player.increasePlayedMeeples();
        game.getListener().onMeeplePlaced(chunk, chunk.getMeeple());

        placeMeepleState.complete();
    }
}