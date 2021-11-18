package logic.command;

import logic.Game;
import logic.GameTurn;
import logic.board.GameBoard;
import logic.math.Vector2;
import logic.player.Player;
import logic.tile.ChunkId;
import logic.tile.Tile;
import logic.tile.TileFlags;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Command to remove a meeple on a tile.
 */
public class RemoveMeepleCommand implements ICommand {
    private Vector2 tilePosition;
    private ChunkId chunkId;

    public RemoveMeepleCommand() {
    }

    public RemoveMeepleCommand(Tile tile, ChunkId chunkId) {
        this.tilePosition = tile.getPosition();
        this.chunkId = chunkId;
    }

    /**
     * Gets the command type.
     * @return the command type
     */
    @Override
    public CommandType getType() {
        return CommandType.REMOVE_MEEPLE;
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
        Tile tile = game.getBoard().getTileAt(tilePosition);

        if (tile == null) {
            game.getCommandExecutor().getListener().onCommandFailed(this, "Tile does not exist.");
            return false;
        }

        return true;
    }

    /**
     * Executes the command.
     * @param game the game context
     * @return true if the meeple was removed, false otherwise
     */
    @Override
    public void execute(Game game) {
        GameTurn turn = game.getTurn();
        Player player = turn.getPlayer();
        Tile tile = game.getBoard().getTileAt(tilePosition);

        tile.getChunk(chunkId).setMeeple(null);
        player.addRemainingMeepleCount();
        turn.setMeeplePlaced();

        game.getListener().onMeepleRemoved(player, tile, chunkId);
    }
}
