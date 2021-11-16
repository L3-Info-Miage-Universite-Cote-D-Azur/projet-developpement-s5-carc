package logic.math;

public enum Direction {
    TOP(1),
    BOTTOM(-1),
    MIDDLE(0),
    LEFT(-1),
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
