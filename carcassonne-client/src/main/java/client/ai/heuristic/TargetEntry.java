package client.ai.heuristic;

import logic.math.Vector2;
import logic.tile.TileRotation;

public final class TargetEntry {
    public final Vector2 position;
    public final TileRotation rotation;
    public final int score;

    public TargetEntry(Vector2 position, TileRotation rotation, int score) {
        this.position = position;
        this.rotation = rotation;
        this.score = score;
    }
}
