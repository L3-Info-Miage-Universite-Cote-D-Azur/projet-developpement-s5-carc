package network.stream;

import org.junit.jupiter.api.Test;
import stream.ByteInputStream;

import static org.junit.jupiter.api.Assertions.*;

public class ByteInputStreamTest {
    @Test
    void testReadBoolean() {
        ByteInputStream inputStream = new ByteInputStream(new byte[]{1, 0}, 2);

        assertEquals(true, inputStream.readBoolean());
        assertEquals(false, inputStream.readBoolean());
        assertTrue(inputStream.isAtEnd());
    }

    @Test
    void testReadByte() {
        ByteInputStream inputStream = new ByteInputStream(new byte[]{15, 50}, 2);

        assertEquals((byte) 15, inputStream.readByte());
        assertEquals((byte) 50, inputStream.readByte());
        assertTrue(inputStream.isAtEnd());
    }

    @Test
    void testReadShort() {
        ByteInputStream inputStream = new ByteInputStream(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0x50, (byte) 0x60}, 4);

        assertEquals((short) 0xFFFF, inputStream.readShort());
        assertEquals((short) 0x5060, inputStream.readShort());
        assertTrue(inputStream.isAtEnd());
    }

    @Test
    void testReadInt() {
        ByteInputStream inputStream = new ByteInputStream(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x50, (byte) 0x60, (byte) 0x70, (byte) 0x80}, 8);

        assertEquals(0xFFFFFFFF, inputStream.readInt());
        assertEquals(0x50607080, inputStream.readInt());
        assertTrue(inputStream.isAtEnd());
    }

    @Test
    void testReadString() {
        ByteInputStream inputStream = new ByteInputStream(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34}, 13);

        assertEquals(null, inputStream.readString());
        assertEquals("01234", inputStream.readString());
        assertTrue(inputStream.isAtEnd());
    }

    @Test
    void testReadBytes() {
        ByteInputStream inputStream = new ByteInputStream(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34}, 13);

        assertArrayEquals(null, inputStream.readBytes());
        assertArrayEquals(new byte[]{(byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34}, inputStream.readBytes());
        assertTrue(inputStream.isAtEnd());
    }
}
