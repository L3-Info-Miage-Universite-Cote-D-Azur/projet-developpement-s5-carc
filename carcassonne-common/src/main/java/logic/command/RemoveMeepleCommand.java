package logic.command;

import logic.Game;
import logic.board.GameBoard;
import logic.math.Vector2;
import logic.meeple.Meeple;
import logic.state.GameStateType;
import logic.state.turn.GameTurnPlaceMeepleState;
import logic.tile.Tile;
import logic.tile.TileFlags;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkId;
import stream.ByteInputStream;
import stream.ByteOutputStream;
import stream.ByteStreamHelper;

public class RemoveMeepleCommand implements ICommand {
    private Vector2 tilePosition;
    private ChunkId tileChunkId;

    public RemoveMeepleCommand() {
    }

    public RemoveMeepleCommand(Vector2 tilePosition, ChunkId tileChunkId) {
        this.tilePosition = tilePosition;
        this.tileChunkId = tileChunkId;
    }

    /**
     * Gets the command type.
     *
     * @return the command type
     */
    @Override
    public CommandType getType() {
        return CommandType.REMOVE_MEEPLE;
    }

    /**
     * Encodes the command attributes to the output stream.
     *
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {
        ByteStreamHelper.encodeVector(stream, tilePosition);
        stream.writeInt(tileChunkId.ordinal());
    }

    /**
     * Decodes the command attributes from the input stream.
     *
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {
        tilePosition = ByteStreamHelper.decodeVector(stream);
        tileChunkId = ChunkId.values()[stream.readInt()];
    }

    /**
     * Checks if the command is valid and can be executed.
     *
     * @return true if the command is valid
     */
    @Override
    public boolean canBeExecuted(Game game) {
        GameTurnPlaceMeepleState state = (GameTurnPlaceMeepleState) game.getState();
        Tile tileDrawn = game.getBoard().getTileAt(state.getTileDrawnPosition());

        if (!tileDrawn.hasFlag(TileFlags.PRINCESS)) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "You can only remove a meeple when the drawn tile is a princess.");
            return false;
        }

        Tile tile = game.getBoard().getTileAt(tilePosition);

        if (tile == null) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "Tile does not exist.");
            return false;
        }

        Chunk chunk = tile.getChunk(tileChunkId);

        if (chunk == null) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "Chunk does not exist.");
            return false;
        }

        if (!chunk.hasMeeple()) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "Chunk does not have a meeple.");
            return false;
        }

        if (!chunk.getArea().hasTile(tileDrawn)) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "You can only remove a meeple from a chunk tile is on the same area as the drawn tile.");
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
        return GameStateType.TURN_PLACE_MEEPLE;
    }

    /**
     * Executes the command.
     *
     * @param game the game context
     * @return true if the meeple was removed, false otherwise
     */
    @Override
    public void execute(Game game) {
        removeMeeple(game.getBoard().getTileAt(tilePosition).getChunk(tileChunkId));
        game.getState().complete();
    }

    /**
     * Removes the meeple from the chunk.
     * @param chunk the chunk
     */
    public static void removeMeeple(Chunk chunk) {
        Game game = chunk.getParent().getGame();
        GameBoard board = game.getBoard();

        Meeple meeple = chunk.getMeeple();
        chunk.setMeeple(null);
        meeple.getOwner().decreasePlayedMeeples();

        if (board.hasFairy() && board.getFairy().getChunk() == chunk) {
            board.destructFairy();
        }

        game.getListener().onMeepleRemoved(chunk);
    }
}
