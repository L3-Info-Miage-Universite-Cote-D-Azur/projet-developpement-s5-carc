package client.utils;

import logic.math.Vector2;

/**
 * A class that represents a 2D bounding box.
 */
public final class Bounds {
    /**
     * Start point of the bounding box.
     */
    public final Vector2 start;

    /**
     * End point of the bounding box.
     */
    public final Vector2 end;


    public Bounds(int startX, int startY, int endX, int endY) {
        this.start = new Vector2(startX, startY);
        this.end = new Vector2(endX, endY);
    }

    /**
     * Gets the width of the bounding box.
     *
     * @return the width of the bounding box.
     */
    public int getWidth() {
        return end.x() - start.x();
    }

    /**
     * Gets the height of the bounding box.
     *
     * @return the height of the bounding box.
     */
    public int getHeight() {
        return end.y() - start.y();
    }

    /**
     * Reverse the y-axis of the bounding box.
     *
     * @return the reversed bounding box.
     */
    public Bounds reverseY() {
        return new Bounds(start.x(), -end.y(), end.x(), -start.y());
    }

    /**
     * Scales the bounding box.
     *
     * @param x the x-axis scale factor.
     * @param y the y-axis scale factor.
     * @return the scaled bounding box.
     */
    public Bounds scale(int x, int y) {
        return new Bounds(start.x() * x, start.y() * y, end.x() * x, end.y() * y);
    }
}
