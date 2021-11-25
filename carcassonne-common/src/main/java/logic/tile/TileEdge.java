package logic.tile;

import logic.math.Vector2;
import logic.tile.chunk.ChunkId;

/**
 * Represents a tile edge.
 */
public enum TileEdge {
    /**
     * Represents the top edge.
     */
    TOP(new Vector2(0, 1)),

    /**
     * Represents the bottom edge.
     */
    BOTTOM(new Vector2(0, -1)),

    /**
     * Represents the left edge.
     */
    LEFT(new Vector2(-1, 0)),

    /**
     * Represents the right edge.
     */
    RIGHT(new Vector2(1, 0));

    private final Vector2 value;

    TileEdge(Vector2 value) {
        this.value = value;
    }

    /**
     * Gets the value of the edge.
     *
     * @return the value of the edge
     */
    public Vector2 getValue() {
        return value;
    }

    /**
     * Gets the opposite edge.
     *
     * @return the opposite edge
     */
    public TileEdge negate() {
        return switch (this) {
            case TOP -> BOTTOM;
            case BOTTOM -> TOP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }

    /**
     * Gets the chunks that are adjacent to the edge.
     *
     * @return
     */
    public ChunkId[] getChunkIds() {
        return switch (this) {
            case TOP -> new ChunkId[]{ChunkId.NORTH_LEFT, ChunkId.NORTH_MIDDLE, ChunkId.NORTH_RIGHT};
            case BOTTOM -> new ChunkId[]{ChunkId.SOUTH_LEFT, ChunkId.SOUTH_MIDDLE, ChunkId.SOUTH_RIGHT};
            case LEFT -> new ChunkId[]{ChunkId.WEST_TOP, ChunkId.WEST_MIDDLE, ChunkId.WEST_BOTTOM};
            case RIGHT -> new ChunkId[]{ChunkId.EAST_TOP, ChunkId.EAST_MIDDLE, ChunkId.EAST_BOTTOM};
        };
    }
}
