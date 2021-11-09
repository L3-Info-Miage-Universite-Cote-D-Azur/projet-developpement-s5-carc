package logic.tile;

import logic.math.Vector2;

public enum TileEdge {
    UP(new Vector2(0, 1)),
    DOWN(new Vector2(0, -1)),
    LEFT(new Vector2(-1, 0)),
    RIGHT(new Vector2(1, 0));

    private final Vector2 value;

    TileEdge(Vector2 value) {
        this.value = value;
    }

    public Vector2 getValue() {
        return value;
    }
}
