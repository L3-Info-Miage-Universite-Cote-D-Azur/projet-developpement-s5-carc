package logic.tile;

import logic.math.Vector2;

public abstract class Tile {
    private Vector2 position;

    public abstract TileType getType();

    public boolean canBePlaced(Tile tile, TileEdge edge) {
        return true;
    }

    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
