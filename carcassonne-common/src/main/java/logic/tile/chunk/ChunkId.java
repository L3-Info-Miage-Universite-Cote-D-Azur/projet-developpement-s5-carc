package logic.tile.chunk;

import logic.tile.Direction;

import java.util.Arrays;

/**
 * A chunk id is a unique identifier for a chunk. It contains the chunk's position in the tile grid.
 */
public enum ChunkId {
    /**
     * Chunk id for the chunk in the top left corner of the tile grid.
     */
    NORTH_LEFT(Direction.TOP),

    /**
     * Chunk id for the chunk in the top middle corner of the tile grid.
     */
    NORTH_MIDDLE(Direction.TOP),

    /**
     * Chunk id for the chunk in the top right corner of the tile grid.
     */
    NORTH_RIGHT(Direction.TOP),

    /**
     * Chunk id for the chunk in the right top corner of the tile grid.
     */
    EAST_TOP(Direction.RIGHT),

    /**
     * Chunk id for the chunk in the right middle corner of the tile grid.
     */
    EAST_MIDDLE(Direction.RIGHT),

    /**
     * Chunk id for the chunk in the right bottom corner of the tile grid.
     */
    EAST_BOTTOM(Direction.RIGHT),

    /**
     * Chunk id for the chunk in the bottom right corner of the tile grid.
     */
    SOUTH_RIGHT(Direction.BOTTOM),

    /**
     * Chunk id for the chunk in the bottom middle corner of the tile grid.
     */
    SOUTH_MIDDLE(Direction.BOTTOM),

    /**
     * Chunk id for the chunk in the bottom left corner of the tile grid.
     */
    SOUTH_LEFT(Direction.BOTTOM),

    /**
     * Chunk id for the chunk in the left bottom corner of the tile grid.
     */
    WEST_BOTTOM(Direction.LEFT),

    /**
     * Chunk id for the chunk in the left middle corner of the tile grid.
     */
    WEST_MIDDLE(Direction.LEFT),

    /**
     * Chunk id for the chunk in the left top corner of the tile grid.
     */
    WEST_TOP(Direction.LEFT),

    /**
     * Chunk id for the chunk in the middle of the tile grid.
     */
    CENTER_MIDDLE(null);

    private final Direction edge;

    ChunkId(Direction edge) {
        this.edge = edge;
    }

    /**
     * Gets the edge of the tile.
     *
     * @return The edge of the tile.
     */
    public Direction getEdge() {
        return edge;
    }

    /**
     * Returns the chunk ids for chunks around the current chunk.
     *
     * @return Chunk ids for chunks around the current chunk.
     */
    public ChunkId[] getNeighbours() {
        return this == CENTER_MIDDLE ? Arrays.stream(ChunkId.values()).filter(v -> v != CENTER_MIDDLE).toArray(ChunkId[]::new) : new ChunkId[]{
                values()[Math.floorMod((this.ordinal() - 1), (ChunkId.values().length - 1))],
                values()[(ordinal() + 1) % (values().length - 1)],
        };
    }
}
