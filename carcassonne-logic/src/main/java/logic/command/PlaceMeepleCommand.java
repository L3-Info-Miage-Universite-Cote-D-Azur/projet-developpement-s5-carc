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
 * Command to place a meeple on a tile.
 */
public class PlaceMeepleCommand implements ICommand {
    private Vector2 tilePosition;
    private ChunkId chunkId;

    public PlaceMeepleCommand(Tile tile, ChunkId chunkId) {
        this.tilePosition = tile.getPosition();
        this.chunkId = chunkId;
    }

    @Override
    public CommandId getId() {
        return CommandId.PLACE_MEEPLE;
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
     * @return true if the meeple was placed, false otherwise
     */
    @Override
    public boolean execute(Game game) {
        Player player = game.getTurn().getPlayer();

        if (player.getRemainingMeepleCount() < 1) {
            game.getListener().onCommandFailed("Player has no meeple left.");
            return false;
        }

        Tile tile = game.getBoard().getTileAt(tilePosition);

        if (tile == null) {
            game.getListener().onCommandFailed("Tile does not exist.");
            return false;
        }

        Chunk chunk = tile.getChunk(chunkId);

        if (chunk.hasMeeple()) {
            game.getListener().onCommandFailed("Chunk %s already has a meeple.", chunkId);
            return false;
        }

        GameTurn turn = game.getTurn();

        if (!turn.hasPlacedTile()) {
            game.getListener().onCommandFailed("Player has not placed a tile yet.");
            return false;
        }

        if (turn.hasPlacedMeeple()) {
            game.getListener().onCommandFailed("Player has already placed a meeple.");
            return false;
        }

        chunk.setMeeple(new Meeple(player));
        player.removeRemainingMeepleCount();
        turn.setMeeplePlaced();

        game.getListener().onMeeplePlaced(player, tile, chunkId);

        return true;
    }
}
