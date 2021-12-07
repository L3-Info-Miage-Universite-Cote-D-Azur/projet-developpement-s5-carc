package logic.command;

import logic.Game;
import logic.dragon.Fairy;
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
 * Command to place a fairy on a chunk.
 */
public class PlaceFairyCommand implements ICommand {
    private Vector2 tilePosition;
    private ChunkId chunkId;

    public PlaceFairyCommand() {
    }

    public PlaceFairyCommand(Vector2 tilePosition, ChunkId chunkId) {
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
        return CommandType.PLACE_FAIRY;
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
     * Checks if the command is valid and can be executed.
     *
     * @return true if the command is valid
     */
    @Override
    public boolean canBeExecuted(Game game) {
        Tile tile = game.getBoard().getTileAt(tilePosition);

        if (tile == null) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "Tile does not exist");
            return false;
        }

        Chunk chunk = tile.getChunk(chunkId);

        if (!chunk.hasMeeple() || chunk.getMeeple().getOwner() != game.getTurnExecutor()) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "Chunk does not have a meeple or meeple is not owned by the player");
            return false;
        }

        Fairy fairy = game.getBoard().getFairy();

        if (fairy != null && fairy.getChunk() == chunk) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "Fairy is already on this chunk");
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
     * @return true if the fairy was placed, false otherwise
     */
    @Override
    public void execute(Game game) {
        GameTurnPlaceMeepleState placeMeepleState = (GameTurnPlaceMeepleState) game.getState();
        Tile tile = game.getBoard().getTileAt(tilePosition);
        Chunk chunk = tile.getChunk(chunkId);

        game.getBoard().spawnFairy(chunk);
        placeMeepleState.complete();

        game.getListener().onFairyPlaced(chunk);
    }
}
