package logic.command;

import logic.Game;
import logic.GameTurn;
import logic.math.Vector2;
import logic.meeple.Meeple;
import logic.player.Player;
import logic.tile.Chunk;
import logic.tile.ChunkId;
import logic.tile.Tile;
import stream.ByteInputStream;
import stream.ByteOutputStream;

/**
 * Command to remove a meeple on a tile.
 */
public class RemoveMeepleCommand implements ICommand {
    private Vector2 tilePosition;
    private ChunkId chunkId;

    public RemoveMeepleCommand(Tile tile, ChunkId chunkId) {
        this.tilePosition = tile.getPosition();
        this.chunkId = chunkId;
    }

    @Override
    public CommandId getId() {
        return CommandId.REMOVE_MEEPLE;
    }

    @Override
    public void encode(ByteOutputStream stream) {
        stream.writeInt(tilePosition.getX());
        stream.writeInt(tilePosition.getY());
        stream.writeInt(chunkId.ordinal());
    }

    @Override
    public void decode(ByteInputStream stream) {
        tilePosition = new Vector2(stream.readInt(), stream.readInt());
        chunkId = ChunkId.values()[stream.readInt()];
    }

    /**
     * Executes the command.
     * @param game the game context
     * @return true if the meeple was removed, false otherwise
     */
    @Override
    public boolean execute(Game game) {
        GameTurn turn = game.getTurn();
        Player player = turn.getPlayer();
        Tile tile = game.getBoard().getTileAt(tilePosition);

        if (tile == null) {
            game.getListener().onCommandFailed("Tile does not exist.");
            return false;
        }

        tile.getChunk(chunkId).setMeeple(null);
        player.addRemainingMeepleCount();
        turn.setMeeplePlaced();

        game.getListener().onMeepleRemoved(player, tile, chunkId);
        return true;
    }
}
