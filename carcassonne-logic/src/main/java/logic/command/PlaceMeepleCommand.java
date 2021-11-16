package logic.command;

import logic.Game;
import logic.GameTurn;
import logic.meeple.Meeple;
import logic.player.Player;
import logic.tile.Chunk;
import logic.tile.ChunkId;
import logic.tile.Tile;

/**
 * Command to place a meeple on a tile.
 */
public class PlaceMeepleCommand implements ICommand {
    private final Tile tile;
    private final ChunkId chunkId;

    public PlaceMeepleCommand(Tile tile, ChunkId chunkId) {
        this.tile = tile;
        this.chunkId = chunkId;
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
