package client.ai;

import logic.board.GameBoard;
import logic.dragon.Dragon;
import logic.math.Vector2;
import logic.player.Player;
import logic.tile.Direction;
import logic.tile.Tile;
import logic.tile.TileRotation;
import logic.tile.area.Area;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkId;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Simple AI that places the tile drawn randomly and randomly places meeple on the board.
 */
public class SimpleAI extends AI {
    private static final int MEEPLE_PLACEMENT_PROBABILITY = 80;
    private static final int MEEPLE_REMOVING_PROBABILITY = 75;

    protected final Random random;

    public SimpleAI(Player player) {
        super(player);
        random = new Random();
    }

    /**
     * Finds a tile's chunk where the meeple can be placed.
     * Returns null if no chunk should be placed.
     *
     * @return The chunk where the meeple can be placed.
     */
    @Override
    protected Chunk findChunkToPlaceMeeple(Tile tileDrawn) {
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
     * Finds a tile's chunk where the meeple can be removed.
     * Returns null if no chunk should be removed.
     *
     * @return The chunk where the meeple can be placed.
     */
    @Override
    protected Chunk findChunkToRemoveMeeple(Tile tileDrawn) {
        if (random.nextInt(100) >= MEEPLE_REMOVING_PROBABILITY) {
            for (Area area : tileDrawn.getAreas()) {
                for (Chunk chunk : area.getChunks()) {
                    if (chunk.hasMeeple()) {
                        return chunk;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Finds a position for the given dragon.
     *
     * @param dragon The dragon to find a position for.
     * @return The position where the dragon can be placed.
     */
    @Override
    protected Direction findDirectionForDragon(Dragon dragon) {
        ArrayList<Direction> freePositions = new ArrayList<>(Direction.values().length);

        for (Direction direction : Direction.values()) {
            Vector2 position = dragon.getPosition().add(direction.value());

            if (dragon.canMoveTo(position)) {
                freePositions.add(direction);
            }
        }

        if (freePositions.isEmpty()) {
            return null;
        }

        return freePositions.get(random.nextInt(freePositions.size()));
    }

    /**
     * Finds the best position to place the tile.
     *
     * @param tile The tile to place.
     * @return The position to place the tile.
     */
    @Override
    public TilePosition findPositionForTile(Tile tile) {
        LinkedList<TilePosition> freePoints = findAllFreePositionsForTile(tile);

        if (freePoints.isEmpty()) {
            return null;
        }

        return freePoints.get(random.nextInt(freePoints.size()));
    }

    /**
     * Finds all free positions for the tile.
     *
     * @param tile The tile to find free positions for.
     * @return A list of all free positions for the tile.
     */
    private LinkedList<TilePosition> findAllFreePositionsForTile(Tile tile) {
        GameBoard board = player.getGame().getBoard();
        LinkedList<TilePosition> freePoints = new LinkedList<>();

        for (int i = 0; i < TileRotation.NUM_ROTATIONS; i++) {
            tile.rotate();

            for (Vector2 freePosition : board.findFreePlacesForTile(tile)) {
                freePoints.add(new TilePosition(freePosition, tile.getRotation()));
            }
        }

        return freePoints;
    }
}
