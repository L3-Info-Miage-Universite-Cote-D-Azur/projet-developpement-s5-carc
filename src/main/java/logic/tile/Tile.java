package logic.tile;

import logic.math.Vector2;

public abstract class Tile {
    private final Vector2 position;

    public Tile(Vector2 position) {
        this.position = position;
    }

    public abstract TileType getType();

    public Vector2 getPosition() {
        return position;
    }
}
