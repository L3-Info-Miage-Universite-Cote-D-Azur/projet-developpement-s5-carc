package stream;

import java.nio.charset.StandardCharsets;

/**
 * Stream for reading primitive types from a byte array.
 */
public class ByteInputStream {
    private final byte[] buf;
    private final int length;
    private int pos;

    public ByteInputStream(byte[] buf, int length) {
        this.buf = buf;
        this.length = length;
    }

    public boolean readBoolean() {
        return readByte() != 0;
    }

    public byte readByte() {
        return buf[pos++];
    }

    public short readShort() {
        return (short) (((buf[pos++] & 0xff) << 8) | (buf[pos++] & 0xff));
    }

    public int readInt() {
        return ((buf[pos++] & 0xff) << 24) | ((buf[pos++] & 0xff) << 16) | ((buf[pos++] & 0xff) << 8) | (buf[pos++] & 0xff);
    }

    public String readString() {
        return readString(readInt());
    }

    public String readString(int len) {
        if (len == -1) {
            return null;
        }

        String str = new String(buf, pos, len, StandardCharsets.UTF_8);
        pos += len;
        return str;
    }

    public byte[] readBytes() {
        return readBytes(readInt());
    }

    public byte[] readBytes(int len) {
        byte[] bytes = null;
        if (len == -1) {
            return bytes;
        }

        bytes = new byte[len];
        System.arraycopy(buf, pos, bytes, 0, len);
        pos += len;
        return bytes;
    }


    public int getBytesLeft() {
        return length - pos;
    }

    public boolean isAtEnd() {
        return pos >= length;
    }
}
