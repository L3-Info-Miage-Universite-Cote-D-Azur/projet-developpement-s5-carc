package logic.tile;

import logic.math.Direction;

import java.util.Arrays;

public enum ChunkId {
    NORTH_LEFT(Direction.LEFT, Direction.TOP),
    NORTH_MIDDLE(Direction.MIDDLE, Direction.TOP),
    NORTH_RIGHT(Direction.RIGHT, Direction.TOP),

    EAST_TOP(Direction.RIGHT, Direction.TOP),
    EAST_MIDDLE(Direction.RIGHT, Direction.MIDDLE),
    EAST_BOTTOM(Direction.RIGHT, Direction.RIGHT),

    SOUTH_LEFT(Direction.LEFT, Direction.BOTTOM),
    SOUTH_MIDDLE(Direction.MIDDLE, Direction.BOTTOM),
    SOUTH_RIGHT(Direction.RIGHT, Direction.BOTTOM),

    WEST_TOP(Direction.LEFT, Direction.TOP),
    WEST_MIDDLE(Direction.LEFT, Direction.MIDDLE),
    WEST_BOTTOM(Direction.LEFT, Direction.RIGHT),

    CENTER_MIDDLE(Direction.MIDDLE, Direction.MIDDLE);

    private final Direction column;
    private final Direction row;

    ChunkId(Direction column, Direction row) {
        this.column = column;
        this.row = row;
    }

    public Direction getColumn() {
        return column;
    }

    public Direction getRow() {
        return row;
    }

    public static ChunkId getChunkId(Direction column, Direction row) {
        return Arrays.stream(ChunkId.values()).filter(chunkId -> chunkId.getColumn() == column && chunkId.getRow() == row).findFirst().get();
    }
}
