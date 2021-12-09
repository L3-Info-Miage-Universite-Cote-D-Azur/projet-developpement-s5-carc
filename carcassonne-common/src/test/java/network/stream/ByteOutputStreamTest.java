package network.stream;

import org.junit.jupiter.api.Test;
import stream.ByteOutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ByteOutputStreamTest {
    @Test
    void testWriteBoolean() {
        ByteOutputStream out = new ByteOutputStream(0);
        out.writeBoolean(true);
        out.writeBoolean(false);
        assertArrayEquals(new byte[]{1, 0}, out.toByteArray());
    }

    @Test
    void testWriteByte() {
        ByteOutputStream out = new ByteOutputStream(0);
        out.writeByte((byte) 0xFF);
        out.writeByte((byte) 0x50);
        assertEquals((byte) 0xFF, out.getBytes()[0]);
        assertEquals((byte) 0x50, out.getBytes()[1]);
        assertEquals(2, out.getLength());
    }

    @Test
    void testWriteShort() {
        ByteOutputStream out = new ByteOutputStream(0);
        out.writeShort((short) 0xFFFF);
        out.writeShort((short) 0x5050);
        assertEquals((byte) 0xFF, out.getBytes()[0]);
        assertEquals((byte) 0xFF, out.getBytes()[1]);
        assertEquals((byte) 0x50, out.getBytes()[2]);
        assertEquals((byte) 0x50, out.getBytes()[3]);
        assertEquals(4, out.getLength());
    }

    @Test
    void testWriteInt() {
        ByteOutputStream out = new ByteOutputStream(0);
        out.writeInt(0xFFFFFFFF);
        out.writeInt(0x50505050);
        assertEquals((byte) 0xFF, out.getBytes()[0]);
        assertEquals((byte) 0xFF, out.getBytes()[1]);
        assertEquals((byte) 0xFF, out.getBytes()[2]);
        assertEquals((byte) 0xFF, out.getBytes()[3]);
        assertEquals((byte) 0x50, out.getBytes()[4]);
        assertEquals((byte) 0x50, out.getBytes()[5]);
        assertEquals((byte) 0x50, out.getBytes()[6]);
        assertEquals((byte) 0x50, out.getBytes()[7]);
        assertEquals(8, out.getLength());
    }

    @Test
    void testWriteString() {
        ByteOutputStream out = new ByteOutputStream(0);
        out.writeString("Hello World!");
        assertEquals((byte) 0x00, out.getBytes()[0]);
        assertEquals((byte) 0x00, out.getBytes()[1]);
        assertEquals((byte) 0x00, out.getBytes()[2]);
        assertEquals((byte) 0x0C, out.getBytes()[3]);

        assertEquals((byte) 'H', out.getBytes()[4]);
        assertEquals((byte) 'e', out.getBytes()[5]);
        assertEquals((byte) 'l', out.getBytes()[6]);
        assertEquals((byte) 'l', out.getBytes()[7]);
        assertEquals((byte) 'o', out.getBytes()[8]);
        assertEquals((byte) ' ', out.getBytes()[9]);
        assertEquals((byte) 'W', out.getBytes()[10]);
        assertEquals((byte) 'o', out.getBytes()[11]);
        assertEquals((byte) 'r', out.getBytes()[12]);
        assertEquals((byte) 'l', out.getBytes()[13]);
        assertEquals((byte) 'd', out.getBytes()[14]);
        assertEquals((byte) '!', out.getBytes()[15]);
        assertEquals(16, out.getLength());
    }

    @Test
    void testWriteStringWithNull() {
        ByteOutputStream out = new ByteOutputStream(0);
        out.writeString("Hello World!");
        out.writeString(null);
        assertEquals((byte) 0x00, out.getBytes()[0]);
        assertEquals((byte) 0x00, out.getBytes()[1]);
        assertEquals((byte) 0x00, out.getBytes()[2]);
        assertEquals((byte) 0x0C, out.getBytes()[3]);

        assertEquals((byte) 'H', out.getBytes()[4]);
        assertEquals((byte) 'e', out.getBytes()[5]);
        assertEquals((byte) 'l', out.getBytes()[6]);
        assertEquals((byte) 'l', out.getBytes()[7]);
        assertEquals((byte) 'o', out.getBytes()[8]);
        assertEquals((byte) ' ', out.getBytes()[9]);
        assertEquals((byte) 'W', out.getBytes()[10]);
        assertEquals((byte) 'o', out.getBytes()[11]);
        assertEquals((byte) 'r', out.getBytes()[12]);
        assertEquals((byte) 'l', out.getBytes()[13]);
        assertEquals((byte) 'd', out.getBytes()[14]);
        assertEquals((byte) '!', out.getBytes()[15]);
        assertEquals((byte) 0xFF, out.getBytes()[16]);
        assertEquals((byte) 0xFF, out.getBytes()[17]);
        assertEquals((byte) 0xFF, out.getBytes()[18]);
        assertEquals((byte) 0xFF, out.getBytes()[19]);
        assertEquals(20, out.getLength());
    }
}
