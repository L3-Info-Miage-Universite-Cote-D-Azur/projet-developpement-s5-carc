package logic.tile;

import logic.Game;
import logic.math.Vector2;

public abstract class Tile {
    private final TileData data;
    private final Vector2 position;

    public Tile(TileData data, Vector2 position) {
        this.data = data;
        this.position = position;
    }

    public abstract TileType getType();

    public Vector2 getPosition() {
        return position;
    }
}
