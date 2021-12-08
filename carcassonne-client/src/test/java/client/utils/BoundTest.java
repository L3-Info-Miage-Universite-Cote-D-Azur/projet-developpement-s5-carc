package client.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoundTest {

    private static final int startX = 45;
    private static final int startY = 20;
    private static final int endX = 15;
    private static final int endY = 10;

    private static Bounds bounds;

    @BeforeAll
    static void initialized() {
        bounds = new Bounds(startX, startY, endX, endY);
    }

    @Test
    void boundTest() {
        assertEquals(startX, bounds.start.getX());
        assertEquals(startY, bounds.start.getY());
        assertEquals(endX, bounds.end.getX());
        assertEquals(endY, bounds.end.getY());
    }

    @Test
    void sizeTest() {
        assertEquals(endX - startX, bounds.getWidth());
        assertEquals(endY - startY, bounds.getHeight());
    }

    @Test
    void reverseTest() {
        Bounds bounds1 = bounds.reverseY();
        assertEquals(bounds.start.getX(), bounds1.start.getX());
        assertEquals(-bounds.end.getY(), bounds1.start.getY());
        assertEquals(bounds.end.getX(), bounds1.end.getX());
        assertEquals(-bounds.start.getY(), bounds1.end.getY());
    }

    @Test
    void scaleTest() {
        int x = 5;
        int y = 20;
        Bounds bounds1 = bounds.scale(x, y);
        assertEquals(bounds.start.getX() * x, bounds1.start.getX());
        assertEquals(bounds.start.getY() * y, bounds1.start.getY());
        assertEquals(bounds.end.getX() * x, bounds1.end.getX());
        assertEquals(bounds.end.getY() * y, bounds1.end.getY());
    }
}
