package logic.tile.chunk;

import logic.tile.TileEdge;

import java.util.Arrays;

/**
 * A chunk id is a unique identifier for a chunk. It contains the chunk's position in the tile grid.
 */
public enum ChunkId {
    /**
     * Chunk id for the chunk in the top left corner of the tile grid.
     */
    NORTH_LEFT(TileEdge.TOP),

    /**
     * Chunk id for the chunk in the top middle corner of the tile grid.
     */
    NORTH_MIDDLE(TileEdge.TOP),

    /**
     * Chunk id for the chunk in the top right corner of the tile grid.
     */
    NORTH_RIGHT(TileEdge.TOP),

    /**
     * Chunk id for the chunk in the right top corner of the tile grid.
     */
    EAST_TOP(TileEdge.RIGHT),

    /**
     * Chunk id for the chunk in the right middle corner of the tile grid.
     */
    EAST_MIDDLE(TileEdge.RIGHT),

    /**
     * Chunk id for the chunk in the right bottom corner of the tile grid.
     */
    EAST_BOTTOM(TileEdge.RIGHT),

    /**
     * Chunk id for the chunk in the bottom right corner of the tile grid.
     */
    SOUTH_RIGHT(TileEdge.BOTTOM),

    /**
     * Chunk id for the chunk in the bottom middle corner of the tile grid.
     */
    SOUTH_MIDDLE(TileEdge.BOTTOM),

    /**
     * Chunk id for the chunk in the bottom left corner of the tile grid.
     */
    SOUTH_LEFT(TileEdge.BOTTOM),

    /**
     * Chunk id for the chunk in the left bottom corner of the tile grid.
     */
    WEST_BOTTOM(TileEdge.LEFT),

    /**
     * Chunk id for the chunk in the left middle corner of the tile grid.
     */
    WEST_MIDDLE(TileEdge.LEFT),

    /**
     * Chunk id for the chunk in the left top corner of the tile grid.
     */
    WEST_TOP(TileEdge.LEFT),

    /**
     * Chunk id for the chunk in the middle of the tile grid.
     */
    CENTER_MIDDLE(null);

    private final TileEdge edge;

    ChunkId(TileEdge edge) {
        this.edge = edge;
    }

    /**
     * Gets the edge of the tile.
     * @return The edge of the tile.
     */
    public TileEdge getEdge() {
        return edge;
    }
}
