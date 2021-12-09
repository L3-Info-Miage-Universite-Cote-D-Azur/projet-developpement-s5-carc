package logic.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {
    @Test
    void testVector() {
        int n = 10;
        Vector2 vector2 = new Vector2(n, n * 2);
        assertEquals(n, vector2.x());
        assertEquals(n * 2, vector2.y());
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
        assertNotNull(vector0);
    }

    @Test
    void testGetX() {
        int n = 30;
        Vector2 vector0 = new Vector2(n, n * 2);
        assertEquals(30, vector0.x());
    }

    @Test
    void testGetY() {
        int n = 40;
        Vector2 vector0 = new Vector2(n, n * 2);
        assertEquals(80, vector0.y());
    }

    @Test
    void testAdd() {
        int n = 50;
        Vector2 vector0 = new Vector2(n, n * 2);
        Vector2 vector1 = new Vector2(n * 2, n);
        Vector2 vectorFinal = new Vector2(150, 150);
        Vector2 vectorResult = vector1.add(vector0);
        assertEquals(vectorResult, vectorFinal);
    }

    @Test
    void testSubtract() {
        Vector2 vector0 = new Vector2(100, 100);
        Vector2 vector1 = new Vector2(50, 50);
        Vector2 vectorFinal = new Vector2(50, 50);
        Vector2 vectorResult = vector0.subtract(vector1);
        assertEquals(vectorResult, vectorFinal);
    }

    @Test
    void testSubtract2() {
        Vector2 vector0 = new Vector2(100, 100);
        Vector2 vector1 = new Vector2(50, 50);
        Vector2 vectorFinal = new Vector2(50, 50);
        Vector2 vectorResult = vector0.subtract(vector1.x(), vector1.y());
        assertEquals(vectorResult, vectorFinal);
    }

    @Test
    void testMultiply() {
        Vector2 vector0 = new Vector2(100, 100);
        Vector2 vector1 = new Vector2(50, 50);
        Vector2 vectorFinal = new Vector2(5000, 5000);
        Vector2 vectorResult = vector0.multiply(vector1);
        assertEquals(vectorResult, vectorFinal);
    }

    @Test
    void testMultiply2() {
        Vector2 vector0 = new Vector2(100, 100);
        Vector2 vector1 = new Vector2(50, 50);
        Vector2 vectorFinal = new Vector2(5000, 5000);
        Vector2 vectorResult = vector0.multiply(vector1.x(), vector1.y());
        assertEquals(vectorResult, vectorFinal);
    }

    @Test
    void testReverse() {
        Vector2 vector0 = new Vector2(100, 100);
        Vector2 vectorFinal = new Vector2(-100, -100);
        Vector2 vectorResult = vector0.reverse();
        assertEquals(vectorResult, vectorFinal);
    }

    @Test
    void testReverseX() {
        Vector2 vector0 = new Vector2(100, 100);
        Vector2 vectorFinal = new Vector2(-100, 100);
        Vector2 vectorResult = vector0.reverseX();
        assertEquals(vectorResult, vectorFinal);
    }

    @Test
    void testReverseY() {
        Vector2 vector0 = new Vector2(100, 100);
        Vector2 vectorFinal = new Vector2(100, -100);
        Vector2 vectorResult = vector0.reverseY();
        assertEquals(vectorResult, vectorFinal);
    }

    @Test
    void testSqrLength() {
        Vector2 vector0 = new Vector2(100, 100);
        double vectorFinal = 20000;
        double vectorResult = vector0.sqrMagnitude();
        assertEquals(vectorResult, vectorFinal);
    }

    @Test
    void testLength() {
        Vector2 vector0 = new Vector2(100, 100);
        assertEquals((int) Math.sqrt(20000), vector0.magnitude());
    }

    @Test
    void testToString() {
        Vector2 vector0 = new Vector2(100, 100);
        assertEquals("Vector2(100, 100)", vector0.toString());
    }
}
