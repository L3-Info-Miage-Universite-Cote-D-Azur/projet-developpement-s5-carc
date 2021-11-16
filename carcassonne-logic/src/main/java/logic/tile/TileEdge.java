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
            case TOP -> new ChunkId[] { ChunkId.TOP_LEFT, ChunkId.TOP_CENTER, ChunkId.TOP_RIGHT };
            case BOTTOM -> new ChunkId[] { ChunkId.BOTTOM_LEFT, ChunkId.BOTTOM_CENTER, ChunkId.BOTTOM_RIGHT };
            case LEFT -> new ChunkId[] { ChunkId.TOP_LEFT, ChunkId.MIDDLE_LEFT, ChunkId.BOTTOM_LEFT };
            case RIGHT -> new ChunkId[] { ChunkId.TOP_RIGHT, ChunkId.MIDDLE_RIGHT, ChunkId.BOTTOM_RIGHT };
        };
    }
}
