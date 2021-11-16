package logic.tile;

import logic.math.Direction;

import java.util.Arrays;

public enum ChunkId {
    TOP_LEFT(Direction.LEFT, Direction.TOP),
    TOP_CENTER(Direction.MIDDLE, Direction.TOP),
    TOP_RIGHT(Direction.RIGHT, Direction.TOP),
    MIDDLE_LEFT(Direction.LEFT, Direction.MIDDLE),
    MIDDLE_CENTER(Direction.MIDDLE, Direction.MIDDLE),
    MIDDLE_RIGHT(Direction.RIGHT, Direction.MIDDLE),
    BOTTOM_LEFT(Direction.LEFT, Direction.BOTTOM),
    BOTTOM_CENTER(Direction.MIDDLE, Direction.BOTTOM),
    BOTTOM_RIGHT(Direction.RIGHT, Direction.BOTTOM);

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

    public ChunkId getOpposite() {
        return Arrays.stream(ChunkId.values()).filter(chunkId -> chunkId.getColumn() == column.getOpposite() && chunkId.getRow() == row.getOpposite()).findFirst().get();
    }

    public static ChunkId getChunkId(Direction column, Direction row) {
        return Arrays.stream(ChunkId.values()).filter(chunkId -> chunkId.getColumn() == column && chunkId.getRow() == row).findFirst().get();
    }
}
