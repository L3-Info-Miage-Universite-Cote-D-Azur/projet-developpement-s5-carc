package logic.command;

import logic.Game;
import logic.math.Vector2;
import logic.meeple.Meeple;
import logic.player.Player;
import logic.state.GameStateType;
import logic.state.turn.GameTurnExtraActionState;
import logic.tile.Chunk;
import logic.tile.ChunkId;
import logic.tile.Tile;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Command to place a meeple on a tile.
 */
public class PlaceMeepleCommand implements ICommand {
    private Vector2 tilePosition;
    private ChunkId chunkId;

    public PlaceMeepleCommand() {
    }

    public PlaceMeepleCommand(Tile tile, ChunkId chunkId) {
        this.tilePosition = tile.getPosition();
        this.chunkId = chunkId;
    }

    public PlaceMeepleCommand(Vector2 tilePosition, ChunkId chunkId) {
        this.tilePosition = tilePosition;
        this.chunkId = chunkId;
    }

    /**
     * Gets the command type.
     * @return the command type
     */
    @Override
    public CommandType getType() {
        return CommandType.PLACE_MEEPLE;
    }

    /**
     * Encodes the command attributes to the output stream.
     * @param stream the output stream
     */
    @Override
    public void encode(ByteOutputStream stream) {
        stream.writeInt(tilePosition.getX());
        stream.writeInt(tilePosition.getY());
        stream.writeInt(chunkId.ordinal());
    }

    /**
     * Decodes the command attributes from the input stream.
     * @param stream the input stream
     */
    @Override
    public void decode(ByteInputStream stream) {
        tilePosition = new Vector2(stream.readInt(), stream.readInt());
        chunkId = ChunkId.values()[stream.readInt()];
    }

    /**
     * Checks if the command is valid and can be executed.
     * @return true if the command is valid
     */
    @Override
    public boolean canBeExecuted(Game game) {
        GameTurnExtraActionState extraActionState = (GameTurnExtraActionState) game.getState();

        if (extraActionState.hasPlacedMeeple()) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "Player has already placed a meeple.");
            return false;
        }

        Player player = game.getTurnExecutor();

        if (!player.hasRemainingMeeples()) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "Player has no meeple left.");
            return false;
        }

        Tile tile = game.getBoard().getTileAt(tilePosition);

        if (tile == null) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "Tile does not exist.");
            return false;
        }

        Chunk chunk = tile.getChunk(chunkId);

        if (chunk.hasMeeple()) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "Chunk %s already has a meeple.", chunkId);
            return false;
        }

        return true;
    }

    /**
     * Gets the game state required to execute the command.
     * @return the game state
     */
    @Override
    public GameStateType getRequiredState() {
        return GameStateType.TURN_EXTRA_ACTION;
    }

    /**
     * Executes the command.
     * @param game the game context
     * @return true if the meeple was placed, false otherwise
     */
    @Override
    public void execute(Game game) {
        GameTurnExtraActionState extraActionState = (GameTurnExtraActionState) game.getState();
        Player player = game.getTurnExecutor();
        Tile tile = game.getBoard().getTileAt(tilePosition);
        Chunk chunk = tile.getChunk(chunkId);

        chunk.setMeeple(new Meeple(player));
        player.addScore(1, chunk.getType());
        player.increasePlayedMeeples();
        extraActionState.setPlacedMeeple();

        game.getListener().onMeeplePlaced(player, tile, chunkId);
    }
}