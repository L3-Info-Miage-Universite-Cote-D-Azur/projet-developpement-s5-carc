package logic.tile;

import logic.math.Direction;

import java.util.Arrays;

/**
 * A chunk id is a unique identifier for a chunk. It contains the chunk's position in the tile grid.
 */
public enum ChunkId {
    /**
     * Chunk id for the chunk in the top left corner of the tile grid.
     */
    NORTH_LEFT(Direction.LEFT, Direction.TOP),

    /**
     * Chunk id for the chunk in the top middle corner of the tile grid.
     */
    NORTH_MIDDLE(Direction.MIDDLE, Direction.TOP),

    /**
     * Chunk id for the chunk in the top right corner of the tile grid.
     */
    NORTH_RIGHT(Direction.RIGHT, Direction.TOP),

    /**
     * Chunk id for the chunk in the right top corner of the tile grid.
     */
    EAST_TOP(Direction.RIGHT, Direction.TOP),

    /**
     * Chunk id for the chunk in the right middle corner of the tile grid.
     */
    EAST_MIDDLE(Direction.RIGHT, Direction.MIDDLE),

    /**
     * Chunk id for the chunk in the right bottom corner of the tile grid.
     */
    EAST_BOTTOM(Direction.RIGHT, Direction.RIGHT),

    /**
     * Chunk id for the chunk in the bottom right corner of the tile grid.
     */
    SOUTH_RIGHT(Direction.RIGHT, Direction.BOTTOM),

    /**
     * Chunk id for the chunk in the bottom middle corner of the tile grid.
     */
    SOUTH_MIDDLE(Direction.MIDDLE, Direction.BOTTOM),

    /**
     * Chunk id for the chunk in the bottom left corner of the tile grid.
     */
    SOUTH_LEFT(Direction.LEFT, Direction.BOTTOM),

    /**
     * Chunk id for the chunk in the left bottom corner of the tile grid.
     */
    WEST_BOTTOM(Direction.LEFT, Direction.RIGHT),

    /**
     * Chunk id for the chunk in the left middle corner of the tile grid.
     */
    WEST_MIDDLE(Direction.LEFT, Direction.MIDDLE),

    /**
     * Chunk id for the chunk in the left top corner of the tile grid.
     */
    WEST_TOP(Direction.LEFT, Direction.TOP),

    /**
     * Chunk id for the chunk in the middle of the tile grid.
     */
    CENTER_MIDDLE(Direction.MIDDLE, Direction.MIDDLE);

    private final Direction column;
    private final Direction row;

    ChunkId(Direction column, Direction row) {
        this.column = column;
        this.row = row;
    }

    /**
     * Returns the column of the chunk.
     * @return The column of the chunk.
     */
    public Direction getColumn() {
        return column;
    }

    /**
     * Returns the row of the chunk.
     * @return The row of the chunk.
     */
    public Direction getRow() {
        return row;
    }

    /**
     * Returns the chunk ids for chunks around the current chunk.
     * @return Chunk ids for chunks around the current chunk.
     */
    public ChunkId[] getNeighbours() {
        return this == CENTER_MIDDLE ? Arrays.stream(ChunkId.values()).filter(v -> v != CENTER_MIDDLE).toArray(ChunkId[]::new) : new ChunkId[] {
                values()[Math.floorMod((this.ordinal() - 1), (ChunkId.values().length - 1))],
                values()[(ordinal() + 1) % (values().length - 1)],
                CENTER_MIDDLE
        };
    }
}
