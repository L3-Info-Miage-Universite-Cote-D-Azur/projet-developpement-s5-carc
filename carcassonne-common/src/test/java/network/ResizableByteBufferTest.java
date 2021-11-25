package network;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ResizableByteBufferTest {
    @Test
    void testIOAndResize() {
        ResizableByteBuffer buffer = new ResizableByteBuffer(10, 50);

        /* Writing on the initial 10-bytes segment */
        buffer.put(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        /* Writing on the resized 50-bytes segment */
        buffer.put(new byte[]{
                11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
                31, 32, 33, 34, 35, 36, 37, 38, 39, 40
        });
        buffer.put(new byte[]{41, 42, 43, 44, 45, 46, 47, 48, 49, 50});

        /* Removes the first 10-bytes */
        buffer.remove(10);

        assertEquals(40, buffer.size());
        assertEquals(11, buffer.getBuffer()[0]);
        assertEquals(12, buffer.getBuffer()[1]);
        assertEquals(50, buffer.getBuffer()[39]);

        /* Throws an exception because we exceed the max buffer size */
        assertThrows(RuntimeException.class, () -> {
            buffer.put(new byte[]{
                    51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
                    61
            });
        });
    }

    @Test
    void testRemove() {
        ResizableByteBuffer buffer = new ResizableByteBuffer(10, 50);

        /* Writing on the initial 10-bytes segment */
        buffer.put(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        /* Removes the first 10-bytes */
        buffer.remove(10);

        /* Removes bytes that are not in the buffer */
        assertThrows(IllegalArgumentException.class, () -> {
            buffer.remove(10);
        });
    }

    @Test
    void testClear() {
        ResizableByteBuffer buffer = new ResizableByteBuffer(10, 50);

        /* Writing on the initial 10-bytes segment */
        buffer.put(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        /* Removes the first 10-bytes */
        buffer.clear();

        assertEquals(0, buffer.size());
    }
}
