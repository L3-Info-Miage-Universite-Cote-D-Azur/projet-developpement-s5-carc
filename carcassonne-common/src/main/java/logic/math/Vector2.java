package logic.math;

import java.util.Objects;

/**
 * Represents a 2D vector (x,y).
 */
public record Vector2(int x, int y) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;
        return x == vector2.x && y == vector2.y;
    }

    /**
     * Returns the hash code for this vector.
     *
     * @return the hash code for this vector.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Creates a new vector with the addition of the given values.
     *
     * @param x the x value to add.
     * @param y the y value to add.
     * @return the new vector.
     */
    public Vector2 add(int x, int y) {
        return new Vector2(this.x + x, this.y + y);
    }

    /**
     * Creates a new vector with the addition of the given vector.
     *
     * @param vector the vector to add.
     * @return the new vector.
     */
    public Vector2 add(Vector2 vector) {
        return new Vector2(this.x + vector.x, this.y + vector.y);
    }

    /**
     * Creates a new vector with the subtraction of the given values.
     *
     * @param x the x value to subtract.
     * @param y the y value to subtract.
     * @return the new vector.
     */
    public Vector2 subtract(int x, int y) {
        return new Vector2(this.x - x, this.y - y);
    }

    /**
     * Creates a new vector with the subtraction of the given vector.
     *
     * @param vector the vector to subtract.
     * @return the new vector.
     */
    public Vector2 subtract(Vector2 vector) {
        return new Vector2(this.x - vector.x, this.y - vector.y);
    }

    /**
     * Creates a new vector with the multiplication of the given values.
     *
     * @param x the x value to multiply.
     * @param y the y value to multiply.
     * @return the new vector.
     */
    public Vector2 multiply(int x, int y) {
        return new Vector2(this.x * x, this.y * y);
    }

    /**
     * Creates a new vector with the multiplication of the given vector.
     *
     * @param vector the vector to multiply.
     * @return the new vector.
     */
    public Vector2 multiply(Vector2 vector) {
        return new Vector2(this.x * vector.x, this.y * vector.y);
    }

    /**
     * Returns a new vector with the reversed values.
     *
     * @return the new vector.
     */
    public Vector2 reverse() {
        return new Vector2(-x, -y);
    }

    /**
     * Returns a new vector with the reversed x value.
     *
     * @return the new vector.
     */
    public Vector2 reverseX() {
        return new Vector2(-x, y);
    }

    /**
     * Returns a new vector with the reversed y value.
     *
     * @return the new vector.
     */
    public Vector2 reverseY() {
        return new Vector2(x, -y);
    }


    /**
     * Returns the squared magnitude of this vector.
     *
     * @return the squared magnitude of this vector.
     */
    public int sqrMagnitude() {
        return x * x + y * y;
    }

    /**
     * Returns the magnitude of this vector.
     *
     * @return the magnitude of this vector.
     */
    public int magnitude() {
        return (int) Math.sqrt(sqrMagnitude());
    }

    /**
     * Get the distance between two vector using manhattan method
     *
     * @param a The origin the vector
     * @param b The destination vector
     * @return the distance between two vector
     */
    public static int manhattan(Vector2 a, Vector2 b) {
        Vector2 vector2 = a.subtract(b);
        return Math.abs(vector2.x) + Math.abs(vector2.y);
    }

    /**
     * Converts this vector to string.
     *
     * @return the string representation of this vector.
     */
    @Override
    public String toString() {
        return "Vector2(" + x + ", " + y + ")";
    }
}
