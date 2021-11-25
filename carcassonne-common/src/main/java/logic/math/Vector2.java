package logic.math;

import java.util.Objects;

/**
 * Represents a 2D vector (x,y).
 */
public final class Vector2 {
    private final int x;
    private final int y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;
        return x == vector2.x && y == vector2.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Vector2 add(int x, int y) {
        return new Vector2(this.x + x, this.y + y);
    }

    public Vector2 add(Vector2 vector) {
        return new Vector2(this.x + vector.x, this.y + vector.y);
    }

    public Vector2 subtract(int x, int y) {
        return new Vector2(this.x - x, this.y - y);
    }

    public Vector2 subtract(Vector2 vector) {
        return new Vector2(this.x - vector.x, this.y - vector.y);
    }

    public Vector2 multiply(Vector2 vector) {
        return new Vector2(this.x * vector.x, this.y * vector.y);
    }

    public Vector2 multiply(int x, int y) {
        return new Vector2(this.x * x, this.y * y);
    }

    public Vector2 reverse() {
        return new Vector2(-x, -y);
    }

    public Vector2 reverseX() {
        return new Vector2(-x, y);
    }

    public Vector2 reverseY() {
        return new Vector2(x, -y);
    }

    @Override
    public String toString() {
        return "Vector2(" + x + ", " + y + ")";
    }
}
