package client.ai;

import logic.math.Vector2;
import logic.tile.TileRotation;

/**
 * Represents a tile position.
 */
public final class TilePosition {
    private final Vector2 position;
    private final TileRotation rotation;

    public TilePosition(Vector2 position, TileRotation rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    /**
     * Gets the position of the tile.
     *
     * @return The position of the tile.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Gets the rotation of the tile.
     *
     * @return The rotation of the tile.
     */
    public TileRotation getRotation() {
        return rotation;
    }
}
