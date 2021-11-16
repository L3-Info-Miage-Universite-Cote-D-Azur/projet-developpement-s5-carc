package logic.math;

/**
 * Represents a direction in a 2D coordinate system.
 */
public enum Direction {
    /**
     * Represents the direction of the positive x-axis.
     */
    TOP(1),

    /**
     * Represents the direction of the negative x-axis.
     */
    BOTTOM(-1),

    /**
     * Represents the direction of the null x-axis y-axis.
     */
    MIDDLE(0),

    /**
     * Represents the direction of the negative y-axis.
     */
    LEFT(-1),

    /**
     * Represents the direction of the positive y-axis.
     */
    RIGHT(1);

    private final int value;

    Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
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
