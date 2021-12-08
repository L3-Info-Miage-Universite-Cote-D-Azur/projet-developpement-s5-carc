package client.utils;

import logic.math.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PolygonTest {
    @Test
    void testPolygon() {
        Vector2 vector0 = new Vector2(0, 0);
        Vector2 vector1 = new Vector2(0, 1);
        Vector2 vector2 = new Vector2(1, 0);
        Vector2 vector3 = new Vector2(1, 1);
        Polygon polygon = new Polygon(vector0, vector1, vector2, vector3);
        assertEquals(vector0, polygon.getVector(0));
        assertEquals(vector1, polygon.getVector(1));
        assertEquals(vector2, polygon.getVector(2));
        assertEquals(vector3, polygon.getVector(3));

        assertEquals(4, polygon.getVectorCount());
    }

    @Test
    void testGetAxe() {
        Vector2 vector0 = new Vector2(0, 0);
        Vector2 vector1 = new Vector2(0, 1);
        Vector2 vector2 = new Vector2(1, 0);
        Vector2 vector3 = new Vector2(1, 1);
        Polygon polygon = new Polygon(vector0, vector1, vector2, vector3);
        int offset = 5;
        int[] vx = {0, 0, 1, 1};
        int[] vy = {offset, 1 + offset, offset, 1 + offset};

        assertArrayEquals(vx, polygon.getXs(0));
        assertArrayEquals(vy, polygon.getYs(offset));
    }
}
