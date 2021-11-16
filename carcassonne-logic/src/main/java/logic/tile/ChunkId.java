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

    SOUTH_RIGHT(Direction.RIGHT, Direction.BOTTOM),
    SOUTH_MIDDLE(Direction.MIDDLE, Direction.BOTTOM),
    SOUTH_LEFT(Direction.LEFT, Direction.BOTTOM),

    WEST_BOTTOM(Direction.LEFT, Direction.RIGHT),
    WEST_MIDDLE(Direction.LEFT, Direction.MIDDLE),
    WEST_TOP(Direction.LEFT, Direction.TOP),

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

    public ChunkId[] getNeighbours() {
        return this == CENTER_MIDDLE ? Arrays.stream(ChunkId.values()).filter(v -> v != CENTER_MIDDLE).toArray(ChunkId[]::new) : new ChunkId[] {
                values()[Math.floorMod((this.ordinal() - 1), (ChunkId.values().length - 1))],
                values()[(ordinal() + 1) % (values().length - 1)],
                CENTER_MIDDLE
        };
    }
}
