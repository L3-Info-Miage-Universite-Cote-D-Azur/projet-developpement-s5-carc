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
        assertEquals(startX, bounds.start.x());
        assertEquals(startY, bounds.start.y());
        assertEquals(endX, bounds.end.x());
        assertEquals(endY, bounds.end.y());
    }

    @Test
    void sizeTest() {
        assertEquals(endX - startX, bounds.getWidth());
        assertEquals(endY - startY, bounds.getHeight());
    }

    @Test
    void reverseTest() {
        Bounds bounds1 = bounds.reverseY();
        assertEquals(bounds.start.x(), bounds1.start.x());
        assertEquals(-bounds.end.y(), bounds1.start.y());
        assertEquals(bounds.end.x(), bounds1.end.x());
        assertEquals(-bounds.start.y(), bounds1.end.y());
    }

    @Test
    void scaleTest() {
        int x = 5;
        int y = 20;
        Bounds bounds1 = bounds.scale(x, y);
        assertEquals(bounds.start.x() * x, bounds1.start.x());
        assertEquals(bounds.start.y() * y, bounds1.start.y());
        assertEquals(bounds.end.x() * x, bounds1.end.x());
        assertEquals(bounds.end.y() * y, bounds1.end.y());
    }
}
