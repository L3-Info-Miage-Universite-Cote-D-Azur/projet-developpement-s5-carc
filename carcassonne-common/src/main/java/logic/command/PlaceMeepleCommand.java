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
     * Checks if the command is valid and can be executed.
     *
     * @return true if the command is valid
     */
    @Override
    public boolean canBeExecuted(Game game) {
        GameTurnPlaceMeepleState placeMeepleState = (GameTurnPlaceMeepleState) game.getState();

        Tile tile = game.getBoard().getTileAt(tilePosition);
        Tile tileDrawn = game.getBoard().getTileAt(placeMeepleState.getTileDrawnPosition());

        if (tile != tileDrawn && !tileDrawn.isPortal()) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "Tile is not the tile drawn and the tile drawn is not a portal.");
            return false;
        }

        Chunk chunk = tile.getChunk(chunkId);

        if (chunk.getArea().hasMeeple()) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "Meeple already present in the area.");
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
     * @return true if the meeple was placed, false otherwise
     */
    @Override
    public void execute(Game game) {
        GameTurnPlaceMeepleState placeMeepleState = (GameTurnPlaceMeepleState) game.getState();
        Player player = game.getTurnExecutor();
        Tile tile = game.getBoard().getTileAt(tilePosition);
        Chunk chunk = tile.getChunk(chunkId);

        chunk.setMeeple(new Meeple(player));
        player.increasePlayedMeeples();
        placeMeepleState.complete();

        game.getListener().onMeeplePlaced(player, tile, chunkId);
    }
}