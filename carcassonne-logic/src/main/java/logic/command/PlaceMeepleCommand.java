package logic.command;

import logic.Game;
import logic.meeple.Meeple;
import logic.player.PlayerBase;
import logic.tile.Chunk;
import logic.tile.ChunkOffset;
import logic.tile.Tile;

public class PlaceMeepleCommand implements ICommand {
    private final Tile tile;
    private final ChunkOffset chunkOffset;
    private final PlayerBase executor;

    public PlaceMeepleCommand(Tile tile, ChunkOffset offset, PlayerBase executor) {
        this.tile = tile;
        this.chunkOffset = offset;
        this.executor = executor;
    }

    @Override
    public boolean execute(Game game) {
        if (executor.getRemainingMeepleCount() < 1) {
            game.getListener().logWarning("Player %d has no meeple to place.", executor.getId());
            return false;
        }

        Chunk chunk = tile.getChunk(chunkOffset);

        if (chunk.hasMeeple()) {
            game.getListener().logWarning("Try to place meeple on a chunk that already have meeple.");
            return false;
        }

        game.getListener().logWarning("Player %d places meeple at tile %s, chunk %s", executor.getId(), tile.getPosition(), chunkOffset);

        chunk.setMeeple(new Meeple(executor));
        executor.removeRemainingMeepleCount();

        return true;
    }
}
