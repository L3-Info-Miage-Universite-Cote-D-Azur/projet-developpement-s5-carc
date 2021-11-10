package logic.player;

import logic.board.GameBoard;
import logic.command.PlaceMeepleCommand;
import logic.math.Vector2;
import logic.tile.Chunk;
import logic.tile.ChunkOffset;
import logic.tile.Tile;

import java.util.ArrayList;
import java.util.Random;

public class SimpleAIPlayer extends AIPlayerBase {
    private static final int MEEPLE_PLACEMENT_PROBABILITY = 80;

    private final Random random;

    public SimpleAIPlayer(int id) {
        super(id);
        this.random = new Random();
    }

    @Override
    public void placeMeepleIfNeeded() {
        // IA Based algorithm :).

        if (getRemainingMeepleCount() >= 1) {
            if (random.nextInt(100) >= MEEPLE_PLACEMENT_PROBABILITY) {
                GameBoard board = game.getBoard();
                Tile tilePicked = board.getTiles().get(random.nextInt(board.getTileCount()));

                ChunkOffset chunkOffset = ChunkOffset.values()[random.nextInt(ChunkOffset.values().length)];
                Chunk chunkToPlaceMeeple = tilePicked.getChunk(chunkOffset);

                if (!chunkToPlaceMeeple.hasMeeple()) {
                    new PlaceMeepleCommand(tilePicked, chunkOffset, this).execute(game);
                }
            }
        }
    }

    @Override
    public Vector2 findPositionForTile(Tile tile) {
        GameBoard board = game.getBoard();
        ArrayList<Vector2> freePoints = board.findFreePlaceForTile(tile);

        if (freePoints.isEmpty()) {
            return null;
        }

        return freePoints.get(random.nextInt(freePoints.size()));
    }
}
