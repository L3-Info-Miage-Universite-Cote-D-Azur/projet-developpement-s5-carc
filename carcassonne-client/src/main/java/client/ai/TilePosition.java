package client.ai;

import logic.math.Vector2;
import logic.tile.TileRotation;

/**
 * Represents a tile position.
 */
public record TilePosition(Vector2 position, TileRotation rotation) {
}
