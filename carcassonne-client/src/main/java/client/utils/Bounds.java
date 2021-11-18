package client.utils;

import logic.math.Vector2;

public final class Bounds {
    public final Vector2 start;
    public final Vector2 end;

    public Bounds(int startX, int startY, int endX, int endY) {
        this.start = new Vector2(startX, startY);
        this.end = new Vector2(endX, endY);
    }

    public int getWidth() {
        return end.getX() - start.getX();
    }

    public int getHeight() {
        return end.getY() - start.getY();
    }

    public Bounds reverseY() {
        return new Bounds(start.getX(), -end.getY(), end.getX(), -start.getY());
    }

    public Bounds scale(int x, int y) {
        return new Bounds(start.getX() * x, start.getY() * y, end.getX() * x, end.getY() * y);
    }
}
