package logic.tile;

import logic.math.Vector2;

public enum TileEdge {
    TOP(new Vector2(0, 1)),
    BOTTOM(new Vector2(0, -1)),
    LEFT(new Vector2(-1, 0)),
    RIGHT(new Vector2(1, 0));

    private final Vector2 value;

    TileEdge(Vector2 value) {
        this.value = value;
    }

    public Vector2 getValue() {
        return value;
    }

    public TileEdge negate() {
        return switch (this) {
            case TOP -> BOTTOM;
            case BOTTOM -> TOP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }

    public ChunkId[] getChunkIds() {
        return switch (this) {
            case TOP -> new ChunkId[]{ChunkId.NORTH_LEFT, ChunkId.NORTH_MIDDLE, ChunkId.NORTH_RIGHT};
            case BOTTOM -> new ChunkId[]{ChunkId.SOUTH_LEFT, ChunkId.SOUTH_MIDDLE, ChunkId.SOUTH_RIGHT};
            case LEFT -> new ChunkId[]{ChunkId.WEST_TOP, ChunkId.WEST_MIDDLE, ChunkId.WEST_BOTTOM};
            case RIGHT -> new ChunkId[]{ChunkId.EAST_TOP, ChunkId.EAST_MIDDLE, ChunkId.EAST_BOTTOM};
        };
    }
}
