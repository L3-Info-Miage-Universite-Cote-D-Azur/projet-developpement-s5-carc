package client.ai;

import logic.board.GameBoard;
import logic.math.Vector2;
import logic.player.Player;
import logic.tile.Tile;
import logic.tile.TileRotation;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkId;

import java.util.LinkedList;
import java.util.Random;

/**
 * Simple AI that places the tile drawn randomly and randomly places meeple on the board.
 */
public class SimpleAI extends AI {
    private static final int MEEPLE_PLACEMENT_PROBABILITY = 80;

    protected final Random random;

    public SimpleAI(Player player) {
        super(player);
        random = new Random();
    }

    /**
     * Picks a tile's chunk where the meeple can be placed.
     * Returns null if no chunk should be placed.
     *
     * @return The chunk where the meeple can be placed.
     */
    @Override
    protected Chunk pickChunkToPlaceMeeple() {
        if (random.nextInt(100) >= MEEPLE_PLACEMENT_PROBABILITY) {
            GameBoard board = player.getGame().getBoard();
            Tile tilePicked = board.getTiles().get(random.nextInt(board.getTileCount()));

            ChunkId chunkId = ChunkId.values()[random.nextInt(ChunkId.values().length)];
            Chunk chunkToPlaceMeeple = tilePicked.getChunk(chunkId);

            if (!chunkToPlaceMeeple.hasMeeple()) {
                return chunkToPlaceMeeple;
            }
        }

        return null;
    }

    /**
     * Finds the best position to place the tile.
     *
     * @param tile The tile to place.
     * @return The position to place the tile.
     */
    @Override
    public Vector2 findPositionForTile(Tile tile) {
        LinkedList<SelectableTilePosition> freePoints = findAllFreePositionsForTile(tile);

        if (freePoints.size() == 0) {
            return null;
        }

        SelectableTilePosition positionPicked = freePoints.get(random.nextInt(freePoints.size()));
        tile.setRotation(positionPicked.rotation);

        return positionPicked.position;
    }

    private LinkedList<SelectableTilePosition> findAllFreePositionsForTile(Tile tile) {
        GameBoard board = player.getGame().getBoard();
        LinkedList<SelectableTilePosition> freePoints = new LinkedList<>();

        for (int i = 0; i < TileRotation.NUM_ROTATIONS; i++) {
            tile.rotate();

            for (Vector2 freePosition : board.findFreePlacesForTile(tile)) {
                freePoints.add(new SelectableTilePosition(freePosition, tile.getRotation()));
            }
        }

        return freePoints;
    }

    private class SelectableTilePosition {
        private final Vector2 position;
        private final TileRotation rotation;

        public SelectableTilePosition(Vector2 position, TileRotation rotation) {
            this.position = position;
            this.rotation = rotation;
        }
    }
}
