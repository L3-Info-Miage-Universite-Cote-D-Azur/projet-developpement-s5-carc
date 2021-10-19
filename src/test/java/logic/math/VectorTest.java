package logic.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VectorTest {
    @Test
    void testVector() {
        int n = 10;
        Vector2 vector2 = new Vector2(n, n * 2);
        assertEquals(n, vector2.getX());
        assertEquals(n * 2, vector2.getY());
    }

    @Test
    void testEquals() {
        int n = 20;
        Vector2 vector0 = new Vector2(n, n * 2);
        Vector2 vector1 = new Vector2(n * 3, n * 4);
        Vector2 vector2 = new Vector2(n, n * 2);

        assertNotEquals(vector0, vector1);
        assertEquals(vector0, vector0);
        assertEquals(vector0, vector2);
        assertNotEquals(vector0, null);
    }
}
