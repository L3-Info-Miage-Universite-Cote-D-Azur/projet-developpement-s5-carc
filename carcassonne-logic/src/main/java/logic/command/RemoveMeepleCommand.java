package logic.command;

import logic.Game;
import logic.GameTurn;
import logic.meeple.Meeple;
import logic.player.Player;
import logic.tile.Chunk;
import logic.tile.ChunkId;
import logic.tile.Tile;

/**
 * Command to remove a meeple on a tile.
 */
public class RemoveMeepleCommand implements ICommand{
    private final Tile tile;
    private final ChunkId chunkId;

    public RemoveMeepleCommand(Tile tile, ChunkId chunkId) {
        this.tile = tile;
        this.chunkId = chunkId;
    }

    /**
     * Executes the command.
     * @param game the game context
     * @return true if the meeple was removed, false otherwise
     */
    @Override
    public boolean execute(Game game) {
        Player player = game.getTurn().getPlayer();
        Chunk chunk = tile.getChunk(chunkId);
        GameTurn turn = game.getTurn();

        chunk.setMeeple(null);
        player.addRemainingMeepleCount();
        turn.setMeeplePlaced();

        game.getListener().onMeepleRemoved(player, tile, chunkId);
        return true;
    }
}
