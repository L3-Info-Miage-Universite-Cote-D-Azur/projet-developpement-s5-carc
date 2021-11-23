package logic.math;

/**
 * Represents a direction in a 2D coordinate system.
 */
public enum Direction {
    /**
     * Represents the direction of the positive x-axis.
     */
    TOP(0, 1),

    /**
     * Represents the direction of the negative x-axis.
     */
    BOTTOM(0, -1),

    /**
     * Represents the direction of the null x-axis y-axis.
     */
    MIDDLE(0, 0),

    /**
     * Represents the direction of the negative y-axis.
     */
    LEFT(-1, 0),

    /**
     * Represents the direction of the positive y-axis.
     */
    RIGHT(1, 0);

    private final int x;
    private final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the offset of this direction.
     * @return The offset of this direction.
     */
    public Vector2 getOffset() {
        return new Vector2(x, y);
    }

    public Direction getOpposite() {
        switch (this) {
            case TOP:
                return BOTTOM;
            case BOTTOM:
                return TOP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return MIDDLE;
        }
    }
}
