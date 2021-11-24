package logic.tile;

/**
 * Enum for the different rotations of a tile.
 */
public enum TileRotation {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    public static final int NUM_ROTATIONS = values().length;
    public static final int ANGLE_ROTATION = 90;

    public TileRotation next() {
        return values()[(ordinal() + 1) % values().length];
    }
}
